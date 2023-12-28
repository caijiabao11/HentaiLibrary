package com.example.administrator.lztsg.httpjson;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HtmlServiceOkHttp {
    private static int maxRequests = 10; // 最大并发请求数量
    private static int maxRequestsPerHost = 5; // 同一个主机的最大并发请求数量

    public static void getHtml(final String path, final String videourl, final int GOingUrl, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                OkHttpClient client = new OkHttpClient();
                OkHttpClient client = new OkHttpClient.Builder()
                        .dispatcher(new Dispatcher(
                                new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                                        new SynchronousQueue<Runnable>())))
                        .build();

                client.dispatcher().setMaxRequests(maxRequests);
                client.dispatcher().setMaxRequestsPerHost(maxRequestsPerHost);
                //新建一个url
                String url = null;
                if (GOingUrl == 1){
                    //第一次跑首页
                    url = path;
                } else if (GOingUrl == 2){
                    //第二次跑视频链接
                    url = videourl;
                }
                if (url != null){
                    Request request = new Request.Builder()
                            .url(url) // 替换为服务器上数据库文件的URL
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            // 处理服务器响应数据
                            String responseData = response.body().string();
                            Log.i("HTML数据","HTML数据"+ responseData);
                            //将服务器返回的数据给接口
                            if (listener != null){
                                //回调onFinish方法
                                listener.onFinish(responseData);
                            }
                        } else {
                            // 处理请求失败的情况
                            Log.i("连接状态码错误", "" + response.isSuccessful());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("htmlservice请求错误回调", "run: "+ e );
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }

    public static void postHtml(final String path, RequestBody body, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                if (path != null){

                    Request request = new Request.Builder()
                            .url(path)
                            .post(body)
                            .addHeader("Charset", "UTF-8")
                            .addHeader("Connection", "Keep-Alive")
                            .addHeader("Content-type", "image/*")
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            // 处理服务器响应数据
                            //将服务器返回的数据给接口
                            if (listener != null){
                                //回调onFinish方法
                                listener.onFinish(responseData);
                            }
                        } else {
                            // 处理请求失败的情况
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
