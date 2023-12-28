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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.utils.HideUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AsmrActivity extends AppCompatActivity  implements View.OnClickListener {
    public static ImageButton mBut_more, mBut_live, mBut_mass, mBut_label,mBut_vas,mBut_random,mBut_Sleepmore;
    public static Fragment currentFragment;
    public static Fragment mFrag1;
    private static Fragment mFrag2;
    private static Fragment mFrag3;
    private static Fragment mFrag4;
    private static Fragment mFrag5;
    private static Fragment mFrag6;
    private static Fragment mFrag7;
    private static Fragment mSearchFrag;
    public static MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
    public static EditText mSearch;
    public static String data;
    public static int nowselecttab;
    // 创建BroadcastReceiver实例
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals("com.lztsg.musicplayer_stop")) {
//                Log.d("获取广播", "暂停音乐");
//                MusicPlayerFragment.but_player.setImageResource(R.drawable.ic_music_play_24);
//            }
//            if (intent.getAction().equals("com.lztsg.musicplayer_play")) {
//
//                Log.d("获取广播", "播放音乐");
//                MusicPlayerFragment.but_player.setImageResource(R.drawable.ic_music_pause_24);
//            }
//        }
//    };
    public static int[] firstStaggeredGridPosition = {0, 0};
    public static int[] lastStaggeredGridPosition = {0, 0};
    // 标志用于控制是否执行TextWatcher中的操作
    public static boolean shouldExecuteTextWatcher = true;
    private static int total = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asmr);
        init();
        initEvents();
        onSearch();
        addFragment(musicPlayerFragment);
        //默认选第一个Tab
        selectTab(0);


        // 创建IntentFilter并添加广播的Action
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.lztsg.musicplayer_play");
//        intentFilter.addAction("com.lztsg.musicplayer_stop");
//        // 注册BroadcastReceiver
//        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void addFragment(MusicPlayerFragment fragment) {
        // 获取FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开启一个Fragment事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 替换当前的Fragment为新的Fragment
        fragmentTransaction.replace(R.id.fragment, fragment);

//        // 添加该Fragment事务进返回栈中
//        fragmentTransaction.addToBackStack(null);

        // 提交事务
        fragmentTransaction.commit();
    }

    private void init() {
        mSearch = findViewById(R.id.edt_search);
        mBut_more = findViewById(R.id.but_more);
        mBut_live = findViewById(R.id.but_live);
        mBut_mass = findViewById(R.id.but_mass);
        mBut_label = findViewById(R.id.but_label);
        mBut_vas = findViewById(R.id.but_vas);
        mBut_random = findViewById(R.id.but_random);
        mBut_Sleepmore = findViewById(R.id.but_sleepmore);
    }

    private void initEvents() {
        mBut_more.setOnClickListener(this);
        mBut_live.setOnClickListener(this);
        mBut_mass.setOnClickListener(this);
        mBut_label.setOnClickListener(this);
        mBut_vas.setOnClickListener(this);
        mBut_random.setOnClickListener(this);
        mBut_Sleepmore.setOnClickListener(this);
    }

    //载入数据
    private void initData(){
    }

    @Override
    public void onClick(View v) {
        resetImgs();//按钮全变成灰色
        switch (v.getId()) {
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
            case R.id.but_vas:
                selectTab(4);
                nowselecttab = 4;
                break;
            case R.id.but_random:
                selectTab(5);
                nowselecttab = 5;
                break;
            case R.id.but_sleepmore:
                selectTab(6);
                nowselecttab = 6;
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
                    mFrag1 = new AsmrMoreFragment();
                    currentFragment = mFrag1;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag1);
                break;
            case 1:
                mBut_live.setBackground(getResources().getDrawable(R.drawable.but_like_radius_bg_black));
                if (mFrag2 == null) {
                    mFrag2 = new AsmrLiveFragment();
                    currentFragment = mFrag2;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag2);
                break;
            case 2:
                mBut_mass.setBackground(getResources().getDrawable(R.drawable.but_mass_radius_bg_black));
                if (mFrag3 == null) {
                    mFrag3 = new AsmrMassFragment();
                    currentFragment = mFrag3;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag3);
                break;
            case 3:
                mBut_label.setBackground(getResources().getDrawable(R.drawable.but_label_radius_bg_black));
                if (mFrag4 == null) {
                    mFrag4 = new AsmrTagFragment();
                    currentFragment = mFrag4;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag4);
                break;
            case 4:
                mBut_vas.setBackground(getResources().getDrawable(R.drawable.but_voiceactor_radius_bg_black));
                if (mFrag5 == null) {
                    mFrag5 = new AsmrVasFragment();
                    currentFragment = mFrag5;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag5);
                break;
            case 5:
                mBut_random.setBackground(getResources().getDrawable(R.drawable.but_random_radius_bg_black));
                if (mFrag6 == null) {
                    mFrag6 = new AsmrRandomFragment();
                    currentFragment = mFrag6;
                }
                fragmentTransaction.replace(R.id.fragmentLayout, mFrag6);
                break;
            case 6:
                mBut_Sleepmore.setBackground(getResources().getDrawable(R.drawable.but_sleep_radius_bg_black));
                if (mFrag7 == null) {
                    mFrag7 = new AsmrSleepMoreFragment();
                    currentFragment = mFrag7;
                }

                fragmentTransaction.replace(R.id.fragmentLayout, mFrag7);
                break;
        }
