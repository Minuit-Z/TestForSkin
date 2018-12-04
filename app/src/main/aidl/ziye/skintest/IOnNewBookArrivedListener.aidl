// IOnNewBookArrivedListener.aidl
package ziye.skintest;
import ziye.skintest.Book;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {

//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
   void onNewBookArrived(in Book book);
}
