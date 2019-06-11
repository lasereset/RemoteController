package com.dmrjkj.remotecontroller.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.bluetooth.action.SPYAction;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

/**
 * Created by xinchen on 19-4-24.
 */

public class SlopeDialog extends Dialog implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private OnResultListener onResultListener;

    private boolean isLongClick;
    private long onLongPressId;

    private SPYAction action;
    private Vibrator mVibrator;//手机震动
    private int current_slope;

    private CheckBox x_z_checkbox,y_checkbox;

    private static final int X_Z_SLOPE_CHANGE_CODE = 2;

    public SlopeDialog(Activity activity, Vibrator mVibrator, SPYAction action, OnResultListener onResultListene) {
        super(activity);
        this.mVibrator = mVibrator;
        this.action = action;
        this.onResultListener = onResultListene;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.popup_slope);

        initUI();

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private void initUI() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        getWindow().setWindowAnimations(-1);


        TextView x_z_reduce_btn = (TextView) findViewById(R.id.x_z_reduce_btn);
        TextView x_z_add_btn = (TextView) findViewById(R.id.x_z_add_btn);
        TextView y_reduce_btn = (TextView) findViewById(R.id.y_reduce_btn);
        TextView y_add_btn = (TextView) findViewById(R.id.y_add_btn);
        Button exitBtn = (Button) findViewById(R.id.exit_btn);
        x_z_checkbox = (CheckBox) findViewById(R.id.xz_checkBox);
        y_checkbox = (CheckBox) findViewById(R.id.y_checkBox);

        exitBtn.setOnClickListener(this);

        x_z_reduce_btn.setOnClickListener(this);
        x_z_add_btn.setOnClickListener(this);
        x_z_reduce_btn.setOnLongClickListener(this);
        x_z_add_btn.setOnLongClickListener(this);
        x_z_reduce_btn.setOnTouchListener(this);
        x_z_add_btn.setOnTouchListener(this);
        x_z_checkbox.setOnClickListener(this);

        y_reduce_btn.setOnClickListener(this);
        y_add_btn.setOnClickListener(this);
        y_reduce_btn.setOnLongClickListener(this);
        y_add_btn.setOnLongClickListener(this);
        y_reduce_btn.setOnTouchListener(this);
        y_add_btn.setOnTouchListener(this);
        y_checkbox.setOnClickListener(this);
    }

    public static SlopeDialog open(Activity activity, Vibrator mVibrator, SPYAction action, OnResultListener onResultListene) {
        SlopeDialog dialog = new SlopeDialog(activity, mVibrator, action, onResultListene);
        dialog.show();
        return dialog;
    }

    public void show(int current_slope) {
        this.current_slope = current_slope;
        if (current_slope == 0) {
            //如果时坡度关闭，进入自动开始X/Z的坡度
            mHandler.sendEmptyMessageDelayed(X_Z_SLOPE_CHANGE_CODE, 500);
        } else {
            x_z_checkbox.setChecked(current_slope == 1);
            y_checkbox.setChecked(current_slope == 2);
        }
        show();
    }

    @Override
    public void onClick(View view) {
        mVibrator.vibrate(50);
        switch (view.getId()) {
            case R.id.x_z_add_btn:
                if (current_slope != 1) {
                    return;
                }
                this.action.actionRight();
                break;
            case R.id.x_z_reduce_btn:
                if (current_slope != 1) {
                    return;
                }
                this.action.actionLeft();
                break;
            case R.id.y_add_btn:
                if (current_slope != 2) {
                    return;
                }
                this.action.actionRight();
                break;
            case R.id.y_reduce_btn:
                if (current_slope != 2) {
                    return;
                }
                this.action.actionLeft();
                break;
            case R.id.exit_btn:
                onResultListener.onResult(ToolUtils.getFormatValueNum(0), ToolUtils.getFormatValueNum(0));
                dismiss();
                break;
            case R.id.xz_checkBox:
                x_z_checkbox.setChecked(!x_z_checkbox.isChecked());
                y_checkbox.setChecked(!y_checkbox.isChecked());
                changeSlope();
                break;
            case R.id.y_checkBox:
                y_checkbox.setChecked(!y_checkbox.isChecked());
                x_z_checkbox.setChecked(!x_z_checkbox.isChecked());
                changeSlope();
                break;
        }
    }

    private void changeSlope() {
        if (x_z_checkbox.isChecked()) {
            // current_slope > 1
            int times = 0;
            switch (current_slope) {
                case 0:
                    times = 1;
                    break;
                case 1:
                    times = 0;
                    break;
                case 2:
                    times = 2;
                    break;
            }
            actionSlopeMutiple(action, 0, times);
            SlopeDialog.this.current_slope = 1;
        } else {
            // current_slope > 2
            int times = 0;
            switch (current_slope) {
                case 0:
                    times = 2;
                    break;
                case 1:
                    times = 1;
                    break;
                case 2:
                    times = 0;
                    break;
            }
            actionSlopeMutiple(action, 0, times);
            SlopeDialog.this.current_slope = 2;
        }
    }

    /**
     * 多次调用达到指定的动作
     */
    public static void actionSlopeMutiple(final SPYAction action, final int i, final int times) {
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
                actionSlopeMutiple(action, i + 1, times);
            }

            @Override
            public void onWriteFailure(BleException exception) {
            }
        };
        action.actionSlope(callback);
    }

    @Override
    public boolean onLongClick(View view) {
        int vieId = view.getId();
        if ((vieId == R.id.x_z_add_btn || vieId == R.id.x_z_reduce_btn) && current_slope != 1) {
            return true;
        }
        if ((vieId == R.id.y_add_btn || vieId == R.id.y_reduce_btn) && current_slope != 2) {
            return true;
        }
        this.isLongClick = true;
        switch (view.getId()) {
            case R.id.x_z_add_btn:
                this.action.actionRightStart();
                longClickThread(view.getId(), false, 0);
                break;
            case R.id.x_z_reduce_btn:
                this.action.actionLeftStart();
                longClickThread(view.getId(), true, 0);
                break;
            case R.id.y_add_btn:
                this.action.actionRightStart();
                longClickThread(view.getId(), false, 1);
                break;
            case R.id.y_reduce_btn:
                this.action.actionLeftStart();
                longClickThread(view.getId(), true, 1);
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.onLongPressId = view.getId();
        updateSlopeViewTouch(view, motionEvent);
        return false;
    }

    private void updateSlopeViewTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS || event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_HOVER_ENTER || event.getAction() == MotionEvent.ACTION_MASK
                || event.getAction() == MotionEvent.ACTION_POINTER_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
        } else {
            if (isLongClick) {
                switch (view.getId()) {
                    case R.id.x_z_add_btn:
                        this.action.actionRightEnd();
                        break;
                    case R.id.x_z_reduce_btn:
                        this.action.actionLeftEnd();
                        break;
                    case R.id.y_add_btn:
                        this.action.actionRightEnd();
                        break;
                    case R.id.y_reduce_btn:
                        this.action.actionLeftEnd();
                        break;
                }
            }
            isLongClick = false;
        }
    }

    private void longClickThread(final long viewId, final boolean isReduce, final int what) {
        if (isLongClick && viewId == onLongPressId) {
            Message message = new Message();
            message.obj = isReduce;
            message.what = what;
            mVibrator.vibrate(10);
            mHandler.sendMessageDelayed(message, 50);
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    boolean isReduce = (boolean) msg.obj;
                    longClickThread(onLongPressId, isReduce, 0);
                    break;
                case 1:
                    isReduce = (boolean) msg.obj;
                    longClickThread(onLongPressId, isReduce, 1);
                    break;
                case X_Z_SLOPE_CHANGE_CODE:
                    SlopeDialog.this.action.actionSlope();
                    x_z_checkbox.setChecked(true);
                    SlopeDialog.this.current_slope = 1;
                    break;
            }
        }
    };

    public interface OnResultListener {
        void onResult(String xZ, String y);
    }
}
