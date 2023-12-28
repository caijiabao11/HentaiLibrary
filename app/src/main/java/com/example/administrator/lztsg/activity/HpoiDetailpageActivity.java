package com.example.administrator.lztsg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.httpjson.HpoiHttpJson;
import com.example.administrator.lztsg.httpjson.HttpHpoiDataResolution;
import com.example.administrator.lztsg.items.Hpoidetailpageitem;
import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class HpoiDetailpageActivity extends AppCompatActivity {
    public static LinearAdapter mLinearAdaoter;
    private static LinearAdapter.HpoiHolder mHpoiHolder;
    private static StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private static RecyclerView mRecyclerView;
    private LinearLayout mItem_TextLayout;
    private ImageView mItem_img,mItem_img_bg;
    private TextView mItem_tit;
    private Button mBut_loaduserpic;
    private String Itemid = null;
    private static int offsetindex = 0;
    public static List<MultipleItem> mData, mAllData;
    public static int[] firstStaggeredGridPosition = {0, 0};
    public static int[] lastStaggeredGridPosition = {0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpoi_detailpage);
        init();
        initData();
    }

    private void init(){
        mItem_TextLayout = findViewById(R.id.item_textlayout);
        mItem_img = findViewById(R.id.item_img);
        mItem_img_bg = findViewById(R.id.item_img_bg);
        mItem_tit = findViewById(R.id.item_tit);
        mRecyclerView = findViewById(R.id.run_main);
        mBut_loaduserpic = findViewById(R.id.but_loaduserpic);
    }

    private void initData() {
        mData = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //图片
        Glide.with(MyApplication.getContext())
                .load(bundle.getString("itemImg"))
                .placeholder(R.drawable.none)
                .fitCenter()
                .into(mItem_img);

        //背景图片
        Glide.with(MyApplication.getContext())
                .load(bundle.getString("itemImg"))
                .placeholder(R.drawable.none)
                .centerCrop()
                .into(mItem_img_bg);

        mItem_tit.setText("中文名称：" + bundle.getString("itemTit"));

        HpoiHttpJson.indata(bundle.getString("itemUrl"), new HttpHpoiDataResolution() {
            @Override
            public void onFinish(String title, String imageurl, String url) {
                Itemid = url;
                mData.add(new Hpoidetailpageitem(imageurl));
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 在这里进行UI界面的访问或修改操作
                        mLinearAdaoter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onInitData(String name, String value) {
                additemtextlayout(name,value);
            }

            @Override
            public void onPages(int total) {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        //导入相册数据
        LinearRecyclerView();

        //加载跟多的按钮
        mBut_loaduserpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Itemid != null){
                    offsetindex++;
                    int offset = 18 * offsetindex;
                    String loadpics = "https://www.hpoi.net/hobby/gallery/" +  Itemid + "?offset="+ offset +"&action=user&items=18";
                    HpoiHttpJson.loadtopic(loadpics, new HttpHpoiDataResolution() {
                        @Override
                        public void onFinish(String title, String imageurl, String url) {
                            mData.add(new Hpoidetailpageitem(imageurl));
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 在这里进行UI界面的访问或修改操作
                                    mLinearAdaoter.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void onInitData(String name, String value) {

                        }

                        @Override
                        public void onPages(int total) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
            }
        });
    }

    private void additemtextlayout(String name, String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 创建外层LinearLayout
                LinearLayout parentLayout = new LinearLayout(HpoiDetailpageActivity.this);
                parentLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                parentLayout.setOrientation(LinearLayout.HORIZONTAL);
                parentLayout.setPadding(DensityUtils.dip2px(HpoiDetailpageActivity.this,16), 0, 0, 0);

                // 创建内层TextView
                TextView textView = new TextView(HpoiDetailpageActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, DensityUtils.dip2px(HpoiDetailpageActivity.this,4), 0, 0);
                textView.setLayoutParams(params);
                textView.setText(name);
                textView.setTextColor(getResources().getColor(R.color.colorAllTextDark));
                textView.setTextSize(DensityUtils.dip2px(HpoiDetailpageActivity.this,4));

                TextView textViewvalue = new TextView(HpoiDetailpageActivity.this);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.setMargins(DensityUtils.dip2px(HpoiDetailpageActivity.this,10), DensityUtils.dip2px(HpoiDetailpageActivity.this,4), 0, 0);
                textViewvalue.setLayoutParams(params1);
                textViewvalue.setText(value);
                textViewvalue.setTextColor(getResources().getColor(R.color.colorAllTextDark));
                textViewvalue.setTextSize(DensityUtils.dip2px(HpoiDetailpageActivity.this,4));

                // 将TextView添加到父LinearLayout中
                parentLayout.addView(textView);
                parentLayout.addView(textViewvalue);

                // 将父LinearLayout添加到目标LinearLayout中
                mItem_TextLayout.addView(parentLayout);
            }
        });
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
//                        MyToast.makeText(HpoiDetailpageActivity.this, "已经在谷底了>_<", 1000,
//                                Gravity.FILL_HORIZONTAL | Gravity.BOTTOM).show();

                        mBut_loaduserpic.setVisibility(View.VISIBLE);

                    } else if (!recyclerView.canScrollVertically(-1)) {
                        //滑到顶部
                    } else if (dy > 0) {
                        //监听上滑
//                    mItemHolder.itemView.setAnimation();
                    } else if (dy < 0) {
                        //监听下滑
                        mBut_loaduserpic.setVisibility(View.GONE);
//                        mItemHolder.itemView.setAnimation(AnimationUtils.loadAnimation(mItemHolder.itemView.getContext(),R.anim.alpha));
                    }
                }
            }
        });

    }
}
