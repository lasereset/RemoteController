package com.dmrjkj.remotecontroller.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by xinchen on 19-4-10.
 */

public class AnamationUtils {

    /**
     * View渐隐动画效果
     */
    public static void setHideAnimation(View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        // 监听动画结束的操作
        AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        view.startAnimation(mHideAnimation);
    }

    /**
     * View渐现动画效果
     */
    public static void setShowAnimation(View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        view.startAnimation(mShowAnimation);
    }
}
