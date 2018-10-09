package ziye.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/10/9 0009.
 */

public class SpHelper {

    private static SpHelper instance;
    private Context mContext;

    private SpHelper(Context context) {
        this.mContext = context;
    }

    public static SpHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (SpHelper.class) {
                if (instance == null) {
                    instance = new SpHelper(context);
                }
            }
        }

        return instance;
    }

    /**
     * @author 张子扬
     * @time 2018/10/9 0009 10:38
     * @desc 保存当前皮肤的路径
     */
    public void saveSkinPath(String skinPath) {
        mContext.getSharedPreferences(Config.SP_SKIN_FILE, Context.MODE_PRIVATE).edit().
                putString(Config.SP_SKIN_VALUE, skinPath).commit();
    }

    public String getSkinPath() {
        return mContext.getSharedPreferences(Config.SP_SKIN_FILE, Context.MODE_PRIVATE)
                .getString(Config.SP_SKIN_VALUE , "");
    }

    public void clearInfo() {
        saveSkinPath("");
    }
}
