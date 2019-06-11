package com.dmrjkj.remotecontroller.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dmrjkj.remotecontroller.AppApplication;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        AppApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);

        setContentView(getContentViewResId());

        ButterKnife.bind(this);

        initAndBindConfig();

        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        AppApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    protected void initAndBindConfig() {
    }

    protected void initData(Bundle savedInstanceState) {
    }

    protected abstract int getContentViewResId();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
