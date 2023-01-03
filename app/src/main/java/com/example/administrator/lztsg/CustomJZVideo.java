package com.example.administrator.lztsg;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;

public class CustomJZVideo extends JzvdStd {
    private boolean isLockScreen;
    private ImageView lockIv;
    private TextView tvSpeed;
    int currentSpeedIndex = 2;
    float starX, startY;
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
    public void init(Context context) {
        super.init(context);
        //获取自定义控件
        loadingProgressBar = findViewById(R.id.loading);
        lockIv = findViewById(R.id.lock);
        lockIv.setOnClickListener(this);
        tvSpeed = findViewById(R.id.tv_speed);
        tvSpeed.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.custon_videoview;
    }

    //这里应该还没有判断完  目前还没有测试出什么问题  这里是拦截父亲得一些事件比如滑动快进 改变亮度
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                starX = event.getX();
                startY = event.getY();
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (screen == SCREEN_FULLSCREEN && isLockScreen){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (screen == SCREEN_FULLSCREEN && isLockScreen){
                    //&& Math.abs(Math.abs(event.getX() - starX)) > ViewConfiguration.get(getContext()).getScaledTouchSlop()  && Math.abs(Math.abs(event.getY() - startY)) > ViewConfiguration.get(getContext()).getScaledTouchSlop()
                    if (event.getX() == starX || event.getY() == startY){
                        startDismissControlViewTimer();
                        onClickUiToggle();
                        bottomProgressBar.setVisibility(VISIBLE);
                    }
                    return true;
                }
                break;
        }
        return super.onTouch(v, event);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //控件点击事件
        switch (v.getId()) {
            case R.id.lock:
                if (screen == SCREEN_FULLSCREEN) {
                    lockIv.setTag(1);
                    if (!isLockScreen) {
                        isLockScreen = true;
                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        lockIv.setBackgroundResource(R.mipmap.lock);
                        dissmissControlView();
                    } else {
                        JZUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        isLockScreen = false;
                        lockIv.setBackgroundResource(R.mipmap.unlock);
                        bottomContainer.setVisibility(VISIBLE);
                        bottomProgressBar.setVisibility(GONE);
                        topContainer.setVisibility(VISIBLE);
                        startButton.setVisibility(VISIBLE);
                    }
                }
                break;
            case R.id.tv_speed:
                if (currentSpeedIndex == 6) {
                    currentSpeedIndex = 0;
                } else {
                    currentSpeedIndex += 1;
                }
                mediaInterface.setSpeed(getSpeedFromIndex(currentSpeedIndex));
                tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "X");
                jzDataSource.objects[0] = currentSpeedIndex;
                break;
        }
    }

    private float getSpeedFromIndex(int index) {
        float ret = 0f;
        if (index == 0) {
            ret = 0.5f;
        } else if (index == 1) {
            ret = 0.75f;
        } else if (index == 2) {
            ret = 1.0f;
        } else if (index == 3) {
            ret = 1.25f;
        } else if (index == 4) {
            ret = 1.5f;
        } else if (index == 5) {
            ret = 1.75f;
        } else if (index == 6) {
            ret = 2.0f;
        }
        return ret;
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        if (screen == SCREEN_FULLSCREEN) {
            if (!isLockScreen) {
                if (bottomContainer.getVisibility() == View.VISIBLE) {
                    lockIv.setVisibility(View.VISIBLE);
                } else {
                    lockIv.setVisibility(View.GONE);
                }
            } else {
                if ((int) lockIv.getTag() == 1) {
                    bottomProgressBar.setVisibility(GONE);
                    if (lockIv.getVisibility() == View.GONE) {
                        lockIv.setVisibility(View.VISIBLE);
                    } else {
                        lockIv.setVisibility(View.GONE);
                    }

                }
            }

        }
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        if (isLockScreen) {
            bottomContainer.setVisibility(GONE);
            topContainer.setVisibility(GONE);
            startButton.setVisibility(GONE);
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        lockIv.setVisibility(View.GONE);
        tvSpeed.setVisibility(View.GONE);
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        lockIv.setBackgroundResource(R.mipmap.unlock);
        lockIv.setVisibility(View.VISIBLE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            tvSpeed.setVisibility(View.VISIBLE);

        if (jzDataSource.objects == null) {
            Object[] object = {2};
            jzDataSource.objects = object;
            currentSpeedIndex = 2;
        } else {
            currentSpeedIndex = (int) jzDataSource.objects[0];
        }
        if (currentSpeedIndex == 2) {
            tvSpeed.setText("倍速");
        } else {
            tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "X");
        }

    }

    @Override
    public void dissmissControlView() {
        super.dissmissControlView();
        post(() -> {
            if (screen == SCREEN_FULLSCREEN) {
                lockIv.setVisibility(View.GONE);
                bottomProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        if (screen == SCREEN_FULLSCREEN) {
            bottomProgressBar.setVisibility(GONE);
            if (isLockScreen) {
                topContainer.setVisibility(GONE);
                bottomContainer.setVisibility(GONE);
                startButton.setVisibility(GONE);
            } else {
                topContainer.setVisibility(VISIBLE);
                bottomContainer.setVisibility(VISIBLE);
                startButton.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
        if (screen == SCREEN_FULLSCREEN) {
            bottomProgressBar.setVisibility(GONE);
            lockIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen, Class mediaInterfaceClass) {
        super.setUp(jzDataSource, screen, mediaInterfaceClass);
        //设置播放时屏幕状态
        titleTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        //播放完毕显示最后一帧画面
        posterImageView.setVisibility(GONE);
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
