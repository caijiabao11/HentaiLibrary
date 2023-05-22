package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.CbirSitesItem;
import com.example.administrator.lztsg.items.MultipleItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class SearchMapFragment extends BaseFragment implements SearchImgActivity.SetInto {
    private static final String KEY_POSITION = "position";
    private int position;
    private RecyclerView mRecyclerView;
    public static LinearAdapter mLinearAdaoter;
    private GridLayoutManager mGridLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private static ArrayList<String>
            YandexImage = new ArrayList<String>(),
            YandexSize = new ArrayList<String>(),
            YandexTitle = new ArrayList<String>(),
            YandexTitleurl = new ArrayList<String>(),
            YandexDomain = new ArrayList<String>(),
            YandexDescription = new ArrayList<String>(),
            SimilarimgImage = new ArrayList<String>();

    public static List<MultipleItem> mData = new ArrayList<>();
    public static List<MultipleItem> mSimilar = new ArrayList<>();

    public static SearchMapFragment newInstance(int position) {
        SearchMapFragment fragment = new SearchMapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SearchImgActivity.setInto(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void LinearRecyclerView() {
        //初始化线性布局管理器
        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData, new LinearAdapter.OnItemClickListener() {
            @Override
            public void itemonClick(int position, List<MultipleItem> mItems) {
                //跳转链接到本地浏览器
                CbirSitesItem item = (CbirSitesItem) mItems.get(position);
                Uri uri = Uri.parse( item.getTitleUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
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
        return R.layout.fragment_searchmap;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.run_main);
    }

    @Override
    protected void initData(Context context) {
        this.mData = new ArrayList<>();
        position = getArguments().getInt(KEY_POSITION);
        LinearRecyclerView();

        if (position == 0) {
            if (YandexTitle.size() > 0) {
                for (int i = 0; i < YandexTitle.size(); i++) {
                    mData.add(new CbirSitesItem("" + YandexImage.get(i), "" + YandexSize.get(i), "" + YandexTitle.get(i), "" + YandexTitleurl.get(i), "" + YandexDomain.get(i), "" + YandexDescription.get(i)));
                }
            }
        }
    }

    @Override
    public void onDestroyFragment() {

    }

    @Override
    public void onYandexFinish(String imageurl, String size, String title, String titleurl, String domain, String description) {
        YandexImage.add(imageurl);
        YandexSize.add(size);
        YandexTitle.add(title);
        YandexTitleurl.add(titleurl);
        YandexDomain.add(domain);
        YandexDescription.add(description);
        myDataAddOneTime(imageurl, size, title, titleurl, domain, description, 0);
    }

    private void myDataAddOneTime(String imageurl, String size, String title, String titleurl, String domain, String description, int i) {
        Log.e("激活成功" + title, "激活成功");

        if (position == i) {
            mData.add(new CbirSitesItem(imageurl, size, title, titleurl, domain, description));
            mLinearAdaoter.notifyDataSetChanged();
//            if (YandexTitle.size() > 0 ) {
//                mLoading.setVisibility(View.GONE);
//            }
        }

    }
}
