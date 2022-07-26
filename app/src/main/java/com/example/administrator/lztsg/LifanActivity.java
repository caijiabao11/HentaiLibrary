package com.example.administrator.lztsg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MultipleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.transition.TransitionSet;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static com.example.administrator.lztsg.R.drawable.btn_arrow_black;
import static com.example.administrator.lztsg.R.drawable.btn_search;
import static com.example.administrator.lztsg.R.drawable.btn_search_notcolor;

public class LifanActivity extends AppCompatActivity {
    private LinearAdapter mLinearAdaoter;
    private LinearAdapter.ItemHolder mItemHolder;
    private ImageButton mImgButton,mSearchButton;
    private CardView mCardView;
    private EditText mSearch;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private RelativeLayout mRelativeLayoutSearch;
    private TransitionSet mSet;
    private Animator mShowAnim,mHideAnim;
    private Toolbar mToolbar;
    private List<MultipleItem> mData,mAllData;
    public int[] firstStaggeredGridPosition={0,0};
    public int[] lastStaggeredGridPosition={0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifan);
        setSupportActionBar(mToolbar);
        bindViews();
        onSearch();
        initData();
        LinearRecyclerView();
        ImageButtonOnClick();
        SearchButtonOnClick();
    }

    private void bindViews() {
        mRecyclerView = findViewById(R.id.run_main);
        mSearch = findViewById(R.id.edt_search);
        mImgButton = findViewById(R.id.imgbutton);
        mSearchButton = findViewById(R.id.searchbutton);
        mToolbar = findViewById(R.id.toolbar);
        mRelativeLayoutSearch = findViewById(R.id.rela_search);
        mCardView = findViewById(R.id.cardview);
    }

    //载入图片+标题
    private void initData() {
        this.mData = new ArrayList<>();
        this.mAllData = new ArrayList<>();
        int i = 1;
        while (i <= 1113)
        {
            Object localObject1 = getResources();
            Object localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("img_");
            ((StringBuilder)localObject2).append(i);
            int j = ((Resources)localObject1).getIdentifier(((StringBuilder)localObject2).toString(), "drawable", "com.example.administrator.lztsg");
            try
            {
                localObject1 = String.format(getResources().getString(R.string.title_ + i), new Object[0]);
                localObject2 = this.mData;
                StringBuilder localStringBuilder = new StringBuilder();
                //输出标题
                localStringBuilder.append("");
                localStringBuilder.append((String)localObject1);
                ((List)localObject2).add(new Item(j, localStringBuilder.toString()) {
                });

            }
            catch (NumberFormatException localNumberFormatException)
            {
                localNumberFormatException.printStackTrace();
            }
            i += 1;
        }
        mAllData.addAll(mData);
    }

    public void LinearRecyclerView(){
    //初始化线性布局管理器
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
    //设置布局管理器
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData, new LinearAdapter.OnItemClickListener() {

            @Override
            public void itemonClick(int position, List<MultipleItem> mItems) {
                  final Intent intent = new Intent(getApplication(),LifanDetailpageActivity.class);
//                //传递图片、标题信息
                Bundle bundle = new Bundle();
                Item item = (Item) mItems.get(position);
                bundle.putInt("itemImageId",item.getImageResId());
                bundle.putString("itemTitle",item.getTitle());
                intent.putExtras(bundle);

//                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                int realFirstPosition = Math.min(firstStaggeredGridPosition[0],firstStaggeredGridPosition[1]);
                mItemHolder = (LinearAdapter.ItemHolder)mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(position - realFirstPosition));
                //获取共享动画的View对象
                ItemsRoundImageView card_info_image= (ItemsRoundImageView) mItemHolder.image;
                //绑定共享空间，并赋予标签（便于寻找）
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LifanActivity.this,
                        Pair.<View, String>create(card_info_image, "item_info_image"));
                startActivity(intent,options.toBundle());
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
                if(layoutManager !=null && layoutManager instanceof StaggeredGridLayoutManager){
                    if (!recyclerView.canScrollVertically(1)){
                        //滑到顶部
                    } else if (!recyclerView.canScrollVertically(-1)){
                        //滑到底部
                    } else if (dy >0){
                        //监听上滑
//                    mItemHolder.itemView.setAnimation();
                    } else if (dy < 0){
                        //监听下滑
//                        mItemHolder.itemView.setAnimation(AnimationUtils.loadAnimation(mItemHolder.itemView.getContext(),R.anim.alpha));
                    }
                }
            }
        });
    }

    //返回箭头点击事件
    public void ImageButtonOnClick(){
        mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //搜索按钮点击展开
    public void SearchButtonOnClick(){
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            boolean isShow = true;
            @Override
            public void onClick(View v) {
                mRelativeLayoutSearch.setVisibility(View.VISIBLE);

                if (isShow == true) {
                    initShowAnim();
                    isShow = false;
                }else if (isShow == false){
                    initHideAnim();
                    isShow = true;
                }
            }
        });
    }

    //搜索框显示时的布局
    private void initShowAnim(){
        int centerX = mRelativeLayoutSearch.getMeasuredWidth() - (mSearchButton.getMeasuredWidth()+ mSearchButton.getMeasuredWidth() /2);
        int centerY = mRelativeLayoutSearch.getMeasuredHeight()/2;
        float startRadius = 0f;
        float endRadius = Math.max(mRelativeLayoutSearch.getWidth(),mRelativeLayoutSearch.getHeight());
        mShowAnim = ViewAnimationUtils.createCircularReveal(mRelativeLayoutSearch,centerX,centerY,startRadius,endRadius);
        expand();
        mShowAnim.setDuration(500).start();
        mShowAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSearch.setFocusable(true);
                mSearch.setFocusableInTouchMode(true);
                mSearch.requestFocus();

                InputMethodManager imm = (InputMethodManager)LifanActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

    }

    //搜索框隐藏时布局
    private void initHideAnim(){
        int centerX = mRelativeLayoutSearch.getMeasuredWidth() - (mSearchButton.getMeasuredWidth()+ mSearchButton.getMeasuredWidth() /2);
        int centerY = mRelativeLayoutSearch.getMeasuredHeight()/2;
        float startRadius = Math.max(mRelativeLayoutSearch.getWidth(),mRelativeLayoutSearch.getHeight());
        float endRadius = 0f;
        mHideAnim = ViewAnimationUtils.createCircularReveal(mRelativeLayoutSearch,centerX,centerY,startRadius,endRadius);
        reduce();
        if (reduce()==true){
            //延时500毫秒
            Timer timer = new Timer();
            TimerTask tack = new TimerTask(){
                public void run(){
                    mSearchButton.setBackgroundResource(btn_search);
                }
            };
            timer.schedule(tack,500);

        }
        mHideAnim.setDuration(500).start();
        mHideAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRelativeLayoutSearch.setVisibility(View.INVISIBLE);

                InputMethodManager imm = (InputMethodManager)LifanActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
            }
        });

    }

    //搜索按钮————>返回按钮
    private void expand(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mSearchButton,"translationX",0,-840);
        objectAnimator.setDuration(500).start();
        mSearchButton.setBackgroundResource(btn_arrow_black);
    }

    //返回按钮————>搜索按钮
    private boolean reduce(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mSearchButton,"translationX",-840,0);
        objectAnimator.setDuration(500).start();
        mSearchButton.setBackgroundResource(btn_search_notcolor);
        return true;
    }

    //搜索框
    public void onSearch(){
        mSearch.setWidth(885);
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            //监听搜索按钮
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) mSearch.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            LifanActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void afterTextChanged(Editable s) {
                //这是文本框改变之后 会执行的动作
                    doChangeColor(mSearch.toString().trim());

            }
            private void doChangeColor(String text) {
                    String data = mSearch.getText().toString();
                    mData.clear();

                    for (MultipleItem item:mAllData){
                        final Item value = (Item) item;
                        if (value.getTitle().contains(data)){
                            mData.add(value);
                        }
                    }
                    //刷新
                    mLinearAdaoter.notifyDataSetChanged();
            }
        });

    }
}