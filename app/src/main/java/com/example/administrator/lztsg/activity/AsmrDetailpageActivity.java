package com.example.administrator.lztsg.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.CustomJZMp3;
import com.example.administrator.lztsg.MusicPlayerService;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.httpjson.AsmrHttpJson;
import com.example.administrator.lztsg.httpjson.HttpAsmrJsonResolution;
import com.example.administrator.lztsg.items.AsmrItemTags;
import com.example.administrator.lztsg.items.AsmrItemVas;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AsmrDetailpageActivity extends AppCompatActivity {
    private ImageView mImage;
    private TextView mId, mRelease, mTitle, mName;
    public static CustomJZMp3 mCustomJZMp3;
    private RecyclerView mRecyclerView;
    private AsmrDetailpageTracksAdapter mAsmrDetailpageTracksAdapter;
    private GridLayoutManager mGridLayoutManager;
    private ChipGroup mTags;
    public static MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
    public static JSONArray tracksList,allurl;
    public static Stack<JSONArray> itemStack = new Stack<>();
    public static int currentVideoIndex = 0;
    private SharedPreferences sp;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asmr_detailpage);
        init();
        initData();
        addFragment(musicPlayerFragment);
    }

    private void init() {
        mImage = findViewById(R.id.item_img);
        mId = findViewById(R.id.text_id);
        mRelease = findViewById(R.id.text_release);
        mTitle = findViewById(R.id.item_tit);
        mTags = findViewById(R.id.item_tag);
        mName = findViewById(R.id.item_mass);
        mRecyclerView = findViewById(R.id.run_main);
        mCustomJZMp3 = findViewById(R.id.noise_mp3);

    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //图片
        Glide.with(MyApplication.getContext())
                .load(bundle.getString("itemCoverUrl"))
                .placeholder(R.drawable.none)
                .fitCenter()
                .into(mImage);
        icon = bundle.getString("itemCoverUrl");
        mId.setText("RJ0" + bundle.getString("itemId"));
        mRelease.setText(bundle.getString("itemRelease"));
        mTitle.setText(bundle.getString("itemTitle"));

        List<AsmrItemVas> vaslist = new ArrayList<>(bundle.getParcelableArrayList("itemVaslist"));
        List<AsmrItemTags> tagslist = new ArrayList<>(bundle.getParcelableArrayList("itemTagslist"));
        for (AsmrItemVas value : vaslist) {
            String vas_id = value.getId();
            String vas_name = value.getName();
            tagslist.add(new AsmrItemTags(vas_id, vas_name));
        }
        //标签
        addTag(mTags, tagslist, vaslist.size());

        mName.setText(bundle.getString("itemName"));
        String url = "https://api.asmr.one/api/tracks/0" + bundle.getString("itemId");
        AsmrHttpJson.indata(url, new HttpAsmrJsonResolution() {

            @Override
            public void onFinish(String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl) {

            }

            @Override
            public void onTracksFinish(JSONArray trackslist) {
                LinearRecyclerView(trackslist);
                AsmrDetailpageActivity.tracksList = trackslist;
            }

            @Override
            public void onPages(int total) {

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void LinearRecyclerView(JSONArray json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //初始化线性布局管理器
                mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                //设置布局管理器
                mRecyclerView.setLayoutManager(mGridLayoutManager);
                mAsmrDetailpageTracksAdapter = new AsmrDetailpageTracksAdapter(json, new AsmrDetailpageTracksAdapter.OnItemClickListener() {
                    @Override
                    public void itemAsmrTracksClick(int position) {
                        //点击item判断是否还有下一级
                        try {
                            JSONObject item = (JSONObject) json.get(position);

                            String type = item.getString("type");
                            String mediaStreamUrl;
                            Intent intent;
                            switch (type) {
                                case "folder":
                                    JSONArray jsonArray = item.getJSONArray("children");
                                    itemStack.push(jsonArray);
                                    LinearRecyclerView(jsonArray);
                                    break;
                                case "image":
                                    mediaStreamUrl = item.getString("mediaStreamUrl");
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mediaStreamUrl));
                                    startActivity(intent);
                                    break;
                                case "audio":
                                    //获取本级层所有媒体文件组
                                    allurl = new JSONArray(json.toString());
                                    for (int i=0;i<allurl.length();i++){
                                        JSONObject item1 = (JSONObject) allurl.get(i);
                                        String types = item1.getString("type");
                                        String title = item1.getString("title");
                                        if (!types.equals("audio")){
                                            allurl.remove(i);
                                            i--;
                                        }
                                        if (title.equals(item.getString("title"))){
                                            currentVideoIndex = i;
                                        }
                                    }
                                    mediaStreamUrl = item.getString("mediaStreamUrl");
                                    String title = item.getString("title");
                                    intent = new Intent(MyApplication.getContext(), MusicPlayerService.class);
                                    intent.putExtra("mediaStreamUrl", mediaStreamUrl);
                                    intent.putExtra("icon",icon);
                                    intent.putExtra("title", title);
                                    startService(intent);
                                    break;
                                case "text":
                                    mediaStreamUrl = item.getString("mediaStreamUrl");
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mediaStreamUrl));
                                    startActivity(intent);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mRecyclerView.setAdapter(mAsmrDetailpageTracksAdapter);
            }
        });
    }

    private void addFragment(Fragment fragment){
        // 获取FragmentManager
        fragmentManager = getSupportFragmentManager();

        // 开启一个Fragment事务
        fragmentTransaction = fragmentManager.beginTransaction();

        // 替换当前的Fragment为新的Fragment
        fragmentTransaction.replace(R.id.fragment, fragment);

//        // 添加该Fragment事务进返回栈中
//        fragmentTransaction.addToBackStack(null);

        // 提交事务
        fragmentTransaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除注册BroadcastReceiver
//        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (!itemStack.isEmpty()) {
            // 弹出栈顶的item，即返回到上一级item
            itemStack.pop();

            if (!itemStack.isEmpty()) {
                // 获取上一级item的子项列表
                JSONArray children = itemStack.peek();
                // 更新适配器的数据集
                LinearRecyclerView(children);
            } else {
                // 如果栈为空，表示返回到了最顶层，更新适配器的数据集为最顶层的数据
                JSONArray children = tracksList;
                LinearRecyclerView(children);
            }

        }else if (itemStack.isEmpty() && fragmentTransaction != null){
            fragmentTransaction.remove(musicPlayerFragment);
            super.onBackPressed();
        }else {
            super.onBackPressed();
        }
    }

    private void addTag(ChipGroup chipGroup, List<AsmrItemTags> tagslist, int vassize) {
        chipGroup.removeAllViews(); // 清除之前的标签
        int position = 0;
        for (int i = 0; i < tagslist.size(); i++) {
            AsmrItemTags value = tagslist.get(i);
            String tag = value.getName();

            if (i >= tagslist.size() - vassize) {
                chipGroup.addView(createChiptext(tag, tagslist, position, vassize));
            } else {
                chipGroup.addView(createChiptext(tag));
            }
            position++;
        }
    }

    private Chip createChiptext(String str, List<AsmrItemTags> tagslist, int position, int vassize) {
        Chip chip = new Chip(this);
        if (position >= tagslist.size() - vassize && tagslist.get(position).getName().equals(str)) {
            chip.setText(str);
            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#747CAD")));
            chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        } else {
            chip.setText(str);
        }
        chip.setCloseIconVisible(false);
        chip.setClickable(false);
        return chip;
    }

    private Chip createChiptext(String str) {
        Chip chip = new Chip(this);
        chip.setText(str);
        chip.setCloseIconVisible(false);
        chip.setClickable(false);
        return chip;
    }

}

class AsmrDetailpageTracksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private JSONArray jsonArray;
    private OnItemClickListener mListener;

    AsmrDetailpageTracksAdapter(JSONArray jsonArray, OnItemClickListener listener) {
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
            AsmrTracksHolder asmrTracksHolder = (AsmrTracksHolder) holder;

            String title = item.getString("title");
            String type = item.getString("type");
            //设置Item文字
            asmrTracksHolder.textView.setText(title);
            switch (type) {
                case "folder":
                    asmrTracksHolder.imageView.setImageResource(R.drawable.ic_folder_black);
                    JSONArray j = item.getJSONArray("children");
                    asmrTracksHolder.duration.setText(j.length() + " 项目");
                    break;
                case "image":
                    asmrTracksHolder.imageView.setImageResource(R.drawable.ic_image_black);
                    break;
                case "audio":
                    asmrTracksHolder.imageView.setImageResource(R.drawable.ic_play_black);

                    double duration = Double.parseDouble(item.getString("duration"));
                    int totalSeconds = (int) Math.round(duration); // 将持续时间四舍五入为最接近的整数秒数

                    int hours = totalSeconds / 3600; // 计算小时数
                    int minutes = (totalSeconds % 3600) / 60; // 计算分钟数
                    int seconds = totalSeconds % 60; // 计算秒数
                    String formattedTime = String.format("%02d:%02d", minutes, seconds); // 格式化为“分钟:秒钟”的形式
                    if (hours > 0) {
                        formattedTime = String.format("%02d:%s", hours, formattedTime); // 如果有小时数，添加到时间表示中
                    }
                    asmrTracksHolder.duration.setText(formattedTime);
                    break;
                case "text":
                    asmrTracksHolder.imageView.setImageResource(R.drawable.ic_document_black);
                    break;
            }
            asmrTracksHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.itemAsmrTracksClick(position);
                }
            });
            // 设置下划线的可见性
            if (position == getItemCount() - 1) {
                asmrTracksHolder.divider.setVisibility(View.GONE); // 最后一项不显示下划线
            } else {
                asmrTracksHolder.divider.setVisibility(View.VISIBLE); // 其他项显示下划线
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_asmrtracks_item, parent, false);
        return new AsmrTracksHolder(view);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    //手办图鉴详细页item
    class AsmrTracksHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, duration;
        View divider;

        AsmrTracksHolder(View item) {
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
