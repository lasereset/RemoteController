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
public class DeviceItem extends BaseListItem{

    private boolean show_delete;

    private String tag;

    public DeviceItem(String item, String desc, Object entity) {
        super(item, desc, entity);
    }

    public DeviceItem(String item, String desc, Object entity, boolean show_delete) {
        super(item, desc, entity);
        this.show_delete = show_delete;
    }

    public DeviceItem(String item, String desc, Object entity, boolean show_delete, boolean show_add, boolean bluetooth) {
        super(item, desc, entity);
        this.show_delete = show_delete;
    }
}
