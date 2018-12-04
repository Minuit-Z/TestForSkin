package ziye.skintest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ziye.impl.ComputeImpl;
import ziye.impl.SecurityCenterImpl;
import ziye.utils.BinderPool;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/12/4 0004.
 */

public class BinderPoolActivity extends Activity {
    private ISecurityCenter mISecurityCenter;
    private IComputer mICompute;

    private TextView mTvEncryptMsg; // 加密数据的显示
    private TextView mTvAddMsg; // 累计数据的显示

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mTvEncryptMsg.setText((String) msg.obj);
                    break;
                case 1:
                    mTvAddMsg.setText((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binderpool);

        mTvEncryptMsg = findViewById(R.id.main_tv_encrypt_msg);
        mTvAddMsg = findViewById(R.id.main_tv_add_msg);
    }


    /**
     * 加密解密的点击回调
     *
     * @param view 界面
     */
    public void encryptMsg(View view) {
        new Thread(new Runnable() {
            @Override public void run() {
                doEncrypt();
            }
        }).start();
    }

    /**
     * 调用加密服务
     */
    private void doEncrypt() {
        BinderPool binderPool = BinderPool.getInstance(getApplicationContext());
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mISecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        String msg = "Hello, I am Spike!";
        try {
            String encryptMsg = mISecurityCenter.encrypt(msg);
            Log.e(TAG, "加密信息: " + encryptMsg);
            String decryptMsg = mISecurityCenter.decrypt(encryptMsg);
            Log.e(TAG, "解密信息: " + decryptMsg);
            Message hm = new Message();
            hm.what = 0;
            hm.obj = encryptMsg + "\n" + decryptMsg;
            mHandler.sendMessage(hm);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加法的点击回调
     * @param view 视图
     */
    public void addNumbers(View view) {
        new Thread(new Runnable() {
            @Override public void run() {
                doAddition();
            }
        }).start();
    }

    /**
     * 调用加法服务
     */
    private void doAddition() {
        BinderPool binderPool = BinderPool.getInstance(getApplicationContext());
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mICompute = ComputeImpl.asInterface(computeBinder);
        try {
            int result = mICompute.add(12, 12);
            Log.e(TAG, "12 + 12 = " + result);

            Message hm = new Message();
            hm.what = 1;
            hm.obj = result + "";
            mHandler.sendMessage(hm);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
