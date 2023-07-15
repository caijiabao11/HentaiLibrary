package com.example.administrator.lztsg.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.LongRunningService;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.anima.ZoomOutPageTransformer;
import com.example.administrator.lztsg.httpjson.HentaiJoiHttpJson;
import com.example.administrator.lztsg.utils.MobileInfoUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


public class TestholekilnActivity extends AppCompatActivity{
    private static TextView itv;
    private WebView mWebView;
    private ImageView mImagetoing;
    private Switch mSwitch;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2,mTopbgViewPeger;
    private SharedPreferences sp;
    private String[] mTitles={"地狱寸止","颅内高潮","节奏大师"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testholekiln);
        init();
        LinearRecyclerView();
        initData();
    }

    private void init() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager2 = findViewById(R.id.v_pager);
        mTopbgViewPeger = findViewById(R.id.vtop_pager);
        itv = findViewById(R.id.itv);
        mImagetoing = findViewById(R.id.iv_itembg);
        mWebView = findViewById(R.id.web_gethtml);
        mSwitch = findViewById(R.id.daka_switch);
    }

    //载入数据
    private void initData(){
        Glide.with(TestholekilnActivity.this)
                .load(R.drawable.topitemtoing)
                .centerCrop()
                .into(mImagetoing);
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
                textView.setTextColor(getResources().getColor(R.color.colorTextTitle));
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

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new HentaiJoiHttpJson(), "HTMLOUT");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                super.onPageFinished(view, url);
            }
        });
        mWebView.loadUrl("https://www.xvideos.com/channels/wutfaced#_tabVideos");

        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (sp != null){
            boolean key = sp.getBoolean("daka_switch",false);
            mSwitch.setChecked(key);
        }
        //自启动权限弹窗
        boolean ziqiChecked = sp.getBoolean("ziqiChecked",true);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //开启时

                    //保存设置
                    sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("daka_switch",true);
                    editor.commit();

                    if (ziqiChecked != false){
                        //只提示一次

                        jumpStartInterface();
                        editor.putBoolean("ziqiChecked",false);
                        editor.commit();
                    }

                    startService(new Intent(MyApplication.getContext(), LongRunningService.class));

                }else{
                    //关闭时

                    sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("daka_switch",false);
                    editor.commit();

                    stopService(new Intent(MyApplication.getContext(),LongRunningService.class));
                }
            }
        });
    }

    private void jumpStartInterface() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("开启时自动提醒需要“自启动”权限，请注意授予自启权限")
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MobileInfoUtils.jumpStartInterface(TestholekilnActivity.this);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }catch (Exception e){
        }
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
        mTopbgViewPeger.setPageTransformer(new ZoomOutPageTransformer());
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
}
