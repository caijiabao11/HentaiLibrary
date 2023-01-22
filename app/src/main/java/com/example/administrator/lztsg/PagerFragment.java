package com.example.administrator.lztsg;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.items.TestholeItem;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PagerFragment extends BaseFragment implements BaseFragment.SetInto {
    private static final String KEY_POSITION = "position";
    private RecyclerView mRecyclerView;
    private ImageView mLoading;
    private int position;
    private static ArrayList<String>
            CunzhiHellTitle = new ArrayList<>(),
            CunzhiHellImage = new ArrayList<>(),
            CunzhiHellVideo = new ArrayList<>(),
            CunzhiHellDuration = new ArrayList<>(),

            HentaiJoiTitle = new ArrayList<>(),
            HentaiJoiImage = new ArrayList<>(),
            HentaiJoiVideo = new ArrayList<>(),
            HentaiJoiDuration = new ArrayList<>(),

            FapHeroTitle = new ArrayList<>(),
            FapHeroImage = new ArrayList<>(),
            FapHeroVideo = new ArrayList<>(),
            FapHeroDuration = new ArrayList<>();

    private LinearAdapter mLinearAdaoter;
    private GridLayoutManager mGridLayoutManager;
    private static List<MultipleItem> mData = new ArrayList<>(), mAllData = new ArrayList<>();


    public static PagerFragment newInstance(int position) {
        PagerFragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setInto(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void LinearRecyclerView() {
        //初始化线性布局管理器
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData, new LinearAdapter.OnItemClickListener() {
            @Override
            public void itemonClick(int position, List<MultipleItem> mItems) {
                final Intent intent = new Intent(getActivity(), TestholekilnVideoActivity.class);
                //传递图片、标题信息
                Bundle bundle = new Bundle();
                TestholeItem item = (TestholeItem) mItems.get(position);
                bundle.putString("itemImageUrl", item.getmImageUrl());
                bundle.putString("itemTitle", item.getTitle());
                bundle.putString("itemVideo", item.getmVideoUrl());
                intent.putExtras(bundle);
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
        return R.layout.fragment_pager;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.run_main);
        mLoading = view.findViewById(R.id.item_loading);
    }

    @Override
    public void initData(Context context) {
        this.mData = new ArrayList<>();
//        this.mAllData = new ArrayList<MultipleItem>();
        position = getArguments().getInt(KEY_POSITION);
        LinearRecyclerView();

        if (position == 0) {
            if (CunzhiHellTitle.size() > 0) {
                mLoading.setVisibility(View.GONE);
                for (int i = 0; i < CunzhiHellTitle.size(); i++) {
                    mData.add(new TestholeItem("" + CunzhiHellTitle.get(i), "" + CunzhiHellVideo.get(i), "" + CunzhiHellImage.get(i), "" + CunzhiHellDuration.get(i)));
                }
            }
        } else if (position == 1) {
            if (HentaiJoiTitle.size() > 0) {
                mLoading.setVisibility(View.GONE);
                for (int i = 0; i < HentaiJoiTitle.size(); i++) {
                    mData.add(new TestholeItem("" + HentaiJoiTitle.get(i), "" + HentaiJoiVideo.get(i), "" + HentaiJoiImage.get(i), "" + HentaiJoiDuration.get(i)));
                }
            }
        } else if (position == 2) {
            if (FapHeroTitle.size() > 0) {
                mLoading.setVisibility(View.GONE);
                for (int i = 0; i < FapHeroTitle.size(); i++) {
                    mData.add(new TestholeItem("" + FapHeroTitle.get(i), "" + FapHeroVideo.get(i), "" + FapHeroImage.get(i), "" + FapHeroDuration.get(i)));
                }
            }
        }
    }

    @Override
    public void onDestroyFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCunzhiHellFinish(String title, String imageurl, String videourl, String duration) {
        CunzhiHellTitle.add(title);
        CunzhiHellImage.add(imageurl);
        CunzhiHellVideo.add(videourl);

        String format = null;

        if (duration.contains("PT")) {
            Duration pares = Duration.parse(duration);
            long minutes = pares.toMinutes(); //获取分钟数
            long millis = pares.toMillis(); //获取毫秒时间 毫秒数包含分钟数
            millis = (millis / 1000 % 60);
            /**
             * millis 除以 1000 将毫秒数转换秒数
             * 再对60求余 去除分钟数可得秒数
             */
            format = (minutes < 10 ? "0" + minutes : minutes) + ":" + (millis < 10 ? "0" + millis : millis);

            CunzhiHellDuration.add(format);
        } else {
            if (duration.startsWith("00")) {
                //判断开头是否为00

                format = duration.replaceFirst("^0*\\:", "");
            } else {
                format = duration;
            }
            CunzhiHellDuration.add(format);
        }

        myDataAddOneTime(title,videourl,imageurl,format, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onHentaiFinish(String title, String imageurl, String videourl, String duration) {
        HentaiJoiTitle.add(title);
        HentaiJoiImage.add(imageurl);
        HentaiJoiVideo.add(videourl);
        Duration pares = Duration.parse(duration);
        long minutes = pares.toMinutes(); //获取分钟数
        long millis = pares.toMillis(); //获取时间 毫秒数包含分钟数
        millis = (millis / 1000 % 60);
        /**
         * millis 除以 1000 将毫秒数转换秒数
         * 再对60求余 去除分钟数可得秒数
         */
        String format = (minutes < 10 ? "0" + minutes : minutes) + ":" + (millis < 10 ? "0" + millis : millis);
        HentaiJoiDuration.add(format);

        myDataAddOneTime(title,videourl,imageurl,format,1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onFapHeroFinish(final String title, final String imageurl, final String videourl, final String duration) {
        //赋予数组
//            mTitle = (ArrayList<String>) title.clone();
        FapHeroTitle.add(title);
        FapHeroImage.add(imageurl);
        FapHeroVideo.add(videourl);
        Duration pares = Duration.parse(duration);

        long minutes = pares.toMinutes(); //获取分钟数
        long millis = pares.toMillis(); //获取时间 毫秒数包含分钟数
        millis = (millis / 1000 % 60);
        /**
         * millis 除以 1000 将毫秒数转换秒数
         * 再对60求余 去除分钟数可得秒数
         */
        String format = (minutes < 10 ? "0" + minutes : minutes) + ":" + (millis < 10 ? "0" + millis : millis);
        FapHeroDuration.add(format);

        myDataAddOneTime(title,videourl,imageurl,format, 2);
    }

    private void myDataAddOneTime(String title, String videourl, String imageurl, String format, int i) {
        Log.e("激活成功" + title, "激活成功");

        if (position == i){
            mData.add(new TestholeItem(title,videourl,imageurl,format));

            if (CunzhiHellTitle.size() > 0 || HentaiJoiTitle.size() > 0 || FapHeroTitle.size() > 0) {
                mLoading.setVisibility(View.GONE);
            }
            mLinearAdaoter.notifyDataSetChanged();
        }
    }

}

