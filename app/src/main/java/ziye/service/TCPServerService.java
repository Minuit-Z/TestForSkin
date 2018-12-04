package ziye.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Administrator on 2018/12/4 0004.
 */

public class TCPServerService extends Service {
    public boolean mIsServiceDestoryed = false;
    private String[] mRandomMessgae = new String[]{
            "msg1111",
            "msg2222",
            "msg3333"
    };

    @Override
    public IBinder onBind(Intent intent) {
        //不使用binder通信 , 暂时返回null
        return null;
    }

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8868);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestoryed) {
                //接收客户端请求
                try {
                    final Socket client = serverSocket.accept();
                    Log.e("msg: ", "accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("聊天室链接");

        while (!mIsServiceDestoryed) {
            String readLine = in.readLine();
            Log.e("msg: ", "msg from client :" + readLine);

            if (readLine == null) {
                //断开连接
                break;
            }

            int i = new Random().nextInt(mRandomMessgae.length);
            String msg = mRandomMessgae[i];

            out.println(msg);
            Log.e("msg: ", msg);
        }

        Log.e("msg: ", "client quit");
        //关闭流
        in.close();
        out.close();
        client.close();
    }
}
