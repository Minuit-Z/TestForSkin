package ziye.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.core.internal.registry.Handle;

import ziye.skintest.UserAidl;

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public class MessageService extends Service {

    private final Messenger messenger=new Messenger(new MessangerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //绑定
        return mBinder;
//        return messenger.getBinder();
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

    public static class MessangerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e( "handleMessage: ", msg.what+"");
        }
    }
}
