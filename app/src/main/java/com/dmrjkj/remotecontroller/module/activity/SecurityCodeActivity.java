package com.dmrjkj.remotecontroller.module.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.exception.BleException;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.bluetooth.action.SPYAction;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;
import com.dmrjkj.remotecontroller.bluetooth.entity.Password;
import com.dmrjkj.remotecontroller.bluetooth.parse.ParseCallBack;
import com.dmrjkj.remotecontroller.bluetooth.parse.ParseEngine;
import com.dmrjkj.remotecontroller.bluetooth.parse.ParseResult;
import com.dmrjkj.remotecontroller.bluetooth.parse.SPYParseCore;
import com.dmrjkj.remotecontroller.events.ChangePwdEvent;
import com.dmrjkj.remotecontroller.events.SetPasswordEvent;
import com.dmrjkj.remotecontroller.module.base.BaseActivity;
import com.dmrjkj.remotecontroller.utils.ToastUtils;
import com.dmrjkj.remotecontroller.weight.PswView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by xinchen on 19-5-29.
 */

public class SecurityCodeActivity extends BaseActivity implements PswView.InputCallBack, ParseCallBack {
    @BindView(R.id.psw_view)
    PswView pswView;
    @BindView(R.id.ok_view)
    TextView okView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.show_hide_pwd)
    ImageView showHidePwd;
    @BindView(R.id.show_tip)
    TextView showTip;
    @BindView(R.id.show_error)
    TextView showError;

    public static final int SET_PWD = 0;
    public static final int CHANGE_PWD_1 = 1;
    public static final int INPUT_PWD = 2;
    public static final int CHANGE_PWD_2 = 3;

    private SPYAction action;
    private Device device;
    private ParseEngine parseEngine;
    private int model = SET_PWD;//0 设置密码 1 修改密码之前的验证密码  2 验证密码 3 修改密码

    public static void intentActivity(Activity activity, Device device) {
        Intent intent = new Intent(activity, SecurityCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("device", device);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void intentActivityForModel(Activity activity, Device device, int model) {
        Intent intent = new Intent(activity, SecurityCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("device", device);
        bundle.putInt("model", model);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void intentActivityForChangePwd(Activity activity, Device device, int model, String code) {
        Intent intent = new Intent(activity, SecurityCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("device", device);
        bundle.putInt("model", model);
        bundle.putString("code", code);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void initAndBindConfig() {
        super.initAndBindConfig();

        device = (Device) getIntent().getExtras().get("device");
        model = getIntent().getIntExtra("model", SET_PWD);
        action = new SPYAction(device);

        if (model == SET_PWD) {
            model = device.isNeedPwd() ? CHANGE_PWD_1 : SET_PWD;
        }

        this.parseEngine = new ParseEngine(new SPYParseCore(), SecurityCodeActivity.this);

        switch (model) {
            default:
                //设置密码
                okView.setClickable(false);
                toolbarTitle.setText("Set password");
                showTip.setText("please set password");
                break;
            case CHANGE_PWD_1:
                //修改密码
                okView.setClickable(false);
                toolbarTitle.setText("Change password");
                showTip.setText("please input old password");
                break;
            case CHANGE_PWD_2:
                okView.setClickable(false);
                toolbarTitle.setText("Change password");
                showTip.setText("please input new password");
                break;
            case INPUT_PWD:
                //验证密码
                okView.setClickable(false);
                toolbarTitle.setText("Verify password");
                showTip.setText("please input password");
                break;
        }

        showHidePwd.setImageResource(pswView.isShowPsw() ? R.mipmap.eye_close : R.mipmap.eye_open);

        pswView.setInputCallBack(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceNotify();
        startReportStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.action.actionReportEnd();
        this.device.removeNotify();
    }

    /**
     * 开始
     */
    private void startReportStatus() {
        this.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                SecurityCodeActivity.this.action.actionReportStart();
            }
        }, 300L);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_securitycode;
    }

    @OnClick({R.id.iv_back, R.id.top_view, R.id.left_view, R.id.del_view, R.id.down_view, R.id.right_view, R.id.ok_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.top_view:
                pswView.setPsw(0);
                break;
            case R.id.left_view:
                pswView.setPsw(1);
                break;
            case R.id.del_view:
                pswView.delPsw();
                okView.setClickable(false);
                okView.setTextColor(getResources().getColor(R.color.gray_15));
                break;
            case R.id.down_view:
                pswView.setPsw(2);
                break;
            case R.id.right_view:
                pswView.setPsw(3);
                break;
            case R.id.ok_view:
                String result = pswView.getCodeValues();
                switch (model) {
                    default:
                        this.action.actionSetPwd(result);
                        break;
                    case CHANGE_PWD_1:
                        SecurityCodeActivity.intentActivityForChangePwd(SecurityCodeActivity.this, device, CHANGE_PWD_2, result);
                        break;
                    case CHANGE_PWD_2:
                        String code = getIntent().getStringExtra("code");
                        this.action.actionChangePwd(code + result);
                        break;
                    case INPUT_PWD:
                        this.action.actionInputPwd(result);
                        break;
                }
                break;
        }
    }

    @OnTouch(R.id.show_hide_pwd)
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pswView.setClearTextPsw(true);
            showHidePwd.setImageResource(R.mipmap.eye_close);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            pswView.setClearTextPsw(false);
            showHidePwd.setImageResource(R.mipmap.eye_open);
        }
        return true;
    }

    @Override
    public void onInputFinish(String password) {
        okView.setClickable(true);
        okView.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 监听蓝牙
     */
    private void deviceNotify() {
        this.device._notify(new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
            }

            @Override
            public void onNotifyFailure(BleException exception) {
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                parseEngine.execParse(data);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePwdEvent(ChangePwdEvent event) {
        if (event.isOk()) {
            ToastUtils.toast("password change success");
            finish();
        } else {
            showError.setText(event.getErrorMsg());
        }
    }

    @Override
    public void onResult(ParseResult paramParseResult) {
        Object result = paramParseResult.getFrameObj();
        if (result instanceof Password) {
            Password password = (Password) result;
            if (password == Password.YES) {
                switch (model) {
                    case SET_PWD:
                        ToastUtils.toast("password setting success");
                        break;
                    case CHANGE_PWD_1:
                        ToastUtils.toast("password change success");
                        break;
                    case CHANGE_PWD_2:
                        EventBus.getDefault().post(new ChangePwdEvent(true, null));
                        break;
                    case INPUT_PWD:
                        EventBus.getDefault().post(new SetPasswordEvent(device));
                        break;
                }
                finish();
            } else {
                switch (model) {
                    case SET_PWD:
                        showError.setText("password setting fail");
                        break;
                    case CHANGE_PWD_1:
                        showError.setText("password change fail");
                        break;
                    case CHANGE_PWD_2:
                        showError.setText("password is error");
                        EventBus.getDefault().post(new ChangePwdEvent(false, "password is error"));
                        finish();
                        break;
                    case INPUT_PWD:
                        showError.setText("password is error");
                        break;
                }
            }
        }
    }
}
