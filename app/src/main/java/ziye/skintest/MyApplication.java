package ziye.skintest;

import android.app.Application;

import ziye.utils.JUtils;

/**
 * Created by Administrator on 2018/10/26 0026.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.initialize(this);
    }
}
