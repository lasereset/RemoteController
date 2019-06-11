package com.dmrjkj.remotecontroller.events;

import com.dmrjkj.remotecontroller.entity.BaseListItem;
import com.dmrjkj.remotecontroller.enums.OperationAction;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xinchen on 19-4-16.
 */

@Data
@NoArgsConstructor
public class DeviceEvenet {

    private OperationAction action;

    private int position;

    private BaseListItem baseListItem;

    public DeviceEvenet(OperationAction action, int position) {
        this.action = action;
        this.position = position;
    }

    public DeviceEvenet(OperationAction action, int position, BaseListItem baseListItem) {
        this.action = action;
        this.position = position;
        this.baseListItem = baseListItem;
    }
}
