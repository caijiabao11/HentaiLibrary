package com.example.administrator.lztsg.httpjson;

public interface HttpHpoiDataResolution {
    void onFinish(String title, String imageurl, String url);
    void onInitData(String name, String value);
    void onPages(int total);
    void onError(Exception e);
}
