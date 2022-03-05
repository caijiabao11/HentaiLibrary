package com.example.administrator.lztsg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MoreActivity extends AppCompatActivity {
    private ImageButton mImgButton;
    private LinearLayout mTvBeta,mTestHoleKiln;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bindViews();
        ImageButtonOnClick();
        TvbetaOnClick();
        mTestHoleKilnOnClick();
    }

    private void bindViews() {
        mImgButton = findViewById(R.id.imgbutton);
        mTvBeta = findViewById(R.id.tv_beta);
        mTestHoleKiln = findViewById(R.id.test_holekiln);
    }

    //叉叉按钮点击事件
    public void ImageButtonOnClick(){
        mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //小电视beta点击事件
    public void TvbetaOnClick(){
        mTvBeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MoreActivity.this,LifanActivity.class);
                startActivity(intent);
            }
        });
    }
    //试炼洞窑点击事件
    public void mTestHoleKilnOnClick(){
        mTestHoleKiln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MoreActivity.this,TestholekilnActivity.class);
                startActivity(intent);
            }
        });
    }
}
