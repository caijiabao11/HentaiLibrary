package com.example.administrator.lztsg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

public class CustomJZVideo extends JzvdStd {
    Context context;
    public CustomJZVideo(Context context) {
        super(context);
        this.context = context;
    }

    public CustomJZVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.custon_videoview;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        //获取自定义控件
        loadingProgressBar = findViewById(R.id.loading);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //控件点击事件
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen, Class mediaInterfaceClass) {
        super.setUp(jzDataSource, screen, mediaInterfaceClass);
        //设置播放时屏幕状态
        titleTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void gotoFullscreen() {
        //全屏时
        super.gotoFullscreen();
        titleTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void gotoNormalScreen() {
        //不全屏时
        super.gotoNormalScreen();
        titleTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        //并播放完后这里切换下一个视频的url
    }


}
