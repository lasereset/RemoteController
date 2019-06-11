package com.dmrjkj.remotecontroller.baseRecycleView;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.baseRecycleView.contract.FragmentView;
import com.dmrjkj.remotecontroller.baseRecycleView.presenter.RecyclePresenter;
import com.dmrjkj.remotecontroller.module.base.BaseActivity;
import com.dmrjkj.remotecontroller.support.WrapContentLinearLayoutManager;
import com.dmrjkj.remotecontroller.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xinchen on 18-11-10.
 */

public abstract class RecycleActivity extends BaseActivity implements FragmentView {

    @BindView(R.id.rv_list)
    protected RecyclerView rvList;
    @BindView(R.id.pull_to_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_back)
    protected ImageView ivBack;
    @BindView(R.id.search_iv_search)
    protected ImageView searchIvSearch;
    @BindView(R.id.toolbar_title)
    protected TextView toolbarTitle;
    @BindView(R.id.toolbar_title_2)
    protected TextView toolbarTitle2;

    private RecyclePresenter presenter;

    public abstract RecyclePresenter getRecyclePresenter();

    public abstract String getToolbarTitle();

    protected boolean neesShowPullToRefresh = false;

    public void setNeesShowPullToRefresh(boolean neesShowPullToRefresh) {
        this.neesShowPullToRefresh = neesShowPullToRefresh;
    }

    @Override
    protected void initAndBindConfig() {
        super.initAndBindConfig();
        if (!neesShowPullToRefresh) {
            swipeRefreshLayout.setEnabled(false);
        } else {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        }
        this.presenter = getRecyclePresenter();
        this.presenter.initFragmentConfig();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_list;
    }

    @Override
    public void getAdapter(BaseQuickAdapter adapter) {
        //设置布局管理器
        WrapContentLinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(this);
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

        toolbarTitle.setText(Html.fromHtml(getToolbarTitle()));
        setTitle(toolbarTitle.getText().toString());
    }

    @OnClick(R.id.iv_back)
    public void backClick() {
        onBackPressed();
    }
}
