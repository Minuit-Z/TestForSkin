package ziye.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import ziye.skintest.R;

/**
 * Created by Administrator on 2018/10/12 0012.
 */

public class MyTextView extends View {

    private float radius;
    private float strokeWidth;
    private int background;

    private int defaultColor;

    private Paint paint;
    private int centerX;
    private int centerY;

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultColor = context.getResources().getColor(R.color.colorAccent);
        //首先获取布局定义
        initAttrs(context, attrs);
        //根据定义初始化画笔
        initPaint();
    }


    /**
     * @author 张子扬
     * @time 2018/10/12 0012 10:27
     * @desc 画笔的初始化
     */
    private void initPaint() {
        paint=new Paint();
        paint.setColor(background);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(strokeWidth);

        paint.setStrokeCap(Paint.Cap.ROUND);
    }


    // 获取自定义的属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MinuitCircle,
                0, 0);

        radius = array.getDimension(R.styleable.MinuitCircle_radius, 1);
        background = array.getColor(R.styleable.MinuitCircle_color, defaultColor);
        strokeWidth = array.getDimension(R.styleable.MinuitCircle_strokeWidth, 1);
    }

    //绘制布局
    @Override
    protected void onDraw(Canvas canvas) {
        centerX=getWidth()/2;
        centerY=getWidth()/2;

//        RectF r=new RectF();
//        r.bottom=centerX-radius;

        canvas.drawCircle(centerX,centerY,radius,paint);

    }
}
