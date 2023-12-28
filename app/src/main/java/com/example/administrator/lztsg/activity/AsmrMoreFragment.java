package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.httpjson.AsmrHttpJson;
import com.example.administrator.lztsg.httpjson.HttpAsmrJsonResolution;
import com.example.administrator.lztsg.items.MultipleItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class AsmrMoreFragment extends Fragment{
    private static Context context;
    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;
    public static boolean mQieHuan = true;
    public static List<MultipleItem> mData, mAllData;
    private String path = "https://api.asmr.one/api/works?order=create_date&sort=desc&page=1&subtitle=0";
    private static int total = 1;

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(context);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_asmr_more, container, false);
        initView(rootview);
        onViewpage(path,context);
        return rootview;
    }

    private void initView(View view) {
        mViewPager2 = view.findViewById(R.id.v_pager);
        mTabLayout = view.findViewById(R.id.tab_layout);
    }

    //载入图片+标题
    private void initData(Context context) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //标题
                for (int i=1;i<=total;i++){
                    mTabLayout.addTab(mTabLayout.newTab().setText("" + i),false);
                }
                mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
                    // 在Activity中定义一个标志变量
                    private boolean isTabSelectedCallbackExecuted = false;
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        // 判断是否已经执行过一次回调
                        if (!isTabSelectedCallbackExecuted) {
                            //设置tab选中状态下的文本状态
                            TextView textView = new TextView(context);
                            float selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 16, getResources().getDisplayMetrics());
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);
                            textView.setTextColor(getResources().getColor(R.color.colorTextTitle));
                            textView.setText(tab.getText());
                            textView.setGravity(Gravity.CENTER);
                            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                            tab.setCustomView(textView);
                            // 将标志变量设置为true，表示已经执行过一次回调
                            isTabSelectedCallbackExecuted = true;
                        }
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
                        tab.setText("" + (position + 1));
                    }
                }).attach();

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mTabLayout.getTabCount() >0){
                                mTabLayout.getTabAt(0).select();//默认选中第一个
                            }
                            mViewPager2.setCurrentItem(0);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void onViewpage(String path, Context context) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                AsmrHttpJson.getPage(path,new HttpAsmrJsonResolution() {

                    @Override
                    public void onFinish(String id, String title, String name, String release, JSONArray vas, JSONArray tags, String CoverUrl) {

                    }

                    @Override
                    public void onTracksFinish(JSONArray trackslist) {

                    }

                    @Override
                    public void onPages(int total) {
                        if (total == 0){
                            total++;
                        }
                        AsmrMoreFragment.total = total;
                        initData(context);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }).start();
        //是否保存自身状态
        mViewPager2.setSaveEnabled(false);
        mViewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mViewPager2.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return AsmrFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return total;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AsmrMove", "onDestroy: "+"is rover");
    }
}
