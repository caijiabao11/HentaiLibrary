package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.lztsg.MyToast;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.httpjson.BetaTvHttpJson;
import com.example.administrator.lztsg.httpjson.HttpJsonResolution;
import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.items.TestholeItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class LifanRandomFragment extends Fragment{
    private static final String KEY_POSITION = "position";
    public static LinearAdapter mLinearAdaoter;
    private static LinearAdapter.TestholeHolder mTestholeHolder;
    private LinearLayoutManager mLinearLayoutManager;
    private static RecyclerView mRecyclerView;
    public static Context context;
    public static List<MultipleItem> mData, mAllData;
    public static int[] firstStaggeredGridPosition = {0, 0};
    public static int[] lastStaggeredGridPosition = {0, 0};

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(context);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_lifan_random,container,false);
        initView(rootview);
        initData(context);

        return rootview;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.run_main);
    }
    private void initData(Context context) {
        this.mData = new ArrayList<>();
        this.mAllData = new ArrayList<>();
        this.context = context;
        this.firstStaggeredGridPosition = new int[]{0, 0};
        this.lastStaggeredGridPosition = new int[]{0, 0};

        Handler handler = new Handler();
        BetaTvHttpJson.getData(new HttpJsonResolution() {
            @Override
            public void onFinish(String title, String imageurl, String videourl, String duration) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDataAddOneTime(title,imageurl,videourl);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });

        LinearRecyclerView();
    }

    public void LinearRecyclerView() {
        //初始化线性布局管理器
        mLinearLayoutManager = new LinearLayoutManager(context);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //初始化适配器
        mLinearAdaoter = new LinearAdapter(context,mData, new LinearAdapter.OnItemClickListener() {
            @Override
            public void itemonClick(int position, List<MultipleItem> mItems) {
                TestholeItem item = (TestholeItem) mItems.get(position);

                BetaTvHttpJson.indata(item.getmVideoUrl(), new HttpJsonResolution() {
                    @Override
                    public void onFinish(String title, String imageurl, String videourl, String duration) {

                        final Intent intent = new Intent(getActivity(), TestholekilnVideoActivity.class);
                        //传递图片、标题信息
                        Bundle bundle = new Bundle();
                        bundle.putString("itemImageUrl", imageurl);
                        bundle.putString("itemTitle", title);
                        bundle.putString("itemVideo", videourl);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            @Override
            public void itemonClick(int position, List<MultipleItem> mItems, String url) {

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

    private void myDataAddOneTime(String title, String imageurl, String videourl) {
        Log.e("激活成功" + title, "激活成功");
        mData.add(new TestholeItem(title,videourl,imageurl,""));
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
