package com.example.administrator.lztsg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JzvdStd;

public class CustomJZMp3 extends JzvdStd {
    Context context;
    public CustomJZMp3(Context context) {
        super(context);
        this.context = context;
    }

    public CustomJZMp3(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    public int getLayoutId() {
        return R.layout.custon_mp3view;
    }

    @Override
    public void onClick(View v) {
    }

    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();

    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();

    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();

    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();

    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }
}
