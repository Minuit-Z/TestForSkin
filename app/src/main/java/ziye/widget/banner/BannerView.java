package ziye.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ziye.skintest.R;

import static ziye.utils.JUtils.dip2px;

/**
 * Created by Administrator on 2018/11/6 0006.
 */

public class BannerView extends RelativeLayout {
    private Context mContext;
    private BannerAdapter mAdapter; //抽象adaptee 适配器模式

    /**
     * 初始化一些控件
     */
    private BannerViewPager pager;
    private LinearLayout llDotContainer;
    private TextView tvDesc;

    private IIndicator indicator;

    /**
     * 接收属性
     */
    private int mDotGravity = 1; //点的展示位置 , 默认在右边
    private Drawable mIndicatorFocusDrawable;//点指示器: 选中
    private Drawable mIndicatorNormalDrawable;//点指示器: 默认

    private int mDotSize = 8; //点的大小: 默认8dp
    private int mDotDistance = 8; //点的间距: 默认8dp

    private int mBottomColor = Color.TRANSPARENT;

    private float mWidthProportion, mHeightProportion; // 宽高比例
    private int mCurrentPosition;//当前的点选中位置


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        //选择layout为组合控件的View, viewGroup为本类
        inflate(context, R.layout.ui_banner_layout, this);

        initAttribute(attrs);
        initView();
    }

    /**
     * @author 张子扬
     * @time 2018/11/7 0007 9:33
     * @desc 获取自定义的属性
     */
    private void initAttribute(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);

        // 获取点的位置
        mDotGravity = array.getInt(R.styleable.BannerView_dotGravity, mDotGravity);
        // 获取点的颜色（默认、选中）
        mIndicatorFocusDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if (mIndicatorFocusDrawable == null) {
            // 如果在布局文件中没有配置点的颜色  有一个默认值
            mIndicatorFocusDrawable = new ColorDrawable(Color.RED);
        }
        mIndicatorNormalDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if (mIndicatorNormalDrawable == null) {
            // 如果在布局文件中没有配置点的颜色  有一个默认值
            mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        }
        // 获取点的大小和距离
        mDotSize = (int) array.getDimension(R.styleable.BannerView_dotSize, dip2px(mDotSize));
        mDotDistance = (int) array.getDimension(R.styleable.BannerView_dotDistance, dip2px(mDotDistance));

        // 获取底部的颜色
        mBottomColor = array.getColor(R.styleable.BannerView_bottomColor, mBottomColor);

        // 获取宽高比例
        mWidthProportion = array.getFloat(R.styleable.BannerView_withProportion, mWidthProportion);
        mHeightProportion = array.getFloat(R.styleable.BannerView_heightProportion, mHeightProportion);
        array.recycle();
    }

    private void initView() {
        pager = findViewById(R.id.banner_vp);
        llDotContainer = findViewById(R.id.dot_container);
        tvDesc = findViewById(R.id.banner_desc_tv);
    }

    /**
     * @author 张子扬
     * @time 2018/11/8 0008 9:56
     * @desc 设置点击事件
     */
    public void setOnBannerViewClickListener(IBannerItemListener listener) {
        pager.setupListener(listener);
    }


    /**
     * @author 张子扬
     * @time 2018/11/7 0007 16:28
     * @desc 设置适配器 , 并做好宽高处理
     */
    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        pager.setAdapter(adapter);
        initIndicator();

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            // 接口的实现类 ,只实现一种方法
            @Override
            public void onPageSelected(int position) {
                pageChanged(position);
            }
        });

        if (mHeightProportion == 0 || mWidthProportion == 0) {
            //不参与计算
            return;
        }

        //动态计算高度
        post(new Runnable() {
            @Override
            public void run() {
                // 动态指定宽高  计算高度
                int width = getMeasuredWidth();
                // 计算高度
                int height = (int) (width * mHeightProportion / mWidthProportion);
                // 指定宽高
                getLayoutParams().height = height;
                pager.getLayoutParams().height = height;
            }
        });
    }

    /**
     * @author 张子扬
     * @time 2018/11/8 0008 9:59
     * @desc 初始化点指示器
     */
    private void initIndicator() {
        int count = mAdapter.getCount();
        llDotContainer.setGravity(getDotGravity());

        //兼容重复设置 , 移除已添加的子view
        llDotContainer.removeAllViews();

        for (int i = 0; i < count; i++) {
            // 不断的往点的指示器添加圆点
            DotIndicatorView indicatorView = new DotIndicatorView(mContext);
            // 设置大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            // 设置左右间距
            params.leftMargin = mDotDistance;
            indicatorView.setLayoutParams(params);

            if (i == 0) {
                // 选中位置
                indicatorView.setImageDrawable(mIndicatorFocusDrawable);
            } else {
                // 未选中的
                indicatorView.setImageDrawable(mIndicatorNormalDrawable);
            }
            llDotContainer.addView(indicatorView);
        }
    }

    public void setupIndicator(IIndicator indicator){

    }

    /**
     * @author 张子扬
     * @time 2018/11/8 0008 10:12
     * @desc 选中点的指示
     */
    private void pageChanged(int position) {
        // 6.1 把之前亮着的点 设置为默认
        DotIndicatorView oldIndicatorView = (DotIndicatorView)
                llDotContainer.getChildAt(mCurrentPosition);
        oldIndicatorView.setImageDrawable(mIndicatorNormalDrawable);

        // 6.2 把当前位置的点 点亮  position 0 --> 2的31次方
        mCurrentPosition = position % mAdapter.getCount();
        DotIndicatorView currentIndicatorView = (DotIndicatorView)
                llDotContainer.getChildAt(mCurrentPosition);
        currentIndicatorView.setImageDrawable(mIndicatorFocusDrawable);
    }


    /**
     * @author 张子扬
     * @time 2018/11/8 0008 10:02
     * @desc 根据点属性确定显示位置
     */
    private int getDotGravity() {
        switch (mDotGravity) {
            case 0:
                return Gravity.CENTER;
            case 1:
                return Gravity.RIGHT;
            case -1:
                return Gravity.LEFT;
            default:
                return Gravity.CENTER;
        }
    }

    /**
     * @author 张子扬
     * @time 2018/11/7 0007 16:32
     * @desc 设置立即开始滚动
     */
    public void startRoll() {
        pager.startRoll();
    }
}
