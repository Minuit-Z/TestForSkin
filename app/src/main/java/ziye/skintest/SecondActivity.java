package ziye.skintest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public class SecondActivity extends Activity {

    private Button btn_name, btn_pass;
    private ExpandableListView expandableListView;

    public String[] groupString = {"射手", "辅助", "坦克", "法师"};
    public String[][] childString = {{"孙尚香", "后羿", "马可波罗", "狄仁杰"},
            {"孙膑", "蔡文姬", "鬼谷子", "杨玉环"},
            {"张飞", "廉颇", "牛魔", "项羽"},
            {"诸葛亮", "王昭君", "安琪拉", "干将"}};

    private TextExpandableAdapter adapter;
    //客服端必须获取aidl的实例 ,而实例为服务端返回的,即service中
    private UserAidl mUserAidl;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //链接开始,连接好的service即为服务端的iBinder
            //利用服务端的IBinder可以调用相关的方法
            mUserAidl = UserAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            mUserAidl = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btn_name = findViewById(R.id.btn_name);
        btn_pass = findViewById(R.id.btn_pass);
        expandableListView = findViewById(R.id.expanded_rcy);

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

        Log.e( "initExpanableList: ", "");
        adapter = new TextExpandableAdapter(this, lists,childLists);
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
    }

}
