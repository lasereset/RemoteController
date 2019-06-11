package com.dmrjkj.remotecontroller.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.AppApplication;
import com.dmrjkj.remotecontroller.R;


/**
 * Toast工具
 */

public class ToastUtils {

    private ToastUtils() {
        //no instance
    }

    public static void playAuditory(int resId) {
        AppApplication.getInstance().getmFeedbackController().playAuditory(resId);
    }


    public static void setEmptyView(BaseQuickAdapter baseQuickAdapter, View.OnClickListener onClickListener) {
        setEmptyView(baseQuickAdapter, "Load failed! No content was retrieved!", onClickListener);
    }

    public static void setEmptyView(BaseQuickAdapter baseQuickAdapter, String errorMsg, View.OnClickListener onClickListener) {
        baseQuickAdapter.loadMoreFail();
        baseQuickAdapter.setEmptyView(getEmptyView("Refresh", errorMsg, onClickListener));
    }

    public static View getEmptyView() {
        return getEmptyView("Refresh", "empty content!", null);
    }

    public static View getEmptyView(boolean needRefresh, View.OnClickListener onClickListener) {
        return getEmptyView(needRefresh ? "Refresh" : null, "empty content!", onClickListener);
    }

    public static View getEmptyView(String prompts, String wraning, View.OnClickListener onClickListener) {
        final Context context = AppApplication.getInstance().getApplicationContext();
        View view = LayoutInflater.from(context).inflate(R.layout.error_page, null);
        TextView textViewWran = (TextView) view.findViewById(R.id.common_text_warnning);
        TextView textViewPrompts = (TextView) view.findViewById(R.id.common_text_tologin);
        if (prompts == null) {
            textViewPrompts.setVisibility(View.GONE);
        } else {
            textViewPrompts.setText(prompts);
        }
        if (wraning == null) {
            textViewWran.setVisibility(View.GONE);
        } else {
            textViewWran.setText(wraning);
        }
        textViewPrompts.setOnClickListener(onClickListener);
        return view;
    }

    public static void toast(String toast) {
        Toast.makeText(AppApplication.getInstance(), toast, Toast.LENGTH_SHORT).show();
    }
}
