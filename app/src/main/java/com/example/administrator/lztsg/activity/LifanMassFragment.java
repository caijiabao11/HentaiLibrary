package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.administrator.lztsg.Constants;
import com.example.administrator.lztsg.Dao;
import com.example.administrator.lztsg.DatabaseHelper;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MultipleItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class LifanMassFragment extends Fragment implements View.OnClickListener  {
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
        if (dao == null){
            this.mData = new ArrayList<>();
            this.mAllData = new ArrayList<>();
            imageButton.setOnClickListener(this);
            DatabaseHelper helper = new DatabaseHelper(context);
            helper.getWritableDatabase();

            dao = new Dao(context);
            dao.query(Constants.TABLE_NAME_MASS,"mass_id");
            int indeax = dao.detaMasslist.size();
            int i = 0;
            while (i < indeax){
                Item a = (Item) dao.detaMasslist.get(i);

                String id = a.getId();
                String title = a.getTitle();
                String imgurl = a.getImgurl();

                mData.add(new Item(id,title,imgurl));
                i++;
            }
            mAllData.addAll(mData);
            LinearRecyclerView(mQieHuan);
        }else{
            LinearRecyclerView(mQieHuan);
        }
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
                    //传递社团标题、
                    Item item = (Item) mItems.get(position);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    LifanMass_putFragment mFrag5 = new LifanMass_putFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("itemMasstit",item.getTitle());
                    mFrag5.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragmentLayout, mFrag5);
                    fragmentTransaction.addToBackStack(null);
                    LifanActivity.currentFragment = mFrag5;
                    fragmentTransaction.commit();
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
