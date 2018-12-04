package ziye.dagger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/11/26 0026.
 */

public class Beanss implements Parcelable{

    public int attack;

    public Beanss(int attack) {
        this.attack = attack;
    }

    protected Beanss(Parcel in) {
        attack = in.readInt();
    }

    public static final Creator<Beanss> CREATOR = new Creator<Beanss>() {
        @Override
        public Beanss createFromParcel(Parcel in) {
            return new Beanss(in);
        }

        @Override
        public Beanss[] newArray(int size) {
            return new Beanss[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(attack);
    }


}
