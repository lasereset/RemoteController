package com.dmrjkj.remotecontroller.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.entity.BaseListItem;
import com.dmrjkj.remotecontroller.entity.MainDeviceItem;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

import java.util.List;

public class MainDeviceAdapter extends BaseQuickAdapter<MainDeviceItem, BaseViewHolder> {

    public MainDeviceAdapter() {
        super(R.layout.single_item_index);
    }

    public MainDeviceAdapter(List<MainDeviceItem> data) {
        super(R.layout.single_item_index, data);
        openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainDeviceItem item) {
        helper.setText(R.id.base_title, item.getItem());
        if (ToolUtils.isEmpty(item.getDescription())) {
            helper.getView(R.id.base_description).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.base_description).setVisibility(View.VISIBLE);
            helper.setText(R.id.base_description, item.getDescription());
        }
        helper.setVisible(R.id.check, item.isCheck());
    }

    public void setCheckItem(int position) {
        List<MainDeviceItem> items = getData();
        for (int i = 0; i < items.size(); i++) {
            if (position == i) {
                items.get(i).setCheck(true);
            } else {
                items.get(i).setCheck(false);
            }
        }
        notifyDataSetChanged();
    }
}
