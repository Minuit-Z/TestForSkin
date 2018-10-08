package ziye.skin;

import android.app.Activity;
import android.content.Context;
import android.util.ArrayMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ziye.skin.attr.SkinView;
import ziye.skintest.MainActivity;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public class SkinManager {

    private static SkinManager instance;
    private Context context;
    private SkinResource skinResource;

    private static Map<Activity,List<SkinView>> mSkinViews=new ArrayMap<>();

    public static SkinManager getInstance() {
        if (instance == null)
            instance = new SkinManager();
        return instance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }


    public int loadSkin(String path) {
        //校验签名

        //初始化资源管理
        skinResource = new SkinResource(path, context);

        //改变皮肤
        Set<Activity> keys = mSkinViews.keySet();
        for (Activity key : keys) {
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
        return 0;
    }

    public int loadDefault() {
        return 0;
    }

    //通过Activity获取属性集
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    /**
    *@author 张子扬
    *@time 2018/10/8 0008 14:46
    *@desc 注册
    */
    public void register(MainActivity mainActivity, List<SkinView> skinViews) {
        mSkinViews.put(mainActivity,skinViews);
    }

    /**
    *@author 张子扬
    *@time 2018/10/8 0008 14:48
    *@desc 获取当前的资源管理
    */
    public SkinResource getSkinResource(){
        return skinResource;
    }
}
