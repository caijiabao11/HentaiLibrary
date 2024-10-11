package com.example.administrator.lztsg;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lztsg.activity.LinearAdapter;
import com.example.administrator.lztsg.items.MultipleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyListDialog extends Dialog {
    private static final String KEY_POSITION = "position";
    public static LinearAdapter mLinearAdaoter;
    private MusicPlayerlistAdapter.MusicPlaylistHolder musicPlaylistHolder;
    private GridLayoutManager mGridLayoutManager;
    private MusicPlayerlistAdapter mMusicPlayerlistAdapter;
    private static RecyclerView mRecyclerView;
    public static Context context;
    public static List<MultipleItem> mData, mAllData;
    public static int[] firstStaggeredGridPosition = {0, 0};
    public static int[] lastStaggeredGridPosition = {0, 0};

    public MyListDialog(@NonNull Context context) {
        super(context);
    }

    protected MyListDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist_dialog);
        init();
    }

    private void init() {
//        mImage = findViewById(R.id.item_img);
//        mId = findViewById(R.id.text_id);
//        mRelease = findViewById(R.id.text_release);
//        mTitle = findViewById(R.id.item_tit);
//        mTags = findViewById(R.id.item_tag);
//        mName = findViewById(R.id.item_mass);
        mRecyclerView = findViewById(R.id.run_main);

    }

    public void LinearRecyclerView(JSONArray json) {
     //初始化线性布局管理器
        mGridLayoutManager = new GridLayoutManager(getContext(), 1);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mMusicPlayerlistAdapter = new MusicPlayerlistAdapter(json, new MusicPlayerlistAdapter.OnItemClickListener() {
            @Override
            public void itemAsmrTracksClick(int position) {
                //点击item判断是否还有下一级
                try {
                    JSONObject item = (JSONObject) json.get(position);
                    for (int i=0;i<json.length();i++){
                        JSONObject item1 = (JSONObject) json.get(i);


                        String title = item1.getString("title");
                        if (title.equals(item.getString("title"))){
                            //设置Item文字
                            MusicPlayerlistAdapter.musicPlaylistHolder.textView.setText("正在播放");
                            playbroadcast(title);
                        } else{
                            MusicPlayerlistAdapter.musicPlaylistHolder.textView.setText(title);
                        }
                    }
                    MusicPlayerService.nextplay(position - 1);
                    mMusicPlayerlistAdapter.notifyDataSetChanged();
//                    Intent intent = new Intent(MyApplication.getContext(), MusicPlayerService.class);
//                    intent.putExtra("itemAudios",json.toString());
//                    intent.putExtra("currentAudeoIndex",position);
//                    getContext().startService(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mRecyclerView.setAdapter(mMusicPlayerlistAdapter);
    }

    public void playbroadcast(String tit){
        //更新通知广播
        Intent but_play = new Intent(MyApplication.getContext(), MusicPlayerReceiver.class);

        but_play.setAction("com.lztsg.musicplayer_receiver_play");
        but_play.putExtra("title",tit);
        getContext().sendBroadcast(but_play);
    }

    // 添加进入动画效果
    @Override
    public void show() {
        super.show();
//        getWindow().setWindowAnimations(R.style.TimePickerDialogAnimation);
        // 添加震动效果
        startEnterAnimationWithVibration();
    }

    // 添加退出动画效果
    @Override
    public void dismiss() {
        super.dismiss();
//        getWindow().setWindowAnimations(R.style.TimePickerDialogAnimation);
    }

    // 通过ValueAnimator添加震动效果
    private void startEnterAnimationWithVibration() {

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
//            getWindow().getDecorView().setTranslationY(value);
            Log.d("TAG", "cuurent value is " + value);
            getWindow().getDecorView().setTranslationY((1 - value) * 500);
        });
        animator.start();
    }

}

class MusicPlayerlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static MusicPlaylistHolder musicPlaylistHolder;
    private JSONArray jsonArray;
    private OnItemClickListener mListener;

    MusicPlayerlistAdapter(JSONArray jsonArray,OnItemClickListener listener) {
        this.jsonArray = jsonArray;
        this.mListener = listener;
        notifyDataSetChanged();
    }

    public void setData(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            JSONObject item = (JSONObject) jsonArray.get(position);
            musicPlaylistHolder = (MusicPlaylistHolder) holder;

            String title = item.getString("title");
            String type = item.getString("type");
            //设置Item文字
            musicPlaylistHolder.textView.setText(title);
            if (title.equals(MusicPlayerService.title)){
                //设置Item文字
                MusicPlayerlistAdapter.musicPlaylistHolder.textView.setText("正在播放");
                MusicPlayerlistAdapter.musicPlaylistHolder.textView.setTextColor(ContextCompat.getColor(MyApplication.getContext(),R.color.colorAccent));
            } else{
                MusicPlayerlistAdapter.musicPlaylistHolder.textView.setText(title);
                MusicPlayerlistAdapter.musicPlaylistHolder.textView.setTextColor(ContextCompat.getColor(MyApplication.getContext(),R.color.colorTextTitle));
            }
            switch (type) {
                case "audio":
                    musicPlaylistHolder.imageView.setImageResource(R.drawable.ic_play_black);

                    double duration = Double.parseDouble(item.getString("duration"));
                    int totalSeconds = (int) Math.round(duration); // 将持续时间四舍五入为最接近的整数秒数

                    int hours = totalSeconds / 3600; // 计算小时数
                    int minutes = (totalSeconds % 3600) / 60; // 计算分钟数
                    int seconds = totalSeconds % 60; // 计算秒数
                    String formattedTime = String.format("%02d:%02d", minutes, seconds); // 格式化为“分钟:秒钟”的形式
                    if (hours > 0) {
                        formattedTime = String.format("%02d:%s", hours, formattedTime); // 如果有小时数，添加到时间表示中
                    }
                    musicPlaylistHolder.duration.setText(formattedTime);
                    break;
            }
            musicPlaylistHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.itemAsmrTracksClick(position);
                }
            });
            // 设置下划线的可见性
            if (position == getItemCount() - 1) {
                musicPlaylistHolder.divider.setVisibility(View.GONE); // 最后一项不显示下划线
            } else {
                musicPlaylistHolder.divider.setVisibility(View.VISIBLE); // 其他项显示下划线
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_asmrtracks_item, parent, false);
        return new MusicPlaylistHolder(view);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    //手办图鉴详细页item
    class MusicPlaylistHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, duration;
        View divider;

        MusicPlaylistHolder(View item) {
            super(item);
            imageView = item.findViewById(R.id.image_view);
            textView = item.findViewById(R.id.text_title);
            duration = item.findViewById(R.id.text_duration);

            divider = item.findViewById(R.id.divider);
        }
    }

    public interface OnItemClickListener {
        void itemAsmrTracksClick(int position);
    }
}
