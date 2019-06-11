package com.dmrjkj.remotecontroller.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.entity.BaseListItem;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

import java.util.List;

public class BaseListItemAdapter<T extends BaseListItem> extends BaseQuickAdapter<T,BaseViewHolder> {

    public BaseListItemAdapter() {
        super(R.layout.single_item);
    }

    public BaseListItemAdapter(List<T> data) {
        super(R.layout.single_item, data);
        openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        helper.setText(R.id.base_title, item.getItem());
        if (ToolUtils.isEmpty(item.getDescription())) {
            helper.getView(R.id.base_description).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.base_description).setVisibility(View.VISIBLE);
            helper.setText(R.id.base_description, item.getDescription());
        }
    }
}
