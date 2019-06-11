package com.dmrjkj.remotecontroller.baseRecycleView.presenter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.adapter.BaseListItemAdapter;
import com.dmrjkj.remotecontroller.baseRecycleView.contract.FragmentView;
import com.dmrjkj.remotecontroller.response.Pagination;
import com.dmrjkj.remotecontroller.response.QueryResponse;

/**
 * Created by xinchen on 19-3-29.
 */

public class BaseRecyclePresenter extends RecyclePresenter implements BaseQuickAdapter.RequestLoadMoreListener {
    protected Pagination pagination;

    public BaseRecyclePresenter(FragmentView view) {
        super(view);
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        if (adapter == null) {
            adapter = new BaseListItemAdapter();
        }
        return adapter;
    }

    public void requestItemsData(int page) {
    }

    /**
     * 加载完成的回调
     */
    public void requestComplete(boolean success, int page) {
        if (page > 1 && !success) {
            adapter.loadMoreFail();
        } else {
            recycleContract.refreshComplete();
        }
    }

    /**
     * 成功的数据返回
     * @param response
     */
    public void requestBackData(QueryResponse response) {
        pagination = response.getPage();
        if (pagination != null && pagination.isHasNext()) {
            adapter.setEnableLoadMore(true);
            adapter.setOnLoadMoreListener(this);
        } else {
            adapter.setEnableLoadMore(false);
            adapter.setOnLoadMoreListener(null);
        }
        if (pagination != null) {
            adapter.loadMoreComplete();
        }

        if (pagination != null && pagination.getPage() > 1) {
            adapter.addData(response.getItems());
        } else {
            adapter.setNewData(response.getItems());
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (pagination == null || !pagination.isHasNext()) {
            adapter.loadMoreEnd();
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestItemsData(pagination.getPage() + 1);
    }
}
