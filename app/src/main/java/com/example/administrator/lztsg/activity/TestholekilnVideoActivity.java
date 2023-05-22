package com.example.administrator.lztsg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.CustomJZVideo;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.utils.ScreenRotateUtils;

import androidx.appcompat.app.AppCompatActivity;

public class TestholekilnVideoActivity extends AppCompatActivity implements ScreenRotateUtils.OrientationChangeListener {
    private CustomJZVideo mCustomJZVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_holekiln_video);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String url = bundle.getString("itemVideo");
        String title = bundle.getString("itemTitle");
        String image = bundle.getString("itemImageUrl");
        mCustomJZVideo = findViewById(R.id.betatv_video);
        mCustomJZVideo.setUp(url,title);
        //填充方式
//        CustomJZVideo.setVideoImageDisplayType(CustomJZVideo.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
        Glide.with(MyApplication.getContext())
                .load(image)
                .fitCenter()
                .into(mCustomJZVideo.posterImageView);
        //预加载
//        mCustomJZVideo.startPreloading();//开始预加载，加载完等待播放
//        mCustomJZVideo.startVideoAfterPreloading();//如果预加载完会开始播放，如果未加载则开始加载
        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenRotateUtils.getInstance(this).start(this);
    }

    @Override
    public void onBackPressed() {
        if(CustomJZVideo.backPress()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenRotateUtils.getInstance(this).stop();
        CustomJZVideo.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 根据传感器自动旋转屏幕进入全屏
     * @param orientation
     */
    @Override
    public void orientationChange(int orientation) {
        if (CustomJZVideo.CURRENT_JZVD != null
                && (mCustomJZVideo.state == CustomJZVideo.STATE_PLAYING || mCustomJZVideo.state == CustomJZVideo.STATE_PAUSE)
                && mCustomJZVideo.screen != CustomJZVideo.SCREEN_TINY) {
            if (orientation >= 45 && orientation <= 315 && mCustomJZVideo.screen == CustomJZVideo.SCREEN_NORMAL) {
                changeScreenFullLandscape(ScreenRotateUtils.orientationDirection);
            } else if (((orientation >= 0 && orientation < 45) || orientation > 315) && mCustomJZVideo.screen == CustomJZVideo.SCREEN_FULLSCREEN) {
                changeScrenNormal();
            }
        }
    }

    /**
     * 竖屏并退出全屏
     */
    private void changeScrenNormal() {
        if (mCustomJZVideo != null && mCustomJZVideo.screen == CustomJZVideo.SCREEN_FULLSCREEN){
            mCustomJZVideo.autoQuitFullscreen();
        }
    }

    /**
     * 横屏
     */
    private void changeScreenFullLandscape(float x) {
        //从竖屏状态进入横屏
        if (mCustomJZVideo != null && mCustomJZVideo.screen != CustomJZVideo.SCREEN_FULLSCREEN) {
            if ((System.currentTimeMillis() - CustomJZVideo.lastAutoFullscreenTime) > 2000) {
                mCustomJZVideo.autoFullscreen(x);
                CustomJZVideo.lastAutoFullscreenTime = System.currentTimeMillis();
            }
        }
    }

}
