package com.example.administrator.lztsg;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class LoadingView extends AppCompatImageView {

    public LoadingView(Context context){
        super(context);
    }
    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(View view) {
        Glide.with(getContext())
                .load(R.drawable.jinyi01_agadkquaajguavc)
                .into((LoadingView) view);
    }
}
