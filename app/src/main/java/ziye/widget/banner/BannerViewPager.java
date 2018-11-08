package ziye.widget.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/6 0006.
 */

public class BannerViewPager extends ViewPager {
    private BannerAdapter mAdapter;
    private List<View> mConvertViews;

    // 实现自动轮播 - 发送消息的msgWhat
    private final int SCROLL_MSG = 0x0011;

    // 实现自动轮播 - 页面切换间隔时间
    private int mCutDownTime = 3500;

    //设置是否可以滚动
    private boolean mScrollAble = true;
    private IBannerItemListener listener;
    private MyHandler mHandler;
    // 10.内存优化 --> 当前Activity
    private Activity mActivity;
    private Application.ActivityLifecycleCallbacks callbacks;

    public BannerViewPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        mConvertViews = new ArrayList();
        initHandler();
    }

    public void initHandler() {
        mHandler = new MyHandler(this);
        callbacks = new DefaultACtivityCallback() {
            @Override
            public void onActivityResumed(Activity activity) {
                // 是不是监听的当前Activity的生命周期
                // Log.e("TAG", "activity --> " + activity + "  context-->" + getContext());
                if (activity == mActivity) {
                    // 开启轮播
                    startRoll();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (activity == mActivity) {
                    // 停止轮播
                    mHandler.removeMessages(SCROLL_MSG);
                }
            }
        };
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mHandler != null)
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHandler.removeMessages(SCROLL_MSG);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mHandler.removeMessages(SCROLL_MSG);
                    break;
                case MotionEvent.ACTION_UP:
                    mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
                    break;
            }
        return super.onTouchEvent(ev);
    }

    /**
     * @author 张子扬
     * @time 2018/11/7 0007 16:33
     * @desc 设置适配器
     */
    public void setAdapter(BannerAdapter adapter) {
        mAdapter = adapter;
        setAdapter(new BannerPagerAdapter());
    }


    /**
     * @author 张子扬
     * @time 2018/11/7 0007 16:32
     * @desc 开始滚动, 实现轮播
     */
    public void startRoll() {
        if (mAdapter == null) {
            return;
        }
        // 判断是不是只有一条数据
        mScrollAble = mAdapter.getCount() != 1;

        if (mScrollAble && mHandler != null) {
            // 清除消息
            mHandler.removeMessages(SCROLL_MSG);
            // 消息  延迟时间  让用户自定义  有一个默认  3500
            mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
        }
    }

    /**
     * @author 张子扬
     * @time 2018/11/8 0008 9:33
     * @desc 设置点击监听
     */
    public void setupListener(IBannerItemListener listener) {
        this.listener = listener;
    }


    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        /**
         * @author 张子扬
         * @time 2018/11/7 0007 16:42
         * @desc ViewPager创建页卡
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            //从mAdapter中拿到创建的View
            View view = mAdapter.getView(position % mAdapter.getCount(),
                    getConvertView());

            // 添加ViewPager里面
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 回调点击监听
                    if (listener != null) {
                        listener.click(position % mAdapter.getCount());
                    }
                }
            });

            return view;
        }

        /**
         * @author 张子扬
         * @time 2018/11/7 0007 16:42
         * @desc ViewPager销毁页卡
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            mConvertViews.add((View) object);
        }
    }

    class MyHandler extends Handler {

        private WeakReference<BannerViewPager> reference;

        public MyHandler(BannerViewPager bannerViewPager) {
            this.reference = new WeakReference<>(bannerViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            // 转到下一页
            BannerViewPager viewPager = reference.get();
            if (viewPager != null) {
                viewPager.setCurrentItem(getCurrentItem() + 1);
                viewPager.startRoll();//循环
            }
        }
    }

    /**
     * 获取复用界面来创建pager
     */
    public View getConvertView() {
        for (int i = 0; i < mConvertViews.size(); i++) {
            if (mConvertViews.get(i).getParent() == null) {
                return mConvertViews.get(i);
            }
        }
        return null;
    }

    /**
     * 2.销毁Handler停止发送  解决内存泄漏
     */
    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null) {
            // 销毁Handler的生命周期
            mHandler.removeMessages(SCROLL_MSG);
            // 解除绑定
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(callbacks);
            mHandler = null;
        }
        super.onDetachedFromWindow();
    }


    @Override
    protected void onAttachedToWindow() {
        if (mAdapter != null) {
            initHandler();
            startRoll();
            // 管理Activity的生命周期
            mActivity.getApplication().registerActivityLifecycleCallbacks(callbacks);
        }
        super.onAttachedToWindow();
    }
}
