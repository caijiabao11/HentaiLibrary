package com.example.administrator.lztsg;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyTimePickerDialog extends Dialog {
    public MyTimePickerDialog(@NonNull Context context) {
        super(context);
    }

    protected MyTimePickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytimepicker_dialog);
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
