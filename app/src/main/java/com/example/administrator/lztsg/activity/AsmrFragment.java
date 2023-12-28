package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.lztsg.MyToast;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.AsmrItem;
import com.example.administrator.lztsg.items.AsmrItemTags;
import com.example.administrator.lztsg.items.AsmrItemVas;
import com.example.administrator.lztsg.items.MultipleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class AsmrFragment extends BaseFragment implements BaseFragment.SetInto {
    private static final String KEY_POSITION = "position";
    public static LinearAdapter mLinearAdaoter;
    private static LinearAdapter.AsmrHolder mAsmrHolder;
    private LinearLayoutManager mLinearLayoutManager;
    private static RecyclerView mRecyclerView;
    public static Context context;
    public static List<MultipleItem> mData, mAllData;
    public static int[] firstStaggeredGridPosition = {0, 0};
    public static int[] lastStaggeredGridPosition = {0, 0};

    int selectedPosition = -1; // 默认为-1，表示没有选中任何项

    public static AsmrFragment newInstance(int position) {
        AsmrFragment fragment = new AsmrFragment();
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
        return R.layout.fragment_asmr;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = view.findViewById(R.id.run_main);
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
        mLinearLayoutManager = new LinearLayoutManager(context);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //初始化适配器
        mLinearAdaoter = new LinearAdapter(context,mData, new LinearAdapter.OnItemClickListener() {
            @Override
            public void itemonClick(int position, List<MultipleItem> mItems) {
                final Intent intent = new Intent(context, AsmrDetailpageActivity.class);
                //传递图片、标题、喜好、简介、番号信息
                Bundle bundle = new Bundle();
                AsmrItem item = (AsmrItem) mItems.get(position);

                bundle.putString("itemId", item.getId());
                bundle.putString("itemTitle", item.getTitle());

                bundle.putString("itemName", item.getName());
                bundle.putString("itemRelease", item.getRelease());
                JSONArray vas = item.getVas();
                JSONArray tags = item.getTags();

                List<AsmrItemVas> vasList = new ArrayList<>();
                List<AsmrItemTags> tagsList = new ArrayList<>();

                for (int i=0;i<vas.length();i++){
                    try {
                        JSONObject obj = (JSONObject) vas.get(i);
                        String vas_id = obj.getString("id");
                        String vas_name = obj.getString("name");
                        vasList.add(new AsmrItemVas(vas_id,vas_name));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i=0;i<tags.length();i++){
                    try {
                        JSONObject obj = (JSONObject) tags.get(i);
                        String tags_id = obj.getString("id");
                        String tags_name = obj.getString("name");
                        tagsList.add(new AsmrItemTags(tags_id,tags_name));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                bundle.putParcelableArrayList("itemVaslist", (ArrayList<? extends Parcelable>) vasList);
                bundle.putParcelableArrayList("itemTagslist", (ArrayList<? extends Parcelable>) tagsList);
                bundle.putString("itemCoverUrl", item.getCoverUrl());
                bundle.putInt("itemPosition", position);

                intent.putExtras(bundle);
                startActivity(intent);
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

    }

    @Override
    public void onAsmrFinish(String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl) {


        myDataAddOneTime(id,title,name,release,vas,tags,CoverUrl);
    }

    private void myDataAddOneTime( String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl) {
        Log.e("激活成功" + title, "激活成功");
        mData.add(new AsmrItem(id,title,name,release,vas,tags,CoverUrl));
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
