package ziye.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import ziye.skintest.R;

/**
 * Created by Administrator on 2018/10/25 0025.
 */

public class MyStyleButton extends View {
    private String leftText;
    private String rightText;

    private int checkedColor;
    private int defaultColor;
    private int centerRadius;
    private int textSize;

    private Paint paintChecked;
    private Paint paintDefault;
    private boolean lightLeft = true;
    private int bézier;

    private int offset;

    private Path leftPath, rightPath;
    private Paint textPaint;
    private Paint rectPaint;
    private Rect mBoundLeft, mBoundRight;
    private OnSwitchClick iListener;

    public MyStyleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }

    // 获取自定义的属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyStyleButton,
                0, 0);
        leftText = array.getString(R.styleable.MyStyleButton_leftText);
        rightText = array.getString(R.styleable.MyStyleButton_rightText);
        centerRadius = array.getInteger(R.styleable.MyStyleButton_centerRadius, 60);

        defaultColor = array.getColor(R.styleable.MyStyleButton_defaultColor, 0xff8e8d8a);
        checkedColor = array.getColor(R.styleable.MyStyleButton_checkedColor, 0xffff4081);

        textSize = array.getDimensionPixelSize(R.styleable.MyStyleButton_textSize, 13);
        bézier=array.getInteger(R.styleable.MyStyleButton_bézier,10);
    }

    /**
     * @author 张子扬
     * @time 2018/10/12 0012 10:27
     * @desc 画笔的初始化
     */
    private void initPaint() {
        paintChecked = new Paint();
        paintChecked.setColor(checkedColor);
        paintChecked.setStyle(Paint.Style.FILL);
        paintChecked.setStrokeCap(Paint.Cap.BUTT);

        paintDefault = new Paint();
        paintDefault.setColor(defaultColor);
        paintDefault.setStyle(Paint.Style.FILL);
        paintDefault.setStrokeCap(Paint.Cap.BUTT);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setStrokeWidth(1);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeCap(Paint.Cap.BUTT);

        rectPaint=new Paint();
        rectPaint.setColor(Color.TRANSPARENT);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int halfHeight = getHeight() / 2;
        offset = (int) (halfHeight / Math.tan(Math.toRadians(centerRadius)));
        Log.e("onDraw: ", offset + "");

        leftPath = new Path();
        leftPath.moveTo(bézier, 0);
        leftPath.lineTo(getWidth() / 2 + offset, 0);
        leftPath.lineTo(getWidth() / 2 - offset, getHeight());
        leftPath.lineTo(bézier, getHeight());
        leftPath.quadTo(0,getHeight(),0,getHeight()-bézier);
        leftPath.lineTo(0, bézier);
        leftPath.quadTo(0,0,bézier,0);
        canvas.drawPath(leftPath, paintChecked);

        rightPath = new Path();
        rightPath.moveTo(getWidth() / 2 + offset, 0);
        rightPath.lineTo(getWidth()-bézier, 0);
        rightPath.quadTo(getWidth(),0,getWidth(),bézier);
        rightPath.lineTo(getWidth(), getHeight()-bézier);
        rightPath.quadTo(getWidth(),getHeight(),getWidth()-bézier,getHeight());
        rightPath.lineTo(getWidth() / 2 - offset, getHeight());
        rightPath.lineTo(getWidth() / 2 + offset, 0);
        rightPath.close();

        canvas.drawPath(rightPath, paintDefault);

        mBoundLeft = new Rect(0, 0, getWidth() / 2, getHeight());
        mBoundRight = new Rect(getWidth() / 2, 0, getWidth(), getHeight());

        //画左侧边界
        canvas.drawRect(mBoundLeft, rectPaint);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (mBoundLeft.bottom + mBoundLeft.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(leftText, mBoundLeft.centerX(), baseline, textPaint);

        //画右侧边界
        canvas.drawRect(mBoundRight, rectPaint);
        Paint.FontMetricsInt fontMetrics2 = textPaint.getFontMetricsInt();
        int baselineR = (mBoundLeft.bottom + mBoundLeft.top - fontMetrics2.bottom - fontMetrics2.top) / 2;
        canvas.drawText(rightText, mBoundRight.centerX(), baselineR, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        } else {
            int x = (int) event.getX();
            int y = (int) event.getY();

            RectF bounds = new RectF();
            leftPath.computeBounds(bounds, true);
            Region region = new Region();
            region.setPath(leftPath, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));

            if (region.contains(x, y)) {
                //点击左边
                Log.e("currentState: ", "左边");
                if (!lightLeft) {
                    //左边暗, 重绘View, 并回调方法
                    iListener.currentState("NEW");
                    paintChecked.setColor(checkedColor);
                    paintDefault.setColor(defaultColor);
                    postInvalidate();
                    lightLeft = true;
                }
            } else {
                Log.e("currentState: ", "右边");
                //点击右边
                if (lightLeft) {
                    //左边已经点亮, 重绘View , 并回调
                    iListener.currentState("HOT");
                    paintChecked.setColor(defaultColor);
                    paintDefault.setColor(checkedColor);
                    postInvalidate();
                    lightLeft = false;
                }
            }
            return false;
        }
    }

    public void setOnClickListener(OnSwitchClick l) {
        this.iListener = l;
    }

    public interface OnSwitchClick {
        void currentState(String sortBy);
    }
}
