package com.dmrjkj.remotecontroller.adapter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.data.BleDevice;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.entity.DeviceItem;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MineDeviceAdapter extends BaseQuickAdapter<DeviceItem, BaseViewHolder> {

    private Timer timer;
    private OnChangeBlueToothCallback callback;

    public static interface OnChangeBlueToothCallback {
        void changeBlueTooth(String title);
    }

    public MineDeviceAdapter(OnChangeBlueToothCallback callback) {
        super(R.layout.item_mine_device);
        this.callback = callback;
    }

    public MineDeviceAdapter(List<DeviceItem> data) {
        super(R.layout.item_mine_device, data);
        openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    @Override
    protected void convert(final BaseViewHolder helper, DeviceItem item) {
        helper.setText(R.id.base_title, item.getItem());
        if (ToolUtils.isEmpty(item.getDescription())) {
            helper.getView(R.id.base_description).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.base_description).setVisibility(View.VISIBLE);
            helper.setText(R.id.base_description, item.getDescription());
        }
        if (item.getItem().length() >= 3) {
            helper.setText(R.id.base_tag, item.getItem().substring(0, 3));
        } else {
            helper.setText(R.id.base_tag, "无");
        }
        helper.setVisible(R.id.delete_btn, item.isShow_delete())
                .setImageResource(R.id.bluetoolth, item.getEntity() != null && BleController.getInstance().isConnect((String) item.getEntity()) ? R.mipmap.icon_bluetooth_on : R.mipmap.icon_bluetooth_off)
                .addOnClickListener(R.id.delete_btn)
                .addOnClickListener(R.id.content);
    }

    public void startStatus() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    MineDeviceAdapter.this.mHandler.sendMessage(Message.obtain());
                }
            }, 0L, 3000L);
        }
    }

    public void stopStatus() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String title = null;
            for (DeviceItem item : getData()) {
                if (item.getEntity() != null && BleController.getInstance().isConnect((String) item.getEntity())) {
                    title = item.getItem() + " connecting...";
                }
            }
            callback.changeBlueTooth(title);
            MineDeviceAdapter.this.notifyDataSetChanged();
        }
    };
}
