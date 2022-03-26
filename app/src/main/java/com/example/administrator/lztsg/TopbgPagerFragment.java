package com.example.administrator.lztsg;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TopbgPagerFragment extends BaseFragment{
    private static final String KEY_POSITION = "position";
    private ImageView mImageView;

    public static TopbgPagerFragment newInstance(int position) {
        TopbgPagerFragment fragment = new TopbgPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int setContentLayout() {
        return 0;
    }

    @Override
    protected int initLyout() {
        return R.layout.fragment_topbgpager;
    }

    @Override
    protected void initView(View view) {
        mImageView = view.findViewById(R.id.top_iv);
    }

    @Override
    protected void initData(Context context) {
        int position = getArguments().getInt(KEY_POSITION);
        if (position == 0){
            mImageView.setImageResource(R.drawable.testholekiln_item_dycz_img);
        } else if (position == 1){
            mImageView.setImageResource(R.drawable.testholekiln_item_lngc_img);
        } else if (position ==2){
            mImageView.setImageResource(R.drawable.testholekiln_item_jzds_img);
        }
    }

    @Override
    public void onDestroyFragment() {

    }
}
