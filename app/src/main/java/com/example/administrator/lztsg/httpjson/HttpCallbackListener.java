package com.example.administrator.lztsg.httpjson;

public interface HttpCallbackListener {
    String onFinish(String response);
    void onError(Exception e);
}
