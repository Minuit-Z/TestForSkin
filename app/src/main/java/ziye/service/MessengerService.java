package ziye.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 2018/11/27 0027.
 */

public class MessengerService extends Service {

    private String TAG = getClass().getSimpleName();

    private Messenger messenger = new Messenger(new MyHandler(this));

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    static class MyHandler extends Handler {

        WeakReference<Context> weakReference;

         MyHandler(Context context) {
            if (weakReference == null)
                weakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            MessengerService context = (MessengerService) weakReference.get();
            Log.e(context.TAG, "handleMessage: "+msg.getData().getString("msg"));

            Messenger replyTo = msg.replyTo;
            Message msgs=Message.obtain();
            Bundle bundle=new Bundle();
            bundle.putString("reply","I got`t");
            msgs.setData(bundle);
            try {
                replyTo.send(msgs);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    }
}
