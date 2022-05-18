package com.example.administrator.lztsg.httpjson;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlService {
    private static int GOingUrl = 1;

    public static void getHtml(final String path, final String videourl, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                //抓取百度首页的数据，网络访问的数据可能会产生一些异常的情况，建议操作写在try里面
                //1,获取HttpURLConnection的实例
                //新建一个url
                URL url = null;
                try {
                    if (GOingUrl == 1){
                        //第一次跑首页
                        url = new URL(path);
                        GOingUrl = 2;
                    } else if (GOingUrl == 2){
                        //第二次跑视频链接
                        url = new URL(videourl);
                    }
                    connection = (HttpURLConnection) url.openConnection();
                    //2,设置http请求的参数
                    connection.setRequestMethod("GET");//设置请求方式为GET
                    connection.setConnectTimeout(8000);//设置连接超时为8s
                    connection.setReadTimeout(8000);//设置读取操作超时为8s
                    //3,调用connect（）方法连接远程资源，并对服务器响应进行判断
                    connection.connect();
                    int responsCode = connection.getResponseCode();//得到服务器响应的一个代码
                    if (responsCode == HttpURLConnection.HTTP_OK){
                        //进行数据读取操作
                        //4,利用getInputStream方法访问资源
                        //4.1利用getInputStream（）获取响应流
                        InputStream in = connection.getInputStream();
                        //4.2构建BufferedReader对象
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        //4.3构建字符串对象，用来接受缓冲流中的数据
                        StringBuilder sb = new StringBuilder();//动态拼接
                        String line = null;
                        while ((line = reader.readLine()) !=null){
                            sb.append(line);//使用StringBuilder的字符串可以实现动态的叠加
                        }
                        Log.e("HTML数据","HTML数据"+sb.toString());
                        //4.4将服务器返回的数据给接口
                        //textView.setText(toString());错误原因：UI的操作不能放在子线程操作，应该给放在主线程完成
                        if (listener != null){
                            //回调onFinish方法
                            listener.onFinish(sb.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                  if (connection != null){
                      //5.请求完成后，关闭连接
                      connection.disconnect();
                  }
                }
            }
        }).start();
    }
}
