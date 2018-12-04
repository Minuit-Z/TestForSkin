// IBookAidl.aidl
package ziye.skintest;
import ziye.skintest.Book;
import ziye.skintest.IOnNewBookArrivedListener;

// Declare any non-default types here with import statements

interface IBookAidl {

   List<Book> getBookList();
   void addBook(in Book book);

   void registerListener(IOnNewBookArrivedListener listener);
   void unregisterListener(IOnNewBookArrivedListener listener);
}
