package ziye.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/9/29 0029.
 */

public class SkinResource {

    //资源通过该对象获取
    private Resources resources;
    private String mPackageName;

    public SkinResource(String path, Context context) {
        try {
            //点击 读取本地的.skin文件中的资源
            Resources superResource = context.getResources();
            //AssetsManager创建实例
            AssetManager manager = AssetManager.class.newInstance();
            //添加资源包
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);

//            String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.apk";
            //反射执行方法
            method.invoke(manager, path);
            resources = new Resources(manager, superResource.getDisplayMetrics(),
                    superResource.getConfiguration());

            mPackageName = context.getPackageManager().
                    getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
                    .packageName;
//            int drawableID2 = resources.getIdentifier("ic_test", "drawable", "ziye.skinplugin");
//            Drawable drawable = resources.getDrawable(drawableID2);
//
//            iv.setImageDrawable(drawable);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author 张子扬
     * @time 2018/10/8 0008 14:02
     * @desc 通过名字获取图片
     */
    public Drawable getDrawableByName(String resourceName) {
        try {
            int drawableID = resources.getIdentifier(resourceName, "drawable", mPackageName);
            return resources.getDrawable(drawableID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @author 张子扬
     * @time 2018/10/8 0008 14:02
     * @desc 通过名字获取颜色
     */
    public ColorStateList getColorByName(String colorName) {
        try {
            int drawableID = resources.getIdentifier(colorName, "color", mPackageName);
            return resources.getColorStateList(drawableID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
