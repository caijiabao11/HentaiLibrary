package com.example.administrator.lztsg;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class MusicPlayerService extends Service {
    public static MediaPlayer mediaPlayer;
    private final IBinder binder = new MyBinder();
    public static String mediaStreamUrl,title,icon;
    public static int progress;
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
        //判断是否处于播放状态
        public boolean isPlaying(){
            return mediaPlayer.isPlaying();
        }

        //播放或暂停歌曲
        public void play() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
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
        // 发送广播通知歌曲
        Intent i = new Intent(this,AlarmReceiver.class);
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

    public String getMediaStreamUrl(){
        return mediaStreamUrl;
    }
    public String getTitle(){
        return title;
    }
    public String getIcon(){
        return icon;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
