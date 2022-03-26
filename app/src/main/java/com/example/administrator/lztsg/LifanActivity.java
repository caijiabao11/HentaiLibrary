package com.example.administrator.lztsg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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

import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MultipleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionSet;

import static com.example.administrator.lztsg.R.drawable.btn_arrow_black;
import static com.example.administrator.lztsg.R.drawable.btn_search;
import static com.example.administrator.lztsg.R.drawable.btn_search_notcolor;

public class LifanActivity extends AppCompatActivity {
    private LinearAdapter mLinearAdaoter;
    private ImageButton mImgButton,mSearchButton;
    private EditText mSearch;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private RelativeLayout mRelativeLayoutSearch;
    private TransitionSet mSet;
    private Animator mShowAnim,mHideAnim;
    private Toolbar mToolbar;
    private List<MultipleItem> mData,mAllData;


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
        mGridLayoutManager = new GridLayoutManager(this,2);
    //设置布局管理器
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    //初始化适配器
        mLinearAdaoter = new LinearAdapter(mData, new LinearAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
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
        int centerX = mRelativeLayoutSearch.getMeasuredWidth() - (mSearchButton.getMeasuredWidth()+ mSearchButton.getMeasuredWidth() /2)+ 6;
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