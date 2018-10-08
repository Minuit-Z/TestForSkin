package ziye.skin.attr;

import android.view.View;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public class SkinAttr {
    private String mResourceName;
    private SkinType mType;

    public SkinAttr(String resName, SkinType type) {
        this.mResourceName=resName;
        this.mType=type;
    }

    public void skin(View mView) {
        mType.skin(mView,mResourceName);
    }
}
