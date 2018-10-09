package ziye.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ziye.skin.SkinManager;
import ziye.skin.SkinResource;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public enum SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View mView, String mResourceName) {
            //获取资源配置
            SkinResource skinResource = getSkinResource();
            ColorStateList colorName = skinResource.getColorByName(mResourceName);
            if (colorName == null) return;

            ((TextView) mView).setTextColor(colorName);
        }
    },
    BACKGROUND("background") {
        @Override
        public void skin(View mView, String mResourceName) {
            //获取资源配置

            //背景可能是图片和颜色
            SkinResource skinResource = getSkinResource();

            //视为背景
            Drawable drawableByName = skinResource.getDrawableByName(mResourceName);
            if (drawableByName != null)
                mView.setBackground(drawableByName);
            else {
                ColorStateList colorByName = skinResource.getColorByName(mResourceName);
                if (colorByName != null)
                    mView.setBackgroundColor(colorByName.getDefaultColor());
            }

        }
    },
    SRC("src") {
        @Override
        public void skin(View mView, String mResourceName) {
            //获取资源配置
            SkinResource skinResource = getSkinResource();
            Drawable drawableByName = skinResource.getDrawableByName(mResourceName);
            if (drawableByName == null) return;

            ((ImageView) mView).setImageDrawable(drawableByName);
        }
    };

    private String resourceName;

    SkinType(String resourceName) {
        this.resourceName =resourceName;
    }

    public abstract void skin(View mView, String mResourceName);

    public String getResName() {
        return resourceName;
    }

    public SkinResource getSkinResource() {
        return SkinManager.getInstance(null).getSkinResource();
    }
}
