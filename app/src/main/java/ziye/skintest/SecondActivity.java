package ziye.skintest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ziye.service.MessageService;

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public class SecondActivity extends Activity {

    private Button btn_name,btn_pass;

    //客服端必须获取aidl的实例 ,而实例为服务端返回的,即service中
    private UserAidl mUserAidl;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //链接开始,连接好的service即为服务端的iBinder
            //利用服务端的IBinder可以调用相关的方法
            mUserAidl=UserAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            mUserAidl=null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btn_name=findViewById(R.id.btn_name);
        btn_pass=findViewById(R.id.btn_pass);


        startService(new Intent(this, MessageService.class));


        //此处应该为客户端代码
        //使用隐式意图
        Intent i = new Intent(this, MessageService.class);
//        i.setAction("com.minuit.aidl");
        bindService(i, connection, BIND_AUTO_CREATE);

        initListener();
    }

    private void initListener() {
        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(SecondActivity.this, mUserAidl.getUserPass()+"", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Toast.makeText(SecondActivity.this, mUserAidl.getUserName()+"", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
