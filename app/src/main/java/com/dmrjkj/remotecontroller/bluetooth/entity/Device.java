package com.dmrjkj.remotecontroller.bluetooth.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.bluetooth.util.AdvUtil;

/**
 * Created by xinchen on 19-5-27.
 */

public class Device implements Parcelable {
    public static final Creator<Device> CREATOR = new Creator() {
        public Device createFromParcel(Parcel paramAnonymousParcel) {
            return new Device(paramAnonymousParcel);
        }

        public Device[] newArray(int paramAnonymousInt) {
            return new Device[paramAnonymousInt];
        }
    };
    public static final int DEVICE_CJY = 4;
    public static final int DEVICE_SMG = 3;
    public static final int DEVICE_SPY = 1;
    public static final int DEVICE_TXY = 2;
    public static final int DEVICE_XZT = 5;
    private static String UUID_NOTIFY_CHARACTERISTIC;
    private static String UUID_NOTIFY_SERVICE;
    private static String UUID_WRITE_CHARACTERISTIC;
    private static String UUID_WRITE_SERVICE = "0000ffe5-0000-1000-8000-00805f9b34fb";
    private String address;
    private BleDevice bleDevice;
    private boolean isNeedPwd;
    private boolean isPair;
    private String name;
    private int type = -1;

    static {
        UUID_WRITE_CHARACTERISTIC = "0000ffe9-0000-1000-8000-00805f9b34fb";
        UUID_NOTIFY_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
        UUID_NOTIFY_CHARACTERISTIC = "0000ffe4-0000-1000-8000-00805f9b34fb";
    }

    public Device() {
    }

    protected Device(Parcel paramParcel) {
        this.address = paramParcel.readString();
        this.name = paramParcel.readString();
        this.type = paramParcel.readInt();
        if (paramParcel.readByte() != 0) {
        }
        for (boolean bool = true; ; bool = false) {
            this.isNeedPwd = bool;
            this.bleDevice = ((BleDevice) paramParcel.readParcelable(BleDevice.class.getClassLoader()));
            return;
        }
    }

    public Device(BleDevice paramBleDevice) {
        this.bleDevice = paramBleDevice;
        this.address = paramBleDevice.getMac();
        this.name = paramBleDevice.getName();
        filterType();
        this.isNeedPwd = isNeedPwd(paramBleDevice);
    }

    private void filterType() {
        if (isDevice(this.bleDevice)) {
            String str = AdvUtil.parse(HexUtil.encodeHexStr(this.bleDevice.getScanRecord()), "FF").data;
            this.type = 0;
            if (str.contains("484401")) {
                this.type = 1;
            }
            if (str.contains("484402")) {
                this.type = 2;
            }
            if (str.contains("484403")) {
                this.type = 3;
            }
            if (str.contains("484404")) {
                this.type = 4;
            }
            if (str.contains("484405")) {
                this.type = 5;
            }
        }
    }

    public static boolean isDevice(BleDevice paramBleDevice) {
        if (paramBleDevice.getScanRecord() == null) {
            return false;
        }
        AdvUtil.Adv adv = AdvUtil.parse(HexUtil.encodeHexStr(paramBleDevice.getScanRecord()), "FF");
        if (adv == null) {
            return false;
        }
        String paramAdvData = adv.data;
        if ((paramAdvData == null) || (paramAdvData.isEmpty()) || (!paramAdvData.contains("4844"))) {
            return false;
        }
        return true;
    }

    public static boolean isNeedPwd(BleDevice paramBleDevice) {
        if (!isDevice(paramBleDevice)) {
        }
        while (!AdvUtil.parse(HexUtil.encodeHexStr(paramBleDevice.getScanRecord()), "FF").data.contains("4844011001")) {
            return false;
        }
        return true;
    }

    public static boolean isTargetDevice(BleDevice paramBleDevice, int paramInt) {
        if (!isDevice(paramBleDevice)) {
            return false;
        }
        AdvUtil.Adv adv = AdvUtil.parse(HexUtil.encodeHexStr(paramBleDevice.getScanRecord()), "FF");
        if ((adv == null || !adv.data.contains("48440110")) || (paramInt == 0)) {
            return false;
        }
        return true;
    }

    public void _notify(BleNotifyCallback paramBleNotifyCallback) {
        if (this.bleDevice != null) {
            BleController.getInstance().registerNotify(bleDevice, paramBleNotifyCallback);
        }
    }

    public int describeContents() {
        return 0;
    }

    public String getAddress() {
        return this.address;
    }

    public BleDevice getBleDevice() {
        return this.bleDevice;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public boolean isNeedPwd() {
        return this.isNeedPwd;
    }

    public boolean isPair() {
        return this.isPair;
    }

    public void removeNotify() {
        if (this.bleDevice != null) {
            BleController.getInstance().unRegisterNotify(this.bleDevice);
        }
    }

    public void setAddress(String paramString) {
        this.address = paramString;
    }

    public void setBleDevice(BleDevice paramBleDevice) {
        this.bleDevice = paramBleDevice;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setNeedPwd(boolean paramBoolean) {
        this.isNeedPwd = paramBoolean;
    }

    public void setPair(boolean paramBoolean) {
        this.isPair = paramBoolean;
    }

    public void setType(int paramInt) {
        this.type = paramInt;
    }

    public void write(String paramString, BleWriteCallback paramBleWriteCallback) {
        write(HexUtil.hexStringToBytes(paramString), paramBleWriteCallback);
    }

    public void write(byte[] paramArrayOfByte, BleWriteCallback paramBleWriteCallback) {
        Object localObject = paramBleWriteCallback;
        if (paramBleWriteCallback == null) {
            localObject = new BleWriteCallback() {
                public void onWriteFailure(BleException paramAnonymousBleException) {
                    Log.d("Device", "Enter into this onWriteFailure ==>");
                }

                public void onWriteSuccess(int paramAnonymousInt1, int paramAnonymousInt2, byte[] paramAnonymousArrayOfByte) {
                    Log.d("Device", "Enter into this onWriteSuccess ==>");
                }
            };
        }
        if (this.bleDevice != null) {
            BleController.getInstance().write(this.bleDevice, paramArrayOfByte, (BleWriteCallback) localObject);
        }
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.address);
        paramParcel.writeString(this.name);
        paramParcel.writeInt(this.type);
        if (this.isNeedPwd) {
        }
        for (int i = 1; ; i = 0) {
            paramParcel.writeByte((byte) i);
            paramParcel.writeParcelable(this.bleDevice, paramInt);
            return;
        }
    }
}
