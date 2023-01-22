package com.example.administrator.lztsg.httpjson;

public interface HttpLoadDataResolution {
    void onFinish(String imageurl,String size,String title,String titleurl,String domain,String description);
    void onError(Exception e);
}
