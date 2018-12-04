package ziye.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ziye.utils.BinderPool;

/**
 * Created by Administrator on 2018/12/4 0004.
 */

public class BindPoolService extends Service{
    private static final String TAG = "DEBUG-WCL: " + BindPoolService.class.getSimpleName();

    private Binder mBinderPool = new BinderPool.BinderPoolImpl(); // 动态选择Binder

    @Nullable @Override public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mBinderPool;
    }
}
