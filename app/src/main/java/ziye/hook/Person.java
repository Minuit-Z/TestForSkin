package ziye.hook;

/**
 * Created by Administrator on 2018/10/24 0024.
 */

public class Person {

    private Knife weaponMain;

    public Person(Knife weaponMain) {
        this.weaponMain = weaponMain;
    }

    public void attack() {
        weaponMain.attack();
    }

}
