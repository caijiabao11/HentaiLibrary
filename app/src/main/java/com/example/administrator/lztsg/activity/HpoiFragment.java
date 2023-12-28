package com.example.administrator.lztsg.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.lztsg.ItemsRoundImageView;
import com.example.administrator.lztsg.MyToast;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.Hpoiitem;
import com.example.administrator.lztsg.items.MultipleItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class HpoiFragment extends BaseFragment implements BaseFragment.SetInto {
    private static final String KEY_POSITION = "position";
    public static LinearAdapter mLinearAdaoter;
    private static LinearAdapter.HpoiHolder mHpoiHolder;
    private static StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private static RecyclerView mRecyclerView;
    public static Context context;
    public static List<MultipleItem> mData, mAllData;
    private static ArrayList<String>
            HpoiTitle = new ArrayList<String>(),
            HpoiImage = new ArrayList<String>(),
            HpoiUrl = new ArrayList<String>();
    public static int[] firstStaggeredGridPosition = {0, 0};
    public static int[] lastStaggeredGridPosition = {0, 0};
    private ImageView mImageView;
    int selectedPosition = -1; // 默认为-1，表示没有选中任何项

    public static HpoiFragment newInstance(int position) {
        HpoiFragment fragment = new HpoiFragment();
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

    @Override
    public int setContentLayout() {
        return 0;
    }

    @Override
    protected int initLyout() {
        return R.layout.fragment_hpoi;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.run_main);
        mImageView = view.findViewById(R.id.top_iv);
    }

    //载入图片+标题
    @Override
    protected void initData(Context context) {
        this.mData = new ArrayList<>();
        this.mAllData = new ArrayList<>();
        this.context = context;
        this.firstStaggeredGridPosition = new int[]{0, 0};
        this.lastStaggeredGridPosition = new int[]{0, 0};
        int position = getArguments().getInt(KEY_POSITION);
        Log.i(TAG, "initData: 第" + position+"页");
        LinearRecyclerView();
    }

    @Override
    public void onDestroyFragment() {

    }

    public void LinearRecyclerView() {
        //初始化线性布局管理器
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData, new LinearAdapter.OnItemClickListener() {
            @Override
            public void itemonClick(int position, List<MultipleItem> mItems) {

            }

            @Override
            public void itemonClick(int position, List<MultipleItem> mItems, String url) {
                if (isAdded()) {
                    final Intent intent = new Intent(context, HpoiDetailpageActivity.class);
                    selectedPosition = position;
                    //传递图片、标题、喜好、简介、番号信息
                    Bundle bundle = new Bundle();
                    Hpoiitem item = (Hpoiitem) mItems.get(position);

                    bundle.putString("itemTit", item.getmTitle());
                    bundle.putString("itemImg", item.getmImgurl());
                    bundle.putString("itemUrl", item.getUrl());
                    bundle.putInt("itemSelectedPosition", selectedPosition);

                    intent.putExtras(bundle);
//                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                    if (mHpoiHolder != null){
                        mHpoiHolder = null;
                    }
                    int realFirstPosition = Math.min(firstStaggeredGridPosition[0], lastStaggeredGridPosition[1]);
                    if (mRecyclerView.getChildCount() > position - realFirstPosition) {
                        mHpoiHolder = (LinearAdapter.HpoiHolder) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(position - realFirstPosition));
                    }
                    //获取共享动画的View对象

                    ItemsRoundImageView card_info_image = (ItemsRoundImageView) mHpoiHolder.image;
                    if (card_info_image.getParent() != null && mHpoiHolder.image.getParent() != null){
                        //绑定共享空间，并赋予标签（便于寻找）
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                                Pair.<View, String>create(card_info_image, "item_info_image"));
                        startActivity(intent, options.toBundle());
                    }
                }
            }

            @Override
            public void itemHoldersonClick(int position) {

            }
        });
        //设置适配器
        mRecyclerView.setAdapter(mLinearAdaoter);
        //设置recycleView的滚动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //该if判断的是滚动的状态，其意义是放置不断的刷新if内的语句
                if (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_DRAGGING) {
                    // DES: 找出当前可视Item位置
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //我们使用的layoutManager是StaggeredGrid
                    if (layoutManager instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager linearManager = (StaggeredGridLayoutManager) layoutManager;
                        //获取绝对坐标
                        //第一个可见位置
                        linearManager.findFirstVisibleItemPositions(firstStaggeredGridPosition);
                        //最后一个可见位置
                        linearManager.findLastVisibleItemPositions(lastStaggeredGridPosition);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                LinearAdapter.ItemHolder itemHolder = (LinearAdapter.ItemHolder) recyclerView.getChildViewHolder(mRecyclerView);
                if (layoutManager != null && layoutManager instanceof StaggeredGridLayoutManager) {
                    if (!recyclerView.canScrollVertically(1)) {
                        //滑到底部
                        MyToast.makeText(context,"已经在谷底了>_<",1000,
                                Gravity.FILL_HORIZONTAL | Gravity.BOTTOM).show();

                    } else if (!recyclerView.canScrollVertically(-1)) {
                        //滑到顶部
                    } else if (dy > 0) {
                        //监听上滑
//                    mItemHolder.itemView.setAnimation();
                    } else if (dy < 0) {
                        //监听下滑
//                        mItemHolder.itemView.setAnimation(AnimationUtils.loadAnimation(mItemHolder.itemView.getContext(),R.anim.alpha));
                    }
                }
            }
        });
    }

    @Override
    public void onCunzhiHellFinish(String title, String imageurl, String videourl, String duration) {

    }

    @Override
    public void onHentaiFinish(String title, String imageurl, String videourl, String duration) {

    }

    @Override
    public void onFapHeroFinish(String title, String imageurl, String videourl, String duration) {

    }

    @Override
    public void onHpoiFinish(String title, String imageurl, String url) {
        if (!(HpoiTitle.contains(title))){
            HpoiTitle.add(title);
            HpoiImage.add(imageurl);
            HpoiUrl.add(url);

        }

        myDataAddOneTime(title,imageurl,url);
    }

    @Override
    public void onAsmrFinish(String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl) {

    }

    private void myDataAddOneTime( String title, String imageurl, String url) {
        Log.e("激活成功" + title, "激活成功");
        mData.add(new Hpoiitem(title, imageurl, url));
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI界面的访问或修改操作
                mLinearAdaoter.notifyDataSetChanged();
            }
        });
    }
}
