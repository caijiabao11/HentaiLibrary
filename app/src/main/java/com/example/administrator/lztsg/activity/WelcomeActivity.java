package com.example.administrator.lztsg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;


public class WelcomeActivity extends AppCompatActivity {
    private ImageView mImageView;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
        initData();
    }

    private void init() {
        mImageView = findViewById(R.id.welcom_bg);
        mWebView = findViewById(R.id.web_gethtml);
    }

    private void initData() {
        Glide.with(MyApplication.getContext())
                .load(R.drawable.video_bgimg_1)
                .centerCrop()
                .into(mImageView);
        final Intent intent = new Intent(this,MainActivity.class);
        Timer timer = new Timer();
        TimerTask tack = new TimerTask(){
            public void run(){
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(tack,1000 * 2);
    }
}
