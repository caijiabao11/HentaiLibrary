package com.example.administrator.lztsg;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class MmdActivity extends AppCompatActivity {
    private WebView mWvMain;
    private Toolbar mToolbar;
    private ImageButton mImgButton;
    private FloatingActionButton mFloatingActionButton;
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmd);
        bindViews();
        setview();
        ImageButtonOnClick();
        FloatingActionButtonreloadOnClick();
        setSupportActionBar(mToolbar);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    private void bindViews(){
        mWvMain = findViewById(R.id.wv);
        mToolbar = findViewById(R.id.toolbar);
        mImgButton = findViewById(R.id.imgbutton);
        mFloatingActionButton = findViewById(R.id.fbutton);
    }
    private void setview() {
        //缩放操作
        mWvMain.getSettings().setSupportZoom(true);//支持缩放，默认为true。是下面那个的前提。
        mWvMain.getSettings().setBuiltInZoomControls(true);//设置内置的缩放控件。若为false，则该WebView不可缩放
        mWvMain.getSettings().setDisplayZoomControls(false);//隐藏原生的缩放控件
        //设置自适应屏幕
        mWvMain.getSettings().setUseWideViewPort(true);//自适应全屏
        mWvMain.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWvMain.getSettings().setJavaScriptEnabled(true);//支持js
        mWvMain.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWvMain.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片
        mWvMain.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存设置，直接加载网络数据
        mWvMain.getSettings().setAllowFileAccess(true); //设置可以访问文件
        mWvMain.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWvMain.getSettings().setAppCacheEnabled(true);//开启H5(APPCache)缓存功能
        mWvMain.getSettings().setBlockNetworkImage(false);
        mWvMain.getSettings().setDefaultTextEncodingName("UTF-8");//编码
        mWvMain.getSettings().setLoadWithOverviewMode(true);
        mWvMain.getSettings().setAllowUniversalAccessFromFileURLs(true);//允许跨域访问
        mWvMain.getSettings().setDomStorageEnabled(true);// 开启 DOM storage 功能
        //加载网络url
        mWvMain.loadUrl("https://www.iwara.tv/?language=zh-hans");
        mWvMain.setVerticalScrollBarEnabled(false);
        mWvMain.setHorizontalScrollBarEnabled(false);
        mWvMain.setWebViewClient(webClient);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//同时允许HTTP和HTTPS
            mWvMain.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //视频播放相关的方法
        mWvMain.setWebChromeClient(new WebChromeClient() {
            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(MmdActivity.this);
                frameLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }
        });
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


    //返回箭头点击事件
    public void ImageButtonOnClick(){
        mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //悬浮按钮刷新点击事件
    public void FloatingActionButtonreloadOnClick(){
        mFloatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mWvMain.reload();
                Toast.makeText(MmdActivity.this, "少女祈祷中ing...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //视频播放全屏
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        MmdActivity.this.getWindow().getDecorView();
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(MmdActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    //隐藏视频全屏
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mWvMain.setVisibility(View.VISIBLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 取消全屏
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    // 全屏容器界面
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    //设置全屏
    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //点击返回上一页面而不是退出浏览器 优先级:视频播放全屏-网页回退-关闭页面
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (customView != null) {
                    hideCustomView();
                } else if (keyCode == KeyEvent.KEYCODE_BACK && mWvMain.canGoBack()) {
                    mWvMain.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}