package com.dmrjkj.remotecontroller.module.activity.presenter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.adapter.DeviceListItemAdapter;
import com.dmrjkj.remotecontroller.baseRecycleView.presenter.BaseRecyclePresenter;
import com.dmrjkj.remotecontroller.baseRecycleView.contract.FragmentView;
import com.dmrjkj.remotecontroller.entity.DeviceItem;
import com.dmrjkj.remotecontroller.response.Pagination;
import com.dmrjkj.remotecontroller.response.QueryResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinchen on 19-4-16.
 */

public class DeviceListPresenter extends BaseRecyclePresenter{

    private String type = "扫描仪";

    public DeviceListPresenter(FragmentView contract) {
        super(contract);
    }

    @Override
    public void initFragmentConfig() {
        super.initFragmentConfig();
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        if (adapter == null) {
            adapter = new DeviceListItemAdapter();
        }
        return adapter;
    }

    public void requestItemsData(String type, int page) {
        QueryResponse<DeviceItem> baseListItemQueryResponse = new QueryResponse<>();
        List<DeviceItem> baseListItemList = new ArrayList<>();
        int start = page == 1 ? 0 : ((page - 1) * 20 - 1);
        for (int i = start; i < 20 * page; i++) {
            baseListItemList.add(new DeviceItem(type + " Rotary laser " + (i + 1), "这里是简单的描述信息...", i, false));
        }
        baseListItemQueryResponse.setItems(baseListItemList);
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setHasNext(page < 3);
        pagination.setHasPrev(page > 1);
        baseListItemQueryResponse.setPage(pagination);

        requestComplete(true, page);
        requestBackData(baseListItemQueryResponse);
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
        requestItemsData(type, page);
    }
}
