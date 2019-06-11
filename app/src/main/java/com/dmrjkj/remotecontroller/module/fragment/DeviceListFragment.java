package com.dmrjkj.remotecontroller.module.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.adapter.MainDeviceAdapter;
import com.dmrjkj.remotecontroller.baseRecycleView.NoToolBarRecycleFragment;
import com.dmrjkj.remotecontroller.baseRecycleView.contract.FragmentView;
import com.dmrjkj.remotecontroller.baseRecycleView.presenter.RecyclePresenter;
import com.dmrjkj.remotecontroller.cache.ShareprefrenceCache;
import com.dmrjkj.remotecontroller.entity.BaseListItem;
import com.dmrjkj.remotecontroller.entity.MainDeviceItem;
import com.dmrjkj.remotecontroller.enums.OperationAction;
import com.dmrjkj.remotecontroller.events.DeviceEvenet;
import com.dmrjkj.remotecontroller.module.activity.presenter.DeviceListPresenter;
import com.dmrjkj.remotecontroller.support.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xinchen on 19-4-17.
 */

public class DeviceListFragment extends NoToolBarRecycleFragment implements FragmentView {

    @BindView(R.id.main_device_list)
    RecyclerView mainDeviceList;
    @BindView(R.id.search_btn)
    ImageView searchBtn;
    @BindView(R.id.search_edit)
    EditText searchEdit;

    private DeviceListPresenter presenter;

    @Override
    protected void initFragmentConfig() {
        setNeesShowPullToRefresh(true);
        super.initFragmentConfig();

        setMainAdapter();

        presenter.requestItemsData(1);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_device_list;
    }

    @Override
    public BaseQuickAdapter.OnItemClickListener getOnItemClickListener() {
        return new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseListItem baseListItem = (BaseListItem) adapter.getItem(position);
                if (baseListItem == null)
                    return;
                ShareprefrenceCache.getInstance().saveDeviceToCache(baseListItem.getItem(), (String) baseListItem.getEntity());
                EventBus.getDefault().post(new DeviceEvenet(OperationAction.ADD, 0, baseListItem));
                getBaseActivity().onBackPressed();
            }
        };
    }

    @Override
    public RecyclePresenter getRecyclePresenter() {
        if (presenter == null)
            presenter = new DeviceListPresenter(this);
        return presenter;
    }

    public void setMainAdapter() {
        //设置布局管理器
        WrapContentLinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainDeviceList.setLayoutManager(linearLayoutManager);

        List<MainDeviceItem> baseListItemList = new ArrayList<>();
        baseListItemList.add(new MainDeviceItem(R.string.saopingyi, true));
        baseListItemList.add(new MainDeviceItem(R.string.touxianyi));
        baseListItemList.add(new MainDeviceItem(R.string.zhinengfujian));
        baseListItemList.add(new MainDeviceItem(R.string.cejuyi));
        baseListItemList.add(new MainDeviceItem(R.string.jieshouqi));
        baseListItemList.add(new MainDeviceItem(R.string.celianglun));
        baseListItemList.add(new MainDeviceItem(R.string.rexiangchengyi));
        baseListItemList.add(new MainDeviceItem(R.string.leida));

        final MainDeviceAdapter mainDeviceAdapter = new MainDeviceAdapter(baseListItemList);
        mainDeviceList.setAdapter(mainDeviceAdapter);

        mainDeviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MainDeviceItem baseListItem = (MainDeviceItem) adapter.getItem(position);
                if (baseListItem == null)
                    return;
                mainDeviceAdapter.setCheckItem(position);

                presenter.requestItemsData(baseListItem.getItem(), 1);
            }
        });
    }

    @Override
    public String getBaseTitle() {
        return "Add by list";
    }

    @Override
    public void getOnRefreshInterface(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    @Override
    public void refreshComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
