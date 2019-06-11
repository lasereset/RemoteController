package com.dmrjkj.remotecontroller.entity;



import com.dmrjkj.remotecontroller.AppApplication;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xinchen on 18-11-23.
 */

@Data
@NoArgsConstructor
public class BaseListItem {

    protected int itemId;

    protected String item;

    protected String description;

    protected Object entity;

    public BaseListItem(int itemId, boolean isId) {
        this.itemId = itemId;
        if (isId) {
            this.item = AppApplication.getInstance().getResources().getString(itemId);
        }
    }

    public BaseListItem(int itemId) {
        this.itemId = itemId;
        this.item = AppApplication.getInstance().getResources().getString(itemId);
    }

    public BaseListItem(String item, Object entity) {
        this.item = item;
        this.entity = entity;
    }

    public BaseListItem(int itemId, String description) {
        this(itemId);
        this.description = description;
    }

    public BaseListItem(int itemId, String item, String description) {
        this.itemId = itemId;
        this.item = item;
        this.description = description;
    }

    public BaseListItem(String item, String description, Object entity) {
        this(item, entity);
        this.description = description;
    }

    public BaseListItem(int itemId, String item, String description, Object entity) {
        this.itemId = itemId;
        this.item = item == null ? AppApplication.getInstance().getResources().getString(itemId) : item;
        this.description = description;
        this.entity = entity;
    }
}
