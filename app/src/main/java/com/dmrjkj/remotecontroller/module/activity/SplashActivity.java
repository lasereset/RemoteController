package com.dmrjkj.remotecontroller.module.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.clj.fastble.BleManager;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.module.base.BaseActivity;

public class SplashActivity extends BaseActivity{

    @Override
    protected int getContentViewResId() {
        return R.layout.loading;
    }

    @Override
    protected void initAndBindConfig() {
        super.initAndBindConfig();

        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
