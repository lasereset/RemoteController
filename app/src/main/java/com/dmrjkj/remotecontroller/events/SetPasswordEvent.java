package com.dmrjkj.remotecontroller.events;

import com.clj.fastble.data.BleDevice;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xinchen on 19-6-10.
 */

@Data
@NoArgsConstructor
public class SetPasswordEvent {

    private Device device;

    public SetPasswordEvent(Device device) {
        this.device = device;
    }
}
