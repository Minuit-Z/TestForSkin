package ziye.hook;

import android.util.Log;

/**
 * Created by Administrator on 2018/10/24 0024.
 */

public class Knife {
    int damage = 10;

    public void attack() {
        Log.e("attack: ", String.format("对目标造成 %d 点伤害", damage));
    }
}
