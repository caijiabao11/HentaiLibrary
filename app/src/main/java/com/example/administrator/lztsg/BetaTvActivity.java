package com.example.administrator.lztsg;

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
//    private String url = "https://v.avgigi.com/acg/watch/107254/video.m3u8";
        private String url = "https://video.storangeunderh.com/manifest/802.m3u8?videoData=pTZmAga7wsqiW4q&p=/&f=playlist.m3u8";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beta_tv);
        init();
        mButPlayer.setOnClickListener(this);
        mButNextUrl.setOnClickListener(this);
        mButHolp.setOnClickListener(this);
        mButStart.setOnClickListener(this);
    }

    private void setview(String videourl,String imgsrc) {
        mCustomJZVideo.setUp(videourl,"");
//        CustomJZVideo.setVideoImageDisplayType(CustomJZVideo.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
        Glide.with(MyApplication.getContext())
                .load(imgsrc)
                .fitCenter()
                .into(mCustomJZVideo.posterImageView);
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
