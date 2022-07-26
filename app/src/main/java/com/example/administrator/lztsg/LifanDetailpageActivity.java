package com.example.administrator.lztsg;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class LifanDetailpageActivity extends AppCompatActivity {
    private ImageView mImageView;
    private TextView mTextView;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifan_detailpage);
        bindViews();
        initData();
    }

    private void bindViews() {
        mImageView = findViewById(R.id.item_img);
        mTextView = findViewById(R.id.item_tit);
        mFloatingActionButton = findViewById(R.id.live_fab);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mImageView.setImageResource(bundle.getInt("itemImageId"));
        mTextView.setText(bundle.getString("itemTitle"));
        //进场悬浮按钮动画
        mFloatingActionButton.setVisibility(View.VISIBLE);
        Animation livefabon = AnimationUtils.loadAnimation(this,R.anim.scale);
        mFloatingActionButton.setAnimation(livefabon);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键监听
        mFloatingActionButton.setVisibility(View.INVISIBLE);
//        Animation livefabon = AnimationUtils.loadAnimation(this,R.anim.scale_off);
//        mFloatingActionButton.setAnimation(livefabon);
        return super.onKeyDown(keyCode, event);
    }
}
