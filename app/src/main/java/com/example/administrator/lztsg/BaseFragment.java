package com.example.administrator.lztsg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.lztsg.httpjson.CunzhiHellHttpJson;
import com.example.administrator.lztsg.httpjson.FapHeroHttpJson;
import com.example.administrator.lztsg.httpjson.HentaiJoiHttpJson;
import com.example.administrator.lztsg.httpjson.HttpJsonResolution;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    //获取TAG的fragment名称
    protected final String TAG = this.getClass().getSimpleName();
    private static SetInto mSetInto;
    private static final String KEY_POSITION = "position";
    private int position;
    private boolean isFirstLoad = true; //是否第一次加载
    public Context context;
    public TestholekilnActivity application;

    public static void setInto(SetInto setInto){
        mSetInto = setInto;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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
        initView(rootview);
        initData(context);
        return rootview;
    }

    public abstract int setContentLayout();

    protected abstract int initLyout();//初始化布局

    protected abstract void initView(View view);//控件获取

    protected abstract void initData(Context context);//数据结构

    public void onDataLoad(){
        //数据加载
        Handler handler = new Handler();

        position = getArguments().getInt(KEY_POSITION);
        Log.d("当前页面","当前页面"+position);
        switch (position){
           //地狱寸止fragment页面 ==0
           case 0:
               CunzhiHellHttpJson.getData(new HttpJsonResolution() {
                   @Override
                   public void onFinish(String title, String imageurl, String videourl, String duration) {
                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                               mSetInto.onCunzhiHellFinish(title, imageurl, videourl, duration);
                           }
                       });
                   }

                   @Override
                   public void onError(Exception e) {

                   }
               });
               break;

           //颅内高潮fragment页面 ==1
           case 1:
               HentaiJoiHttpJson.getData(new HttpJsonResolution() {
                   @Override
                   public void onFinish(String title, String imageurl, String videourl, String duration) {
                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                               mSetInto.onHentaiFinish(title, imageurl, videourl, duration);
                           }
                       });
                   }

                   @Override
                   public void onError(Exception e) {
                   }
               });
               break;

           //节奏大师fragment页面 ==2
           case 2:
               FapHeroHttpJson.getData(new HttpJsonResolution() {
                   @Override
                   public void onFinish(final String title, final String imageurl, final String videourl, final String duration) {
                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                               mSetInto.onFapHeroFinish(title, imageurl, videourl, duration);
                            }
                       });
                   }

                   @Override
                   public void onError(Exception e) {

                   }
               });
               break;
       }
    }

    public void onDataLoadStop(){
        //停止数据加载
    }

    @Override
    public void onResume() {
        super.onResume();
        //懒加载
        if (isFirstLoad && TAG.equals("PagerFragment")){
            Log.d(TAG,"onResume");
            onDataLoad();
            isFirstLoad = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract void onDestroyFragment();

    public interface SetInto{
        void onCunzhiHellFinish(String title, String imageurl, String videourl, String duration);
        void onHentaiFinish(String title, String imageurl, String videourl, String duration);
        void onFapHeroFinish(String title, String imageurl, String videourl, String duration);
    }
}