package ziye.dagger;

/**
 * Created by Administrator on 2018/11/26 0026.
 */

public class DaggerTest {
    Clothes clothes = new Clothes();
    Pants pants = new Pants();


    public void test() {
        Hero hero = new Hero(clothes, pants);
        hero.printDefense();
    }


}
