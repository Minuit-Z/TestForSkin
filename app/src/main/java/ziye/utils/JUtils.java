package ziye.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/10/26 0026.
 */

public class JUtils {
    private static Context mApplicationContent;

    public static void initialize(Application app) {
        mApplicationContent = app.getApplicationContext();
    }

    public static int dip2px(float dpValue) {
        final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
