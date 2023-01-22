package com.example.administrator.lztsg.anima;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ViewSizeChangeAnimation extends Animation {
    int initialHeight;
    int initialWidth;
    int targetHeight;
    int targetWidth;
    View view;

    public ViewSizeChangeAnimation(View view,int targetHeight){
        this.view = view;
        this.targetHeight = targetHeight;
    }

    public ViewSizeChangeAnimation(View view,int targetHeight,int targetWidth){
        this.view = view;
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().height = (int) (initialHeight + ((targetHeight - initialHeight) * interpolatedTime));
//        view.getLayoutParams().width = (int) (initialWidth + ((targetWidth - initialWidth) * interpolatedTime));
        view.getLayoutParams().width = (int) (initialWidth + (ViewGroup.LayoutParams.MATCH_PARENT * interpolatedTime));
//        view.getLayoutParams().height = (int) (initialHeight + (ViewGroup.LayoutParams.MATCH_PARENT * interpolatedTime));
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        this.initialHeight = height;
        this.initialWidth = width;
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
