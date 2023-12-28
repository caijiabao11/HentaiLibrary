package com.example.administrator.lztsg.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.administrator.lztsg.Dao;
import com.example.administrator.lztsg.DatabaseHelper;
import com.example.administrator.lztsg.MyApplication;
import com.example.administrator.lztsg.MyToast;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MultipleItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LifanDetailpageActivity extends AppCompatActivity {
    private ImageView mImageView;
    private TextView mItemTitView, mItemIntroductionView;
    private TextView mItemIdView, mItemMass;
    private LottieAnimationView mLottie;
    private CardView mBtn_like;
    private ChipGroup mItem_tag;
    private SharedPreferences sp;
    private Dao dao = LifanMoreFragment.dao;
    private static int preferences;
    private static int position, position1;
    public static List<MultipleItem> mData = LifanMoreFragment.mData;
    public static List<MultipleItem> mData1 = LifanLiveFragment.mData;
    private static boolean isChecked;
    private Item item, item1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifan_detailpage);
        init();
        initData();
    }

    private void init() {
        mImageView = findViewById(R.id.item_img);
        mItemTitView = findViewById(R.id.item_tit);
        mItemIntroductionView = findViewById(R.id.item_introduction);
        mItemIdView = findViewById(R.id.item_id);
        mLottie = findViewById(R.id.like_lottie);
        mBtn_like = findViewById(R.id.btn_like);
        mItem_tag = findViewById(R.id.item_tag);
        mItemMass = findViewById(R.id.item_mass);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Glide.with(MyApplication.getContext())
                .load(bundle.getString("itemImgurl"))
                .override(500, 300)
                .placeholder(R.drawable.none)
                .fitCenter()
                .into(mImageView);
        mItemTitView.setText(bundle.getString("itemTitle"));
        mItemIntroductionView.setText(bundle.getString("itemIntroduction"));
        mItemIdView.setText(bundle.getString("itemId"));
        mItemMass.setText(bundle.getString("itemMasstit"));
        preferences = bundle.getInt("itemPreferences");
        position = bundle.getInt("itemPosition");
        position1 = bundle.getInt("item1Position");
        try {
            JSONArray tagarr = new JSONArray(bundle.getString("itemTag"));
            addTag(mItem_tag, tagarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //进场悬浮按钮动画
        mLottie.setVisibility(View.VISIBLE);
        Animation livefabon = AnimationUtils.loadAnimation(this, R.anim.scale);
        mLottie.setAnimation(livefabon);
        if (preferences != 0) {
            mLottie.setMinAndMaxProgress(0f, 0.5f);
        } else {
            mLottie.setMinAndMaxProgress(0.5f, 1f);
        }
        mLottie.playAnimation();
        mBtn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLottie();

            }
        });
        //标题长按事件（长按复制内容）
        mItemTitView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cmb = (ClipboardManager) MyApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(mItemTitView.getText());

                MyToast.makeText(LifanDetailpageActivity.this,"复制成功啦，快去粘贴吧:)",
                        1000, Gravity.FILL_HORIZONTAL | Gravity.BOTTOM).show();
                return false;
            }
        });
        //
        mItemIntroductionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemIntroductionView.getMaxLines() == 3) {
                    mItemIntroductionView.setMaxLines(30);
                } else {
                    mItemIntroductionView.setMaxLines(3);
                }
            }
        });
    }

    private void addTag(ChipGroup chipGroup, JSONArray tagarr) {
        // 创建一个LayoutParams对象
        chipGroup.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // 宽度为自适应
                LinearLayout.LayoutParams.WRAP_CONTENT ));// 高度为自适应
        try {
            for (int i = 0; i <= tagarr.length(); i++) {
                String tag = getTagname(tagarr.getString(i));
                chipGroup.addView(createChiptext(tag));
                // 设置Chip之间的间距
                chipGroup.setChipSpacingHorizontal(10); // 设置水平间距
                chipGroup.setChipSpacingVertical(-10); // 设置垂直间距
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Chip createChiptext(String str) {
        Chip chip = new Chip(this);
        chip.setText(str);
//        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#AF4FB0")));
//        chip.setTextColor(getResources().getColor(R.color.colorAllTextDark));
        if (LifanTag_putFragment.tagarrid != null){
            for (String tagid : LifanTag_putFragment.tagarrid){
                if (str.equals(getTagname(tagid))){
                    chip.setChecked(true);
                }
            }
        }
        chip.setCloseIconVisible(false);
        chip.setClickable(false);

        return chip;
    }

    private String getTagname(String id) {
        for (MultipleItem item :  dao.detaTaglist) {
            final Item value = (Item) item;
            //.startsWith   以指定字符串开头筛选（精准搜索）
            //.contains     以字符串中是否存在筛选（模糊搜索）
            if (value.getId().startsWith(id)) {
                return value.getTitle();
            }
        }
        return null;
    }

    private void startLottie() {
        DatabaseHelper helper = new DatabaseHelper(this);
        helper.getWritableDatabase();

        dao = new Dao(this);
        String name = (String) mItemTitView.getText();

        if (mData != null) {
            item = (Item) mData.get(position);
        }
        if (mData1 != null) {
            item1 = (Item) mData1.get(position1);
        }
        //Lottie动画
        if (preferences != 0) {
            preferences = 0;
            dao.update(0, name);
            if (item != null) {
                item.setPreferences(0);
            }
            if (item1 != null) {
                item1.setPreferences(0);
            }
            mLottie.setMinAndMaxProgress(0.5f, 1f);
            mLottie.playAnimation();
        } else {
            preferences = 1;
            dao.update(1, name);
            if (item != null) {
                item.setPreferences(1);
            }
            if (item1 != null) {
                item1.setPreferences(1);
            }
            mLottie.setMinAndMaxProgress(0f, 0.5f);
            mLottie.playAnimation();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键监听
        mLottie.setVisibility(View.INVISIBLE);
//        Animation livefabon = AnimationUtils.loadAnimation(this,R.anim.scale_off);
//        mFloatingActionButton.setAnimation(livefabon);
        return super.onKeyDown(keyCode, event);
    }
}
