package com.dmrjkj.remotecontroller.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.dmrjkj.remotecontroller.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by xinchen on 19-5-6.
 */

public class ChangePosPopup extends BasePopupWindow implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
    private OnClickListener onClickListener;

    public ChangePosPopup(Context context, OnClickListener onClickListener) {
        super(context);

        this.onClickListener = onClickListener;

        ImageView leftView = findViewById(R.id.left_btn);
        ImageView rightView = findViewById(R.id.right_btn);

        setPopupGravity(Gravity.CENTER);
        setClipChildren(false);
        setBackgroundColor(0);

        leftView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        leftView.setOnLongClickListener(this);
        rightView.setOnLongClickListener(this);
        leftView.setOnTouchListener(this);
        rightView.setOnTouchListener(this);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_change_pos);
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
        return null;
    }

    @Override
    public Animator onCreateDismissAnimator() {
        return null;
    }

    @Override
    public void onClick(View view) {
        onClickListener.onItemClick(view);
    }

    @Override
    public boolean onLongClick(View view) {
        return onClickListener.onItemLongClick(view);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return onClickListener.onTouchView(view, motionEvent);
    }

    public interface OnClickListener {
        void onItemClick(View view);
        boolean onItemLongClick(View view);
        boolean onTouchView(View view, MotionEvent motionEvent);
    }
}
