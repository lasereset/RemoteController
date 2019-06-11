package com.dmrjkj.remotecontroller.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.utils.ChartUtils;

/**
 * Created by xinchen on 19-5-6.
 */

public class SlopeView extends View{

    private Paint linePaint;

    private Bitmap bitmap;

    private float centreX, centreY, radius;

    private RectF mThumbRect = new RectF();

    private float base_height;
    private float base_angle;

    public SlopeView(Context context) {
        super(context);
        init(context, null);
    }

    public SlopeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SlopeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        Drawable mThumb = getResources().getDrawable(R.mipmap.base_model);

        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlopeView);
            Drawable drawable = typedArray.getDrawable(R.styleable.SlopeView_slope_background);
            if (drawable !=  null) {
                mThumb = drawable;
            }
            typedArray.recycle();
        }

        if (mThumb != null && mThumb instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) mThumb).getBitmap();
        }

        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float centre_y = centreY - base_height;

        canvas.drawBitmap(bitmap, null, mThumbRect, linePaint);

        PointF pointF = ChartUtils.calcArcEndPointXY(centreX, centre_y, radius, 0, -90);
        canvas.drawLine(centreX, centre_y, pointF.x, pointF.y, linePaint);

        pointF = ChartUtils.calcArcEndPointXY(centreX, centre_y, radius, 0, base_angle);
        canvas.drawLine(centreX, centre_y, pointF.x, pointF.y, linePaint);

        pointF = ChartUtils.calcArcEndPointXY(centreX, centre_y, radius, 0, 180 + base_angle);
        canvas.drawLine(centreX, centre_y, pointF.x, pointF.y, linePaint);

//        changeNumListener.onChange(base_angle, base_height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int mWidth = Math.min(width, height);
        centreX = mWidth / 2;
        centreY = mWidth / 2;

        int padding = 5;
        int maxWidth = 6;
        int diameter = mWidth - getPaddingLeft() - getPaddingRight() - maxWidth - padding * 2;
        radius = 100;
        mThumbRect.set(width / 4, height / 4 + 80, 3 * width / 4, 3 * height / 4 + 80);

        setMeasuredDimension(mWidth, mWidth);
    }

    /**
     * 设置x/z角度变化
     */
    public boolean changeXpZ(boolean isReduce, float changeNum) {
        if ((base_angle <= -90 && isReduce)) {
            base_angle = -90;
            return false;
        }
        if ((base_angle >= 90 && !isReduce)) {
            base_angle = 90;
            return false;
        }
        base_angle = isReduce ? base_angle - changeNum : base_angle + changeNum;
        invalidate();
        return true;
    }

    /**
     * 设置Y变化
     */
    public boolean changeY(boolean isReduce, float changeNum) {
        if ((base_height <= -90 && isReduce)) {
            base_height = -90;
            return false;
        }
        if ((base_height >= 90 && !isReduce)) {
            base_height = 90;
            return false;
        }
        base_height = isReduce ? base_height - changeNum : base_height + changeNum;
        invalidate();
        return true;
    }

    public float getBase_height () {
        return base_height;
    }

    public float getBase_angle() {
        return base_angle;
    }

    private OnChangeNumListener changeNumListener;

    public void setChangeNumListener(OnChangeNumListener changeNumListener) {
        this.changeNumListener = changeNumListener;
    }

    public interface OnChangeNumListener {
        void onChange(float xZ, float y);
    }
}
