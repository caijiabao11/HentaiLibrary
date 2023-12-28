package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.httpjson.HpoiHttpJson;
import com.example.administrator.lztsg.httpjson.HttpHpoiDataResolution;
import com.example.administrator.lztsg.utils.HideUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class HpoiActivity extends AppCompatActivity {
    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;
    public static EditText mSearch;
    private static List<Integer> allpage = new ArrayList<>();
    private String path = "https://www.hpoi.net/hobby/all?order=hits&r18=-1&workers=&view=4&category=100";
    private static int total = 1;
    public static String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpoi);
        init();
        onViewpage(path);
        onSearch();
    }

    private void init() {
        mViewPager2 = findViewById(R.id.v_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mSearch = findViewById(R.id.edt_search);
    }

    private void initData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //标题
                for (int i=1;i<=total;i++){
                    mTabLayout.addTab(mTabLayout.newTab().setText("" + i),false);
                }
                mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
                    // 在Activity中定义一个标志变量
                    private boolean isTabSelectedCallbackExecuted = false;
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        // 判断是否已经执行过一次回调
                        if (!isTabSelectedCallbackExecuted) {
                            //设置tab选中状态下的文本状态
                            TextView textView = new TextView(HpoiActivity.this);
                            float selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 16, getResources().getDisplayMetrics());
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);
                            textView.setTextColor(getResources().getColor(R.color.colorTextTitle));
                            textView.setText(tab.getText());
                            textView.setGravity(Gravity.CENTER);
                            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                            tab.setCustomView(textView);
                            // 将标志变量设置为true，表示已经执行过一次回调
                            isTabSelectedCallbackExecuted = true;
                        }
                    }


                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        //设置tab非选中状态下的文本状态
                        tab.setCustomView(null);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                //使ViewPager2与Tablayout联动
                new TabLayoutMediator(mTabLayout,mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText("" + (position + 1));
                    }
                }).attach();

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    HpoiActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mTabLayout.getTabCount() > 0){
                                mTabLayout.getTabAt(0).select();//默认选中第一个
                            }
                            mViewPager2.setCurrentItem(0);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        if (mFrag1 == null) {
//            mFrag1 = new HpoiFragment();
//        }
//        fragmentTransaction.replace(R.id.fragmentLayout, mFrag1);
//        fragmentTransaction.commit();

    }

    private void onViewpage(String path) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HpoiHttpJson.getPage(path,new HttpHpoiDataResolution() {

                    @Override
                    public void onFinish(String title, String imageurl, String url) {

                    }

                    @Override
                    public void onInitData(String name, String value) {

                    }

                    @Override
                    public void onPages(int total) {
                        if (total == 0){
                            total++;
                        }
                        HpoiActivity.total = total;
                        initData();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }).start();

        mViewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mViewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return HpoiFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return total;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //监听点击空白处关闭软键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (HideUtils.ggetInstance().isShouldHideInput(v, ev)) {
                HideUtils.ggetInstance().hideSoftInput(v.getWindowToken(), this);
                mSearch.setCursorVisible(false);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //搜索框
    public void onSearch() {
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            //监听搜索按钮
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) mSearch.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            HpoiActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    mSearch.setCursorVisible(false);
                    return true;
                }
                return false;
            }
        });

        mSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //点击编辑框的时候显示光标
                    mSearch.setCursorVisible(true);
                }
                return false;
            }
        });
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void afterTextChanged(Editable s) {
                //这是文本框改变之后 会执行的动作
                data = mSearch.getText().toString();
                if (data != null && data != "") {
                    data = "&keyword=" + data;

                    mTabLayout.removeAllTabs();
                    //热门浏览界面链接
                    String path = "https://www.hpoi.net/hobby/all?order=hits&r18=-1&workers=&view=4&category=100" + data;
                    onViewpage(path);
                }
            }
        });
    }
}
