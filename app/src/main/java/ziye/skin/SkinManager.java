package ziye.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ziye.callback.ISkinChangeInterface;
import ziye.skin.attr.SkinView;
import ziye.skintest.MainActivity;
import ziye.utils.Config;
import ziye.utils.SpHelper;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public class SkinManager {

    private static SkinManager instance;
    private static Context mContext;
    private static SkinResource skinResource;

    private static Map<ISkinChangeInterface, List<SkinView>> mSkinViews = new ArrayMap<>();

    public static SkinManager getInstance(@Nullable Context context) {
        if (context != null)
            mContext = context.getApplicationContext();
        if (instance == null)
            instance = new SkinManager();
        return instance;
    }

    public void init() {
        //每次打开应用检测是否皮肤文件可用

        //检查缓存的皮肤路径
        String path = SpHelper.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(path)) {
            loadDefault();
            //path为空, 加载默认资源
        } else {
            //path不为空, 校验文件
            //皮肤包被破坏

            String mPackageName = mContext.getPackageManager().
                    getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
                    .packageName;
            if (TextUtils.isEmpty(mPackageName)) {
                SpHelper.getInstance(mContext).clearInfo();
                loadDefault();
                return;
            }

            //最好获取包名
            File file = new File(path);
            if (!file.exists()) {
                //文件不存在,初始化默认
                SpHelper.getInstance(mContext).clearInfo();
                loadDefault();
                return;
            }

            //最好校验签名

            //做初始化工作,保证重启app时skinResouce被初始化
            skinResource = new SkinResource(path, mContext);
            Log.e("", "init: ");
        }
    }


    public int loadSkin(String path) {
        //如果当前的皮肤即为该皮肤 , 不进行替换
        String currentPath = SpHelper.getInstance(mContext).getSkinPath();
        if (path.equals(currentPath)) {
            return Config.STATUS_NO_CHANGE;
        }

        //检测文件是否存在
        File file = new File(path);
        if (!file.exists()) {
            return Config.STATUS_FILE_NOT_EXISTS;
        }

        //检测是不是一个apk
        String packageName = mContext.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
                .packageName;
        if (TextUtils.isEmpty(packageName)) {
            return Config.STATUS_FILE_EXCEPTION;
        }

        //校验签名(暂不实现)

        //初始化资源管理
        skinResource = new SkinResource(path, mContext);

        //改变皮肤
        changeSkin();

        //保存皮肤的状态
        saveSkinStatusPath(path);
        return Config.STATUS_CHANGE_SUCCESS;
    }

    /**
    *@author 张子扬
    *@time 2018/10/9 0009 17:49
    *@desc 改变皮肤, 记录属性
    */
    private void changeSkin() {
        Set<ISkinChangeInterface> keys = mSkinViews.keySet();
        for (ISkinChangeInterface key : keys) {
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }

            //通知回调
            key.changeSkin(skinResource);
        }
    }

    private void saveSkinStatusPath(String path) {
        SpHelper.getInstance(mContext).saveSkinPath(path);
    }

    public int loadDefault() {
        //判断当前有没有皮肤
        String path = SpHelper.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(path)) {
            //当前未换肤
            return Config.STATUS_NO_CHANGE;
        }

        //传入当前运行的apk的路径
        String localSkinPath = mContext.getPackageResourcePath();
        skinResource = new SkinResource(localSkinPath, mContext);

        //改变皮肤
        changeSkin();

        //皮肤信息清空
        saveSkinStatusPath("");
        return Config.STATUS_CHANGE_SUCCESS;
    }

    //通过Activity获取属性集
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    /**
     * @author 张子扬
     * @time 2018/10/8 0008 14:46
     * @desc 注册
     */
    public void register(ISkinChangeInterface iSkin, List<SkinView> skinViews) {
        mSkinViews.put(iSkin, skinViews);
    }

    /**
     * @author 张子扬
     * @time 2018/10/8 0008 14:48
     * @desc 获取当前的资源管理
     */
    public SkinResource getSkinResource() {
        return skinResource;
    }

    /**
     * @author 张子扬
     * @time 2018/10/9 0009 10:52
     * @desc 检测要不要换肤
     */
    public void checkChangeSkin(SkinView skinView) {
        //如果当前有保存皮肤路径
        String path = SpHelper.getInstance(mContext).getSkinPath();
        if (!TextUtils.isEmpty(path)) {
            skinView.skin();
        }
    }

    public void unregister(ISkinChangeInterface iSkin) {
        //aty退出时, map持有的相应的引用置空
        mSkinViews.remove(iSkin);
    }
}
