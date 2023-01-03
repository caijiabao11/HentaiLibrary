package com.example.administrator.lztsg.httpjson;

import android.os.Handler;
import android.util.Log;

public class TesthoekinIntoo {

    public void getOnCunzhiData(){
        android.os.Handler handler = new Handler();
        CunzhiHellHttpJson.getData(new HttpJsonResolution() {
            @Override
            public void onFinish(String title, String imageurl, String videourl, String duration) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        msetinto.onCunzhiHellFinish(title, imageurl, videourl, duration);
                        Log.e("tite","tite" + title);

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
