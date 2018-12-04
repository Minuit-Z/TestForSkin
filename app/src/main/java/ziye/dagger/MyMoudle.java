package ziye.dagger;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/11/26 0026.
 */

@Module
public class MyMoudle {

    @Provides
    public Beanss createBeanss(){
        return new Beanss(10);
    }
}
