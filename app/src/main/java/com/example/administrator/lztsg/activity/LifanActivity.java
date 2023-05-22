package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.utils.HideUtils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class LifanActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LifanActivity";
    private LinearAdapter mLinearAdaoter;
    private LinearAdapter.ItemHolder mItemHolder;
    private ImageButton mImgButton, mSearchButton;
    private Button mBut_more, mBut_live, mBut_mass, mBut_label;
    private EditText mSearch;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private RelativeLayout mRelativeLayoutSearch;
    private LifanMoreFragment mLifanMoreFragment = new LifanMoreFragment();
    private LifanLiveFragment mLifanLiveFragment = new LifanLiveFragment();
    private LinearLayout mTab_main;
    private Toolbar mToolbar;
    private List<MultipleItem> mData, mAllData;
    private Fragment mFrag1;
    private Fragment mFrag2;
    private Fragment mFrag3;
    private Fragment mFrag4;
    private int nowselecttab;
    public int[] firstStaggeredGridPosition = {0, 0};
    public int[] lastStaggeredGridPosition = {0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifan);
        setSupportActionBar(mToolbar);
        init();
        onSearch();
        initEvents();

//        mImgButton.setOnClickListener(this);

        //默认选第一个Tab
        selectTab(0);
    }

    private void initEvents() {
        mBut_more.setOnClickListener(this);
        mBut_live.setOnClickListener(this);
        mBut_mass.setOnClickListener(this);
        mBut_label.setOnClickListener(this);
    }

    private void init() {
        mRecyclerView = findViewById(R.id.run_main);
        mSearch = findViewById(R.id.edt_search);
        mImgButton = findViewById(R.id.imgbutton);
        mToolbar = findViewById(R.id.toolbar);
        mTab_main = findViewById(R.id.tab_main);

        mBut_more = findViewById(R.id.but_more);
        mBut_live = findViewById(R.id.but_live);
        mBut_mass = findViewById(R.id.but_mass);
        mBut_label = findViewById(R.id.but_label);
    }

    //载入图片+标题
    private void initData() {

        this.mData = mLifanMoreFragment.mData;
        this.mAllData = mLifanMoreFragment.mAllData;
    }

    @Override
    protected void onPause() {
        super.onPause();

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
//        mSearch.setWidth(885);

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            //监听搜索按钮
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) mSearch.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            LifanActivity.this.getCurrentFocus().getWindowToken(),
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
                doChangeColor(mSearch.toString().trim());

            }

            private void doChangeColor(String text) {
                String data = mSearch.getText().toString();
                switch (nowselecttab){
                    case 0:
                        mData = LifanMoreFragment.mData;
                        mAllData = LifanMoreFragment.mAllData;
                        break;
                    case 1:
                        mData = LifanLiveFragment.mData;
                        mAllData = LifanLiveFragment.mAllData;
                        break;
                }
                mData.clear();

                for (MultipleItem item : mAllData) {
                    final Item value = (Item) item;
                    //.startsWith   以指定字符串开头筛选（精准搜索）
                    //.contains     以字符串中是否存在筛选（模糊搜索）
                    if (value.getTitle().contains(data)) {
                        mData.add(value);
                    }
                }
                //刷新
                if( mLifanMoreFragment.mLinearAdaoter != null){
                    mLifanMoreFragment.mLinearAdaoter.notifyDataSetChanged();
                }
                if (mLifanLiveFragment.mLinearAdaoter != null){
                    mLifanLiveFragment.mLinearAdaoter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        resetImgs();//按钮全变成灰色
        switch (v.getId()) {
//            case R.id.imgbutton:
//                if (LifanMoreFragment.mQieHuan) {
////                    LifanMoreFragment.mQieHuan = false;
//                    mTab_main.setVisibility(View.GONE);
//                } else if (LifanMoreFragment.mQieHuan == false) {
////                    LifanMoreFragment.mQieHuan = true;
//                    mTab_main.setVisibility(View.VISIBLE);
//                }
////                mLifanMoreFragment.LinearRecyclerView(LifanMoreFragment.mQieHuan);
//                break;
            case R.id.but_more:
                selectTab(0);
                nowselecttab = 0;
                break;
            case R.id.but_live:
                selectTab(1);
                nowselecttab = 1;
                break;
            case R.id.but_mass:
                selectTab(2);
                break;
            case R.id.but_label:
                selectTab(3);
                break;
        }
    }

    //进行选中Tab的处理
    private void selectTab(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                mBut_more.setBackground(getResources().getDrawable(R.drawable.but_more_radius_bg_black));
                mFrag1 = new LifanMoreFragment();
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag1);
//                if (mFrag1 == null) {
//                    mFrag1 = new LifanMoreFragment();
//                    fragmentTransaction.add(R.id.fragmentLayout, mFrag1);
//                } else {
//                    fragmentTransaction.show(mFrag1);
//                }
                break;
            case 1:
                mBut_live.setBackground(getResources().getDrawable(R.drawable.but_like_radius_bg_black));
                mFrag2 = new LifanLiveFragment();
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag2);
//                if (mFrag2 == null) {
//                    mFrag2 = new LifanLiveFragment();
//                    fragmentTransaction.add(R.id.fragmentLayout, mFrag2);
//                } else {
//                    fragmentTransaction.show(mFrag2);
//                }
                break;
            case 2:
                mBut_mass.setBackground(getResources().getDrawable(R.drawable.but_mass_radius_bg_black));
                if (mFrag3 == null) {
                    mFrag3 = new LifanLiveFragment();
                    fragmentTransaction.add(R.id.fragmentLayout, mFrag3);
                } else {
                    fragmentTransaction.show(mFrag3);
                }
                break;
            case 3:
                mBut_label.setBackground(getResources().getDrawable(R.drawable.but_label_radius_bg_black));
                if (mFrag4 == null) {
                    mFrag4 = new LifanLiveFragment();
                    fragmentTransaction.add(R.id.fragmentLayout, mFrag4);
                } else {
                    fragmentTransaction.show(mFrag4);
                }
                break;
        }
        fragmentTransaction.commit();
        mSearch.getText().clear();//清空输入框
    }

    //将全部的Fragment隐藏
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (mFrag1 != null) {
            fragmentTransaction.hide(mFrag1);
        }
        if (mFrag2 != null) {
            fragmentTransaction.hide(mFrag2);
//            fragmentTransaction.remove(mFrag2);
        }
        if (mFrag3 != null) {
            fragmentTransaction.hide(mFrag3);
        }
        if (mFrag4 != null) {
            fragmentTransaction.hide(mFrag4);
        }
    }

    //将全部Button置为灰色
    private void resetImgs() {
        mBut_more.setBackground(getResources().getDrawable(R.drawable.but_more_radius_bg_white));
        mBut_live.setBackground(getResources().getDrawable(R.drawable.but_like_radius_bg_white));
        mBut_mass.setBackground(getResources().getDrawable(R.drawable.but_mass_radius_bg_white));
        mBut_label.setBackground(getResources().getDrawable(R.drawable.but_label_radius_bg_white));
    }

}