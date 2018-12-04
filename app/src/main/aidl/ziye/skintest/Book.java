package ziye.skintest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/11/27 0027.
 */

public class Book implements Parcelable {

    private String name;
    private int bookId;

    protected Book(Parcel in) {
        name = in.readString();
        bookId = in.readInt();
    }

    public Book(int bookId, String name) {
        this.name = name;
        this.bookId = bookId;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(bookId);
    }

    @Override
    public String toString() {
        return "bookId="+bookId+"  bookName="+name;
    }
}
