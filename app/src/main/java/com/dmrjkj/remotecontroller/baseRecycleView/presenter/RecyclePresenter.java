package com.dmrjkj.remotecontroller.baseRecycleView.presenter;

import android.support.v4.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.baseRecycleView.contract.FragmentView;

/**
 * Created by xinchen on 18-11-10.
 */

public abstract class RecyclePresenter implements SwipeRefreshLayout.OnRefreshListener {
    protected BaseQuickAdapter adapter;
    protected FragmentView recycleContract;

    public RecyclePresenter(FragmentView view) {
        this.recycleContract = view;
    }

    public abstract BaseQuickAdapter getAdapter();

    public void initFragmentConfig() {
        adapter = getAdapter();
        adapter.openLoadAnimation();
        adapter.setOnItemClickListener(recycleContract.getOnItemClickListener());
        recycleContract.getAdapter(adapter);

        recycleContract.getOnRefreshInterface(this);
    }

    @Override
    public void onRefresh() {
    }
}
