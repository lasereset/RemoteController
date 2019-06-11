package com.dmrjkj.remotecontroller.module.base;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;
import com.dmrjkj.remotecontroller.dialog.DialogUtils;
import com.dmrjkj.remotecontroller.module.activity.SecurityCodeActivity;

import java.util.List;

/**
 * Created by xinchen on 19-6-10.
 */

public class BleScanAndConnectUtil {
    private static boolean isConnecting = false;
    public static void connectDevice(final String mac, final Activity activity, final boolean needVerifyCode) {
        final MaterialDialog dialog = DialogUtils.showLoadingDialog(activity, "Connecting the device, please wait a moment...");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isConnecting = false;
            }
        });
        BleController.getInstance().scanDevieList(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
            }

            @Override
            public void onScanStarted(boolean success) {
                dialog.show();
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                if (bleDevice.getMac().equals(mac)) {
                    if (BleController.getInstance().getScanState() == BleScanState.STATE_SCANNING) {
                        BleController.getInstance().cancelScan();
                    }
                    BleController.getInstance().connectDevice(bleDevice, new BleGattCallback() {
                        @Override
                        public void onStartConnect() {
                            isConnecting = true;
                        }

                        @Override
                        public void onConnectFail(BleDevice bleDevice, BleException exception) {
                        }

                        @Override
                        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                            if (!isConnecting) {
                                return;
                            }
                            dialog.cancel();
                            //需要验证密码
                            if (needVerifyCode && Device.isNeedPwd(bleDevice)) {
                                Device device = new Device(bleDevice);
                                SecurityCodeActivity.intentActivityForModel(activity, device, SecurityCodeActivity.INPUT_PWD);
                                return;
                            }
                            Device device = new Device(bleDevice);
                            SecurityCodeActivity.intentActivity(activity, device);
                        }

                        @Override
                        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                        }
                    });
                }
            }
        });
    }
}
