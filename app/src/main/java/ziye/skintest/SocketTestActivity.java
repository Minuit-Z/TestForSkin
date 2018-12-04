package ziye.skintest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import ziye.service.TCPServerService;

/**
 * Created by Administrator on 2018/12/4 0004.
 */

public class SocketTestActivity extends Activity {

    TextView tvContent;
    EditText edComment;
    Button btnSend;

    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private PrintWriter mWriter;
    private Socket mClinetSocket;


    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG:
                    tvContent.setText(tvContent.getText() + msg.obj.toString());
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    btnSend.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        initBase();
        initListener();
    }

    private void initListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = edComment.getText().toString();
                new Thread() {
                    @Override
                    public void run() {
                        if (mWriter != null) {
                            mWriter.println(msg);
                            edComment.setText("");
                            String time = new SimpleDateFormat("HH:mm:ss").format(
                                    SystemClock.currentThreadTimeMillis());
                            String showMsg = "self" + time + " : " + msg + "\n";
                            tvContent.setText(tvContent.getText() + showMsg);
                        }
                    }
                }.start();
            }
        });
    }

    private void initBase() {
        tvContent = findViewById(R.id.tv_content);
        edComment = findViewById(R.id.ed_comment);
        btnSend = findViewById(R.id.btn_send);

        Intent serviceIntent = new Intent(this, TCPServerService.class);
        startService(serviceIntent);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();

    }

    private void connectTCPServer() {
        Socket socket = null;

        //循环建立链接
        while (mClinetSocket == null) {
            try {
                socket = new Socket("localhost", 8868);
                mClinetSocket = socket;

                mWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(mClinetSocket.getOutputStream())
                ), true);

                mhandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                Log.e("msg: ", "connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                Log.e("msg: ", "connected failed , retry.......");
            }
        }

        //链接完成开始取消息
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mClinetSocket.getInputStream()));
            while (!SocketTestActivity.this.isFinishing()) {
                String msg = reader.readLine();
                Log.e("msg: ", "receive: " + msg);

                if (msg != null) {
                    String time = new SimpleDateFormat("HH:mm:ss").format(new Date(SystemClock.currentThreadTimeMillis()));

                    final String showMsg = "server  " + time + " : " + msg + "\n";
                    mhandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showMsg).sendToTarget();
                }
            }

            Log.e("msg: ", "quit....");
            mWriter.close();
            reader.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mClinetSocket != null) {
            try {
                mClinetSocket.shutdownInput();
                mClinetSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onDestroy();
    }
}
