package com.dmrjkj.remotecontroller.baseRecycleView.contract;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by xinchen on 18-11-10.
 */

public interface RecycleContract {

    void getAdapter(BaseQuickAdapter adapter);

    BaseQuickAdapter.OnItemClickListener getOnItemClickListener();
}
