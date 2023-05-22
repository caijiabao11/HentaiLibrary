package com.example.administrator.lztsg.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.administrator.lztsg.R;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import static com.example.administrator.lztsg.R.id.drawer_layout;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView mWvMain;
    private ImageView mIvbtnrest;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setView();
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        onTabBtnrestOnClick();
    }

    private void init(){
        mWvMain = findViewById(R.id.wv);
        mIvbtnrest = findViewById(R.id.tabbtn_more);
        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
    }

    public void setView(){
        mWvMain.setBackgroundColor(0);
        mWvMain.getSettings().setBuiltInZoomControls(false);//设置内置的缩放控件。若为false，则该WebView不可缩放
        //设置自适应屏幕
        mWvMain.getSettings().setUseWideViewPort(true);//自适应全屏
        mWvMain.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWvMain.getSettings().setJavaScriptEnabled(true);//支持js
        mWvMain.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWvMain.getSettings().setAllowFileAccess(true); //设置可以访问文件
        mWvMain.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        mWvMain.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWvMain.getSettings().setBlockNetworkImage(false);
        mWvMain.getSettings().setDefaultTextEncodingName("UTF-8");//编码
        mWvMain.getSettings().setLoadWithOverviewMode(true);
        //文件权限
        mWvMain.getSettings().setAllowFileAccess(true);
        mWvMain.getSettings().setAllowFileAccessFromFileURLs(true);
        mWvMain.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWvMain.getSettings().setAllowContentAccess(true);
        //缓存相关
        mWvMain.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先加载本地缓存，无缓存就网络加载
        mWvMain.getSettings().setDomStorageEnabled(true);//开启DOM storage API功能
        mWvMain.getSettings().setDatabaseEnabled(true);//开启database storeage API功能
        String cacheDirPath = getApplicationContext().getCacheDir().getPath();//缓存路径
        Log.i("cachePath", cacheDirPath);
        mWvMain.getSettings().setAppCachePath(cacheDirPath);//设置AppCaches缓存路径
        mWvMain.getSettings().setAppCacheEnabled(true);//开启AppCaches功能
        //加载网络url
        mWvMain.loadUrl("https://caijiabao11.github.io/");
        mWvMain.setVerticalScrollBarEnabled(false);
        mWvMain.setHorizontalScrollBarEnabled(false);
        mWvMain.setWebViewClient(webClient);
        mWvMain.getSettings().setDomStorageEnabled(false);
        if (Build.VERSION.SDK_INT >= 21) {//同时允许HTTP和HTTPS
            mWvMain.getSettings().setMixedContentMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }
    //Web视图
    WebViewClient webClient = new WebViewClient(){
        //加载错误时回调
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view,request,error);
        }

        //设置不用系统浏览器打开,直接显示在当前Webview
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //侧滑栏列表跳转
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_lifan){
            final Intent intent = new Intent(this,LifanActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_avmovie){
            final Intent intent = new Intent(this, AvmovieActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mmd){
            final Intent intent = new Intent(this,MmdActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting){
            final Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //在这里处理

        }return super.onKeyDown(keyCode, event);
    }
    //更多功能按钮点击事件
    public void onTabBtnrestOnClick(){
        mIvbtnrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this,MoreActivity.class);
                startActivity(intent);
            }
        });
    }
}
