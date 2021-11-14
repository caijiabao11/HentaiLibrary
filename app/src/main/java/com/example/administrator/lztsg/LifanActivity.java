package com.example.administrator.lztsg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.transition.TransitionSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.lztsg.R.drawable.ic_arrow_back_black_24dp;
import static com.example.administrator.lztsg.R.drawable.ic_search_black_24dp;

public class LifanActivity extends AppCompatActivity{
    private LinearAdapter mLinearAdaoter;
    private ImageButton mImgButton;
    private ImageButton mSearchButton;
    private EditText mSearch;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RelativeLayout mRelativeLayoutSearch;
    private TransitionSet mSet;
    private Animator mShowAnim;
    private Animator mHideAnim;
    private Toolbar mToolbar;
    private List<Item> mData;
    private List<Item> mAllData;


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
                ((List)localObject2).add(new Item(j, localStringBuilder.toString()));

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
        mLinearLayoutManager = new LinearLayoutManager(this);
    //设置布局管理器
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData);
    //设置适配器
        mRecyclerView.setAdapter(mLinearAdaoter);
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
        int centerX = mRelativeLayoutSearch.getMeasuredWidth() - (mSearchButton.getMeasuredWidth()+ mSearchButton.getMeasuredWidth() /2)+ 6;
        int centerY = mRelativeLayoutSearch.getMeasuredHeight()/2;
        float startRadius = 0f;
        float endRadius = Math.max(mRelativeLayoutSearch.getWidth(),mRelativeLayoutSearch.getHeight());
        mShowAnim = ViewAnimationUtils.createCircularReveal(mRelativeLayoutSearch,centerX,centerY,startRadius,endRadius);
        mShowAnim.setDuration(500).start();
        expand();
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
        int centerX = mRelativeLayoutSearch.getMeasuredWidth() - (mSearchButton.getMeasuredWidth()+ mSearchButton.getMeasuredWidth() /2)+ 6;
        int centerY = mRelativeLayoutSearch.getMeasuredHeight()/2;
        float startRadius = Math.max(mRelativeLayoutSearch.getWidth(),mRelativeLayoutSearch.getHeight());
        float endRadius = 0f;
        mHideAnim = ViewAnimationUtils.createCircularReveal(mRelativeLayoutSearch,centerX,centerY,startRadius,endRadius);
        mHideAnim.setDuration(500).start();
        reduce();
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
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mSearchButton,"translationX",0,-885);
        objectAnimator.setDuration(500).start();
        mSearchButton.setBackgroundResource(ic_arrow_back_black_24dp);
    }

    //返回按钮————>搜索按钮
    private void reduce(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mSearchButton,"translationX",-885,0);
        objectAnimator.setDuration(500).start();
        mSearchButton.setBackgroundResource(ic_search_black_24dp);
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
                    for (Item item:mAllData){
                        if (item.getTitle().contains(data)){
                            mData.add(item);
                        }
                    }
                    //刷新
                    mLinearAdaoter.notifyDataSetChanged();
            }
        });

    }
}