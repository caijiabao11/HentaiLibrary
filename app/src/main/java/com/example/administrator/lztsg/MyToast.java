package com.example.administrator.lztsg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {
    private Context context;
    private Gravity gravity;
    private TextView textView;
    public Toast toast = null;
    private int animations = -1;
    private static final int ContentID = R.id.toast_text;
    private static final int LayoutID = R.layout.layout_toastmsg;

    public MyToast(Context context) {
        this.context = context;
    }

    public void makeText(CharSequence text,int duration,int gravity) {
        if (toast == null) {
            toast = new Toast(context);
            View view = LayoutInflater.from(context).inflate(LayoutID, null, false);
            textView = view.findViewById(ContentID);
            toast.setView(view);
            toast.setDuration(duration);
            toast.setGravity(gravity,0,0);
            textView.setText(text);
            textView.setTextColor(context.getResources().getColor(R.color.colorAllTextDark));

            View toastView = toast.getView();

            playtoast_anim_in(toastView);
        }

    }

    private void playtoast_anim_in(View v) {
        // 创建一个平移动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", 500, 0);
        animator.setDuration(1000);

        //监听动画完成结束
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playtoast_anim_out(v);
                    }
                },500);
            }
        });
        animator.start();

    }

    private void playtoast_anim_out(View v) {
        // 创建一个平移动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", 0, 500);
        animator.setDuration(1000);
        animator.start();
    }

    public void setGravity(int gravity) {
        toast.setGravity(gravity, 0, 0);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void show() {
        toast.show();
    }

    public void setShowTime(int time) {
        toast.setDuration(time);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextSize(float size) {
        textView.setTextSize(size);
    }

    public int getAnimations() {
        return animations;
    }

    public void setAnimations(int animations) {
        this.animations = animations;
    }
}
