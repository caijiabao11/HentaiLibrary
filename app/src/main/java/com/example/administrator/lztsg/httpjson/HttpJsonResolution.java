package com.example.administrator.lztsg.httpjson;

import java.util.ArrayList;

public interface HttpJsonResolution {
    void onFinish(String title, String imageurl, String videourl);
    void onError(Exception e);
}
