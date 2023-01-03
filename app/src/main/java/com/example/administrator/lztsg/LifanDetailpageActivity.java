package com.example.administrator.lztsg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import androidx.appcompat.app.AppCompatActivity;

public class LifanDetailpageActivity extends AppCompatActivity {
    private ImageView mImageView;
    private TextView mTextView;
    private LottieAnimationView mLottie;
    private SharedPreferences sp;
    private static boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifan_detailpage);
        init();
        initData();
    }

    private void init() {
        mImageView = findViewById(R.id.item_img);
        mTextView = findViewById(R.id.item_tit);
        mLottie = findViewById(R.id.like_lottie);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mImageView.setImageResource(bundle.getInt("itemImageId"));
        mTextView.setText(bundle.getString("itemTitle"));
        //进场悬浮按钮动画
        mLottie.setVisibility(View.VISIBLE);
        Animation livefabon = AnimationUtils.loadAnimation(this,R.anim.scale);
        mLottie.setAnimation(livefabon);
        sp = getSharedPreferences("user",MODE_PRIVATE);
        if (sp != null){
            isChecked = sp.getBoolean("lottie_like",false);
            if (isChecked != false){
                mLottie.setMinAndMaxProgress(0f,0.5f);
            }else{
                mLottie.setMinAndMaxProgress(0.5f,1f);
            }
            mLottie.playAnimation();
        }
        mLottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getSharedPreferences("user",MODE_PRIVATE);
                isChecked = sp.getBoolean("lottie_like",false);
                startLottie(isChecked);

            }
        });
    }

    private void startLottie(boolean isChecked) {
        //Lottie动画
        if (isChecked != false){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("lottie_like",false);
            editor.commit();
            mLottie.setMinAndMaxProgress(0.5f,1f);
            mLottie.playAnimation();
        }else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("lottie_like",true);
            editor.commit();
            mLottie.setMinAndMaxProgress(0f,0.5f);
            mLottie.playAnimation();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键监听
        mLottie.setVisibility(View.INVISIBLE);
//        Animation livefabon = AnimationUtils.loadAnimation(this,R.anim.scale_off);
//        mFloatingActionButton.setAnimation(livefabon);
        return super.onKeyDown(keyCode, event);
    }
}
