package com.dmrjkj.remotecontroller.module.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clj.fastble.data.BleDevice;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;
import com.dmrjkj.remotecontroller.cache.ShareprefrenceCache;
import com.dmrjkj.remotecontroller.dialog.DialogUtils;
import com.dmrjkj.remotecontroller.enums.TouchFeedbackEnums;
import com.dmrjkj.remotecontroller.events.DeviceSettingEvent;
import com.dmrjkj.remotecontroller.module.base.BaseActivity;
import com.dmrjkj.remotecontroller.module.base.BleScanAndConnectUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action2;

/**
 * Created by xinchen on 19-5-29.
 */

@SuppressLint("SetTextI18n")
public class SettingActivity extends BaseActivity{
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_iv_search)
    ImageView searchIvSearch;
    @BindView(R.id.current_name)
    TextView currentName;
    @BindView(R.id.manual_more_view)
    LinearLayout manualMoreView;
    @BindView(R.id.current_feedback)
    TextView currentFeedback;

    public static void intentActivity(Activity activity, String name, String macAddress) {
        activity.startActivity(new Intent(activity, SettingActivity.class).putExtra("name", name).putExtra("macAddress", macAddress));
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initAndBindConfig() {
        super.initAndBindConfig();

        toolbarTitle.setText("Setting");
        searchIvSearch.setVisibility(View.GONE);

        String name = getIntent().getStringExtra("name");
        currentName.setText(name);
        manualMoreView.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.rename_view, R.id.manual_view, R.id.en_view, R.id.fr_view, R.id.nl_view, R.id.security_view, R.id.feedback_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rename_view:
                renameAction();
                break;
            case R.id.manual_view:
                manualMoreView.setVisibility(manualMoreView.isShown() ? View.GONE : View.VISIBLE);
                break;
            case R.id.en_view:
                PdfActivity.intentActivity(this, "en");
                break;
            case R.id.fr_view:
                PdfActivity.intentActivity(this, "fr");
                break;
            case R.id.nl_view:
                PdfActivity.intentActivity(this, "nl");
                break;
            case R.id.security_view:
                String mac = getIntent().getStringExtra("macAddress");
                BleDevice bleDevice = BleController.getInstance().getBleDeviceByMac(mac);
                if (bleDevice == null) {
                    BleScanAndConnectUtil.connectDevice(mac, this, false);
                } else {
                    Device device = new Device(bleDevice);
                    SecurityCodeActivity.intentActivity(this, device);
                }
                break;
            case R.id.feedback_view:
                touchFeedbackDialog();
                break;
        }
    }

    /**
     *重命名
     */
    private void renameAction() {
        String name = getIntent().getStringExtra("name");
        final String mac = getIntent().getStringExtra("macAddress");
        DialogUtils.showInputDialog(this, "Rename", "Please input device name!", name, 1, 15, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                ShareprefrenceCache.getInstance().saveDeviceToCache(mac, input.toString());
                currentName.setText(input.toString());
                EventBus.getDefault().post(new DeviceSettingEvent(input.toString(), true));
            }
        });
    }

    /**
     * 触摸反馈
     */
    private void touchFeedbackDialog() {
        List<String> models = new ArrayList<>();
        for (TouchFeedbackEnums touchFeedbackEnums : TouchFeedbackEnums.values()) {
            models.add(touchFeedbackEnums.name());
        }
        int model = ShareprefrenceCache.getInstance().GInt(ShareprefrenceCache.FEEDBACK_TOUCH_MODEL, 0);
        DialogUtils.showSinglerChoiceDialog(this, "Feedback on touch", model, models, new Action2<Integer, String>() {
            @Override
            public void call(Integer integer, String s) {
                ShareprefrenceCache.getInstance().PInt(ShareprefrenceCache.FEEDBACK_TOUCH_MODEL, integer);
                TouchFeedbackEnums touchFeedbackEnums = TouchFeedbackEnums.values()[integer];
                currentFeedback.setText(touchFeedbackEnums.name());
                EventBus.getDefault().post(new DeviceSettingEvent(true, touchFeedbackEnums));
            }
        });
    }
}
