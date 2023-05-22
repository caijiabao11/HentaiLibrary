package com.example.administrator.lztsg.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.lztsg.CustomJZMp3;
import com.example.administrator.lztsg.MyChronometer;
import com.example.administrator.lztsg.MyToast;
import com.example.administrator.lztsg.PickerView;
import com.example.administrator.lztsg.R;
import com.github.mmin18.widget.RealtimeBlurView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class WhiteNoiseActivity extends AppCompatActivity implements View.OnClickListener {
    private PickerView mPickerView;
    private RealtimeBlurView mBlurView;
    private TextView mTit;
    private CustomJZMp3 mCustomJZMp3;
    private ViewPager2 mViewPager2;
    private CardView mMusicImg;
    private MyChronometer mTimeCmeter;
    private Button mBut_Sleepmore, mBut_Player;
    private String[] datas = {"0", "5", "15", "30", "60"};
    private static List<String> tit = new ArrayList<>();
    private int batetime;
    public static boolean play_or_pause = true;

    private String mp3url = "https://raw.kiko-play-niptan.one/media/stream/daily/2023-01-24/RJ418120/%E3%83%88%E3%83%A9%E3%83%83%E3%82%AF02%E3%80%80%E6%B2%BB%E7%99%82%E6%BA%96%E5%82%99(%E5%8B%83%E8%B5%B7%E3%83%91%E3%83%BC%E3%83%88)%E3%83%BB%E3%83%97%E3%83%AC%E3%82%A4%E5%86%85%E5%AE%B9%E3%80%8A%E6%A1%9C%E8%89%AF%E3%81%A1%E3%82%83%E3%82%93%E3%81%A8%E6%A1%83%E7%8B%90%E3%81%A1%E3%82%83%E3%82%93%E3%81%AE%E3%83%80%E3%83%96%E3%83%AB%E3%83%95%E3%82%A7%E3%83%A9%E3%83%81%E3%82%AA%E3%80%8B.mp3?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2FzbXIub25lIiwic3ViIjoiY2FpaG9tZSIsImF1ZCI6Imh0dHBzOi8vYXNtci5vbmUvYXBpIiwibmFtZSI6ImNhaWhvbWUiLCJncm91cCI6InVzZXIiLCJpYXQiOjE2NzMzNjQwNzAsImV4cCI6MTcwNDkwMDA3MH0.aDQmPPO7pgH-opf1J6Gz6CuFgx2XuOSo2SQL0t77taU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_noise);
        init();
        initData();

    }

    private void init() {
        mBut_Sleepmore = findViewById(R.id.but_sleepmore);
        mPickerView = findViewById(R.id.picler);
        mBlurView = findViewById(R.id.blurview);
        mTit = findViewById(R.id.tit);
        mViewPager2 = findViewById(R.id.v_pager);
        mTimeCmeter = findViewById(R.id.time_cmeter);
        mMusicImg = findViewById(R.id.card);
        mCustomJZMp3 = findViewById(R.id.noise_mp3);
        mBut_Player = findViewById(R.id.but_player);
    }

    private void initData() {
        //点击事件初始化
        mBut_Sleepmore.setOnClickListener(this);
        mBut_Player.setOnClickListener(this);
        mViewPager2.setOnClickListener(this);
        List<String> mData = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            mData.add(datas[i]);
        }
        mPickerView.setData(mData);
        mPickerView.setTextColor(R.color.colorAllTextDark);
        mPickerView.setOnSelectListener(new PickerView.onSelectListener() {
            int oengo = 0;

            @Override
            public void onSelect(String text) {
                MyToast myToast = new MyToast(WhiteNoiseActivity.this);
                myToast.makeText(text + "分钟",1000, Gravity.FILL_HORIZONTAL | Gravity.BOTTOM);
                myToast.show();
//                Toast.makeText(WhiteNoiseActivity.this, "选择了 " + text,
//                        Toast.LENGTH_SHORT).show();
                //选择的分钟
                batetime = (Integer.parseInt(text) * 1000) * 60;
                if (text.equals("0")) {
                    if (oengo == 0) {
                        OnAlphaFadeOutAnim(mBlurView);
//                    OnAlphaFadeOutAnim(mMusicImg);
                        OnAlphaFadeOutAnim(mTimeCmeter);
                    }
                    //归零
                    mTimeCmeter.setBase(SystemClock.elapsedRealtime());
                    oengo = 1;
                } else if (!text.equals("0")) {
                    if (oengo == 1) {
                        OnAlphaFadeAnim(mBlurView);
                        OnAlphaFadeAnim(mMusicImg);
                        OnAlphaFadeAnim(mTimeCmeter);
                    }
                    //倒计时开始
                    mTimeCmeter.setBase(SystemClock.elapsedRealtime() + batetime);
                    mTimeCmeter.start();
                    if (play_or_pause) {
                        OnPlayorOnPause();
                    }
                    oengo = 0;

                }
            }
        });

        mTimeCmeter.setOnChronometerTickListener(new MyChronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(MyChronometer myChronometer) {
                if ((SystemClock.elapsedRealtime() - mTimeCmeter.getBase()) >= 0) {
                    mTimeCmeter.stop();
                    if (play_or_pause == false) {
                        OnPlayorOnPause();
                    }
                }
            }
        });
        mTimeCmeter.setBase(SystemClock.elapsedRealtime());
        mTimeCmeter.setCountDown(true);

        //禁用预加载
        mViewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mViewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return NoiseFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_sleepmore:
                mPickerView.setVisibility(View.VISIBLE);
                mBlurView.setVisibility(View.VISIBLE);
//                mMusicImg.setVisibility(View.VISIBLE);
                mTimeCmeter.setVisibility(View.VISIBLE);
                OnAlphaFadeAnim(mBlurView);
//                OnAlphaFadeAnim(mMusicImg);
                OnAlphaFadeAnim(mTimeCmeter);
                break;
            case R.id.but_player:
                OnPlayorOnPause();
                break;
            case R.id.v_pager:
                OnPlayorOnPause();
                break;
        }
    }

    private void OnPlayorOnPause() {
        if (play_or_pause) {
            //继续
            mCustomJZMp3.goOnPlayOnResume();
            OnAlphaFadeOutAnim(mBut_Player);
            play_or_pause = false;
        } else {
            //暂停
            mCustomJZMp3.goOnPlayOnPause();
            OnAlphaFadeAnim(mBut_Player);
            play_or_pause = true;
        }
    }

    private void OnAlphaFadeAnim(View v) {
        ObjectAnimator alphaanimator = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        alphaanimator.setDuration(500).start();
    }

    private void OnAlphaFadeOutAnim(View v) {
        ObjectAnimator alphaanimator = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
        alphaanimator.setDuration(500).start();
    }
}
