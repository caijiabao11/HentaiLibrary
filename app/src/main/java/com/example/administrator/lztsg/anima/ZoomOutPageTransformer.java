package com.example.administrator.lztsg.anima;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class ZoomOutPageTransformer  implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        if (position <- 1){
            view.setAlpha(0f);

        }else if (position <= 1){
            //当页面刚刚离开屏幕右侧时，其位置值为1
            //修改默认的幻灯片转换以缩小页面
            float scaleFactor = Math.max(MIN_SCALE,1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0){
                //当页面填满整个屏幕时，其位置值为0
//                view.setTranslationX(horzMargin - vertMargin / 2);
            }else{
//                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            //把页面缩小
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
            //相对于页面大小而淡出
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        }else{
            view.setAlpha(0f);
        }
    }
}
