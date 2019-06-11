package com.dmrjkj.remotecontroller.baseRecycleView;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.baseRecycleView.contract.RecycleContract;
import com.dmrjkj.remotecontroller.baseRecycleView.presenter.RecyclePresenter;
import com.dmrjkj.remotecontroller.module.base.BaseFragment;
import com.dmrjkj.remotecontroller.support.WrapContentLinearLayoutManager;
import com.dmrjkj.remotecontroller.utils.ToastUtils;

import butterknife.BindView;

/**
 * Created by xinchen on 18-11-10.
 */

public abstract class NoToolBarRecycleFragment extends BaseFragment implements RecycleContract {

    @BindView(R.id.rv_list)
    protected RecyclerView rvList;
    @BindView(R.id.pull_to_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;

    private RecyclePresenter presenter;

    public abstract RecyclePresenter getRecyclePresenter();

    private boolean neesShowPullToRefresh = false;

    public void setNeesShowPullToRefresh(boolean neesShowPullToRefresh) {
        this.neesShowPullToRefresh = neesShowPullToRefresh;
    }

    @Override
    protected void initFragmentConfig() {
        if (!neesShowPullToRefresh) {
            swipeRefreshLayout.setEnabled(false);
        } else {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);
        }
        this.presenter = getRecyclePresenter();
        this.presenter.initFragmentConfig();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_list;
    }

    @Override
    public void getAdapter(BaseQuickAdapter adapter) {
        //设置布局管理器
        WrapContentLinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(getBaseActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(linearLayoutManager);

        rvList.setAdapter(adapter);
        adapter.setEmptyView(ToastUtils.getEmptyView(neesShowPullToRefresh, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.measure(0, 0);
                swipeRefreshLayout.setRefreshing(true);
                presenter.onRefresh();
            }
        }));
    }
}