//        showFragments(fragmentTransaction,index);
        fragmentTransaction.commit();
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
    }

    //根据index显示对应的Fragment
    private void showFragments(FragmentTransaction fragmentTransaction,int index) {
        if (index == 0) {
            if (mFrag1 != null) {
                fragmentTransaction.show(mFrag1);
            }
        } else if (index == 1) {
            if (mFrag2 != null) {
                fragmentTransaction.show(mFrag2);
            }
        } else if (index == 2) {
            if (mFrag3 != null) {
                fragmentTransaction.show(mFrag3);
            }
        } else if (index == 3) {
            if (mFrag4 != null) {
                fragmentTransaction.show(mFrag4);
            }
        }
    }

    //将全部Button置为灰色
    private void resetImgs() {
        mBut_more.setBackground(getResources().getDrawable(R.drawable.but_more_radius_bg_white));
        mBut_live.setBackground(getResources().getDrawable(R.drawable.but_like_radius_bg_white));
        mBut_mass.setBackground(getResources().getDrawable(R.drawable.but_mass_radius_bg_white));
        mBut_label.setBackground(getResources().getDrawable(R.drawable.but_label_radius_bg_white));
        mBut_vas.setBackground(getResources().getDrawable(R.drawable.but_voiceactor_radius_bg_white));
        mBut_random.setBackground(getResources().getDrawable(R.drawable.but_random_radius_bg_white));
        mBut_Sleepmore.setBackground(getResources().getDrawable(R.drawable.but_sleep_radius_bg_white));
    }

    //监听返回键事件，进行返回上个事务处理
    @Override
    public void onBackPressed() {
        // 检查后退栈是否为空
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            String text = mSearch.getText().toString();
            if (!text.isEmpty()){
                shouldExecuteTextWatcher = false;
                mSearch.getText().clear();
                shouldExecuteTextWatcher = true;
            }
            // 弹出后退栈中的上一个事务
            getSupportFragmentManager().popBackStack();
        } else {
            // 如果后退栈为空，则执行默认的返回操作
            super.onBackPressed();
        }
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
                            AsmrActivity.this.getCurrentFocus().getWindowToken(),
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
                if (data != null && data != "" && shouldExecuteTextWatcher) {
//                    mTabLayout.removeAllTabs();
                    //热门浏览界面链接
                    String path = "https://api.asmr.one/api/search/" + data;
                    AsmrSearchFragment.path = path;

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    mSearchFrag = new AsmrSearchFragment();
                    fragmentTransaction.replace(R.id.fragmentLayout,mSearchFrag);
                    currentFragment = mSearchFrag;
                    fragmentTransaction.addToBackStack(null);//添加到返回栈
                    fragmentTransaction.commit();

                }
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
}
