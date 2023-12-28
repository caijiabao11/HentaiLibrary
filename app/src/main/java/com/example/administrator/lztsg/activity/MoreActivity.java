package com.example.administrator.lztsg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.MoreItem;
import com.example.administrator.lztsg.items.MultipleItem;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mImgButton;
    private RecyclerView mRecyclerView;
    private LinearLayout mBut_search_img,mBut_white_mpose,mBUt_asmr,mBut_hpoi;
    private LinearAdapter mLinearAdaoter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MultipleItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        init();
        initData();
        LinearRecyclerView();
        mImgButton.setOnClickListener(this);
        mBut_search_img.setOnClickListener(this);
        mBut_white_mpose.setOnClickListener(this);
        mBUt_asmr.setOnClickListener(this);
        mBut_hpoi.setOnClickListener(this);
    }

    private void init() {
        mImgButton = findViewById(R.id.imgbutton);
        mRecyclerView = findViewById(R.id.run_main);
        mBut_search_img = findViewById(R.id.but_search_img);
        mBut_white_mpose = findViewById(R.id.but_white_npose);
        mBUt_asmr = findViewById(R.id.but_asmr);
        mBut_hpoi = findViewById(R.id.but_hpoi);
    }
    //载入数据
    private void initData(){
        this.mData = new ArrayList<>();
        mData.add(new MoreItem(R.drawable.bag_1,"里番"));
        mData.add(new MoreItem(R.drawable.bag_2,"ASMR"));
        mData.add(new MoreItem(R.drawable.bag_3,"试炼洞窑"));
        mData.add(new MoreItem(R.drawable.bag_4,"小电视beta"));
    }

    public void LinearRecyclerView(){
        //初始化线性布局管理器
        mLinearLayoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData, new LinearAdapter.OnItemClickListener() {
            @Override
            public void itemonClick(int position, List<MultipleItem> mItems) {

            }

            @Override
            public void itemonClick(int position, List<MultipleItem> mItems, String url) {

            }

            @Override
            public void itemHoldersonClick(int position) {
                MoreItem item = (MoreItem) mData.get(position);
                switch (item.getTitle()){
                    case "里番":
                        //里番点击事件
                        final Intent intent = new Intent(MoreActivity.this,LifanActivity.class);
                        startActivity(intent);
                        break;
                    case "ASMR":
                        //点击事件
                        final Intent intent1 = new Intent(MoreActivity.this,AsmrActivity.class);
                        startActivity(intent1);
                        break;
                    case "试炼洞窑":
                        //试炼洞窑点击事件
                        final Intent intent2 = new Intent(MoreActivity.this,TestholekilnActivity.class);
                        startActivity(intent2);
                        break;
                    case "小电视beta":
                        //小电视beta点击事件
                        final Intent intent3 = new Intent(MoreActivity.this, BetaTvActivity.class);
                        startActivity(intent3);
                        break;
                }
            }
        });
        //设置适配器
        mRecyclerView.setAdapter(mLinearAdaoter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbutton:
                //设置按钮点击事件
                final Intent intent = new Intent(MoreActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.but_search_img:
                //搜图按钮点击事件
                final Intent intent1 = new Intent(MoreActivity.this,SearchImgActivity.class);
                startActivity(intent1);
                break;
            case R.id.but_white_npose:
                //白噪音按钮点击事件
                final Intent intent2 = new Intent(MoreActivity.this,WhiteNoiseActivity.class);
                startActivity(intent2);
                break;
            case R.id.but_asmr:
                //
                final Intent intent3 = new Intent(MoreActivity.this,ScreenCastingActivity.class);
                startActivity(intent3);
                break;
            case R.id.but_hpoi:
                //手办图鉴按钮点击事件
                final Intent intent4 = new Intent(MoreActivity.this,HpoiActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
