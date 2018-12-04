package ziye.skintest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import ziye.aidls.BookManagerService;

/**
 * Created by Administrator on 2018/11/27 0027.
 */

/**
 * 演示AIDL 通信与观察者
 */
public class FourActivity extends Activity {

    private Button btnCreateRemote;
    private Button btnBinderPool;

    private ServiceConnection connection;
    IBookAidl iBookAidl;
    private MyHandler handler;
    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Message msg = Message.obtain();
            msg.obj = book;
            handler.sendMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        initBase();
        initListener();
    }


    private void initBase() {
        btnCreateRemote = findViewById(R.id.btn_service_remote);
        btnBinderPool = findViewById(R.id.btn_binder_pool);
        handler = new MyHandler();
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iBookAidl = IBookAidl.Stub.asInterface(service);
                try {
                    List<Book> bookList = iBookAidl.getBookList();
                    Log.e("fourAty", "onServiceConnected: " + bookList.toString());

                    //绑定完成后在服务端进行添加一本书
                    Book newBook = new Book(3, "艺术探索3");
                    iBookAidl.addBook(newBook);
                    List<Book> bookList1 = iBookAidl.getBookList();
                    Log.e("fourAty", "onServiceConnected: added " + bookList1.toString());

                    iBookAidl.registerListener(listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    private void initListener() {
        btnCreateRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourActivity.this, BookManagerService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
        });

        btnBinderPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourActivity.this, BinderPoolActivity.class);
                startActivity(intent);
            }
        });
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("handleMessage: ", msg.obj.toString());
        }
    }

    @Override
    protected void onDestroy() {
        if (iBookAidl != null && iBookAidl.asBinder().isBinderAlive()) {
            //binder存活时
            try {
                iBookAidl.unregisterListener(listener);
                unbindService(connection);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
