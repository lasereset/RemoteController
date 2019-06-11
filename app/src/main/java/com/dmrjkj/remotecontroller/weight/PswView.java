package com.dmrjkj.remotecontroller.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.dmrjkj.remotecontroller.R;

import java.util.LinkedList;
import java.util.List;


public class PswView extends View {
    private static final String TAG = "PswView";
    private LinkedList<String> result;//保存当前输入的密码
    private int pswLength;//密码长度
    private int borderColor;//密码框颜色
    private int borderShadowColor;//密码框阴影颜色
    private int pswColor;//明文密码颜色
    private int pswTextSize;//明文密码字体大小
    private int inputBorderColor;//输入时密码边框颜色
    private int borderImg;//边框图片
    private int inputBorderImg;//输入时边框图片
    private int delayTime;//延迟绘制圆点时间，1000 = 1s
    private boolean isBorderImg;//是否使用图片绘制边框
    private boolean isShowTextPsw;//是否在按返回键时绘制明文密码
    private boolean isShowBorderShadow;//是否绘制在输入时，密码框的阴影颜色
    private boolean clearTextPsw;//是否只绘制明文密码
    private boolean darkPsw;//是否只绘制圆点密码
    private boolean isChangeBorder;//是否在输入密码时不更改密码框颜色
    private Paint pswDotPaint;//密码圆点画笔
    private Paint pswTextPaint;//明文密码画笔
    private Paint borderPaint;//边框画笔
    private Paint inputBorderPaint;//输入时边框画笔
    private RectF borderRectF;//边框圆角矩形
    private int borderRadius;//边框圆角程度
    private int borderWidth;//边框宽度
    private int spacingWidth;//边框之间的间距宽度
    private InputCallBack inputCallBack;//输入完成时监听
    private int height;//整个view的高度

    private Bitmap[] psdBitMap;

