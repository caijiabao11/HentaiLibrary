package com.example.administrator.lztsg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final Intent intent = new Intent(this,MainActivity.class);
        Timer timer = new Timer();
        TimerTask tack = new TimerTask(){
            public void run(){
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(tack,1000 * 3);
    }
}
