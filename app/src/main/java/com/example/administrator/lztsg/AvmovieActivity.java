package com.example.administrator.lztsg;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AvmovieActivity extends AppCompatActivity {
    private WebView mWvMain;
    private View mErrorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avmovie);
        mWvMain = (WebView) findViewById(R.id.wv);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setview();
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
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showErrorPage();
            }
            //设置不用系统浏览器打开,直接显示在当前Webview
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
    };

    boolean mIsErrorPage;
            private void showErrorPage() {
                LinearLayout webParemtView = (LinearLayout)mWvMain.getParent();
                initErrorPage();
                while (webParemtView.getChildCount()>1){
                    webParemtView.removeViewAt(0);
                }
                @SuppressWarnings("deprecation")
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewPager.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.FILL_PARENT);
                webParemtView.addView(mErrorView, 0, lp);
                mIsErrorPage = true;
            }

            protected void hideErrorPage() {
                LinearLayout webParentView = (LinearLayout)mWvMain.getParent();
                mIsErrorPage = false;
                while (webParentView.getChildCount() > 1) {
                    webParentView.removeViewAt(0);
                }
            }

            protected void initErrorPage() {
                if (mErrorView == null) {
                    mErrorView = View.inflate(this,R.layout.activity_error,null);
                    RelativeLayout layout = (RelativeLayout)mErrorView.findViewById(R.id.online_error_btn_retry);
                    layout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            mWvMain.reload();
                        }
                    });
                    mErrorView.setOnClickListener(null);
                }
            }
    //点击返回上一页面而不是退出浏览器
        public boolean onKeyDown ( int keyCode, KeyEvent event){
            if (keyCode == KeyEvent.KEYCODE_BACK && mWvMain.canGoBack()) {
                mWvMain.goBack();
                return true;
            }

            return super.onKeyDown(keyCode, event);
        }
    //添加返回箭头
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return onOptionsItemSelected(item);
    }
}
