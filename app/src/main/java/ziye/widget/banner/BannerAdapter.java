package ziye.widget.banner;

import android.view.View;

/**
 * Created by Administrator on 2018/11/7 0007.
 */

public abstract class BannerAdapter {

    /**
     * 1.获取根据位置获取ViewPager里面的子View
     */
    public abstract View getView(int position, View convertView);

    /**
     * 5.获取轮播的数量
     */
    public abstract int getCount();
}
