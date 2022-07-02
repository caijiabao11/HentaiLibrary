package com.example.administrator.lztsg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.lztsg.httpjson.TestholekoHttpJson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements TestholekilnActivity.SetInto {
    //获取TAG的fragment名称
    protected final String TAG = this.getClass().getSimpleName();
    public Context context;
    public TestholekoHttpJson testholekoHttpJson;
    public TestholekilnActivity.SetInto setInto;
    public TestholekilnActivity application;

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(ctx);
        context = ctx;
        application = (TestholekilnActivity) ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(initLyout(), container, true);
        TestholekilnActivity.setInto(this);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                    TestholekilnActivity.Into();
            }
        });
        initView(rootview);
        initData(context);
        return rootview;
    }

    public abstract int setContentLayout();

    protected abstract int initLyout();//初始化布局

    protected abstract void initView(View view);//控件获取

    protected abstract void initData(Context context);//数据结构

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract void onDestroyFragment();


}