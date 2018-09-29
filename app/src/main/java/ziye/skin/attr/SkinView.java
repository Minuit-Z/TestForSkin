package ziye.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public class SkinView {

    private View mView;
    private List<SkinAttr> mSkinAttrs;


    public SkinView(View v, List<SkinAttr> skin) {
        this.mView=v;
        this.mSkinAttrs=skin;
    }


    public void skin() {
        for (SkinAttr mAttr : mSkinAttrs) {
            mAttr.skin(mView);
        }
    }
}
