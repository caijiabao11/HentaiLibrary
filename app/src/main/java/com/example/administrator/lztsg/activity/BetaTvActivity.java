package com.example.administrator.lztsg.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.CustomJZVideo;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.httpjson.BetaTvHttpJson;
import com.example.administrator.lztsg.httpjson.HttpJsonResolution;
import com.example.administrator.lztsg.utils.DensityUtils;
import com.example.administrator.lztsg.utils.ScreenRotateUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BetaTvActivity extends AppCompatActivity implements ScreenRotateUtils.OrientationChangeListener,View.OnClickListener {
    private CustomJZVideo mCustomJZVideo;
    private RelativeLayout mHolp_clicktext,mMod;
    private CardView mCard;
    private ImageView mLed_load,mLed_ok;
    private Button mButPlayer,mButNextUrl,mButHolp,mButStart;
    private boolean isFirstLoad = true;
    private String url = "https://v.avgigi.com/acg/watch/107254/video.m3u8";
    private String mp3url = "https://raw.kiko-play-niptan.one/media/stream/daily/2023-01-24/RJ418120/%E3%83%88%E3%83%A9%E3%83%83%E3%82%AF02%E3%80%80%E6%B2%BB%E7%99%82%E6%BA%96%E5%82%99(%E5%8B%83%E8%B5%B7%E3%83%91%E3%83%BC%E3%83%88)%E3%83%BB%E3%83%97%E3%83%AC%E3%82%A4%E5%86%85%E5%AE%B9%E3%80%8A%E6%A1%9C%E8%89%AF%E3%81%A1%E3%82%83%E3%82%93%E3%81%A8%E6%A1%83%E7%8B%90%E3%81%A1%E3%82%83%E3%82%93%E3%81%AE%E3%83%80%E3%83%96%E3%83%AB%E3%83%95%E3%82%A7%E3%83%A9%E3%83%81%E3%82%AA%E3%80%8B.mp3?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2FzbXIub25lIiwic3ViIjoiY2FpaG9tZSIsImF1ZCI6Imh0dHBzOi8vYXNtci5vbmUvYXBpIiwibmFtZSI6ImNhaWhvbWUiLCJncm91cCI6InVzZXIiLCJpYXQiOjE2NzMzNjQwNzAsImV4cCI6MTcwNDkwMDA3MH0.aDQmPPO7pgH-opf1J6Gz6CuFgx2XuOSo2SQL0t77taU";
//        private String url = "https://video.storangeunderh.com/manifest/802.m3u8?videoData=pTZmAga7wsqiW4q&p=/&f=playlist.m3u8";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beta_tv);
        init();
        mButPlayer.setOnClickListener(this);
        mButNextUrl.setOnClickListener(this);
        mButHolp.setOnClickListener(this);
        mButStart.setOnClickListener(this);
        mCustomJZVideo.setUp(mp3url,"");

        Glide.with(MyApplication.getContext())
                .load("https://api.asmr.one/api/cover/418120.jpg?type=main")
                .fitCenter()
                .into(mCustomJZVideo.posterImageView);
    }

    private void setview(String videourl,String imgsrc) {

//        CustomJZVideo.setVideoImageDisplayType(CustomJZVideo.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
//        Glide.with(MyApplication.getContext())
//                .load(imgsrc)
//                .fitCenter()
//                .into(mCustomJZVideo.posterImageView);
        //预加载
//        mCustomJZVideo.startPreloading();//开始预加载，加载完等待播放
//        mCustomJZVideo.startVideoAfterPreloading();//如果预加载完会开始播放，如果未加载则开始加载
        mLed_load.setBackground(getResources().getDrawable(R.drawable.tv_startled_not));
        mLed_ok.setBackground(getResources().getDrawable(R.drawable.tv_startled_ok));
        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(this);
    }

    private void init() {
        mCustomJZVideo = findViewById(R.id.betatv_video);
        mHolp_clicktext = findViewById(R.id.holp_clicktext);
        mMod = findViewById(R.id.tvmian);
        mCard = findViewById(R.id.card);
        mButPlayer = findViewById(R.id.but_player);
        mButNextUrl = findViewById(R.id.but_nexturl);
        mButHolp = findViewById(R.id.but_holp);
        mButStart = findViewById(R.id.but_start);
        mLed_load = findViewById(R.id.startled_loading);
        mLed_ok = findViewById(R.id.startled_ok);
    }

    private void onDataLoad() {
        //数据加载
        Handler handler = new Handler();
        Log.e("Into","激活");
        mLed_load.setBackground(getResources().getDrawable(R.drawable.tv_startled_loading));
        mLed_ok.setBackground(getResources().getDrawable(R.drawable.tv_startled_not));
        BetaTvHttpJson.getData(new HttpJsonResolution() {
            @Override
            public void onFinish(String title, String imageurl, String videourl, String duration) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setview(videourl,imageurl);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        //懒加载
        if (isFirstLoad){
            onDataLoad();
            isFirstLoad = false;
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_player:
                mCustomJZVideo.startButton.performClick();
                break;
            case R.id.but_nexturl:
                onDataLoad();
                break;
            case R.id.but_holp:
                if (mHolp_clicktext.getVisibility() == View.GONE){
                    mHolp_clicktext.setVisibility(View.VISIBLE);
                } else {
                    mHolp_clicktext.setVisibility(View.GONE);
                }
                break;
            case R.id.but_start:
                if (mButStart.getTag().equals("simple")){
                    mButStart.setTag("tv");
                    mButStart.setBackground(getResources().getDrawable(R.drawable.ic_mod_tv));
                    onModSimple();
                    mButHolp.setVisibility(View.GONE);
                    mHolp_clicktext.setVisibility(View.GONE);
                } else if (mButStart.getTag().equals("tv")){
                    mButStart.setTag("simple");
                    mButStart.setBackground(getResources().getDrawable(R.drawable.ic_mod_simple));
                    mButHolp.setVisibility(View.VISIBLE);
                    onModTv();
                }
                break;
        }
    }

    private void onModTv() {
        //TV模式
        int heightPx = DensityUtils.dip2px(this,200);
        int tvmianmmar = DensityUtils.dip2px(this,20);
        int card = DensityUtils.dip2px(this,15);
        int cardRadius = DensityUtils.dip2px(this,10);
        //设高宽
        LinearLayout.LayoutParams limod = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,heightPx);
        RelativeLayout.LayoutParams remod = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        limod.setMargins(tvmianmmar,0,tvmianmmar,0);
        mMod.setLayoutParams(limod);
        remod.setMargins(card,card,card,card);
        mCard.setLayoutParams(remod);
        mCard.setRadius(cardRadius);
    }

    private void onModSimple() {
        //简约模式
        //设高宽
        LinearLayout.LayoutParams limod = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams remod = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        limod.setMargins(0,0,0,0);
        mMod.setLayoutParams(limod);
        remod.setMargins(0,0,0,0);
        mCard.setLayoutParams(remod);
        mCard.setRadius(0f);
    }
}
