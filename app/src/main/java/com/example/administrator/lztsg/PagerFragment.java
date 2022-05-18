package com.example.administrator.lztsg;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.items.TestholeItem;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PagerFragment extends BaseFragment implements TestholekilnActivity.SetInto {
    private static final String KEY_POSITION = "position";
    private RecyclerView mRecyclerView;
    private int position;
    private static String mTitle,mImage;
    private LinearAdapter mLinearAdaoter;
    private GridLayoutManager mGridLayoutManager;
    private static List<MultipleItem> mData,mAllData;


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
//                switch (mData.get(position).getItemType()){
//
//                }
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
    public void initData(Context context) {
        this.mData = new ArrayList<MultipleItem>();
        this.mAllData = new ArrayList<MultipleItem>();
        position = getArguments().getInt(KEY_POSITION);
        LinearRecyclerView();

        if (position == 0 && mTitle != null){
            mAllData.addAll(mData);
//            mData.add(new TestholeItem("dd"+mTitle,"","https://tb.sb-cd.com/t/8488681/8/4/w:1280/t2-enh/hmv-hentai-mv.jpg"));
            mData.add(new TestholeItem("dd"+mTitle,"",""+mImage));
//           mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
//           mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
//           mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
        } else if (position == 1){
//            mAllData.addAll(mData);
           mData.add(new TestholeItem("aa","",""));
        } else if (position ==2){
//           mData.add(new TestholeItem(R.drawable.jzds_hmv7,"HMV7"));
        }

    }

    @Override
    public void onDestroyFragment() {

    }

    @Override
    public void onFinish(final ArrayList<String> title, final String imageurl, final String videourl) {
        this.mTitle = title.get(0);
        this.mImage = imageurl;
//        ArrayList<String> ss = new ArrayList();
//        this.mImage = imageurl;
            mData.add(new TestholeItem(""+mTitle,"",""+imageurl));
//            ss.add(title);
//            ss.toString();
        Log.e("激活成功","激活成功");
//        LinearRecyclerView();
        mLinearAdaoter.notifyDataSetChanged();
    }

}

