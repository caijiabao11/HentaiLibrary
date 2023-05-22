package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.lztsg.R;

public class NoiseFragment extends BaseFragment {
    private static final String KEY_POSITION = "position";
    public ImageView mImageView;

    public static NoiseFragment newInstance(int position) {
        NoiseFragment fragment = new NoiseFragment();
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
        return R.layout.fragment_noise;
    }

    @Override
    protected void initView(View view) {
        mImageView = view.findViewById(R.id.img_noise);
    }

    @Override
    protected void initData(Context context) {
        int position = getArguments().getInt(KEY_POSITION);
        if (position == 0) {

        } else if (position == 1) {

        } else if (position == 2) {

        }
    }

    @Override
    public void onDestroyFragment() {

    }

}
