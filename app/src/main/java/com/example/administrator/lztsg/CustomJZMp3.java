package com.example.administrator.lztsg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.lztsg.activity.AsmrDetailpageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

public class CustomJZMp3 extends JzvdStd {
    Context context;
    private String TAG;
    private int currentVideoIndex = 0;
    private JSONArray mp3List;

    public CustomJZMp3(Context context) {
        super(context);
        this.context = context;
    }

    public CustomJZMp3(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TAG = context.getClass().getSimpleName();
    }

    @Override
    public int getLayoutId() {
        return R.layout.custon_mp3view;
    }

    @Override
    public void onClick(View v) {
    }

    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();

    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();

    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();

    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();

    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        //并播放完后这里切换下一个视频的url
        playNext();
    }

    private void playNext() {
        try {
            switch (TAG){
                case "AsmrDetailpageActivity":
                    mp3List = new JSONArray(AsmrDetailpageActivity.allurl.toString());
                    currentVideoIndex = AsmrDetailpageActivity.currentAudeoIndex;
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 获取当前播放的歌曲索引
        if (currentVideoIndex<mp3List.length()) {
            currentVideoIndex++;
            try {
                JSONObject item = (JSONObject) mp3List.get(currentVideoIndex);
                String mediaStreamUrl = item.getString("mediaStreamUrl");
                String title = item.getString("title");
                changeUrl(new JZDataSource(mediaStreamUrl,title),0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
