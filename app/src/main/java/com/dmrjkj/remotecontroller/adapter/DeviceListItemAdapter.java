package com.dmrjkj.remotecontroller.adapter;

import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.entity.DeviceItem;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

import java.util.List;

public class DeviceListItemAdapter extends BaseQuickAdapter<DeviceItem, BaseViewHolder> {

    public DeviceListItemAdapter() {
        super(R.layout.item_device);
    }

    public DeviceListItemAdapter(List<DeviceItem> data) {
        super(R.layout.item_device, data);
        openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    @Override
    protected void convert(final BaseViewHolder helper, DeviceItem item) {
        helper.setText(R.id.base_title, item.getItem());
        if (ToolUtils.isEmpty(item.getDescription())) {
            helper.getView(R.id.base_description).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.base_description).setVisibility(View.VISIBLE);
            helper.setText(R.id.base_description, item.getDescription());
        }
    }
}
