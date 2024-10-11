package com.example.administrator.lztsg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.MusicPlayerReceiver;
import com.example.administrator.lztsg.MusicPlayerService;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.MyListDialog;
import com.example.administrator.lztsg.PlayerStateViewModel;
import com.example.administrator.lztsg.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MusicPlayerItemActivity extends AppCompatActivity {
    public static TextView mItem_tit,mItem_timemax,mItem_timedo;
    private ImageView mItem_img,mItem_bg,but_player,but_dwon,but_musiclike,but_playlist,but_last,but_next;
    private TimerTask previousTimerTask;
    private SeekBar mProgress;
    private boolean isPlaying;
    private static Context context;
    private PlayerStateViewModel playerStateViewModel;
    // 创建BroadcastReceiver实例
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MusicPlayerItemActivity.context = context;
            if (intent.getAction().equals("com.lztsg.musicplayer_stop")) {
                Log.d("MusicPlayerItemActivity", "暂停音乐");
                playerStateViewModel.setData(false);
                but_player.setImageResource(R.drawable.ic_music_play);
            }
            if (intent.getAction().equals("com.lztsg.musicplayer_play")) {
                Log.d("MusicPlayerItemActivity", "播放音乐");
                playerStateViewModel.setData(true);
                but_player.setImageResource(R.drawable.ic_music_pause);
            }
            if (intent.getAction().equals("com.lztsg.musicplayer_receiver_play")){
                String TAG = context.getClass().getSimpleName();
                if (MusicPlayerService.mediaPlayer != null && TAG.equals("MusicPlayerItemActivity")){
                    Log.d(TAG, "receiver_play: "+"接收广播");

                    isPlaying = MusicPlayerService.mediaPlayer.isPlaying();
//                        progressBar.setProgress(MusicPlayerService.mediaPlayer.getCurrentPosition());
                    play(isPlaying,MusicPlayerItemActivity.this);

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Slide());
        setContentView(R.layout.activity_music_player_item);
        init();
        initData();

        playerStateViewModel = new ViewModelProvider(this).get(PlayerStateViewModel.class);

        // 创建IntentFilter并添加广播的Action
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lztsg.musicplayer_play");
        intentFilter.addAction("com.lztsg.musicplayer_stop");
        intentFilter.addAction("com.lztsg.musicplayer_receiver_play");
        // 注册BroadcastReceiver
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void init(){
        mItem_bg = findViewById(R.id.item_bg);
        mItem_img = findViewById(R.id.img_icon);
        mItem_tit = findViewById(R.id.title);
        mItem_timemax = findViewById(R.id.text_timemax);
        mItem_timedo = findViewById(R.id.text_timedo);
        mProgress = findViewById(R.id.progressBar);
        but_player = findViewById(R.id.but_player);
        but_dwon = findViewById(R.id.but_dwon);
        but_musiclike = findViewById(R.id.but_music_playlike);
        but_playlist = findViewById(R.id.but_playlist);
        but_last = findViewById(R.id.but_last);
        but_next = findViewById(R.id.but_next);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        int max = MusicPlayerService.mediaPlayer.getDuration();



        mItem_timemax.setText(getTime(max));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mItem_tit.setText(bundle.getString("itemTitle"));
                mItem_tit.invalidate();
            }
        });
        mProgress.setMax(max);

        // 创建新的定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (MusicPlayerService.mediaPlayer!=null && MusicPlayerService.mediaPlayer.isPlaying()){
                    int progress = MusicPlayerService.mediaPlayer.getCurrentPosition();
                    Log.d("s", "run: " + progress);

                    // 将毫秒转换为分钟和秒
                    int minutes = (progress / 1000) / 60;
                    int seconds = (progress / 1000) % 60;

                    // 格式化为"0:00"形式
                    String formattedDuration = String.format("%d:%02d", minutes, seconds);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mItem_timedo.setText(getTime(progress));
                        }
                    });

                    mProgress.setProgress(progress);
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

        //返回点击事件
        but_dwon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //喜好点击事件
        but_musiclike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //播放列表点击事件
        but_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyListDialog myListDialog = new MyListDialog(MusicPlayerItemActivity.this);
                Window window = myListDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = window.getAttributes();
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.gravity = Gravity.BOTTOM; // 设置对话框的位置为底部
                    window.setAttributes(layoutParams);
                }
                myListDialog.show();
                myListDialog.LinearRecyclerView(AsmrDetailpageActivity.allurl);
            }
        });
        //上一曲点击事件
        but_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayerService.lastplay(MusicPlayerService.currentAudeoIndex);
                playbroadcast();
            }
        });
        //下一曲点击事件
        but_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayerService.nextplay(MusicPlayerService.currentAudeoIndex);
                playbroadcast();
            }
        });
        //图片

        Glide.with(MyApplication.getContext())
                .load(bundle.getString("itemImgurl"))
                .placeholder(R.drawable.none)
                .fitCenter()
                .into(mItem_bg);
        Glide.with(MyApplication.getContext())
                .load(bundle.getString("itemImgurl"))
                .placeholder(R.drawable.none)
                .fitCenter()
                .into(mItem_img);
        isPlaying = MusicPlayerService.mediaPlayer.isPlaying();
        if (isPlaying){
            but_player.setImageResource(R.drawable.ic_music_pause);
        }else if (!isPlaying){
            but_player.setImageResource(R.drawable.ic_music_play);
        }
        but_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicPlayerService.mediaPlayer != null){
                    //判断是否处于播放状态
                    if (MusicPlayerService.mediaPlayer != null){
                        isPlaying = MusicPlayerService.mediaPlayer.isPlaying();

                        play(isPlaying, MusicPlayerItemActivity.this);

                    }
                }
            }
        });
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 当SeekBar的进度发生变化时调用
                // 可以在这里处理进度变化的逻辑
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 当用户开始拖动SeekBar时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当用户停止拖动SeekBar时调用
                MusicPlayerService.mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private String getTime(int duration) {
        // 将毫秒转换为分钟和秒
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;

        // 格式化为"0:00"形式
        String formattedDuration = String.format("%d:%02d", minutes, seconds);

        return formattedDuration;
    }

    //播放或暂停歌曲
    public void play(boolean isPlaying, Context context) {
        Intent but_stat = new Intent();

        if (isPlaying) {
            MusicPlayerService.mediaPlayer.pause();
            but_player.setImageResource(R.drawable.ic_music_play);
            // 创建意图
            but_stat.setAction("com.lztsg.musicplayer_stop");
            sendBroadcast(but_stat);
        } else {
            MusicPlayerService.mediaPlayer.start();
            but_player.setImageResource(R.drawable.ic_music_pause);

            // 创建意图
            but_stat.setAction("com.lztsg.musicplayer_play");
            sendBroadcast(but_stat);
        }
        playbroadcast();

    }

    public void playbroadcast(){
        //更新通知广播
        Intent but_play = new Intent(MyApplication.getContext(), MusicPlayerReceiver.class);
        String title = MusicPlayerService.title;

        but_play.setAction("com.lztsg.musicplayer_receiver_play");
        but_play.putExtra("title",title);
        sendBroadcast(but_play);
    }
}
