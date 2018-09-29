package ziye.skin.attr;

import android.view.View;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public enum SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View mView, String mResourceName) {

        }
    },
    BACKGROUND("background") {
        @Override
        public void skin(View mView, String mResourceName) {

        }
    },
    SRC("src") {
        @Override
        public void skin(View mView, String mResourceName) {

        }
    };

    private String resourceName;

    SkinType(String resourceName) {
        this.resourceName = name();
    }

    public abstract void skin(View mView, String mResourceName);
}
