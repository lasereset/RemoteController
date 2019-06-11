package com.dmrjkj.remotecontroller.bluetooth;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.dmrjkj.remotecontroller.AppApplication;
import com.dmrjkj.remotecontroller.bluetooth.util.Command;

/**
 * Created by xinchen on 19-5-24.
 */

public class BleController {

    private static final String TAG = "BleController";

    private BleController() {}


    private static class BleControllerInstance {
        private static final BleController INSTANCE = new BleController();
    }

    public static BleController getInstance() {
        return BleControllerInstance.INSTANCE;
    }

    public void initBle(AppApplication context) {
        BleManager.getInstance().enableBluetooth();
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);
        BleManager.getInstance().init(context);
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setAutoConnect(false)
                .setScanTimeOut(10000L)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    /**
     * 扫描设备
     * @param callback
     */
    public void scanDevieList(BleScanCallback callback) {
        BleManager.getInstance().scan(callback);
    }

    /**
     * 取消扫描
     */
    public void cancelScan() {
        BleManager.getInstance().cancelScan();
    }

    /**
     * 连接设备
     */
    public void connectDevice(BleDevice bleDevice, BleGattCallback callback) {
        BleManager.getInstance().connect(bleDevice, callback);
    }

    /**
     * 根据mac连接设备
     */
    public void connectDevice(String mac, BleGattCallback callback) {
        BleManager.getInstance().connect(mac, callback);
    }

    /**
     * 断开某个连接设备
     */
    public void disConnectDevice(BleDevice bleDevice) {
        BleManager.getInstance().disconnect(bleDevice);
    }

    /**
     * 断开搜索已连接的设备
     */
    public void disConnectAllDevice() {
        BleManager.getInstance().disconnectAllDevice();
    }

    /**
     * 退出时，需要清理资源
     */
    public void clear() {
        BleManager.getInstance().destroy();
    }

    /**
     * 判断某个设备是否已连接
     */
    public boolean isConnect(BleDevice bleDevice) {
        return BleManager.getInstance().isConnected(bleDevice);
    }

    /**
     * 根据mac判断某个设备是否已经连接
     */
    public boolean isConnect(String mac) {
        return BleManager.getInstance().isConnected(mac);
    }

    /**
     * 根据mac获取设备
     */
    public BleDevice getBleDeviceByMac(String mac) {
        if (!isConnect(mac)) {
            return null;
        }
        for (BleDevice bleDevice : BleManager.getInstance().getAllConnectedDevice()) {
            if (bleDevice.getMac().equals(mac)) {
                return bleDevice;
            }
        }
        return null;
    }

    /**
     * 获取当前状态
     */
    public BleScanState getScanState() {
        return BleManager.getInstance().getScanSate();
    }

    /**
     * 注册通知
     */
    public void registerNotify(BleDevice bleDevice, BleNotifyCallback callback) {
        BleManager.getInstance().notify(
                bleDevice,
                Command.UUID_NOTIFY_SERVICE,
                Command.UUID_NOTIFY_CHARACTERISTIC, callback);
    }

    /**
     * 解除通知
     */
    public void unRegisterNotify(BleDevice bleDevice) {
        BleManager.getInstance().stopNotify(bleDevice, Command.UUID_NOTIFY_SERVICE, Command.UUID_NOTIFY_CHARACTERISTIC);
    }

    /**
     * 发送通知
     */
    public void write(BleDevice bleDevice, byte[] data, BleWriteCallback callback) {
        BleManager.getInstance().write(
                bleDevice,
                Command.UUID_WRITE_SERVICE,
                Command.UUID_WRITE_CHARACTERISTIC,
                data, callback);
    }

    /**
     * 读取设备的一些信息
     */
    public void read(BleDevice bleDevice) {
        BleManager.getInstance().read(
                bleDevice,
                Command.UUID_WRITE_SERVICE,
                Command.UUID_WRITE_CHARACTERISTIC,
                new BleReadCallback() {
                    @Override
                    public void onReadSuccess(byte[] data) {
                    }

                    @Override
                    public void onReadFailure(BleException exception) {
                    }
                });
    }

    /**
     * 获取某个设备的连接状态
     */
    public int getConnectStatus(BleDevice device) {
        return BleManager.getInstance().getConnectState(device);
    }

}
