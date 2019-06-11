package com.dmrjkj.remotecontroller.module.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.clj.fastble.data.BleDevice;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;
import com.dmrjkj.remotecontroller.dialog.DialogUtils;
import com.dmrjkj.remotecontroller.entity.BaseListItem;
import com.dmrjkj.remotecontroller.enums.OperationAction;
import com.dmrjkj.remotecontroller.events.DeviceEvenet;
import com.dmrjkj.remotecontroller.events.DeviceSettingEvent;
import com.dmrjkj.remotecontroller.events.SetPasswordEvent;
import com.dmrjkj.remotecontroller.module.base.BaseActivity;
import com.dmrjkj.remotecontroller.module.base.BleScanAndConnectUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xinchen on 19-4-16.
 */

public class DeviceInfoActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_iv_search)
    ImageView searchIvSearch;

    private String title;

    public static void intentActivity(Activity activity, String title, String mac, int position) {
        Intent intent = new Intent(activity, DeviceInfoActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("mac", mac);
        intent.putExtra("position", position);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_device_info;
    }

    @Override
    protected void initAndBindConfig() {
        super.initAndBindConfig();
        searchIvSearch.setVisibility(View.GONE);
        title = getIntent().getStringExtra("title");

        toolbarTitle.setText(title);

        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.iv_back, R.id.device_operation, R.id.service_request, R.id.after_sale_service, R.id.setting_btn, R.id.device_produce, R.id.clear_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.device_operation:
                String mac = getIntent().getStringExtra("mac");
                BleDevice bleDevice = BleController.getInstance().getBleDeviceByMac(mac);
                if (bleDevice == null) {
                    BleScanAndConnectUtil.connectDevice(mac, this, true);
                } else {
                    Device device = new Device(bleDevice);
                    SPYControllerActivity.intentActivity(DeviceInfoActivity.this, device);
                }
                break;
            case R.id.device_produce:
                break;
            case R.id.service_request:
                break;
            case R.id.after_sale_service:
                break;
            case R.id.setting_btn:
                mac = getIntent().getStringExtra("mac");
                SettingActivity.intentActivity(this, title, mac);
                break;
            case R.id.clear_bind:
                clearBindDevice();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void DeviceSettingEvent(DeviceSettingEvent event) {
        if (event.isChangeName()) {
            this.title = event.getName();
            toolbarTitle.setText(title);
            int position = getIntent().getIntExtra("position", -1);
            EventBus.getDefault().post(new DeviceEvenet(OperationAction.UPDATE, position, new BaseListItem(title, null)));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPasswordEvent(SetPasswordEvent event) {
        Device device = event.getDevice();
        SPYControllerActivity.intentActivity(DeviceInfoActivity.this, device);
    }

    private void clearBindDevice() {
        final int position = getIntent().getIntExtra("position", -1);
        DialogUtils.showWarnConfirmDialog(this, "Are you sure you need to unbind with " +
                title + "?", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                EventBus.getDefault().post(new DeviceEvenet(OperationAction.DELETE, position));
                onBackPressed();
            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
