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
    private static ArrayList<String> mTitle = new ArrayList<>();
    private static ArrayList<String> mImage = new ArrayList<>();
    private LinearAdapter mLinearAdaoter;
    private GridLayoutManager mGridLayoutManager;
    private static List<MultipleItem> mData = new ArrayList<>(),mAllData = new ArrayList<>();


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
            public void itemonClick(int position, List<MultipleItem> mItems) {

            }

            @Override
            public void itemHoldersonClick(int position) {

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
        this.mData = new ArrayList<>();
//        this.mAllData = new ArrayList<MultipleItem>();
        position = getArguments().getInt(KEY_POSITION);
        LinearRecyclerView();

        if (position == 0 && mTitle != null){

//            for (String name:mTitle){
//
//                mData.add(new TestholeItem(""+name,"",""));
//            }
            for(int i=0;i<mTitle.size();i++){
                mData.add(new TestholeItem("dd"+mTitle.get(i),"",""+mImage.get(i)));

            }
//            mAllData.addAll(mData);

        } else if (position == 1){

        } else if (position ==2){

        }

    }

    @Override
    public void onDestroyFragment() {

    }

    @Override
    public void onFinish(final String title, final String imageurl, final String videourl) {
        //赋予数组
//            mTitle = (ArrayList<String>) title.clone();
        mTitle.add(title);
        mImage.add(imageurl);
        mData.add(new TestholeItem(""+title,"",""+imageurl));
        Log.e("激活成功"+title.toString(),"激活成功");

        mLinearAdaoter.notifyDataSetChanged();
    }

}

