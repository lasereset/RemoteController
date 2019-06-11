package com.dmrjkj.remotecontroller.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.utils.ChartUtils;

/**
 * 项目名称：CircleSeekbarDemo
 * 类描述：
 * 创建人：xinchen
 * 创建时间：2019/4/23 14:32
 */
public class CircleSeekbar2 extends View {
    private final String TAG = "CircleSeekbar";

    private Paint circlePaint;
    private Paint progressPaint;
    private Paint anglePaint;//角度显示
    private Paint linePaint;//指示线
    private Paint pointPaint;//点
    private Paint arcPaint;//角度圆弧
    private int textSize = 9;
    private int progressWidth = 4;
    private int circleWidth = 6;
    private int padding = 5;
    private boolean mTouchInside = true;

    private int maxWidth = 6;
    private int radius;
    private int mWidth;
    private double mCurAngle = 0;
    private int centreX;
    private int centreY;
    private float mTouchIgnoreRadius;
    private RectF mCircleRect = new RectF();
    private Shader mScanShader;
    private Matrix mMatrix = new Matrix();

    private Bitmap bitmap;

    private int mScanShaderColor = Color.parseColor("#565cd6");

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

    public CircleSeekbar2(Context context) {
        super(context);
        init(context, null);
    }

    public CircleSeekbar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleSeekbar2(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Drawable mThumb = getResources().getDrawable(R.drawable.base_model);

        degreeLength_long = (int) (degreeLength_long * density);
        degreeLength_short = (int) (degreeLength_short * density);

        padding = (int) (padding * density);

        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleSeekbar2);
            circleColor = typedArray.getColor(R.styleable.CircleSeekbar2_circleColor1, circleColor);
            progressColor = typedArray.getColor(R.styleable.CircleSeekbar2_progressColor, progressColor);
            textColor = typedArray.getColor(R.styleable.CircleSeekbar2_textColor, textColor);
            textSize = typedArray.getColor(R.styleable.CircleSeekbar2_textSize, textSize);
            circleWidth = (int) typedArray.getDimension(R.styleable.CircleSeekbar2_circleWidth, circleWidth);
            progressWidth = (int) typedArray.getDimension(R.styleable.CircleSeekbar2_progressWidth, progressWidth);
            mTouchInside = typedArray.getBoolean(R.styleable.CircleSeekbar2_touchInside, mTouchInside);
            Drawable drawable = typedArray.getDrawable(R.styleable.CircleSeekbar2_background);
            if (drawable !=  null) {
                mThumb = drawable;
            }
            typedArray.recycle();
        }

        if (mThumb != null && mThumb instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) mThumb).getBitmap();
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
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setShader(mScanShader);

        //绘制角度
        anglePaint = new Paint();
        anglePaint.setColor(Color.RED);
        anglePaint.setAntiAlias(true);
        anglePaint.setStyle(Paint.Style.FILL);
        anglePaint.setStrokeWidth(2);
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
        float startAngle = (float) (mCurAngle - 90 - ((baseAngle % 360) / 2));
        canvas.drawArc(mCircleRect, startAngle, (float) baseAngle, false, arcPaint);
        drawProgress(canvas);

        drawBitmap(canvas);
        drawText(canvas);
//        canvas.translate(mThumbXPos, mThumbYPos);
    }

    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(bitmap, centreX - (bitmap.getWidth() / 2), centreY - (bitmap.getHeight() / 2), progressPaint);
    }

    private void drawProgress(Canvas canvas) {
        if (!mIsScaning) {
            return;
        }
        float sweepAngle, startAngle;
        if (isMoveEnd) {
            mMatrix.setRotate((float) (mRotate - 90 - ((baseAngle % 360) / 2)), centreY, centreY);
            startAngle = (float) (mCurAngle - 90 + mRotate - mCurAngle - ((baseAngle % 360) / 2));
            sweepAngle = (float) (baseAngle - mRotate + mCurAngle);
            mScanShader = new SweepGradient(centreX, centreY,new int[]{changeAlpha(mScanShaderColor, 255),
                    changeAlpha(mScanShaderColor, 255), changeAlpha(mScanShaderColor, 168),
                    changeAlpha(mScanShaderColor, 0), Color.TRANSPARENT
            }, new float[]{0.0f, 0.6f, 0.99f, 0.998f, 1f});
        } else {
            mMatrix.setRotate((float) (mRotate - 90), centreY, centreY);
            startAngle = (float) (mCurAngle - 90 - ((baseAngle % 360) / 2));
            sweepAngle = (float) (mRotate - mCurAngle);
            mScanShader = new SweepGradient(centreX, centreY,new int[]{Color.TRANSPARENT,
                    changeAlpha(mScanShaderColor, 0), changeAlpha(mScanShaderColor, 168),
                    changeAlpha(mScanShaderColor, 255), changeAlpha(mScanShaderColor, 255)
            }, new float[]{0.0f, 0.6f, 0.99f, 0.998f, 1f});
        }
        mScanShader.setLocalMatrix(mMatrix);
        progressPaint.setShader(mScanShader);
        progressPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mCircleRect, startAngle, sweepAngle, true, progressPaint);
    }

    private void drawPoints(Canvas canvas) {
        canvas.drawCircle(centreX, centreY, 8, pointPaint);
    }

    private void drawLine(Canvas canvas) {
        float orginAngle = (float) (mRotate - 90 - ((baseAngle % 360) / 2));
        PointF pointF = ChartUtils.calcArcEndPointXY(centreX, centreY, radius, 0, orginAngle);
        canvas.drawLine(centreX, centreY, pointF.x, pointF.y, linePaint);
    }

    private void drawText(Canvas canvas) {
        String angle = formatAngle(baseAngle) + "°";
        canvas.drawText(angle, centreX - anglePaint.measureText(angle) / 2, centreY - 100/*字体的高度*/, anglePaint);
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

    /**
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
     * 更新雷达扫描区域
     */
    public void updateScan(){
        float curTime = System.currentTimeMillis();
        if (mScanTime == 0 || !mIsScaning) {
            invalidate();
            return;
        }
        if(curTime>=mLastTime + mScanTime){
            mLastTime = curTime;
            removeCallbacks(mRunnable);
            postDelayed(mRunnable, 1000 /mScanTime);
        }
    }

    private double mRotate;
    private boolean isMoveEnd = false;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isMoveEnd) {
                mRotate --;
                if (mRotate < mCurAngle) {
                    isMoveEnd = false;
                    mRotate = mCurAngle;
                }
            } else {
                mRotate++;
                if(mRotate>=mCurAngle + baseAngle){
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

    //更新thum的坐标
    private void updateThumbPosition(double angle) {
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
     * @param time
     */
    public void setmScanTime(int time) {
        mScanTime = time;
        mIsScaning = true;
        updateScan();
    }

    /**
     * 设置起始转动点
     */
    public void setRotate(double rotate) {
        this.isMoveEnd = false;
        this.mCurAngle = rotate;
        this.mRotate = rotate;
        invalidate();
    }

    /**
     * 增加或减少当前转动点
     */
    public void changeCurAngle(boolean isReduce, double changeNum) {
        this.isMoveEnd = false;
        this.mCurAngle = !isReduce ? this.mCurAngle + changeNum : this.mCurAngle - changeNum;
        this.mRotate = mCurAngle;
        invalidate();
    }

    /**
     * 开始扫描动画
     */
    public void start(){
        mIsScaning = true;
        updateScan();
    }

    /**
     * 停止扫描动画
     */
    public void stop(){
        mIsScaning = false;
    }

    public void release() {
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
