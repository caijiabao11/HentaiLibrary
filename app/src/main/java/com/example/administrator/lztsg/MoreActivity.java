package com.example.administrator.lztsg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.administrator.lztsg.items.More1Item;
import com.example.administrator.lztsg.items.MoreItem;
import com.example.administrator.lztsg.items.MultipleItem;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoreActivity extends AppCompatActivity {
    private ImageButton mImgButton;
    private RecyclerView mRecyclerView;
    private LinearAdapter mLinearAdaoter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MultipleItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bindViews();
        initData();
        LinearRecyclerView();
        ImageButtonOnClick();
    }

    private void bindViews() {
        mImgButton = findViewById(R.id.imgbutton);
        mRecyclerView = findViewById(R.id.run_main);
    }
    //载入数据
    private void initData(){
        this.mData = new ArrayList<>();
        mData.add(new MoreItem(R.drawable.bag,"试炼洞窑"));
        mData.add(new More1Item(R.drawable.bag_1,"小电视beta"));
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
            public void itemHoldersonClick(int position) {
                switch (mData.get(position).getItemType()){
                    case MORE:
                        //试炼洞窑点击事件
                        final Intent intent = new Intent(MoreActivity.this,TestholekilnActivity.class);
                        startActivity(intent);
                        break;
                    case MORE1:
                        //小电视beta点击事件
                        final Intent intent1 = new Intent(MoreActivity.this,LifanActivity.class);
                        startActivity(intent1);
                        break;
                }
            }
        });
        //设置适配器
        mRecyclerView.setAdapter(mLinearAdaoter);
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
}
