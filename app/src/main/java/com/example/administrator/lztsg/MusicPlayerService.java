package com.example.administrator.lztsg;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;

import com.example.administrator.lztsg.activity.MusicPlayerItemActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class MusicPlayerService extends Service {
    public static MediaPlayer mediaPlayer;
    private final IBinder binder = new MyBinder();
    public static String mediaStreamUrl,title,icon;
    public static JSONArray item_Audios;
    public static int progress,currentAudeoIndex;
    private TimerTask previousTimerTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }


        //返回歌曲的长度，单位为毫秒
        public int getDuration(){
            return mediaPlayer.getDuration();
        }

        //返回歌曲目前的进度，单位为毫秒
        public int getCurrenPostion(){
            return mediaPlayer.getCurrentPosition();
        }

        //设置歌曲播放的进度，单位为毫秒
        public void seekTo(int mesc){
            mediaPlayer.seekTo(mesc);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        // 获取应用程序的上下文
//        customJZMp3 = AsmrDetailpageActivity.mCustomJZMp3;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mediaStreamUrl = intent.getStringExtra("mediaStreamUrl");
            title = intent.getStringExtra("title");
            icon = intent.getStringExtra("icon");
            currentAudeoIndex = intent.getIntExtra("currentAudeoIndex",0);
            try {
                item_Audios = new JSONArray(intent.getStringExtra("itemAudios"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            switchToNextSong(mediaStreamUrl);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void switchToNextSong(String mediaStreamUrl) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // 创建新的MediaPlayer实例并加载新的音频文件
        mediaPlayer = MediaPlayer.create(this, Uri.parse(mediaStreamUrl));
        // 播放新的音频文件
        mediaPlayer.start();

        // 设置播放完成监听器
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 当当前歌曲播放完成时，自动播放下一曲
                nextplay(currentAudeoIndex);
            }
        });

        // 发送广播通知歌曲
        Intent i = new Intent(this,MusicPlayerReceiver.class);
        i.setAction("com.lztsg.musicplayer_start");
        i.putExtra("title",title);
        i.putExtra("icon",icon);
        sendBroadcast(i);
        Log.e("服务", "准备播放音乐");

        // 创建新的定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent i1 = new Intent();
                // 发送广播播放音乐
                i1.setAction("com.lztsg.musicplayer_start");
                i1.putExtra("title",title);
                i1.putExtra("icon",icon);
                sendBroadcast(i1);
            }
        };
        // 取消之前的定时任务
        if (previousTimerTask != null) {
            previousTimerTask.cancel();
        }
        // 执行新的定时任务
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
        // 将新的定时任务赋值给 previousTimerTask
        previousTimerTask = timerTask;
    }

    public static void nextplay(int index) {
        //下一曲
        index++;
        currentAudeoIndex = index;
        if (currentAudeoIndex < item_Audios.length()) {
            try {
                JSONObject item1 = (JSONObject) item_Audios.get(currentAudeoIndex);
                MusicPlayerService.title = item1.getString("title");
                MusicPlayerService.mediaStreamUrl = item1.getString("mediaStreamUrl");
                mediaPlayer.reset();
                mediaPlayer.setDataSource(MusicPlayerService.mediaStreamUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (MusicPlayerItemActivity.mItem_tit != null && MusicPlayerItemActivity.mItem_tit != null){
            MusicPlayerItemActivity.mItem_tit.setText(MusicPlayerService.title);
            MusicPlayerItemActivity.mItem_tit.invalidate();// 刷新TextView的UI
        }
    }

    public static void lastplay(int index) {
        //上一曲
        index--;
        currentAudeoIndex = index;
        if (currentAudeoIndex < item_Audios.length() && currentAudeoIndex >= 0) {
            try {
                JSONObject item1 = (JSONObject) item_Audios.get(currentAudeoIndex);
                MusicPlayerService.title = item1.getString("title");
                MusicPlayerService.mediaStreamUrl = item1.getString("mediaStreamUrl");
                mediaPlayer.reset();
                mediaPlayer.setDataSource(MusicPlayerService.mediaStreamUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (currentAudeoIndex < 0){
            MyToast.makeText(MyApplication.getContext(),"已经到顶啦~~",
                    1000, Gravity.FILL_HORIZONTAL | Gravity.BOTTOM).show();
        }
        if (MusicPlayerItemActivity.mItem_tit != null && MusicPlayerItemActivity.mItem_tit != null){
            MusicPlayerItemActivity.mItem_tit.setText(MusicPlayerService.title);
            MusicPlayerItemActivity.mItem_tit.invalidate();// 刷新TextView的UI
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
