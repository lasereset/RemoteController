package com.dmrjkj.remotecontroller.module.activity.contract;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.baseRecycleView.contract.FragmentView;
import com.dmrjkj.remotecontroller.entity.DeviceItem;

/**
 * Created by xinchen on 19-4-17.
 */

public interface DeviceListContract extends FragmentView {

    void onItemChidClick(BaseQuickAdapter adapter, View view, int position);

    void onChangeBlueToolth(String deviceItem);
}
