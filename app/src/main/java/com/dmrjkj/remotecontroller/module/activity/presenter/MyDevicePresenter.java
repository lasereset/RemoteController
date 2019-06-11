package com.dmrjkj.remotecontroller.module.activity.presenter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.adapter.MineDeviceAdapter;
import com.dmrjkj.remotecontroller.baseRecycleView.presenter.BaseRecyclePresenter;
import com.dmrjkj.remotecontroller.cache.ShareprefrenceCache;
import com.dmrjkj.remotecontroller.entity.DeviceItem;
import com.dmrjkj.remotecontroller.module.activity.contract.DeviceListContract;
import com.dmrjkj.remotecontroller.response.Pagination;
import com.dmrjkj.remotecontroller.response.QueryResponse;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xinchen on 19-4-16.
 */

public class MyDevicePresenter extends BaseRecyclePresenter implements BaseQuickAdapter.OnItemChildClickListener, MineDeviceAdapter.OnChangeBlueToothCallback {
    private DeviceListContract contract;
    public MyDevicePresenter(DeviceListContract contract) {
        super(contract);
        this.contract = contract;
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        if (adapter == null) {
            adapter = new MineDeviceAdapter(this);
        }
        return adapter;
    }

    @Override
    public void initFragmentConfig() {
        super.initFragmentConfig();
        adapter.setOnItemChildClickListener(this);
    }


    @Override
    public void onRefresh() {
        adapter.setNewData(null);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestItemsData(1);
    }

    @Override
    public void requestItemsData(int page) {
        if (page > 1) {
            requestComplete(true, page);
            requestBackData(null);
            return;
        }
        QueryResponse<DeviceItem> baseListItemQueryResponse = new QueryResponse<>();
        List<DeviceItem> baseListItemList = new ArrayList<>();
        Map<String, String> mapData = ShareprefrenceCache.getInstance().getAllDeviceCache();
        if (ToolUtils.isEmpty(mapData)) {
            return;
        }
        for (Map.Entry<String, String> map : mapData.entrySet()) {
            baseListItemList.add(new DeviceItem(map.getKey(), ("Add Time：" + ToolUtils.formatTimeNormal(new Date())), map.getValue(), true));
        }
        baseListItemQueryResponse.setItems(baseListItemList);
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        baseListItemQueryResponse.setPage(pagination);

        requestComplete(true, page);
        requestBackData(baseListItemQueryResponse);

        contract.onChangeBlueToolth(baseListItemList.get(0).getItem());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.content) {
            contract.getOnItemClickListener().onItemClick(adapter, view, position);
            return;
        }
        contract.onItemChidClick(adapter, view, position);
    }

    @Override
    public void changeBlueTooth(String title) {
        contract.onChangeBlueToolth(title);
    }
}
