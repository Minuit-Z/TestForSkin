package ziye.skintest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ziye.adapter.TextExpandableAdapter;
import ziye.service.MessageService;
import ziye.service.MessengerService;

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public class SecondActivity extends Activity {

    private Button btn_name, btn_pass, btn_create, btn_aty3;
    private ExpandableListView expandableListView;

    public String[] groupString = {"射手", "辅助", "坦克", "法师"};
    public String[][] childString = {{"孙尚香", "后羿", "马可波罗", "狄仁杰"},
            {"孙膑", "蔡文姬", "鬼谷子", "杨玉环"},
            {"张飞", "廉颇", "牛魔", "项羽"},
            {"诸葛亮", "王昭君", "安琪拉", "干将"}};

    private TextExpandableAdapter adapter;
    //客服端必须获取aidl的实例 ,而实例为服务端返回的,即service中
    private UserAidl mUserAidl;
    //IBinder对象的死亡代理
    private IBinder.DeathRecipient deathRecipient;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //链接开始,连接好的service即为服务端的iBinder
            //利用服务端的IBinder可以调用相关的方法
            try {

                mUserAidl = UserAidl.Stub.asInterface(service);
                service.linkToDeath(deathRecipient, 0);

                //messenger
//                Messenger messenger=new Messenger(service);
//                Message msg=Message.obtain();
//                messenger.send();
            } catch (RemoteException e) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            mUserAidl = null;
        }
    };


    //新进程服务
    private Messenger messenger;
    private ServiceConnection createConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            Message msg = Message.obtain();
            Bundle data = new Bundle();
            data.putString("msg", "from main process");
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Messenger mGetReplyMessenger = new Messenger(new MyHandler());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btn_name = findViewById(R.id.btn_name);
        btn_pass = findViewById(R.id.btn_pass);
        btn_create = findViewById(R.id.btn_create_service);
        btn_aty3 = findViewById(R.id.btn_aty3);
        expandableListView = findViewById(R.id.expanded_rcy);
        deathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                if (mUserAidl == null) {
                    return;
                }
                mUserAidl.asBinder().unlinkToDeath(deathRecipient, 0);
                mUserAidl = null;

                Intent i = new Intent(SecondActivity.this, MessageService.class);
                bindService(i, connection, BIND_AUTO_CREATE);
            }
        };

        startService(new Intent(this, MessageService.class));

        //此处应该为客户端代码
        //使用隐式意图
        Intent i = new Intent(this, MessageService.class);
        bindService(i, connection, BIND_AUTO_CREATE);
        initExpanableList();

        initListener();
    }

    private void initExpanableList() {
        ArrayList<String> lists = new ArrayList<>(Arrays.asList(groupString));
        ArrayList<List<String>> childLists = new ArrayList<>();

        for (String[] strings : childString) {
            childLists.add(Arrays.asList(strings));
        }

        Log.e("initExpanableList: ", "");
        adapter = new TextExpandableAdapter(this, lists, childLists);
        expandableListView.setAdapter(adapter);
    }

    private void initListener() {
        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(SecondActivity.this, mUserAidl.getUserPass() + "", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Toast.makeText(SecondActivity.this, mUserAidl.getUserName() + "", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MessengerService.class);
                bindService(intent, createConnection, BIND_AUTO_CREATE);
            }
        });

        btn_aty3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, FourActivity.class));
            }
        });
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("handleMessage: ", msg.getData().getString("reply"));
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
