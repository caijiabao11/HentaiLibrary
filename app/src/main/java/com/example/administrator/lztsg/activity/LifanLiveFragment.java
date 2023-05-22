package com.example.administrator.lztsg.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.administrator.lztsg.Constants;
import com.example.administrator.lztsg.Dao;
import com.example.administrator.lztsg.DatabaseHelper;
import com.example.administrator.lztsg.ItemsRoundImageView;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MultipleItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class LifanLiveFragment extends Fragment implements View.OnClickListener  {
    public static LinearAdapter mLinearAdaoter;
    private static LinearAdapter.ItemHolder mItemHolder;
    private static StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private static RecyclerView mRecyclerView;
    public static Dao dao;
    private static Context context;
    public static boolean mQieHuan = LifanMoreFragment.mQieHuan;
    public static List<MultipleItem> mData;
    public static List<MultipleItem> mAllData;
    public static int[] firstStaggeredGridPosition = {0, 0};
    public static int[] lastStaggeredGridPosition = {0, 0};
    private ImageButton imageButton;
    private LinearLayout mTab_main;

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(context);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_lifan_more,container,false);
        initView(rootview);
        initData(context);

        return rootview;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.run_main);
        imageButton = getActivity().findViewById(R.id.imgbutton);
        mTab_main = getActivity().findViewById(R.id.tab_main);
    }
    private void initData(Context context) {
        this.mData = new ArrayList<>();
        this.mAllData = new ArrayList<>();
        imageButton.setOnClickListener(this);
        DatabaseHelper helper = new DatabaseHelper(context);
        helper.getWritableDatabase();

        dao = new Dao(context);
        dao.query(Constants.TABLE_NAME_MIAN,"preferences","1");
        dao.query(Constants.TABLE_NAME_MASS,"mass_id");
        dao.query(Constants.TABLE_NAME_TAG,"tag_id");
        int indeax = dao.detalist.size();
//        int indeax = LifanDetailpageActivity.mData.size();
        int i = 0;
        while (i < indeax){
            Item a = (Item) dao.detalist.get(i);
//            Item a = (Item) LifanDetailpageActivity.mData.get(i);
            String id = a.getId();
            String title = a.getTitle();
            String introduction = a.getIntroduction();
            int preferences = a.getPreferences();
            String imgurl = a.getImgurl();
            int mass = a.getmMassid();
            String tag = a.getmTag();
            String putmass = getMassname(mass);
            mData.add(new Item(id,title,introduction,preferences,imgurl,putmass,tag));
            i++;
        }
        mAllData.addAll(mData);
        LinearRecyclerView(mQieHuan);
    }

    private String getMassname(int id) {
        String putindex = "" + id;
        for (MultipleItem item :  dao.detaMasslist) {
            final Item value = (Item) item;
            //.startsWith   以指定字符串开头筛选（精准搜索）
            //.contains     以字符串中是否存在筛选（模糊搜索）
            if (value.getId().startsWith(putindex)) {
                return value.getTitle();
            }
        }
        return null;
    }

    public void LinearRecyclerView(boolean mQieHuan) {
        if (mQieHuan) {
            //初始化线性布局管理器
            mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            //设置布局管理器
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        } else if (mQieHuan == false) {
            //初始化线性布局管理器
            mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            //设置布局管理器
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData, mQieHuan, new LinearAdapter.OnItemClickListener() {
            @Override
            public void itemonClick(int position, List<MultipleItem> mItems) {
                if (isAdded()){
                    final Intent intent = new Intent(context, LifanDetailpageActivity.class);
                    //传递图片、标题、喜好、简介、番号信息
                    Bundle bundle = new Bundle();
                    Item item = (Item) mItems.get(position);

                    bundle.putString("itemId", item.getId());
                    bundle.putString("itemTitle", item.getTitle());
                    bundle.putString("itemIntroduction", item.getIntroduction());
                    bundle.putInt("itemPreferences", item.getPreferences());
                    bundle.putString("itemImgurl", item.getImgurl());
                    bundle.putString("itemMassid", item.getmMass_id());
                    bundle.putString("itemTag", item.getmTag());
                    bundle.putInt("item1Position",position);

                    intent.putExtras(bundle);
//                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                    int realFirstPosition = 0;
                    if (mQieHuan) {
                        realFirstPosition = Math.min(firstStaggeredGridPosition[0], lastStaggeredGridPosition[0]);
                    } else if (mQieHuan == false) {
                        realFirstPosition = Math.min(firstStaggeredGridPosition[0], lastStaggeredGridPosition[1]);
                    }
                    mItemHolder = (LinearAdapter.ItemHolder) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(position - realFirstPosition));
                    //获取共享动画的View对象
                    ItemsRoundImageView card_info_image = (ItemsRoundImageView) mItemHolder.image;
//                ImageView card_info_image = mItemHolder.image;
                    //绑定共享空间，并赋予标签（便于寻找）
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                            Pair.<View, String>create(card_info_image, "item_info_image"));
                    startActivity(intent, options.toBundle());
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
                        //滑到顶部
                    } else if (!recyclerView.canScrollVertically(-1)) {
                        //滑到底部
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbutton:
                if (mQieHuan) {
                    mQieHuan = false;
                    mTab_main.setVisibility(View.GONE);
                } else if (mQieHuan == false) {
                    mQieHuan = true;
                    mTab_main.setVisibility(View.VISIBLE);
                }
                LinearRecyclerView(mQieHuan);
                break;
        }
    }
}
