package com.example.administrator.lztsg.httpjson;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
