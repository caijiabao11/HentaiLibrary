package com.example.administrator.lztsg.httpjson;

public interface HttpJsonResolution {
    void onFinish(String title, String imageurl, String videourl, String duration);
    void onError(Exception e);
}
