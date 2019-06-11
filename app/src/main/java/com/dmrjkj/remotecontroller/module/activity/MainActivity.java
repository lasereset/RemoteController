package com.dmrjkj.remotecontroller.module.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dmrjkj.remotecontroller.AppApplication;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.adapter.MineDeviceAdapter;
import com.dmrjkj.remotecontroller.baseRecycleView.RecycleActivity;
import com.dmrjkj.remotecontroller.baseRecycleView.presenter.RecyclePresenter;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.cache.ShareprefrenceCache;
import com.dmrjkj.remotecontroller.entity.BaseListItem;
import com.dmrjkj.remotecontroller.entity.DeviceItem;
import com.dmrjkj.remotecontroller.events.DeviceEvenet;
import com.dmrjkj.remotecontroller.module.activity.contract.DeviceListContract;
import com.dmrjkj.remotecontroller.module.activity.presenter.MyDevicePresenter;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.OnClick;

public class MainActivity extends RecycleActivity implements DeviceListContract {

    private MyDevicePresenter presenter;

    @Override
    public RecyclePresenter getRecyclePresenter() {
        if (presenter == null)
            presenter = new MyDevicePresenter(this);
        return presenter;
    }

    @Override
    public String getToolbarTitle() {
        return AppApplication.getInstance().getString(R.string.my_device);
    }

    @Override
    protected void initAndBindConfig() {
        setNeesShowPullToRefresh(false);
        super.initAndBindConfig();
        ivBack.setVisibility(View.GONE);
        searchIvSearch.setVisibility(View.VISIBLE);
        searchIvSearch.setImageResource(R.mipmap.add);

        EventBus.getDefault().register(this);

        presenter.requestItemsData(1);
    }

    @OnClick({R.id.search_iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_iv_search:
                startActivity(new Intent(this, DeviceListActivity.class));
                break;
        }
    }

    @Override
    public void getOnRefreshInterface(SwipeRefreshLayout.OnRefreshListener listener) {
    }

    @Override
    public void refreshComplete() {
    }

    @Override
    public BaseQuickAdapter.OnItemClickListener getOnItemClickListener() {
        return new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseListItem baseListItem = (BaseListItem) adapter.getItem(position);
                if (baseListItem == null)
                    return;
                DeviceInfoActivity.intentActivity(MainActivity.this, baseListItem.getItem(), (String)baseListItem.getEntity(), position);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BleController.getInstance().disConnectAllDevice();
        BleController.getInstance().clear();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getClearBindMessage(DeviceEvenet deviceEvenet) {
        if (deviceEvenet == null)
            return;
        switch (deviceEvenet.getAction()) {
            case ADD:
                BaseListItem baseListItem = deviceEvenet.getBaseListItem();
                DeviceItem deviceItem = new DeviceItem(baseListItem.getItem(), baseListItem.getDescription(), baseListItem.getEntity());
                deviceItem.setShow_delete(true);
                deviceItem.setDescription("Add time：" + ToolUtils.formatTimeNormal(new Date()));
                presenter.getAdapter().getData().add(0, deviceItem);
                presenter.getAdapter().notifyDataSetChanged();
                break;
            case UPDATE:
                baseListItem = (BaseListItem) presenter.getAdapter().getData().get(deviceEvenet.getPosition());
                baseListItem.setItem(deviceEvenet.getBaseListItem().getItem());
                presenter.getAdapter().notifyDataSetChanged();
                break;
            case DELETE:
                deleteDeviceItem(deviceEvenet.getPosition());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MineDeviceAdapter)presenter.getAdapter()).startStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((MineDeviceAdapter)presenter.getAdapter()).stopStatus();
    }

    @Override
    public void onItemChidClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.delete_btn) {
            deleteDeviceItem(position);
        }
    }

    private void deleteDeviceItem(int position) {
        DeviceItem deviceItem = (DeviceItem) presenter.getAdapter().getItem(position);
        if (deviceItem == null)
            return;
        boolean success = ShareprefrenceCache.getInstance().removeDevice((String) deviceItem.getEntity());
        if (success) {
            presenter.getAdapter().remove(position);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onChangeBlueToolth(String deviceItem) {
        if (!ToolUtils.isEmpty(deviceItem)) {
            toolbarTitle2.setVisibility(View.VISIBLE);
            toolbarTitle2.setText(deviceItem);
        } else {
            toolbarTitle2.setVisibility(View.GONE);
        }
    }
}
