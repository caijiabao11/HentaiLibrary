package com.example.administrator.lztsg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;
import androidx.appcompat.app.AppCompatActivity;


public class WelcomeActivity extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mImageView = findViewById(R.id.welcom_bg);
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
