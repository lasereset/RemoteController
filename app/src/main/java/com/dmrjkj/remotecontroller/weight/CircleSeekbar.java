package com.dmrjkj.remotecontroller.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.utils.ChartUtils;

/**
 * 项目名称：CircleSeekbarDemo
 * 类描述：
 * 创建人：xinchen
 * 创建时间：2019/4/23 14:32
 */
public class CircleSeekbar extends View {
    private final String TAG = "CircleSeekbar";

    private Paint circlePaint;
    private Paint progressPaint;
    private Paint paintDegree;
    private Paint calibrationPaint;//刻度
    private Paint anglePaint;//角度显示
    private Paint linePaint;//指示线
    private Paint pointPaint;//点
    private Paint arcPaint;//角度圆弧
    private int textSize = 9;
    private int progressWidth = 4;
    private int circleWidth = 6;
    private int padding = 5;
    private boolean mTouchInside = true;
    private String[] degreeText = {"0°", "30°", "60°", "90°", "120°", "150°", "180°", "210°", "240°", "270°", "300°", "330°"};

    private int maxWidth = 6;
    private int radius;
    private int mWidth;
    private double mCurAngle = 0;
    private int centreX;
    private int centreY;
    private int mThumbXPos;
    private int mThumbYPos;
    private int mEndXPos;
    private int mEndYPos;
    private float mTouchIgnoreRadius;
    private boolean isFirstTouch = true;
    private RectF mCircleRect = new RectF();

    private double baseAngle = 360;//基础角度 0代表全盘扫描

    private int degreeLength_long = 10;//刻度的长度
    private int degreeLength_short = 6;//短刻度线

    /**
     * 扫描延迟时间（扫描旋转隔时间,默认2毫秒旋转一度）
     */
    private int mScanTime = 15;
    /**
     * 最后扫描刷新时间
     */
    private float mLastTime;
    /**
     * 是否开始转动
     */
    private boolean mIsScaning;

    public CircleSeekbar(Context context) {
        super(context);
        init(context, null);
    }

    public CircleSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float density = context.getResources().getDisplayMetrics().density;
        int thumbHalfheight = 0;

        int circleColor = getResources().getColor(R.color.circleseekbar_gray);
        int progressColor = getResources().getColor(R.color.circleseekbar_blue_light);
        int textColor = getResources().getColor(R.color.circleseekbar_text_color);
        progressWidth = (int) (progressWidth * density);
        circleWidth = (int) (circleWidth * density);
        textSize = (int) (textSize * density);

        degreeLength_long = (int) (degreeLength_long * density);
        degreeLength_short = (int) (degreeLength_short * density);

