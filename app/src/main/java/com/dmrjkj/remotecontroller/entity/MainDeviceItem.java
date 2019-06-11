package com.dmrjkj.remotecontroller.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by xinchen on 19-4-16.
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class MainDeviceItem extends BaseListItem{

    private boolean check;

    public MainDeviceItem(int itemId) {
        super(itemId);
        this.check = false;
    }

    public MainDeviceItem(int itemId, boolean check) {
        super(itemId);
        this.check = check;
    }
}
