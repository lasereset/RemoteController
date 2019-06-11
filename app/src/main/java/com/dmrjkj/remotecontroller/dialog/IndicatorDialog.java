package com.dmrjkj.remotecontroller.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dmrjkj.remotecontroller.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action2;

/**
 * Created by xinchen on 19-4-24.
 */

public class IndicatorDialog extends Dialog implements OnSeekChangeListener {

    @BindView(R.id.xz_indicator_seekbar)
    IndicatorSeekBar xzIndicatorSeekbar;
    @BindView(R.id.y_indicator_seekbar)
    IndicatorSeekBar yIndicatorSeekbar;
    @BindView(R.id.cancle_btn)
    Button cancleBtn;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @BindView(R.id.x_angle)
    TextView xAngle;
    @BindView(R.id.y_angle)
    TextView yAngle;

    private Activity activity;
    private int x_z_angle, y_angle;
    private Action2<Integer, Integer> callback;

    public IndicatorDialog(Activity activity, int x_z_angle, int y_angle, Action2<Integer, Integer> callback) {
        super(activity);
        this.activity = activity;
        this.x_z_angle = x_z_angle;
        this.y_angle = y_angle;
        this.callback = callback;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_indicator);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        getWindow().setWindowAnimations(-1);

        refreshTextView();

        xzIndicatorSeekbar.setOnSeekChangeListener(this);
        yIndicatorSeekbar.setOnSeekChangeListener(this);
    }

    private void refreshTextView() {
        xAngle.setText("X/Z: " + x_z_angle + "°");
        yAngle.setText("Y: " + y_angle + "°");
    }

    @OnClick({R.id.cancle_btn, R.id.confirm_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancle_btn:
                cancel();
                break;
            case R.id.confirm_btn:
                callback.call(x_z_angle, y_angle);
                cancel();
                break;
        }
    }

    @Override
    public void onSeeking(SeekParams seekParams) {
        switch (seekParams.seekBar.getId()) {
            case R.id.xz_indicator_seekbar:
                x_z_angle = seekParams.progress;
                break;
            case R.id.y_indicator_seekbar:
                y_angle = seekParams.progress;
                break;
        }
        refreshTextView();
    }

    @Override
    public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
    }

    public static IndicatorDialog open(Activity activity, int x_z_angle, int y_angle, Action2<Integer, Integer> callback) {
        IndicatorDialog dialog = new IndicatorDialog(activity, x_z_angle, y_angle, callback);
        dialog.show();
        return dialog;
    }
}
