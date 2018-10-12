package ziye.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import ziye.skintest.UserAidl;

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public class MessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //绑定
        return mBinder;
    }

    private final UserAidl.Stub mBinder = new UserAidl.Stub() {
        @Override
        public String getUserName() throws RemoteException {
            return "username";
        }

        @Override
        public String getUserPass() throws RemoteException {
            return "123456";
        }
    };
}
