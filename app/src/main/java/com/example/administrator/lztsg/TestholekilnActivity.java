package com.example.administrator.lztsg;


import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


public class TestholekilnActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2,mTopbgViewPeger;
    private String[] mTitles={"地狱寸止","颅内高潮","节奏大师"};
    private String s=" {\n" +
            "    \"@context\": \"https://schema.org\",\n" +
            "    \"@type\": \"VideoObject\",\n" +
            "    \"name\": \"HMV7\",\n" +
            "    \"description\": \"Watch HMV7 on SpankBang now! - Hmv, Hentai, Big Tits Porn - SpankBang \",\n" +
            "    \"thumbnailUrl\": \"https://tb.sb-cd.com/t/10060872/1/0/w:1280/t7-enh/hmv7.jpg\",\n" +
            "    \"uploadDate\": \"2021-09-21T20:06:05\",\n" +
            "    \"duration\": \"PT00H05M43S\",\n" +
            "    \"contentUrl\": \"https://vdownload-19.sb-cd.com/1/0/10060872-720p.mp4?secure=m5X5QiO3YgsqLgQF_B8YuQ,1647021825&m=19&d=4&_tid=10060872\",\n" +
            "    \"embedUrl\": \"https://spankbang.com/5zn0o/embed/\",\n" +
            "\n" +
            "    \"interactionStatistic\": [\n" +
            "    {\n" +
            "          \"@type\": \"InteractionCounter\",\n" +
            "          \"interactionType\": \"http://schema.org/WatchAction\",\n" +
            "          \"userInteractionCount\": \"6439\"\n" +
            "    },\n" +
            "    {\n" +
            "          \"@type\": \"InteractionCounter\",\n" +
            "          \"interactionType\": \"http://schema.org/LikeAction\",\n" +
            "          \"userInteractionCount\": \"112\"\n" +
            "     }\n" +
            "    ],\n" +
            "\n" +
            "    \"keywords\": \"Hmv, Hentai, Big Tits\"\n" +
            "  }";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testholekiln);
        bindViews();
        LinearRecyclerView();
        initData();
//        try {
//            JSONObject js = new JSONObject(s);
//            js.get("contentUrl");
////            System.out.println();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    private void bindViews() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager2 = findViewById(R.id.v_pager);
        mTopbgViewPeger = findViewById(R.id.vtop_pager);
    }

    //载入数据
    private void initData(){
        //标题
        for (int i=0;i<mTitles.length;i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]),false);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //设置tab选中状态下的文本状态
                TextView textView = new TextView(TestholekilnActivity.this);
                float selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 16, getResources().getDisplayMetrics());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);
                textView.setTextColor(getResources().getColor(R.color.colorSearch));
                textView.setText(tab.getText());
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tab.setCustomView(textView);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //设置tab非选中状态下的文本状态
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //使ViewPager2与Tablayout联动
        new TabLayoutMediator(mTabLayout,mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitles[position]);
            }
        }).attach();
        new TabLayoutMediator(mTabLayout,mTopbgViewPeger, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitles[position]);
            }
        }).attach();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    TestholekilnActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTabLayout.getTabAt(0).select();//默认选中第一个
                            mViewPager2.setCurrentItem(0);
                            mTopbgViewPeger.setCurrentItem(0);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void LinearRecyclerView(){
        //禁用预加载
        mViewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mViewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return PagerFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return mTitles.length;
            }
        });
        mTopbgViewPeger.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mTopbgViewPeger.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @Override
            public int getItemCount() {
                return mTitles.length;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TopbgPagerFragment.newInstance(position);
            }
        });
    }
}
