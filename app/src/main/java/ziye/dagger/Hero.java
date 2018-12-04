package ziye.dagger;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/11/26 0026.
 */

public class Hero {

    private static final String TAG = "";
    private Clothes clothes;
    private Pants pants;

    @Inject
    public Hero(Clothes clothes, Pants pants) {
        this.clothes = clothes;
        this.pants = pants;
    }

    public void printDefense() {
        Log.e(TAG, "您的角色拥有防御值: " + clothes.Defense + pants.Defense );
    }


}
