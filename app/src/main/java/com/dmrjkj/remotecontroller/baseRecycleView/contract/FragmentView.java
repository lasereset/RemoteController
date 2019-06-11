package com.dmrjkj.remotecontroller.baseRecycleView.contract;

import android.support.v4.widget.SwipeRefreshLayout;

import com.dmrjkj.remotecontroller.baseRecycleView.contract.RecycleContract;

/**
 * Created by xinchen on 18-11-10.
 */

public interface FragmentView extends RecycleContract {
    void getOnRefreshInterface(SwipeRefreshLayout.OnRefreshListener listener);

    void refreshComplete();
}