    private static boolean invalidated = false;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    invalidated = true;
                    invalidate();
                    break;
                default:
                    break;
            }
        }
    };

    public interface InputCallBack {
        void onInputFinish(String password);
    }

    public void setInputCallBack(InputCallBack inputCallBack) {
        this.inputCallBack = inputCallBack;
    }


    public void setPswLength(int pswLength) {
        this.pswLength = pswLength;
        invalidate();
    }

    public int getPswLength() {
        return pswLength;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setPswColor(int pswColor) {
        this.pswColor = pswColor;
        pswDotPaint.setColor(pswColor);
        pswTextPaint.setColor(pswColor);
        invalidate();
    }

    public int getPswColor() {
        return pswColor;
    }

    public void setInputBorderColor(int inputBorderColor) {
        this.inputBorderColor = inputBorderColor;
        inputBorderPaint.setColor(inputBorderColor);
        invalidate();
    }

    public int getInputBorderColor() {
        return inputBorderColor;
    }

    public void setBorderShadowColor(int borderShadowColor) {
        this.borderShadowColor = borderShadowColor;
        inputBorderPaint.setShadowLayer(6, 0, 0, borderShadowColor);
        invalidate();
    }

    public int getBorderShadowColor() {
        return borderShadowColor;
    }

    public void setPswTextSize(int pswTextSize) {
        this.pswTextSize = pswTextSize;
        invalidate();
    }

    public int getPswTextSize() {
        return pswTextSize;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setIsBorderImg(boolean borderImg) {
        isBorderImg = borderImg;
        invalidate();
    }

    public boolean isBorderImg() {
        return isBorderImg;
    }

    public void setIsShowTextPsw(boolean showTextPsw) {
        isShowTextPsw = showTextPsw;
        invalidate();
    }

    public boolean isShowTextPsw() {
        return isShowTextPsw;
    }

    public void setBorderImg(int borderImg) {
        this.borderImg = borderImg;
        invalidate();
    }

    public int getBorderImg() {
        return borderImg;
    }

    public void setInputBorderImg(int inputBorderImg) {
        this.inputBorderImg = inputBorderImg;
        invalidate();
    }

    public int getInputBorderImg() {
        return inputBorderImg;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        invalidate();
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setClearTextPsw(boolean clearTextPsw) {
        this.clearTextPsw = clearTextPsw;
        if (!clearTextPsw) {
            invalidated = true;
        }
        invalidate();
    }

    public boolean isClearTextPsw() {
        return clearTextPsw;
    }

    public void setDarkPsw(boolean darkPsw) {
        this.darkPsw = darkPsw;
        invalidate();
    }

    public boolean isDarkPsw() {
        return darkPsw;
    }

    public void setIsChangeBorder(boolean changeBorder) {
        isChangeBorder = changeBorder;
        invalidate();
    }

    public boolean isChangeBorder() {
        return isChangeBorder;
    }

    public void setShowBorderShadow(boolean showBorderShadow) {
        isShowBorderShadow = showBorderShadow;
        invalidate();
    }

    public boolean isShowBorderShadow() {
        return isShowBorderShadow;
    }

    public PswView(Context context) {
        super(context);
    }

    public PswView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public PswView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    //初始化
    private void initView(Context context, AttributeSet attrs) {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        result = new LinkedList<>();
        Drawable up = getResources().getDrawable(R.mipmap.ic_up);
        Drawable left = getResources().getDrawable(R.mipmap.ic_left);
        Drawable down = getResources().getDrawable(R.mipmap.ic_down);
        Drawable right = getResources().getDrawable(R.mipmap.ic_right);

        psdBitMap = new Bitmap[]{((BitmapDrawable) up).getBitmap(), ((BitmapDrawable) left).getBitmap(), ((BitmapDrawable) down).getBitmap()
                , ((BitmapDrawable) right).getBitmap()};

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PswView);
        if (array != null) {
            pswLength = array.getInt(R.styleable.PswView_pswLength, 6);
            pswColor = array.getColor(R.styleable.PswView_pswColor, Color.parseColor("#3779e3"));
            borderColor = array.getColor(R.styleable.PswView_borderColor, Color.parseColor("#999999"));
            inputBorderColor = array.getColor(R.styleable.PswView_inputBorder_color, Color.parseColor("#3779e3"));
            borderShadowColor = array.getColor(R.styleable.PswView_borderShadow_color, Color.parseColor("#3577e2"));
            borderImg = array.getResourceId(R.styleable.PswView_borderImg, R.drawable.pic_dlzc_srk1);
            inputBorderImg = array.getResourceId(R.styleable.PswView_inputBorderImg, R.drawable.pic_dlzc_srk);
            isBorderImg = array.getBoolean(R.styleable.PswView_isDrawBorderImg, false);
            isShowTextPsw = array.getBoolean(R.styleable.PswView_isShowTextPsw, false);
            isShowBorderShadow = array.getBoolean(R.styleable.PswView_isShowBorderShadow, false);
            clearTextPsw = array.getBoolean(R.styleable.PswView_clearTextPsw, false);
            darkPsw = array.getBoolean(R.styleable.PswView_darkPsw, false);
            isChangeBorder = array.getBoolean(R.styleable.PswView_isChangeBorder, false);
            delayTime = array.getInt(R.styleable.PswView_delayTime, 1000);
            pswTextSize = (int) array.getDimension(R.styleable.PswView_psw_textSize,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
            borderRadius = (int) array.getDimension(R.styleable.PswView_borderRadius,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        } else {
            pswLength = 6;
            pswColor = Color.parseColor("#3779e3");
            borderColor = Color.parseColor("#999999");
            inputBorderColor = Color.parseColor("#3779e3");
            borderShadowColor = Color.parseColor("#3577e2");
            borderImg = R.drawable.pic_dlzc_srk1;
            inputBorderImg = R.drawable.pic_dlzc_srk;
            delayTime = 1000;
            clearTextPsw = false;
            darkPsw = false;
            isBorderImg = false;
            isShowTextPsw = false;
            isShowBorderShadow = false;
            isChangeBorder = false;
            //明文密码字体大小，初始化18sp
            pswTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
            //边框圆角程度初始化8dp
            borderRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        }
        //边框宽度初始化40dp
        borderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        //边框之间的间距初始化10dp
        spacingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        borderRectF = new RectF();
        initPaint();
    }

    //初始化画笔
    private void initPaint() {
        //密码圆点画笔初始化
        pswDotPaint = new Paint();
        pswDotPaint.setAntiAlias(true);
        pswDotPaint.setStrokeWidth(3);
        pswDotPaint.setStyle(Paint.Style.FILL);
        pswDotPaint.setColor(pswColor);

        //明文密码画笔初始化
        pswTextPaint = new Paint();
        pswTextPaint.setAntiAlias(true);
        pswTextPaint.setFakeBoldText(true);
        pswTextPaint.setColor(pswColor);

        //边框画笔初始化
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(3);

        //输入时边框画笔初始化
        inputBorderPaint = new Paint();
        inputBorderPaint.setAntiAlias(true);
        inputBorderPaint.setColor(inputBorderColor);
        inputBorderPaint.setStyle(Paint.Style.STROKE);
        inputBorderPaint.setStrokeWidth(3);
        //是否绘制边框阴影
        if (isShowBorderShadow) {
            inputBorderPaint.setShadowLayer(6, 0, 0, borderShadowColor);
            setLayerType(LAYER_TYPE_SOFTWARE, inputBorderPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpec == MeasureSpec.AT_MOST) {
            if (heightSpec != MeasureSpec.AT_MOST) {//高度已知但宽度未知时
                spacingWidth = heightSize / 4;
                widthSize = (heightSize * pswLength) + (spacingWidth * pswLength);
                borderWidth = heightSize;
            } else {//宽高都未知时
                widthSize = (borderWidth * pswLength) + (spacingWidth * pswLength);
                heightSize = (int) (borderWidth + ((borderPaint.getStrokeWidth()) * 2));
            }
        } else {
            //宽度已知但高度未知时
            if (heightSpec == MeasureSpec.AT_MOST) {
                borderWidth = (widthSize * 4) / (5 * pswLength);
                spacingWidth = borderWidth / 4;
                heightSize = (int) (borderWidth + ((borderPaint.getStrokeWidth()) * 2));
            }
        }
        height = heightSize;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int dotRadius = borderWidth / 6;//密码圆点为边框宽度的六分之一

		/*
        * 如果明文密码字体大小为默认大小，则取边框宽度的八分之一，否则用自定义大小
		* */
        if (pswTextSize == (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics())) {
            pswTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, borderWidth / 8, getResources().getDisplayMetrics());
        }
        pswTextPaint.setTextSize(pswTextSize);

        //绘制密码边框
        drawBorder(canvas, height);
        if (isChangeBorder) {
            if (clearTextPsw) {
                for (int i = 0; i < result.size(); i++) {
                    String num = result.get(i) + "";
                    drawText(canvas, num, i);
                }
            } else if (darkPsw) {
                for (int i = 0; i < result.size(); i++) {
                    float circleX = (float) ((i * (borderWidth + spacingWidth)) + (borderWidth / 2) + (0.5 * spacingWidth));
                    float circleY = height / 2;
                    canvas.drawCircle(circleX, circleY, dotRadius, pswDotPaint);
                }
            } else {
                if (invalidated) {
                    drawDelayCircle(canvas, height, dotRadius);
                    return;
                }
                for (int i = 0; i < result.size(); i++) {
                    //明文密码
                    String num = result.get(i) + "";
                    //圆点坐标
                    float circleX = (float) (((i - 1) * (borderWidth + spacingWidth)) + (borderWidth / 2) + (0.5 * spacingWidth));
                    float circleY = height / 2;
                    //密码框坐标
                    drawText(canvas, num, i);

				/*
				* 当输入位置 = 输入长度时
				* 即判断当前绘制位置是否等于当前正在输入密码的位置
				* 若是则延迟delayTime时间后绘制为圆点
				* */
                    if (i + 1 == result.size()) {
                        handler.sendEmptyMessageDelayed(1, delayTime);
                    }
                    //当输入第二个密码时，才开始从第一个位置绘制圆点
                    if (i >= 1) {
                        canvas.drawCircle(circleX, circleY, dotRadius, pswDotPaint);
                    }
                }
            }
        } else {
            if (clearTextPsw) {
                for (int i = 0; i < result.size(); i++) {
                    //计算密码边框坐标
                    int left = (int) (i * (borderWidth + spacingWidth) + (0.5 * spacingWidth));
                    int right = (int) (((i + 1) * borderWidth) + (i * spacingWidth) + (0.5 * spacingWidth));

                    drawBitmapOrBorder(canvas, left, right, height);

                    String num = result.get(i) + "";
                    drawText(canvas, num, i);
                }
            } else if (darkPsw) {
                for (int i = 0; i < result.size(); i++) {
                    float circleX = (float) ((i * (borderWidth + spacingWidth)) + (borderWidth / 2) + (0.5 * spacingWidth));
                    float circleY = height / 2;
                    int left = (int) (i * (borderWidth + spacingWidth) + (0.5 * spacingWidth));
                    int right = (int) (((i + 1) * borderWidth) + (i * spacingWidth) + (0.5 * spacingWidth));
                    drawBitmapOrBorder(canvas, left, right, height);
                    canvas.drawCircle(circleX, circleY, dotRadius, pswDotPaint);
                }
            } else {
                if (invalidated) {
                    drawDelayCircle(canvas, height, dotRadius);
                    return;
                }
                for (int i = 0; i < result.size(); i++) {
                    //明文密码
                    String num = result.get(i) + "";
                    //圆点坐标
                    float circleX = (float) (((i - 1) * (borderWidth + spacingWidth)) + (borderWidth / 2) + (0.5 * spacingWidth));
                    float circleY = height / 2;
                    //密码框坐标
                    int left = (int) (i * (borderWidth + spacingWidth) + (0.5 * spacingWidth));
                    int right = (int) (((i + 1) * borderWidth) + (i * spacingWidth) + (0.5 * spacingWidth));

                    drawBitmapOrBorder(canvas, left, right, height);

                    drawText(canvas, num, i);

				/*
				* 当输入位置 = 输入长度时
				* 即判断当前绘制位置是否等于当前正在输入密码的位置
				* 若是则延迟delayTime时间后绘制为圆点
				* */
                    if (i + 1 == result.size()) {
                        handler.sendEmptyMessageDelayed(1, delayTime);
                    }
                    //当输入第二个密码时，才开始从第一个位置绘制圆点
                    if (i >= 1) {
                        canvas.drawCircle(circleX, circleY, dotRadius, pswDotPaint);
                    }
                }
            }
        }
    }

    //绘制明文密码
    private void drawText(Canvas canvas, String num, int i) {
        Log.d(TAG, "Enter into this drawText clearTextPsw==>" + clearTextPsw);
        Rect mTextBound = new Rect();
        pswTextPaint.getTextBounds(num, 0, num.length(), mTextBound);
//        Paint.FontMetrics fontMetrics = pswTextPaint.getFontMetrics();
//        float textX = (float) ((i * (borderWidth + spacingWidth)) + (borderWidth / 2 - mTextBound.width() / 2) + (0.45 * spacingWidth));
//        float textY = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
//        canvas.drawText(num, textX, textY, pswTextPaint);
        if (clearTextPsw || i == result.size() - 1) {
            Log.d(TAG, "Enter into this drawText==>" + i + "====psdBitMap[i]:" + psdBitMap[i]);
            int dotBitMapWidth = borderWidth / 4;
            float circleX = (float) ((i * (borderWidth + spacingWidth)) + (borderWidth / 2) + (0.5 * spacingWidth));
            float circleY = height / 2;
            canvas.drawBitmap(psdBitMap[Integer.valueOf(num)], null, new RectF(circleX - dotBitMapWidth, circleY - dotBitMapWidth, circleX + dotBitMapWidth, circleY + dotBitMapWidth), pswDotPaint);
        }
        if (result.size() == pswLength) {
            inputCallBack.onInputFinish(getCodeValues());
        }
    }

    //延迟delay时间后，将当前输入的明文密码绘制为圆点
    private void drawDelayCircle(Canvas canvas, int height, int dotRadius) {
        invalidated = false;
        if (isChangeBorder) {
            for (int i = 0; i < result.size(); i++) {
                float circleX = (float) (((i - 1) * (borderWidth + spacingWidth)) + (borderWidth / 2) + (0.5 * spacingWidth));
                float circleY = height / 2;
                canvas.drawCircle(circleX, circleY, dotRadius, pswDotPaint);
            }
            canvas.drawCircle((float) ((float) (((result.size() - 1) * (borderWidth + spacingWidth)) + (borderWidth / 2)) + (0.5 * spacingWidth)),
                    height / 2, dotRadius, pswDotPaint);
        } else {
            for (int i = 0; i < result.size(); i++) {
                float circleX = (float) (((i - 1) * (borderWidth + spacingWidth)) + (borderWidth / 2) + (0.5 * spacingWidth));
                float circleY = height / 2;
                int left = (int) (i * (borderWidth + spacingWidth) + (0.5 * spacingWidth));
                int right = (int) (((i + 1) * borderWidth) + (i * spacingWidth) + (0.5 * spacingWidth));
                canvas.drawCircle(circleX, circleY, dotRadius, pswDotPaint);
                drawBitmapOrBorder(canvas, left, right, height);
            }
            canvas.drawCircle((float) ((float) (((result.size() - 1) * (borderWidth + spacingWidth)) + (borderWidth / 2)) + (0.5 * spacingWidth)),
                    height / 2, dotRadius, pswDotPaint);
        }
        handler.removeMessages(1);
    }

    //绘制初始密码框时判断是否用图片绘制密码框
    private void drawBorder(Canvas canvas, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), borderImg);
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < pswLength; i++) {
            int left = (int) ((i * (borderWidth + spacingWidth)) + (0.5 * spacingWidth));
            int right = (int) (((i + 1) * borderWidth) + (i * spacingWidth) + (0.5 * spacingWidth));
            if (isBorderImg) {
                Rect dst = new Rect(left, (int) borderPaint.getStrokeWidth(), right, (int) (height - (borderPaint.getStrokeWidth())));
                canvas.drawBitmap(bitmap, src, dst, borderPaint);
            } else {
                borderRectF.set(left, borderPaint.getStrokeWidth(), right, height - (borderPaint.getStrokeWidth()));
                canvas.drawRoundRect(borderRectF, borderRadius, borderRadius, borderPaint);
            }
        }
        bitmap.recycle();

    }

    //是否使用图片绘制密码框
    private void drawBitmapOrBorder(Canvas canvas, int left, int right, int height) {
        if (isBorderImg) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), inputBorderImg);
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect dst = new Rect(left, (int) (0 + ((borderPaint.getStrokeWidth()))), right, (int) (height - (borderPaint.getStrokeWidth())));
            canvas.drawBitmap(bitmap, src, dst, inputBorderPaint);
            bitmap.recycle();
        } else {
            borderRectF.set(left, 0 + (borderPaint.getStrokeWidth()), right, height - (borderPaint.getStrokeWidth()));
            canvas.drawRoundRect(borderRectF, borderRadius, borderRadius, inputBorderPaint);
        }
    }

    //默认密码
    public void preResult(List<String> results) {
        this.result = new LinkedList<>(results);
        invalidated = true;
        invalidate();
    }

    //是否输入完成
    public boolean isFillOk() {
        return result.size() == 4;
    }

    //清除密码
    public void clearPsw() {
        result.clear();
        invalidate();
    }

    //删除
    public void delPsw() {
        if (result.size() == 0) {
            return;
        }
        invalidated = true;
        result.remove(result.size() - 1);
        invalidate();
    }

    //放入密码
    public void setPsw(int psw) {
        if (result.size() == 4) {
            return;
        }
        result.add(result.size(), String.valueOf(psw));
        invalidate();
    }

    //获取密码
    public String getPsw() {
        if (result.size() != pswLength) {
            return null;
        }
        return JSON.toJSONString(result);
    }

    //获取加密后的密码
    public String getCodeValues() {
        StringBuilder sb = new StringBuilder();
        for (String i : result) {
            sb.append("0").append(i);
        }
        return sb.toString();
    }

    //获取当前显示方式
    public boolean isShowPsw() {
        return this.clearTextPsw;
    }
}