        padding = (int) (padding * density);

        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleSeekbar2);
            circleColor = typedArray.getColor(R.styleable.CircleSeekbar2_circleColor1, circleColor);
            progressColor = typedArray.getColor(R.styleable.CircleSeekbar2_circleColor1, progressColor);
            textColor = typedArray.getColor(R.styleable.CircleSeekbar2_textColor, textColor);
            textSize = typedArray.getColor(R.styleable.CircleSeekbar2_textSize, textSize);
            circleWidth = (int) typedArray.getDimension(R.styleable.CircleSeekbar2_circleWidth, circleWidth);
            progressWidth = (int) typedArray.getDimension(R.styleable.CircleSeekbar2_progressWidth, progressWidth);
            mTouchInside = typedArray.getBoolean(R.styleable.CircleSeekbar2_touchInside, mTouchInside);
            typedArray.recycle();
        }

        padding = thumbHalfheight + padding;

        maxWidth = circleWidth > progressWidth ? circleWidth : progressWidth;

        circlePaint = new Paint();
        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);

        //绘制指示器进度
        progressPaint = new Paint();
        progressPaint.setColor(Color.RED);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(4);

        paintDegree = new Paint();
        paintDegree.setColor(textColor);
        paintDegree.setAntiAlias(true);
        paintDegree.setStyle(Paint.Style.FILL);
        paintDegree.setStrokeWidth(1);
        paintDegree.setTextSize(textSize);

        //绘制刻度
        calibrationPaint = new Paint();
        calibrationPaint.setColor(Color.BLACK);
        calibrationPaint.setAntiAlias(true);

        //绘制角度
        anglePaint = new Paint();
        anglePaint.setColor(textColor);
        anglePaint.setAntiAlias(true);
        anglePaint.setStyle(Paint.Style.FILL);
        anglePaint.setStrokeWidth(1);
        anglePaint.setTextSize(textSize);

        //绘制指示线
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(2);

        //绘制点
        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);

        //绘制角度圆弧
        arcPaint = new Paint();
        arcPaint.setColor(progressColor);
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(progressWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLine(canvas);
        drawPoints(canvas);

        canvas.drawCircle(mWidth / 2, mWidth / 2, radius, circlePaint);
        canvas.drawArc(mCircleRect, (float) (mCurAngle - 90), (float) baseAngle, false, arcPaint);
        if (isMoveEnd) {
            canvas.drawArc(mCircleRect, (float) (mCurAngle - 90 + mRotate - mCurAngle), (float) (baseAngle - mRotate + mCurAngle), false, progressPaint);
        } else {
            canvas.drawArc(mCircleRect, (float) (mCurAngle - 90), (float) (mRotate - mCurAngle), false, progressPaint);
        }
        drawText(canvas);
//        canvas.translate(mThumbXPos, mThumbYPos);
    }

    private void drawPoints(Canvas canvas) {
        canvas.drawCircle(centreX, centreY, 8, pointPaint);
    }

    private void drawLine(Canvas canvas) {
        PointF pointF = ChartUtils.calcArcEndPointXY(centreX, centreY, radius, 0, (float) mRotate - 90);
        canvas.drawLine(centreX, centreY, pointF.x, pointF.y, linePaint);
    }

    private void drawText(Canvas canvas) {
        String angle = formatAngle(baseAngle) + "°";
        canvas.drawText(angle, centreX - anglePaint.measureText(angle) / 2, centreY - 100/*字体的高度*/, anglePaint);

        float rotation = 360 / degreeText.length;
        for (int i = 0; i < degreeText.length; i++) {
            canvas.drawText(degreeText[i], centreX - paintDegree.measureText(degreeText[i]) / 2, 100/*字体的高度*/, paintDegree);
            canvas.rotate(rotation, centreX, centreY);
        }

        for (int i = 0; i < 60; i++) {
            int degreeLength;
            if (i % 5 == 0) {
                calibrationPaint.setStrokeWidth(6);
                degreeLength = degreeLength_long;
            } else {
                calibrationPaint.setStrokeWidth(3);
                degreeLength = degreeLength_short;
            }
            canvas.drawLine(centreX, centreY - radius, centreX, centreY - radius + degreeLength, calibrationPaint);
            canvas.rotate(360 / 60, centreX, centreY);
        }
    }

    @SuppressLint("DefaultLocale")
    private static String formatAngle(double angle) {
        return String.format("%.1f", angle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mWidth = Math.min(width, height);
        centreX = mWidth / 2;
        centreY = mWidth / 2;

        float left = getPaddingLeft() + maxWidth / 2 + padding;
        float top = getPaddingTop() + maxWidth / 2 + padding;

        int diameter = mWidth - getPaddingLeft() - getPaddingRight() - maxWidth - padding * 2;
        radius = diameter / 2;
        mCircleRect.set(left, top, left + diameter, top + diameter);

        updateThumbPosition(mCurAngle);

        setTouchInSide(mTouchInside);
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.getParent().requestDisallowInterceptTouchEvent(true);//通知父控件勿拦截本次触摸事件

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateOnTouch(event);
                break;
            case MotionEvent.ACTION_MOVE:
                updateOnTouch(event);
                break;
            case MotionEvent.ACTION_UP:
                isFirstTouch = true;
                setPressed(false);
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                isFirstTouch = true;
                setPressed(false);
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    /**
     * 更新雷达扫描区域
     */
    private void updateScan() {
        float curTime = System.currentTimeMillis();
        if (mScanTime == 0 || !mIsScaning) {
            return;
        }
        if (curTime >= mLastTime + mScanTime) {
            mLastTime = curTime;
            removeCallbacks(mRunnable);
            postDelayed(mRunnable, 1000 / mScanTime);
        }
    }

    private double mRotate;
    private boolean isMoveEnd = false;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isMoveEnd) {
                mRotate--;
                if (mRotate < mCurAngle) {
                    isMoveEnd = false;
                    mRotate = mCurAngle;
                }
            } else {
                mRotate++;
                if (mRotate >= mCurAngle + baseAngle) {
                    if (baseAngle == 360) {
                        mRotate = mCurAngle;
                    } else {
                        isMoveEnd = true;
                        mRotate = mCurAngle + baseAngle;
                    }
                }
            }
            invalidate();
            updateScan();
        }
    };

    private void updateOnTouch(MotionEvent event) {
        boolean ignoreTouch = ignoreTouch(event.getX(), event.getY());
        if (ignoreTouch) {
            return;
        }
        setPressed(true);
        getTouchDegrees(event.getX(), event.getY());
        updateThumbPosition(mCurAngle);
        invalidate();
    }

    //根据触摸点的坐标获取角度值
    private double getTouchDegrees(float xPos, float yPos) {
        float x = xPos - centreX;
        float y = yPos - centreY;

        // convert to arc Angle
        double angle = Math.toDegrees(Math.atan2(y, x) + (Math.PI / 2));

        if (angle < 0) {
            angle = 360 + angle;
        }

        mCurAngle = angle;
        if (isFirstTouch) {  //如果是滑动前第一次点击则总是移动到该度数
            mCurAngle = angle;
            isFirstTouch = false;
        }
        return mCurAngle;
    }

    //更新thum的坐标
    private void updateThumbPosition(double angle) {
        PointF pointF = ChartUtils.calcArcEndPointXY(centreX, centreY, radius, (float) 0, (float) angle - 90);
        mThumbXPos = (int) pointF.x;
        mThumbXPos = (int) pointF.y;

        PointF pointFEnd = ChartUtils.calcArcEndPointXY(centreX, centreY, radius, (float) baseAngle, (float) angle - 90);
        mEndXPos = (int) pointFEnd.x;
        mEndYPos = (int) pointFEnd.y;

        this.mRotate = angle;
    }

    //设置触摸生效范围
    public void setTouchInSide(boolean isEnabled) {
        mTouchInside = isEnabled;
        mTouchIgnoreRadius = (float) radius / 4;
    }

    private boolean ignoreTouch(float xPos, float yPos) {
        boolean ignore = false;
        float x = xPos - centreX;
        float y = yPos - centreY;

        float touchRadius = (float) Math.sqrt(((x * x) + (y * y)));
        if (touchRadius < mTouchIgnoreRadius) {
            ignore = true;
        }
        return ignore;
    }

    /**
     * 设置转动角度
     *
     * @param angle
     */
    public void setBaseAngle(double angle) {
        this.isMoveEnd = false;
        this.mRotate = mCurAngle;
        this.baseAngle = angle;
        updateScan();
    }

    /**
     * 设置转动速度（毫秒）
     *
     * @param time
     */
    public void setmScanTime(int time) {
        mScanTime = time;
        mIsScaning = true;
        updateScan();
    }

    /**
     * 开始扫描动画
     */
    public void start() {
        mIsScaning = true;
        updateScan();
    }

    /**
     * 停止扫描动画
     */
    public void stop() {
        mScanTime = 15;
        baseAngle = 360;
        mCurAngle = 0;
        mRotate = 0;
        mIsScaning = false;
        invalidate();
    }

    /**
     * 获取转速
     */
    public int getmScanTime() {
        return mScanTime;
    }

    /**
     * 获取角度
     */
    public double getBaseAngle() {
        return baseAngle;
    }
}
