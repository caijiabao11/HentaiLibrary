package com.example.administrator.lztsg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.MusicPlayerService;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MusicPlayerFragment extends Fragment{
    // 创建BroadcastReceiver实例
    private BroadcastReceiver broadcastReceiver;
    private TextView textView;
    private ImageView imageView,but_player;
    private String gotitle,totit,icon;
    private int progress;
    private ProgressBar progressBar;
    private TimerTask previousTimerTask;
    private MusicPlayerService musicPlayerService;
    private boolean isBound = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player,container,false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        textView = view.findViewById(R.id.title);
        imageView = view.findViewById(R.id.img_icon);
        but_player = view.findViewById(R.id.but_player);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void initData() {
        // 创建BroadcastReceiver对象
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 处理接收到的广播
                if (intent.getAction().equals("com.lztsg.musicplayer_start")) {
                    // 广播处理逻辑
//                    Toast.makeText(context,"接收到了在fragment的广播",Toast.LENGTH_SHORT).show();
                    gotitle = intent.getStringExtra("title");
                    totit = "";
                    icon = intent.getStringExtra("icon");
                    progressBar.setMax(MusicPlayerService.mediaPlayer.getDuration());
                    if (textView.getText() == "" || !gotitle.equals(totit)){
                        totit = gotitle;
                        textView.setText(totit);

                        //图片
                        Glide.with(MyApplication.getContext())
                                .load(icon)
                                .centerCrop()
                                .into(imageView);
                    }

                    // 创建新的定时任务
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (MusicPlayerService.mediaPlayer!=null && MusicPlayerService.mediaPlayer.isPlaying()){
                                int progress = MusicPlayerService.mediaPlayer.getCurrentPosition();
                                Log.d("p", "run: " + progress);
                                progressBar.setProgress(progress);
                            }
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
            }
        };
        // 创建IntentFilter并添加广播的Action
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lztsg.musicplayer_start");
        // 注册BroadcastReceiver
        getActivity().registerReceiver(broadcastReceiver, intentFilter);

        but_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayerService.mediaPlayer != null){
                    //判断是否处于播放状态
                    boolean isPlaying = MusicPlayerService.mediaPlayer.isPlaying();
                    play(isPlaying);
                }
            }
        });
    }

    //播放或暂停歌曲
    public void play(boolean isPlaying) {
        Intent but_stat = new Intent();
        if (isPlaying) {
            MusicPlayerService.mediaPlayer.pause();
            but_player.setImageResource(R.drawable.ic_music_play_24);

//            // 创建意图
//            but_stat.setAction("com.lztsg.musicplayer_stop");
//            getActivity().sendBroadcast(but_stat);
        } else {
            MusicPlayerService.mediaPlayer.start();
            but_player.setImageResource(R.drawable.ic_music_pause_24);

//            // 创建意图
//            but_stat.setAction("com.lztsg.musicplayer_play");
//            getActivity().sendBroadcast(but_stat);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解除注册BroadcastReceiver
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
