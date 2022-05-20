package com.example.administrator.lztsg;



import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.lztsg.httpjson.HttpJsonResolution;
import com.example.administrator.lztsg.httpjson.TestholekoHttpJson;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


public class TestholekilnActivity extends AppCompatActivity{
    private static SetInto setmInto;
    private static TextView itv;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2,mTopbgViewPeger;
    private String[] mTitles={"地狱寸止","颅内高潮","节奏大师"};

    public static void setInto(SetInto setInto){
        setmInto = setInto;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testholekiln);
        bindViews();
        LinearRecyclerView();
        initData();
    }

    private void bindViews() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager2 = findViewById(R.id.v_pager);
        mTopbgViewPeger = findViewById(R.id.vtop_pager);
        itv = findViewById(R.id.itv);
    }

    //载入数据
    private void initData(){
        //标题
        for (int i=0;i<mTitles.length;i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]),false);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //设置tab选中状态下的文本状态
                TextView textView = new TextView(TestholekilnActivity.this);
                float selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 16, getResources().getDisplayMetrics());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);
                textView.setTextColor(getResources().getColor(R.color.colorSearch));
                textView.setText(tab.getText());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tab.setCustomView(textView);
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
                tab.setText(mTitles[position]);
            }
        }).attach();
        new TabLayoutMediator(mTabLayout,mTopbgViewPeger, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitles[position]);
            }
        }).attach();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    TestholekilnActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTabLayout.getTabAt(0).select();//默认选中第一个
                            mViewPager2.setCurrentItem(0);
                            mTopbgViewPeger.setCurrentItem(0);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void LinearRecyclerView(){
        //禁用预加载
        mViewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mViewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return PagerFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return mTitles.length;
            }
        });
        mTopbgViewPeger.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mTopbgViewPeger.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @Override
            public int getItemCount() {
                return mTitles.length;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TopbgPagerFragment.newInstance(position);
            }
        });
    }

    public static void Into(){
        TestholekoHttpJson.getData(new HttpJsonResolution() {
            Handler handler = new Handler();
            @Override
            public void onFinish(final String title, final String imageurl, final String videourl) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        itv.setText(title);
                        setmInto.onFinish(title, imageurl, videourl);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    public interface SetInto{
        void onFinish(String title, String imgurl, String videourl);
    }
}
