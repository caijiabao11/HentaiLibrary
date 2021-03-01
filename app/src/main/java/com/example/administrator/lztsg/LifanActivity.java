package com.example.administrator.lztsg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LifanActivity extends AppCompatActivity{
    private LinearAdapter mLinearAdaoter;
    private ImageButton mImgButton;
    private EditText mSearch;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private Drawable mdrawable;
    private ImageView mimagevuew;
    private List<Item> mData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifan);
        bindViews();
        onSearch();
        initData();
        LinearRecyclerView();

    }

    private void bindViews() {
        mRecyclerView = findViewById(R.id.run_main);
        mSearch = findViewById(R.id.edt_search);
        mImgButton = findViewById(R.id.imgbutton);

    }
    //载入图片+标题
    private void initData() {
        this.mData = new ArrayList<>();
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
        mImgButton.setColorFilter(R.color.colorAccent);
        mImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //搜索框
    public void onSearch(){
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
//                if (s == null) {
//                }
                 //   Toast.makeText(getApplicationContext(),mSearch.getText().toString()+"",Toast.LENGTH_SHORT).show();
                    //doChangeColor(mSearch.toString().trim());
                      mLinearAdaoter.getFilter().filter(s);
                      if (s.length()>0){
                          mSearch.setVisibility(View.VISIBLE);
                      }else{
                          mSearch.setVisibility(View.INVISIBLE);
                      }
            }
//            private void doChangeColor(String text) {
//                    String data = mSearch.getText().toString();
//                    mData.clear();
//                    //刷新
//                    mLinearAdaoter.notifyDataSetChanged();
//            }
        });
    }
}