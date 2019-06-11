package com.dmrjkj.remotecontroller.module.fragment;

import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;
import com.dmrjkj.remotecontroller.cache.ShareprefrenceCache;
import com.dmrjkj.remotecontroller.dialog.DialogUtils;
import com.dmrjkj.remotecontroller.entity.BaseListItem;
import com.dmrjkj.remotecontroller.enums.OperationAction;
import com.dmrjkj.remotecontroller.events.DeviceEvenet;
import com.dmrjkj.remotecontroller.module.base.BaseFragment;
import com.dmrjkj.remotecontroller.utils.ToolUtils;
import com.dmrjkj.remotecontroller.weight.RadarView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xinchen on 19-4-16.
 */

public class SearchDeviceFragment extends BaseFragment {

    @BindView(R.id.radar)
    RadarView radarView;

    private boolean isScaning = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 取消扫描
     */
    public void cancleScan() {
        isScaning = false;
        radarView.stop();
        BleController.getInstance().cancelScan();
    }

    public void beginSearch() {
        isScaning = true;
        radarView.start();
        final List<BleDevice> scanBleDevices = new ArrayList<>();
        BleController.getInstance().scanDevieList(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                if (!isScaning) {
                    return;
                }
                if (ToolUtils.isEmpty(scanBleDevices)) {
                    searchNoneDeviceDialog();
                    return;
                }
                radarView.stop();
                if (scanBleDevices.size() == 1) {
                    searchSomeDeviceDialog(scanBleDevices.get(0));
                } else {
                    showMoreDeviceSelectDialog(scanBleDevices);
                }
            }

            @Override
            public void onScanStarted(boolean success) {
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                if (!Device.isTargetDevice(bleDevice, 1)) {
                    return;
                }
                scanBleDevices.add(bleDevice);
            }
        });
    }

    private void showMoreDeviceSelectDialog(List<BleDevice> scanResultList) {
        List<BaseListItem> baseListItems = new ArrayList<>();
        for (BleDevice device : scanResultList) {
            baseListItems.add(new BaseListItem(ToolUtils.isEmpty(device.getName()) ? "UNKNOWN" : device.getName(), device));
        }
        DialogUtils.showAdapterDialog(getActivity(), "Search Result", baseListItems, new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseListItem baseListItem = (BaseListItem) adapter.getItem(position);
                if (baseListItem == null || baseListItem.getEntity() == null) {
                    return;
                }
                BleDevice bleDevice = (BleDevice) baseListItem.getEntity();
                bindDevice(bleDevice);
            }
        });
    }

    private void searchNoneDeviceDialog() {
        DialogUtils.showWarnConfirmDialog(getContext(), "* Make sure Bluetooth is on.\n" +
                "* Make sure the device is on and Bluetooth is on.\n" +
                "* Make sure the device is nearby.\n", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                getBaseActivity().onBackPressed();
            }
        }).getBuilder().title("Search failed")
        .positiveText("Exit")
        .neutralText("Confirm")
        .onNeutral(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                getBaseActivity().onBackPressed();
            }
        }).build().show();
    }

    private void searchSomeDeviceDialog(final BleDevice bleDevice) {
        DialogUtils.showWarnConfirmDialog(getContext(), "Find new Device " +
                bleDevice.getName(), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                getBaseActivity().onBackPressed();
            }
        }).getBuilder().title("Search success")
                .positiveText("Bind")
                .neutralText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //todo 服务端保存绑定数据
                        bindDevice(bleDevice);
                    }
                }).build().show();
    }

    /**
     * 绑定设备，需要通过服务端保存数据
     * @param bleDevice
     */
    private void bindDevice(BleDevice bleDevice) {
        boolean exist = ShareprefrenceCache.getInstance().saveDeviceToCache(bleDevice.getName(), bleDevice.getMac());
        if (!exist) {
            EventBus.getDefault().post(new DeviceEvenet(OperationAction.ADD, 0, new BaseListItem(bleDevice.getName(), bleDevice.getMac())));
        }
        getActivity().finish();
    }

    @Override
    protected void initFragmentConfig() {
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_search_device;
    }

    @Override
    public String getBaseTitle() {
        return "Nearby device";
    }
}
