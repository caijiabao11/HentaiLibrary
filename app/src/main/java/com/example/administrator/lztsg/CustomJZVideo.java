package com.example.administrator.lztsg;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class CustomJZVideo extends JzvdStd {
    private boolean isLockScreen;
    private ImageView lockIv;
    private TextView tvSpeed;
    private VerticalTabLayout mTabLayout;
    private LinearLayout layout_bottom,start_layout;
    private RelativeLayout layout_top;
    private TabLayout layout;
    private PopupWindow popupWindow;
    private TimerTask mDismissControlViewTimerTask;
    private long startTime,diffTime;
    int currentSpeedIndex = 2;
    float starX, startY;
    private String[] mSpeedIndex={"2.0X","1.5X","1.2X","1.0X","0.7X","0.5X"};
    Context context;

    public CustomJZVideo(Context context) {
        super(context);
        this.context = context;
    }

    public CustomJZVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initData();
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

        layout_bottom = findViewById(R.id.layout_bottom);
        layout_top = findViewById(R.id.layout_top);
        start_layout = findViewById(R.id.start_layout);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupview = inflater.inflate(R.layout.popupwindow_speedview, null);
        popupWindow = new PopupWindow(popupview,300,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        mTabLayout = popupview.findViewById(R.id.tab_layout);
    }

    private void initData() {
//        mTabLayout.getTabAt(3);
        mTabLayout.setTabSelected(3);
        mTabLayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return mSpeedIndex.length;
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {

                return new QTabView.TabTitle.Builder()
                        .setContent(mSpeedIndex[position])
                        .setTextColor(getResources().getColor(R.color.colorPinke),getResources().getColor(R.color.colorAllTextDark))
                        .setTextSize(15)
                        .build();
            }

            @Override
            public int getBackground(int position) {
                return 0;
            }
        });

        mTabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                onClickSpeed(position);
                Toast.makeText(context,mSpeedIndex[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
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
                //按下
                starX = event.getX();
                startY = event.getY();
                startTime = System.currentTimeMillis();
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    return true;
                }
                Log.d(TAG, "onTouch: "+"按下");
                break;
            case MotionEvent.ACTION_MOVE:
                //移动
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    return true;
                }
                long currentTime = System.currentTimeMillis();
                diffTime = currentTime - startTime;
                if (diffTime > 500) {
                    // ACTION_MOVE超过五秒的处理逻辑
                    // ...
                }
                Log.d(TAG, "onTouch: "+"移动" + diffTime);
                break;
            case MotionEvent.ACTION_UP:
                //抬起
                if (screen == SCREEN_FULLSCREEN && isLockScreen) {
                    //&& Math.abs(Math.abs(event.getX() - starX)) > ViewConfiguration.get(getContext()).getScaledTouchSlop()  && Math.abs(Math.abs(event.getY() - startY)) > ViewConfiguration.get(getContext()).getScaledTouchSlop()
                    if (Math.abs(event.getX() - starX) < 1 && Math.abs(event.getY() - startY) < 1) {
                        Log.d(TAG,"触发开启定时任务");
                        startDismissControlViewTimer();
                        onClickUiToggle();
                        bottomProgressBar.setVisibility(VISIBLE);
                    }

                    return true;

                }
                // 清除startTime
                startTime = 0;
                Log.d(TAG, "onTouch: "+"抬起"+ diffTime);
                //视图控件显示隐藏动画
                int visibility = bottomContainer.getVisibility();
                if (visibility == VISIBLE && Math.abs(event.getX() - starX) < 1 && Math.abs(event.getY() - startY) < 1){
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_off);
                    bottomContainer.startAnimation(animation);
                    topContainer.startAnimation(animation);
                    startButton.startAnimation(animation);
                    lockIv.startAnimation(animation);
                }else if (visibility == INVISIBLE && Math.abs(event.getX() - starX) < 1 && Math.abs(event.getY() - startY) < 1){
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha);
                    bottomContainer.startAnimation(animation);
                    topContainer.startAnimation(animation);
                    startButton.startAnimation(animation);
                    lockIv.startAnimation(animation);
                }
                break;
        }
        return super.onTouch(v, event);

    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        mDismissControlViewTimerTask  = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(mDismissControlViewTimerTask, 5000);
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
            Log.d(TAG,"触发关闭定时任务DISMISS_CONTROL_VIEW_TIMER");
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
            Log.d(TAG,"触发关闭定时任务mDismissControlViewTimerTask");
        }
    }

    private class DismissControlViewTimerTask extends TimerTask {
        @Override
        public void run() {
            // 执行控制视图的消失动画
            dissmissControlView1();
//            layout_bottom.startAnimation(animation);
//            layout_top.startAnimation(animation);
        }
    }


    public void dissmissControlView1() {
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(() -> {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_off);

//                layout_bottom.startAnimation(animation);
//                layout_top.startAnimation(animation);
//                lockIv.startAnimation(animation);
//                start_layout.startAnimation(animation);
                int visibility = bottomContainer.getVisibility();
                if (visibility == VISIBLE){
                    Log.d(TAG,"触发自然隐藏视图");
                    bottomContainer.startAnimation(animation);
                    topContainer.startAnimation(animation);
                    startButton.startAnimation(animation);
                    lockIv.startAnimation(animation);
                    bottomContainer.setVisibility(View.INVISIBLE);
                    topContainer.setVisibility(View.INVISIBLE);
                    startButton.setVisibility(View.INVISIBLE);
                    lockIv.setVisibility(View.INVISIBLE);
                }


                if (screen != SCREEN_TINY) {
                    bottomProgressBar.setVisibility(View.VISIBLE);
                }
            });
        }
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
                //倍数弹窗选择
                changeUiToPlayingClear(); //隐藏其他控件
                popupWindow.showAsDropDown(tvSpeed);
                break;
        }
    }

    private void onClickSpeed(int SpeedIndex) {
        mediaInterface.setSpeed(getSpeedFromIndex(SpeedIndex));
        if (SpeedIndex == 3){
            tvSpeed.setText("倍数");
        }else{
            tvSpeed.setText(getSpeedFromIndex(SpeedIndex) + "X");
        }
        jzDataSource.objects[0] = SpeedIndex;
    }

    private float getSpeedFromIndex(int index) {
        float ret = 0f;
        switch (index){
            case 0:
                ret = 2.0f;
                break;
            case 1:
                ret = 1.5f;
                break;
            case 2:
                ret = 1.2f;
                break;
            case 3:
                ret = 1.0f;
                break;
            case 4:
                ret = 0.7f;
                break;
            case 5:
                ret = 0.5f;
                break;
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
        popupWindow.dismiss();
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
