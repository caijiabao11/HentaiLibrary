package com.example.administrator.lztsg.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.administrator.lztsg.Constants;
import com.example.administrator.lztsg.Dao;
import com.example.administrator.lztsg.DatabaseHelper;
import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.items.Item;
import com.example.administrator.lztsg.items.MultipleItem;
import com.example.administrator.lztsg.utils.DensityUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class LifanTagFragment extends Fragment {
    public static LinearAdapter mLinearAdaoter;
    private static LinearAdapter.ItemHolder mItemHolder;
    private static StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private static ChipGroup mChip_Tags_guanxi, mChip_Tags_sheding, mChip_Tags_shengcai, mChip_Tags_juqing, mChip_Tags_tiwei;
    public static Dao dao;
    private static Context context;
    public static boolean mQieHuan = LifanMoreFragment.mQieHuan;
    public static List<MultipleItem> mData;
    public static List<MultipleItem> mAllData;
    public static List<String> checkd_tagarr;
    public static int[] firstStaggeredGridPosition = {0, 0};
    public static int[] lastStaggeredGridPosition = {0, 0};
    private ImageButton imageButton;
    private LinearLayout mTab_main;
    public static EditText mSearch;
    private static FloatingActionButton mbut_QueryTags;
    public static int butcode = 0;

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(context);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_lifan_tag, container, false);
        rootview.getContext().setTheme(R.style.LifanDetailpageTheme);
        initView(rootview);
        initData(context);

        return rootview;
    }

    private void initView(View view) {
        mChip_Tags_guanxi = view.findViewById(R.id.chip_group_guanxi);
        mChip_Tags_sheding = view.findViewById(R.id.chip_group_sheding);
        mChip_Tags_shengcai = view.findViewById(R.id.chip_group_shengcai);
        mChip_Tags_juqing = view.findViewById(R.id.chip_group_juqing);
        mChip_Tags_tiwei = view.findViewById(R.id.chip_group_tiwei);

        imageButton = getActivity().findViewById(R.id.imgbutton);
        mTab_main = getActivity().findViewById(R.id.tab_main);
        mSearch = getActivity().findViewById(R.id.edt_search);
        mbut_QueryTags = getActivity().findViewById(R.id.but_querytags);
    }

    private void initData(Context context) {
        this.mData = new ArrayList<>();
        this.mAllData = new ArrayList<>();
        this.checkd_tagarr = new ArrayList<>();
        DatabaseHelper helper = new DatabaseHelper(context);
        helper.getWritableDatabase();

        dao = new Dao(context);
        dao.query(Constants.TABLE_NAME_TAG, "tag_id");
        int indeax = dao.detaTaglist.size();
        int i = 0;
        while (i < indeax) {
            Item a = (Item) dao.detaTaglist.get(i);
            String id = a.getId();
            String title = a.getTitle();

            mData.add(new Item(id, title));
            i++;
        }
        mAllData.addAll(mData);
        addTag();

        mbut_QueryTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdded()){
                    //传递社团标题、
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    LifanTag_putFragment mFrag6 = new LifanTag_putFragment();
                    Bundle bundle = new Bundle();
                    ArrayList<String> checkd_puttags_id = new ArrayList();
                    for (MultipleItem item : dao.detaTaglist){
                        final Item value = (Item) item;
                        for (int i=0;i<checkd_tagarr.size();i++){
                            if (value.getTitle().equals(checkd_tagarr.get(i))) {
                                checkd_puttags_id.add(value.getId());
                            }
                        }
                    }

                    bundle.putStringArrayList("itemTagarrid",checkd_puttags_id);
                    mFrag6.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragmentLayout, mFrag6);
                    fragmentTransaction.addToBackStack(null);
                    LifanActivity.currentFragment = mFrag6;
                    fragmentTransaction.commit();
                }
            }
        });

    }

    private void addTag() {
        for (MultipleItem item : dao.detaTaglist) {
            final Item value = (Item) item;
            String tag = value.getTitle();
            Chip chip = createChiptext(tag);
            if (Integer.parseInt(value.getId()) <= 8) {

                mChip_Tags_guanxi.addView(chip);
            } else if (Integer.parseInt(value.getId()) > 8 && Integer.parseInt(value.getId()) <= 37) {

                mChip_Tags_sheding.addView(chip);
            } else if (Integer.parseInt(value.getId()) > 37 && Integer.parseInt(value.getId()) <= 66) {

                mChip_Tags_shengcai.addView(chip);
            } else if (Integer.parseInt(value.getId()) > 66 && Integer.parseInt(value.getId()) <= 99) {

                mChip_Tags_juqing.addView(chip);
            } else if (Integer.parseInt(value.getId()) > 99 && Integer.parseInt(value.getId()) <= 131) {

                mChip_Tags_tiwei.addView(chip);
            }
        }
    }

    private Chip createChiptext(String str) {

        Chip chip = new Chip(getContext());
        chip.setText(str);
        chip.setCloseIconVisible(false);
        chip.setClickable(true);
        chip.setOnCheckedChangeListener(new Chip.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String value = (String) buttonView.getText();
                    checkd_tagarr.add(value);
                } else {
                    // 取消选中事件发生，执行相应的操作
                    String value = (String) buttonView.getText();
                    checkd_tagarr.remove(value);
                    if (checkd_tagarr.size() == 0){
                        mSearch.getText().clear();
                        checkd_tagarr.clear();
                        butcode = 0;
                        OnAnimButonTag(-DensityUtils.dip2px(getContext(),150),0);
                    }
                }
//                for (String tag: checkd_tagarr){
//                    String a = tag + "、";
//
//                }
                mSearch.setText(checkd_tagarr.toString());
                if (checkd_tagarr.size() > 0 && butcode == 0){
                    butcode = 1;
                    OnAnimButonTag(0,-DensityUtils.dip2px(getContext(),150));
                }
            }
        });

        return chip;
    }

    public static void OnAnimButonTag(int values,int tovalues){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mbut_QueryTags, "translationY", values, tovalues);
        animator.setDuration(500); // 设置动画持续时间为 500 毫秒
        animator.setInterpolator(new AccelerateInterpolator()); // 设置插值器，使动画效果更加平滑
        animator.start();
    }
}
