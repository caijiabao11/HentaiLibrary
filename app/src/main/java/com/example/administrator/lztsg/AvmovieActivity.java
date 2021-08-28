package com.example.administrator.lztsg;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

public class AvmovieActivity extends AppCompatActivity {
    private WebView mWvMain;
    private Toolbar mToolbar;
    private ImageButton mImgButton;
    private FloatingActionButton mFloatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avmovie);
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
        mWvMain.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//缓存设置，直接加载网络数据
        mWvMain.getSettings().setAllowFileAccess(true); //设置可以访问文件
        mWvMain.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        mWvMain.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWvMain.getSettings().setBlockNetworkImage(false);
        mWvMain.getSettings().setDefaultTextEncodingName("UTF-8");//编码
        mWvMain.getSettings().setLoadWithOverviewMode(true);
        //加载网络url
        mWvMain.loadUrl("https:/www.javlibrary.com/cn/");
        mWvMain.setVerticalScrollBarEnabled(false);
        mWvMain.setHorizontalScrollBarEnabled(false);
        mWvMain.setWebViewClient(webClient);
        mWvMain.getSettings().setDomStorageEnabled(false);
        if (Build.VERSION.SDK_INT >= 21) {//同时允许HTTP和HTTPS
            mWvMain.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
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

    //点击返回上一页面而不是退出浏览器
    public boolean onKeyDown ( int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && mWvMain.canGoBack()) {
            mWvMain.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

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
                Animation anim = AnimationUtils.loadAnimation(AvmovieActivity.this,R.anim.rotate);
                mWvMain.reload();
                mFloatingActionButton.startAnimation(anim);
                Toast.makeText(AvmovieActivity.this, "少女祈祷中ing...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
