package com.example.administrator.lztsg.httpjson;

import org.json.JSONArray;

public interface HttpAsmrJsonResolution {
    void onFinish(String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl);
    void onTracksFinish(JSONArray trackslist);
    void onPages(int total);
    void onError(Exception e);
}
