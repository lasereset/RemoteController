package com.dmrjkj.remotecontroller.bluetooth.action;

import com.clj.fastble.callback.BleWriteCallback;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;
import com.dmrjkj.remotecontroller.bluetooth.util.Command;

/**
 * Created by xinchen on 19-5-27.
 */

public abstract class Action
{
    protected Device device;

    Action(Device paramDevice)
    {
        this.device = paramDevice;
    }

    public void actionReportEnd()
    {
        actionReportEnd(null);
    }

    public void actionReportEnd(BleWriteCallback paramBleWriteCallback)
    {
        write(Command.CLOSE_STATUS, paramBleWriteCallback);
    }

    public void actionReportStart()
    {
        actionReportStart(null);
    }

    public void actionReportStart(BleWriteCallback paramBleWriteCallback)
    {
        write(Command.OPEN_STATUS, paramBleWriteCallback);
    }

    protected void write(String paramString, BleWriteCallback paramBleWriteCallback)
    {
        if (this.device != null) {
            this.device.write(paramString, paramBleWriteCallback);
        }
    }
}

