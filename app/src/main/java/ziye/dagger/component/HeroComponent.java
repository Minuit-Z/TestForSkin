package ziye.dagger.component;

import dagger.Component;
import ziye.dagger.Beanss;
import ziye.dagger.Hero;
import ziye.dagger.MyMoudle;

/**
 * Created by Administrator on 2018/11/26 0026.
 */

@Component(modules = MyMoudle.class)
public interface HeroComponent {
    Hero getHero();
    Beanss getMyMoudle();
}
