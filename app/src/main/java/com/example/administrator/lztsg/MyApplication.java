package com.example.administrator.lztsg;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }
    /**
     * 获取全局上下文
     */
    public static Context getContext(){
        return context;
    }
}
