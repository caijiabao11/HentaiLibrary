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
import com.example.administrator.lztsg.utils.DensityUtils;
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
    public static Button mBut_more, mBut_live, mBut_mass, mBut_label,mBut_random;
    public static EditText mSearch;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private RelativeLayout mRelativeLayoutSearch;
    private LifanMoreFragment mLifanMoreFragment = new LifanMoreFragment();
    private LifanLiveFragment mLifanLiveFragment = new LifanLiveFragment();
    private LifanMassFragment mLifanMassFragment = new LifanMassFragment();
    private LifanTagFragment mLifanTagFragment = new LifanTagFragment();
    private LifanRandomFragment mLifanRandomFragment = new LifanRandomFragment();
    private LifanMass_putFragment mLifanMass_putFragment;
    private LifanTag_putFragment mLifanTag_putFragment;
    public static Fragment currentFragment;
    private LinearLayout mTab_main;
    private Toolbar mToolbar;
    private List<MultipleItem> mData, mAllData;
    public static Fragment mFrag1;
    private static Fragment mFrag2;
    private static Fragment mFrag3;
    private static Fragment mFrag4;
    private static Fragment mFrag5;
    public static int nowselecttab;
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
        mBut_random.setOnClickListener(this);
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
        mBut_random = findViewById(R.id.but_random);
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
                if (!(currentFragment instanceof LifanTagFragment)){
                    doChangeColor(mSearch.toString().trim());
                }
            }

            private void doChangeColor(String text) {
                String data = mSearch.getText().toString();
                switch (nowselecttab) {
                    case 0:
                        mData = LifanMoreFragment.mData;
                        mAllData = LifanMoreFragment.mAllData;
                        break;
                    case 1:
                        mData = LifanLiveFragment.mData;
                        mAllData = LifanLiveFragment.mAllData;
                        break;
                    case 2:
                        if (currentFragment instanceof LifanMassFragment){
                            mData = LifanMassFragment.mData;
                            mAllData = LifanMassFragment.mAllData;
                        } else{
                            mData = LifanMass_putFragment.mData;
                            mAllData = LifanMass_putFragment.mAllData;
                        }
                        break;
                    case 3:
                        if (currentFragment instanceof LifanTagFragment){
                            mData = LifanTagFragment.mData;
                            mAllData = LifanTagFragment.mAllData;
                        } else{
                            mData = LifanTag_putFragment.mData;
                            mAllData = LifanTag_putFragment.mAllData;
                        }
                        break;
                }
                if (mData != null){
                    mData.clear();
                }
                // 判断当前展示的Fragment是哪个Fragment的实例
                if (currentFragment instanceof LifanMoreFragment ||
                        currentFragment instanceof LifanLiveFragment ||
                        currentFragment instanceof LifanMassFragment ||
                        currentFragment instanceof LifanMass_putFragment) {

                    querySelectArrTit(data,mAllData);

                } else if (currentFragment instanceof LifanMass_putFragment){
                    querySelectArrTit(data,mAllData);
                }

                //刷新
                if (mLifanMoreFragment.mLinearAdaoter != null) {
                    mLifanMoreFragment.mLinearAdaoter.notifyDataSetChanged();
                }
                if (mLifanLiveFragment.mLinearAdaoter != null) {
                    mLifanLiveFragment.mLinearAdaoter.notifyDataSetChanged();
                }
                if (mLifanMassFragment.mLinearAdaoter != null) {
                    mLifanMassFragment.mLinearAdaoter.notifyDataSetChanged();
                }
                if (mLifanMass_putFragment.mLinearAdaoter != null){
                    mLifanMass_putFragment.mLinearAdaoter.notifyDataSetChanged();
                }
                if (mLifanTagFragment.mLinearAdaoter != null) {
                    mLifanTagFragment.mLinearAdaoter.notifyDataSetChanged();
                }
                if (mLifanTag_putFragment.mLinearAdaoter != null) {
                    mLifanTag_putFragment.mLinearAdaoter.notifyDataSetChanged();
                }
            }

            private void querySelectArrTit(String data, List<MultipleItem> mAllData) {
                //搜索title
                for (MultipleItem item : mAllData) {
                    final Item value = (Item) item;
                    //.startsWith   以指定字符串开头筛选（精准搜索）
                    //.contains     以字符串中是否存在筛选（模糊搜索）
                    if (value.getTitle().contains(data)) {
                        mData.add(value);
                    }
                }
            }

            private void querySelectArrTag(String data, List<MultipleItem> mAllData) {
                //搜索mass
                for (MultipleItem item : mAllData) {
                    final Item value = (Item) item;
                    //.startsWith   以指定字符串开头筛选（精准搜索）
                    //.contains     以字符串中是否存在筛选（模糊搜索）
                    if (value.getmTag().contains(data)) {
                        mData.add(value);
                    }
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
                nowselecttab = 2;
                break;
            case R.id.but_label:
                selectTab(3);
                nowselecttab = 3;
                break;
            case R.id.but_random:
                selectTab(4);
                nowselecttab = 4;
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
                if (mFrag1 == null) {
                    mFrag1 = new LifanMoreFragment();
                    currentFragment = mFrag1;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag1);
                break;
            case 1:
                mBut_live.setBackground(getResources().getDrawable(R.drawable.but_like_radius_bg_black));
                if (mFrag2 == null) {
                    mFrag2 = new LifanLiveFragment();
                    currentFragment = mFrag2;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag2);
                break;
            case 2:
                mBut_mass.setBackground(getResources().getDrawable(R.drawable.but_mass_radius_bg_black));
                if (mFrag3 == null) {
                    mFrag3 = new LifanMassFragment();
                    currentFragment = mFrag3;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag3);
                break;
            case 3:
                mBut_label.setBackground(getResources().getDrawable(R.drawable.but_label_radius_bg_black));
                if (mFrag4 == null) {
                    mFrag4 = new LifanTagFragment();
                    currentFragment = mFrag4;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag4);
                break;
            case 4:
                mBut_random.setBackground(getResources().getDrawable(R.drawable.but_random_radius_bg_black));
                if (mFrag5 == null) {
                    mFrag5 = new LifanRandomFragment();
                    currentFragment = mFrag5;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag5);
                break;
        }
        fragmentTransaction.commit();
        if (!(currentFragment instanceof LifanTagFragment)){
            if (LifanTagFragment.butcode == 1){
                LifanTagFragment.butcode = 0;
                LifanTagFragment.OnAnimButonTag(-DensityUtils.dip2px(this,150),0);
            }
            mSearch.getText().clear();//清空输入框
        }

    }

    //将全部的Fragment隐藏
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (mFrag1 != null) {
            fragmentTransaction.hide(mFrag1);
        }
        if (mFrag2 != null) {
            fragmentTransaction.hide(mFrag2);
        }
        if (mFrag3 != null) {
            fragmentTransaction.hide(mFrag3);
        }
        if (mFrag4 != null) {
            fragmentTransaction.hide(mFrag4);
        }
        if (mFrag5 != null) {
            fragmentTransaction.hide(mFrag5);
        }
    }

    //将全部Button置为灰色
    private void resetImgs() {
        mBut_more.setBackground(getResources().getDrawable(R.drawable.but_more_radius_bg_white));
        mBut_live.setBackground(getResources().getDrawable(R.drawable.but_like_radius_bg_white));
        mBut_mass.setBackground(getResources().getDrawable(R.drawable.but_mass_radius_bg_white));
        mBut_label.setBackground(getResources().getDrawable(R.drawable.but_label_radius_bg_white));
        mBut_random.setBackground(getResources().getDrawable(R.drawable.but_random_radius_bg_white));
    }

    //监听返回键事件，进行返回上个事务处理
    @Override
    public void onBackPressed() {
        // 检查后退栈是否为空
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // 弹出后退栈中的上一个事务
            getSupportFragmentManager().popBackStack();
        } else {
            // 如果后退栈为空，则执行默认的返回操作
            super.onBackPressed();
        }
    }
}