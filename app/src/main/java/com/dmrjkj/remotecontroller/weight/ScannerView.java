package com.dmrjkj.remotecontroller.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dmrjkj.remotecontroller.R;

/**
 * Created by xinchen on 19-5-6.
 */

public class ScannerView extends View {
    private static final String TAG = "ScannerView";

    private Paint ovalPaint;
    private Paint pointPaint;
    private Paint linePaint;

    private int radius;
    private float mCurAngle = 0;//起始点的角度
    private int centreX;
    private int centreY;

    private boolean isScolling = false;

    private Bitmap bitmap;

    private RectF mOvalRect = new RectF();
    private RectF mThumbRect = new RectF();
    private float mRotate;

    private final int[] speeds = new int[]{0, 44, 22, 11};

    private float baseAngle = 360;//转动角度
    private long mSpeed = speeds[1];//转动速度

    private boolean isFinishOval = false;
    private boolean isMovePoint = false;

    private Matrix mMatrix = new Matrix();
    private int mScanShaderColor = Color.parseColor("#F45736");
    private Paint mSweepPaint; //扫描效果的画笔

    public ScannerView(Context context) {
        super(context);
        init(context, null);
    }

    public ScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        int circle_color = getResources().getColor(R.color.circleseekbar_gray);
        Drawable mThumb = getResources().getDrawable(R.mipmap.model_entry);

        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScannerView);
            Drawable drawable = typedArray.getDrawable(R.styleable.ScannerView_scanner_background);
            if (drawable != null) {
                mThumb = drawable;
            }
            typedArray.recycle();
        }

        if (mThumb != null && mThumb instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) mThumb).getBitmap();
        }

        ovalPaint = new Paint();
        ovalPaint.setColor(circle_color);
        ovalPaint.setAntiAlias(true);
        ovalPaint.setStyle(Paint.Style.STROKE);
        ovalPaint.setStrokeWidth(2);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(1);


        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);

        //绘制指示器进度
        mSweepPaint = new Paint();
        mSweepPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawOval(mOvalRect, ovalPaint);
        canvas.drawBitmap(bitmap, null, mThumbRect, linePaint);

        if (isMovePoint) {
            drawEndPoint(canvas);
        } else {
            drawProgress(canvas);
            drawSweep(canvas);
        }
    }

    private void drawEndPoint(Canvas canvas) {
        float x = (float) (radius * Math.cos((mRotate - 90) * Math.PI / 180f) + centreX);
        float y = (float) (radius / 3f * Math.sin((mRotate - 90) * Math.PI / 180f) + centreY);
        canvas.drawCircle(x, y, 5, pointPaint);
    }

    private void drawProgress(Canvas canvas) {
        if (isFinishOval) {
            canvas.drawArc(mOvalRect, mCurAngle - 90, baseAngle, false, linePaint);
        } else {
            canvas.drawArc(mOvalRect, mCurAngle - 90, mRotate - mCurAngle, false, linePaint);
        }
    }

    public float getSweepAngle() {
        float x = (float) (radius * Math.cos((mRotate - 90) * Math.PI / 180f) + centreX);
        float y = (float) (radius / 3f * Math.sin((mRotate - 90) * Math.PI / 180f) + centreY);
        float p = 0;
        if (mRotate <= 90) {
            p = (float) (Math.atan((x - centreX) / (centreY - y)) * (180 / Math.PI));
        } else if (mRotate <= 180) {
            p = (float) (Math.atan((y - centreY) / (x - centreX)) * (180 / Math.PI) + 90);
        } else if (mRotate <= 270) {
            p = (float) ((Math.atan((centreX - x) / (y - centreY))) * (180 / Math.PI) + 180);
        } else {
            p = (float) ((Math.atan((centreY - y) / (centreX - x))) * (180 / Math.PI) + 270);
        }
        return Math.abs(p);
    }

    /**
     * 画扫描效果
     */
    private void drawSweep(Canvas canvas) {
        float sweepAngle = getSweepAngle();
        //扇形的透明的渐变效果
        Shader mScanShader;
        mMatrix.setRotate(sweepAngle - 90, centreX, centreY);
        if (isMoveEnd) {
            mScanShader = new SweepGradient(centreX, centreY,
                    new int[]{changeAlpha(mScanShaderColor, 255), changeAlpha(mScanShaderColor, 255), changeAlpha(mScanShaderColor, 168),
                            changeAlpha(mScanShaderColor, 0), Color.TRANSPARENT
                    }, new float[]{0.0f, 0.002f, 0.01f, 0.4f, 1f});
        } else {
            mScanShader = new SweepGradient(centreX, centreY,
                    new int[]{Color.TRANSPARENT, changeAlpha(mScanShaderColor, 0), changeAlpha(mScanShaderColor, 168),
                            changeAlpha(mScanShaderColor, 255), changeAlpha(mScanShaderColor, 255)
                    }, new float[]{0.0f, 0.6f, 0.99f, 0.998f, 1f});
        }
        mScanShader.setLocalMatrix(mMatrix);
        mSweepPaint.setStyle(Paint.Style.FILL);
        mSweepPaint.setShader(mScanShader);
        if (isMoveEnd) {
            canvas.drawArc(mOvalRect, mRotate - 90, mCurAngle + baseAngle - mRotate, true, mSweepPaint);
        } else {
            //先旋转画布，再绘制扫描的颜色渲染，实现扫描时的旋转效果。
            if (isFinishOval) {
                canvas.drawArc(mOvalRect, mCurAngle - 90, baseAngle, true, mSweepPaint);
            } else {
                canvas.drawArc(mOvalRect, mCurAngle - 90, mRotate - mCurAngle, true, mSweepPaint);
            }
        }
    }

    public void updateCavas() {
        if (!isScolling || mSpeed == 0) {
            isScolling = false;
            invalidate();
            return;
        }
        removeCallbacks(runnable);
        postDelayed(runnable, mSpeed);
    }

    private boolean isMoveEnd = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isMoveEnd) {
                mRotate -= 12;
                if (baseAngle == 360) {
                    if (mRotate < 0) {
                        mRotate = 0;
                    }
                } else {
                    if (mRotate < mCurAngle) {
                        isMoveEnd = false;
                        mRotate = mCurAngle;
                    }
                }
            } else {
                mRotate += 12;
                if (baseAngle == 360) {//全盘扫描的时候
                    if (mRotate > 360) {
                        isFinishOval = true;
                        mRotate = 0;
                    }
                } else {
                    if (mRotate > mCurAngle + baseAngle) {
                        isMoveEnd = true;
                        mRotate = mCurAngle + baseAngle;
                    }
                }
            }
            invalidate();
            updateCavas();
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int mWidth = Math.min(width, height);
        centreX = mWidth / 2;
        centreY = mWidth / 3;

        int padding = 5;
        int maxWidth = 6;
        int diameter = mWidth - getPaddingLeft() - getPaddingRight() - maxWidth - padding * 2;
        radius = diameter / 2;

        float left = getPaddingLeft() + maxWidth / 2 + padding;
        float top = centreY - (radius / 3);
        float right = 2 * radius;
        float bottom = centreY + (radius / 3);

        mOvalRect.set(left, top, right, bottom);
        mThumbRect.set(width / 4, height / 4, 3 * width / 4, 3 * height / 4);
        setMeasuredDimension(mWidth, mWidth);
    }

    /**
     * 设置转动角度
     *
     * @param angle
     */
    public void setBaseAngle(float angle) {
        mCurAngle = mCurAngle == 0 ? (angle == 0 ? 0 : (180 - angle / 2)) : mCurAngle;
        if (angle != 360) {
            this.mSpeed = speeds[1];
        }
        this.isMoveEnd = false;
        this.isFinishOval = false;
        this.mRotate = mCurAngle;
        this.baseAngle = angle;
        updateCavas();
    }

    public void updateAngle(float angle) {
        if (angle == baseAngle) {
            return;
        }
        if (angle != 360) {
            this.mSpeed = speeds[1];
        }
        this.isMoveEnd = false;
        this.isMovePoint = false;
        this.isFinishOval = false;
        this.baseAngle = angle;
        mCurAngle = mRotate;
        isScolling = true;
        updateCavas();
    }

    /**
     * 设置转动速度（毫秒）
     *
     * @param speedRote
     */
    public void setmSpeed(int speedRote) {
        this.mSpeed = speeds[speedRote];
        updateCavas();
    }

    public void updateSpeed(int speed) {
        if (speed == 0 && baseAngle != 360) {
            return;
        }
        this.isMovePoint = false;
        this.mSpeed = speeds[speed];
    }

    /**
     * 移动起始点
     */
    public void changeCurAngle(boolean isReduce, float changeNum) {
        isMovePoint = true;
        this.isMoveEnd = false;
        this.isFinishOval = false;
        this.mCurAngle = isReduce ? this.mRotate - changeNum : this.mRotate + changeNum;
        mCurAngle = mCurAngle % 360;
        if (this.mCurAngle <= 0) {
            this.mCurAngle = 360 - mCurAngle;
        }
        this.mRotate = mCurAngle;
        this.isScolling = true;
        invalidate();
    }

    /**
     * 停止移动起始点
     */
    public void stopChangeCurAngle() {
        this.isMovePoint = false;
    }


    /**
     * a
     * 改变颜色的透明度
     *
     * @param color
     * @param alpha
     * @return
     */
    private static int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 开始转动
     */
    public void start() {
        isScolling = true;
        isMovePoint = false;
        updateCavas();
    }

    /**
     * 停止转动
     */
    public void stop() {
        isScolling = false;
    }

    /**
     * 获取当前角度
     */
    public float getBaseAngle() {
        return baseAngle;
    }

    /**
     * 重置
     */
    public void release() {
        removeCallbacks(runnable);
        mSpeed = speeds[1];
        baseAngle = 360;
        mCurAngle = 0;
        mRotate = 0;
        this.isMoveEnd = false;
        this.isFinishOval = false;
        isScolling = false;
        invalidate();
    }

    /**
     * 是否正在转动
     */
    public boolean isScolling() {
        return isScolling;
    }
}
