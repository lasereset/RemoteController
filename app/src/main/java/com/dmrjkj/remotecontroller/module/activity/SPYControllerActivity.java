package com.dmrjkj.remotecontroller.module.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.bluetooth.action.SPYAction;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;
import com.dmrjkj.remotecontroller.bluetooth.entity.SPY;
import com.dmrjkj.remotecontroller.bluetooth.parse.ParseCallBack;
import com.dmrjkj.remotecontroller.bluetooth.parse.ParseEngine;
import com.dmrjkj.remotecontroller.bluetooth.parse.ParseResult;
import com.dmrjkj.remotecontroller.bluetooth.parse.SPYParseCore;
import com.dmrjkj.remotecontroller.dialog.DialogUtils;
import com.dmrjkj.remotecontroller.dialog.SlopeDialog;
import com.dmrjkj.remotecontroller.events.DeviceSettingEvent;
import com.dmrjkj.remotecontroller.module.base.BaseActivity;
import com.dmrjkj.remotecontroller.utils.ToastUtils;
import com.dmrjkj.remotecontroller.utils.ToolUtils;
import com.dmrjkj.remotecontroller.weight.ScannerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.widget.QuickPopup;

/**
 * Created by xinchen on 19-4-16.
 */

@SuppressLint("LongLogTag")
public class SPYControllerActivity extends BaseActivity implements View.OnClickListener, SlopeDialog.OnResultListener, ParseCallBack {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_iv_search)
    ImageView searchIvSearch;
    @BindView(R.id.circularSeekBar)
    ScannerView circleSeekbar;
    @BindView(R.id.leveling_img)
    ImageView levelingImg;
    @BindView(R.id.calibration_img)
    TextView calibrationImg;
    @BindView(R.id.electric_img)
    ImageView electricImg;
    @BindView(R.id.sector_img)
    ImageView sectorImg;
    @BindView(R.id.manu_img)
    ImageView manuImg;
    @BindView(R.id.slope_text)
    TextView slopeText;
    @BindView(R.id.warn_img)
    ImageView warnImg;
    @BindView(R.id.tilt_img)
    TextView tiltImg;
    @BindView(R.id.slope_img)
    ImageView slopeImg;
    @BindView(R.id.speed_img)
    TextView speedImg;
    @BindView(R.id.speed_btn)
    ImageView speedBtnView;
    @BindView(R.id.angle_btn)
    ImageView angleBtnView;
    @BindView(R.id.bluetoolth_connect)
    ImageView bluetoolthConnect;
    @BindView(R.id.main)
    LinearLayout main;

    private ParseEngine parseEngine;

    private Vibrator mVibrator;//手机震动

    private boolean isLongClick = false;//是否正在长按
    private int onLongPressId;//当前长按的空间ID

    private QuickPopup speedPopup, anglePopu;
    private SlopeDialog slopeDialog;

    private Device device;
    private SPYAction action;

    private int current_speed, current_angle, current_slope;
    private boolean isHand = false;//是否是手动

    private static final String TAG = "SPYControllerActivity";

    public static void intentActivity(Activity activity, Device device) {
        Intent intent = new Intent(activity, SPYControllerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("device", device);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_device_remote;
    }

    @Override
    protected void initAndBindConfig() {
        super.initAndBindConfig();

        searchIvSearch.setVisibility(View.VISIBLE);
        searchIvSearch.setImageResource(R.mipmap.setting);
        //获取Vibrator震动服务
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        EventBus.getDefault().register(this);

        initData();
    }

    private void initData() {
        device = (Device) getIntent().getExtras().get("device");
        action = new SPYAction(device);
        toolbarTitle.setText(device.getName());

        this.parseEngine = new ParseEngine(new SPYParseCore(), SPYControllerActivity.this);
        checkConnectDeviceStatus();
    }

    private void showSpeedPopupView() {
        if (speedPopup == null) {
            speedPopup = QuickPopupBuilder.with(this).contentView(R.layout.popup_speed)
                    .config(new QuickPopupConfig()
                            .backgroundColor(0)
                            .gravity(Gravity.CENTER | Gravity.TOP)
                            .withClick(R.id.speed_0, this, true)
                            .withClick(R.id.speed_150, this, true)
                            .withClick(R.id.speed_300, this, true)
                            .withClick(R.id.speed_600, this, true)).show(speedBtnView);
        } else {
            speedPopup.showPopupWindow(speedBtnView);
        }
    }

    private void showAnglePopupView() {
        if (anglePopu == null) {
            anglePopu = QuickPopupBuilder.with(this).contentView(R.layout.popup_angle)
                    .config(new QuickPopupConfig()
                            .gravity(Gravity.CENTER | Gravity.TOP)
                            .backgroundColor(0)
                            .withClick(R.id.angle_360, this, true)
                            .withClick(R.id.angle_15, this, true)
                            .withClick(R.id.angle_30, this, true)
                            .withClick(R.id.angle_60, this, true)).show(angleBtnView);
        } else {
            anglePopu.showPopupWindow(angleBtnView);
        }
    }

    private void showSlopePopupView() {
        if (!isHand) {
            //首先打开手动模式
            this.action.actionHand();
        }
        if (slopeDialog == null) {
            slopeDialog = new SlopeDialog(this, mVibrator, action, this);
        }
        slopeDialog.show(current_slope);
    }

    @OnClick({R.id.iv_back, R.id.search_iv_search, R.id.manu_btn, R.id.tilt_btn, R.id.slope_btn, R.id.speed_btn, R.id.angle_btn, R.id.change_left, R.id.change_right})
    public void onViewClicked(View view) {
        if (!BleManager.getInstance().isConnected(device.getBleDevice())) {
            return;
        }
        if (view.getId() != R.id.iv_back && view.getId() != R.id.search_iv_search) {
            mVibrator.vibrate(100);
        }
        switch (view.getId()) {
            case R.id.speed_btn:
                if (current_angle != 0) {
                    return;
                }
                showSpeedPopupView();
                break;
            case R.id.angle_btn:
                showAnglePopupView();
                break;
            case R.id.change_left:
                circleSeekbar.changeCurAngle(true, 0.5f);
                this.action.actionLeft();
                circleSeekbar.stopChangeCurAngle();
                break;
            case R.id.change_right:
                circleSeekbar.changeCurAngle(false, 0.5f);
                this.action.actionRight();
                circleSeekbar.stopChangeCurAngle();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.search_iv_search:
                SettingActivity.intentActivity(this, device.getName(), device.getAddress());
                break;
            case R.id.manu_btn:
                this.action.actionHand();
                break;
            case R.id.tilt_btn:
                this.action.actionTilt();
                break;
            case R.id.slope_btn:
                showSlopePopupView();
                break;
        }
    }

    private void showImageView(boolean isShow, ImageView imageView, int resId) {
        if (!isShow) {
            imageView.setVisibility(View.INVISIBLE);
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(resId);
    }

    private void showTextView(boolean isShow, TextView textView, String text) {
        if (!isShow) {
            textView.setVisibility(View.INVISIBLE);
            return;
        }
        textView.setVisibility(View.VISIBLE);
        if (!ToolUtils.isEmpty(text)) {
            textView.setText(text);
        }
    }

    private MaterialDialog loseConnectDialog;
    private void loseConnectDialog() {
        if (loseConnectDialog == null) {
            loseConnectDialog = DialogUtils.showWarnConfirmDialog(this, "Connection has been disconnected. " +
                    "Please keep the cell phone close to the device and make sure the device is Bluetooth on.\n", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    reConnect();
                }
            }).getBuilder().neutralText("Exit").onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    onBackPressed();
                }
            }).build();
        }
        this.circleSeekbar.stop();
        loseConnectDialog.show();
    }

    /**
     * 多次调用达到指定的动作
     */
    private void actionUpOrDownMutiple(final int i, final int times, final boolean isUp) {
        if (i >= times) {
            return;
        }
        BleWriteCallback callback = new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                actionUpOrDownMutiple(i + 1, times, isUp);
            }

            @Override
            public void onWriteFailure(BleException exception) {
            }
        };
        if (isUp) {
            this.action.actionUp(callback);
        } else {
            this.action.actionDown(callback);
        }
    }

    @Override
    public void onClick(View view) {
        mVibrator.vibrate(100);
        int speed = -1;
        switch (view.getId()) {
            case R.id.speed_0:
                speed = 0;
                break;
            case R.id.speed_150:
                speed = 1;
                break;
            case R.id.speed_300:
                speed = 2;
                break;
            case R.id.speed_600:
                speed = 3;
                break;
        }
        if (speed != -1) {
            if (current_angle != 0) {//当前是扇扫
                return;
            }
            if (speed == 0) {
                circleSeekbar.stop();
            }
            int times = Math.abs(current_speed - speed);
            actionUpOrDownMutiple(0, times, current_speed < speed);
            return;
        }
        int angle = -1, angleId = -1;
        switch (view.getId()) {
            case R.id.angle_360:
                angle = 360;
                angleId = 0;
                break;
            case R.id.angle_15:
                angle = 10;
                angleId = 1;
                break;
            case R.id.angle_30:
                angle = 30;
                angleId = 2;
                break;
            case R.id.angle_60:
                angle = 60;
                angleId = 3;
                break;
        }
        if (angle != -1) {
            if (current_angle == angleId) {
                return;
            }
            changeScanAction(angleId);
        }
    }

    /**
     * 切换扇扫角度
     * @param angleId
     */
    private void changeScanAction(int angleId) {
        final int times = Math.abs(current_angle - angleId);
        final boolean isUp = current_angle < angleId;
        if (angleId == 0) {
            this.action.actionSpeed();
        } else if (current_angle == 0) {
            this.action.actionScan(new BleWriteCallback() {
                @Override
                public void onWriteSuccess(int current, int total, byte[] justWrite) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actionUpOrDownMutiple(0, times, isUp);
                }

                @Override
                public void onWriteFailure(BleException exception) {
                }
            });
        } else {
            actionUpOrDownMutiple(0, times, isUp);
        }
    }

    private void longClickThread(final int viewId, final boolean isReduce) {
        if (current_angle == 0 && current_speed != 0) {
            return;
        }
        if (isLongClick && viewId == onLongPressId) {
            Message message = new Message();
            message.obj = isReduce;
            message.what = 0;
            mVibrator.vibrate(10);
            mHandler.sendMessageDelayed(message, 50);
        } else {
            circleSeekbar.stopChangeCurAngle();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    boolean isReduce = (boolean) msg.obj;
                    circleSeekbar.changeCurAngle(isReduce, 1);
                    longClickThread(onLongPressId, isReduce);
                    break;
            }
        }
    };

    private void updateCircleSeekbar(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS || event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_HOVER_ENTER || event.getAction() == MotionEvent.ACTION_MASK
                || event.getAction() == MotionEvent.ACTION_POINTER_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
        } else {
            if (isLongClick) {
                isLongClick = false;
                if (onLongPressId == R.id.change_left) {
                    this.action.actionLeftEnd();
                } else if (onLongPressId == R.id.change_right) {
                    this.action.actionRightEnd();
                }
                circleSeekbar.start();
            }
        }
    }

    @OnLongClick({R.id.change_left, R.id.change_right})
    public boolean onItemLongClick(View view) {
        switch (view.getId()) {
            case R.id.change_left:
                isLongClick = true;
                circleSeekbar.stop();
                this.action.actionLeftStart();
                longClickThread(view.getId(), true);
                break;
            case R.id.change_right:
                isLongClick = true;
                circleSeekbar.stop();
                this.action.actionRightStart();
                longClickThread(view.getId(), false);
                break;
        }
        return true;
    }

    @OnTouch({R.id.change_left, R.id.change_right})
    public boolean onTouchView(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.change_left:
                this.onLongPressId = view.getId();
                updateCircleSeekbar(event);
                break;
            case R.id.change_right:
                this.onLongPressId = view.getId();
                updateCircleSeekbar(event);
                break;
        }
        return false;
    }


    @Override
    public void onResult(String xZ, String y) {
        //恢复成坡度关闭
        int times = current_slope == 1 ? 2 : 1;
        SlopeDialog.actionSlopeMutiple(action, 0, times);
        this.current_slope = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        deviceNotify();
        startReportStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.action.actionRightEnd();
        this.device.removeNotify();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        if (connectTimer != null) {
            connectTimer.purge();
            connectTimer.cancel();
            connectTimer = null;
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 开始
     */
    private void startReportStatus() {
        this.main.postDelayed(new Runnable() {
            @Override
            public void run() {
                SPYControllerActivity.this.action.actionReportStart();
            }
        }, 300L);
    }

    /**
     * 监听蓝牙
     */
    private void deviceNotify() {
        this.device._notify(new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
            }

            @Override
            public void onNotifyFailure(BleException exception) {
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                parseEngine.execParse(data);
            }
        });
    }

    @Override
    public void onResult(ParseResult paramParseResult) {
        if (paramParseResult.getFrameObj() instanceof SPY) {
            updateView((SPY) paramParseResult.getFrameObj());
        }
    }

    /**
     * 一段时间检测一次并自动重连
     */
    private Timer connectTimer;
    private void checkConnectDeviceStatus() {
        connectTimer = new Timer();
        connectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int status = BleController.getInstance().getConnectStatus(device.getBleDevice());
                if (status != 1 && status != 2) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bluetoolthConnect.setVisibility(View.INVISIBLE);
                            loseConnectDialog();
                        }
                    });
                }
            }
        }, 300L, 5000L);
    }

    /**
     * 重新连接
     */
    private void reConnect() {
        BleController.getInstance().scanDevieList(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
            }

            @Override
            public void onScanStarted(boolean success) {
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                if (bleDevice.getMac().equals(device.getAddress())) {
                    BleController.getInstance().cancelScan();
                    BleController.getInstance().connectDevice(bleDevice, new BleGattCallback() {
                        @Override
                        public void onStartConnect() {
                        }

                        @Override
                        public void onConnectFail(BleDevice bleDevice, BleException exception) {
                        }

                        @Override
                        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                            bluetoolthConnect.setVisibility(View.VISIBLE);
                            SPYControllerActivity.this.device.setBleDevice(bleDevice);
                            deviceNotify();
                            startReportStatus();
                            //todo 是否需要输入密码
                        }

                        @Override
                        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                        }
                    });
                }
            }
        });
    }

    /**
     * 报警提示
     */
    private long current_tip_time = System.currentTimeMillis();
    private void warningTip(SPY spy) {
        if (System.currentTimeMillis() - current_tip_time <= 2000) {
            return;
        }
        current_tip_time = System.currentTimeMillis();
        boolean isNeedTip = false;
        if (spy.isLeveling()) {
            isNeedTip= true;
            showImageView(false, levelingImg, R.mipmap.ic_leveling);
        }
        switch (spy.getWarn()) {
            case 1:
                showImageView(false, warnImg, R.mipmap.ic_warn);
                break;
            case 2:
                isNeedTip= true;
                showTextView(false, tiltImg, null);//TITL状态
                break;
        }
        if (isNeedTip) {
            ToastUtils.playAuditory(R.raw.lealing);
            circleSeekbar.stop();
        }
    }

    /**
     * 根据设备蓝牙通知更新页面状态
     */
    private long last_update_time = 0;
    private void updateView(SPY spy) {
        if (System.currentTimeMillis() - last_update_time < 700) {
            return;
        }
        this.last_update_time = System.currentTimeMillis();
        int betteryStatus = spy.getBatteryStatus();//充电器状态
        showTextView(spy.isTitl(), tiltImg, null);//TITL状态
        //是否正在整平状态
        showImageView(spy.isLeveling(), levelingImg, R.mipmap.ic_leveling);
        //报警
        switch (spy.getWarn()) {
            case 1:
                showImageView(true, warnImg, R.mipmap.ic_warn);
                break;
            case 2:
                showTextView(true, tiltImg, null);//TITL状态
                break;
            default:
                showImageView(false, warnImg, R.mipmap.ic_warn);
                break;
        }
        //手动还是自动
        this.isHand = spy.isHand();
        showImageView(spy.isHand(), manuImg, R.mipmap.ic_manu);
        //电池电量
        switch (spy.getElectricity()) {
            case 3:
                showImageView(true, electricImg, R.mipmap.battery_100);
                break;
            case 2:
                showImageView(true, electricImg, R.mipmap.battery_66);
                break;
            case 1:
                showImageView(true, electricImg, R.mipmap.battery_20);
                break;
            default:
                showImageView(true, electricImg, R.mipmap.battery_10);
                break;
        }
        //速度
        current_speed = spy.getSpeed();
        circleSeekbar.updateSpeed(current_speed);
        switch (current_speed) {
            case 3:
                showTextView(true, speedImg, "600");
                break;
            case 2:
                showTextView(true, speedImg, "300");
                break;
            case 1:
                showTextView(true, speedImg, "150");
                break;
            default:
                showTextView(true, speedImg, "0");
                break;
        }

        //扇扫
        current_angle = spy.getScan();
        float angle;
        switch (current_angle) {
            case 3:
                angle = 60;
                showTextView(true, speedImg, "60°");
                showImageView(true, sectorImg, R.mipmap.ic_scan);
                break;
            case 2:
                angle = 30;
                showTextView(true, speedImg, "30°");
                showImageView(true, sectorImg, R.mipmap.ic_scan);
                break;
            case 1:
                angle = 15;
                showTextView(true, speedImg, "15°");
                showImageView(true, sectorImg, R.mipmap.ic_scan);
                break;
            default:
                angle = 360;
                showImageView(true, sectorImg, R.mipmap.ic_speed);
                break;
        }
        circleSeekbar.updateAngle(angle);

        //是否需要转动起来
        if (current_speed != 0 && !circleSeekbar.isScolling()) {//需要转动起来
            circleSeekbar.start();
        }

        //是否xyz
        showImageView(spy.getSlope() != 0, slopeImg, R.mipmap.ic_slope);
        current_slope = spy.getSlope();
        switch (current_slope) {
            default:
                showTextView(false, slopeText, null);
                break;
            case 1:
                showTextView(true, slopeText, "X/Z");
                break;
            case 2:
                showTextView(true, slopeText, "Y");
                break;
            case 3:
                showTextView(true, slopeText, "Z");
                break;
        }
        //校准
        switch (spy.getCal()) {
            case 3:
                showTextView(true, calibrationImg, "CAL.Z");
                break;
            case 2:
                showTextView(true, calibrationImg, "CAL.Y");
                break;
            case 1:
                showTextView(true, calibrationImg, "CAL.X");
                break;
            default:
                showTextView(false, calibrationImg, null);
                break;
        }

        //报警提醒
        warningTip(spy);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deviceSettingEvent(DeviceSettingEvent event) {
        if (event.isChangeName()) {
            device.setName(event.getName());
            toolbarTitle.setText(event.getName());
        } else if (event.isChangeTouchModel()) {

        }
    }

}
