package ziye.aidls;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ziye.skintest.Book;
import ziye.skintest.IBookAidl;
import ziye.skintest.IOnNewBookArrivedListener;

/**
 * Created by Administrator on 2018/11/27 0027.
 */

/**
 * 充当server端
 */
public class BookManagerService extends Service {

    private final String TAG = getClass().getSimpleName();
    private CopyOnWriteArrayList<Book> lists;

//    private CopyOnWriteArrayList<IOnNewBookArrivedListener> listeners;
    private RemoteCallbackList<IOnNewBookArrivedListener> listeners;
    private Binder binder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lists = new CopyOnWriteArrayList<>();
//        listeners = new CopyOnWriteArrayList<>();
        listeners = new RemoteCallbackList<>();
        binder = new IBookAidl.Stub() {

            @Override
            public List<Book> getBookList() throws RemoteException {
                return lists;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                lists.add(book);
            }

            @Override
            public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
                //普通list时
//                if (listeners.contains(listener)) {
//                    Log.e(TAG, "already register: ");
//                } else {
//                    listeners.add(listener);
//                    Log.e(TAG, "registerListener: " + listeners.size());
//                }

                //RemoteListenerList时
                listeners.register(listener);
            }

            @Override
            public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
                //普通list时
//                listeners.remove(listener);

                //RemoteListenerList时
                listeners.unregister(listener);
            }
        };

        lists.add(new Book(1, "安卓艺术探索 1"));
        lists.add(new Book(2, "安卓艺术探索 2"));
        new Thread(new ServiceWoker()).start();
    }

    /**
     * @author 张子扬
     * @time 2018/11/28 0028 10:24
     * @desc 加入一本书, 并通知注册监听
     */
    private void addBook(Book book) throws RemoteException {
        lists.add(book);

//      普通list时
//        for (int i = 0; i < listeners.size(); i++) {
//            IOnNewBookArrivedListener listener = listeners.get(i);
//            Log.e(TAG, "addBook: notify listener" + book.toString());
//            listener.onNewBookArrived(book);
//        }


        //RemoteListenerList时
        int N = listeners.beginBroadcast();
        for (int i=0;i<N;i++){
            IOnNewBookArrivedListener listenersBroadcastItem = listeners.getBroadcastItem(i);
            if (listenersBroadcastItem!=null){
                listenersBroadcastItem.onNewBookArrived(book);
            }
        }

        listeners.finishBroadcast();
    }

    /**
     * 开启线程 , 模拟5秒加入一本书
     */
    private class ServiceWoker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                    int bookId = lists.size() + 1;
                    Book newBook = new Book(bookId, "Crazy" + bookId);

                    //添加一本书
                    Log.e(TAG, "服务端生成一本书: " + newBook.toString());
                    addBook(newBook);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
