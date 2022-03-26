package com.example.administrator.lztsg;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.items.TestholeItem;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PagerFragment extends BaseFragment  {
    private static final String KEY_POSITION = "position";
    private RecyclerView mRecyclerView;
    private LinearAdapter mLinearAdaoter;
    private GridLayoutManager mGridLayoutManager;
    private List<MultipleItem> mData;

    public static PagerFragment newInstance(int position) {
        PagerFragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;

    }
    public void LinearRecyclerView(){
        //初始化线性布局管理器
        mGridLayoutManager = new GridLayoutManager(getActivity(),2);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData, new LinearAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (mData.get(position).getItemType()){

                }
            }
        });
        //设置适配器
        mRecyclerView.setAdapter(mLinearAdaoter);
    }
    @Override
    public int setContentLayout() {
        return 0;
    }

    @Override
    protected int initLyout() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.run_main);

    }

    @Override
    protected void initData(Context context) {
        this.mData = new ArrayList<>();

        int position = getArguments().getInt(KEY_POSITION);
        if (position == 0){
//            mImageView.setImageResource(R.drawable.bag);
            mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
            mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
            mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
            mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
        } else if (position == 1){
            mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
        } else if (position ==2){
            mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
        }
        LinearRecyclerView();
    }

    @Override
    public void onDestroyFragment() {

    }
}
