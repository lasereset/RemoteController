package com.dmrjkj.remotecontroller.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.weight.SlopeView;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by xinchen on 19-5-6.
 */

public class SlopePopup extends BasePopupWindow implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener, SlopeView.OnChangeNumListener {
    private boolean isLongClick;
    private long onLongPressId;

    private ImageView slopeView_X, slopeView_y;

    private Vibrator mVibrator;//手机震动

    public SlopePopup(Context context, Vibrator mVibrator, OnResultListener onResultListener) {
        super(context);

        this.mVibrator = mVibrator;

        TextView x_z_reduce_btn = findViewById(R.id.x_z_reduce_btn);
        TextView x_z_add_btn = findViewById(R.id.x_z_add_btn);
        TextView y_reduce_btn = findViewById(R.id.y_reduce_btn);
        TextView y_add_btn = findViewById(R.id.y_add_btn);
        Button exitBtn = findViewById(R.id.exit_btn);

        slopeView_X = findViewById(R.id.slope_view_x);
        slopeView_y = findViewById(R.id.slope_view_y);

        setPopupGravity(Gravity.CENTER);
        setClipChildren(false);
        setBackgroundColor(0);
        setAllowDismissWhenTouchOutside(false);
        setBackPressEnable(false);

        exitBtn.setOnClickListener(this);

        x_z_reduce_btn.setOnClickListener(this);
        x_z_add_btn.setOnClickListener(this);
        x_z_reduce_btn.setOnLongClickListener(this);
        x_z_add_btn.setOnLongClickListener(this);
        x_z_reduce_btn.setOnTouchListener(this);
        x_z_add_btn.setOnTouchListener(this);

        y_reduce_btn.setOnClickListener(this);
        y_add_btn.setOnClickListener(this);
        y_reduce_btn.setOnLongClickListener(this);
        y_add_btn.setOnLongClickListener(this);
        y_reduce_btn.setOnTouchListener(this);
        y_add_btn.setOnTouchListener(this);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_slope);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return null;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return null;
    }

    @Override
    public Animator onCreateShowAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(getDisplayAnimateView(), "rotationX", 90f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(getDisplayAnimateView(), "translationY", 250f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(getDisplayAnimateView(), "alpha", 0f, 1f).setDuration(400 * 3 / 2));
        return set;
    }

    @Override
    public Animator onCreateDismissAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(getDisplayAnimateView(), "rotationX", 0f, 90f).setDuration(400),
                ObjectAnimator.ofFloat(getDisplayAnimateView(), "translationY", 0f, 250f).setDuration(400),
                ObjectAnimator.ofFloat(getDisplayAnimateView(), "alpha", 1f, 0f).setDuration(400 * 3 / 2));
        return set;
    }

    @Override
    public void onClick(View view) {
        mVibrator.vibrate(50);
        switch (view.getId()) {
            case R.id.x_z_add_btn:
                break;
            case R.id.x_z_reduce_btn:
                break;
            case R.id.y_add_btn:
                break;
            case R.id.y_reduce_btn:
                break;
            case R.id.exit_btn:
                dismissWithOutAnimate();
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.x_z_add_btn:
                longClickThread(view.getId(), false, 0);
                break;
            case R.id.x_z_reduce_btn:
                longClickThread(view.getId(), true, 0);
                break;
            case R.id.y_add_btn:
                longClickThread(view.getId(), false, 1);
                break;
            case R.id.y_reduce_btn:
                longClickThread(view.getId(), true, 1);
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.onLongPressId = view.getId();
        updateSlopeViewTouch(motionEvent);
        return false;
    }

    private void updateSlopeViewTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS || event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_HOVER_ENTER || event.getAction() == MotionEvent.ACTION_MASK
                || event.getAction() == MotionEvent.ACTION_POINTER_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            isLongClick = true;
        } else {
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
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    public void onChange(float xZ, float y) {
//        x_z_num.setText(ToolUtils.getFormatValueNum(xZ) + "°");
//        y_num.setText(ToolUtils.getFormatValueNum(y) + "°");
    }

    public interface OnResultListener {
        void onResult(String xZ, String y);
    }
}
