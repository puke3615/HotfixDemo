package pk.hotfixdemo;

import android.app.Application;
import android.content.Context;

/**
 * @author zijiao
 * @version 2016/2/19
 * @Mark
 */
public class HotfixApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        //laod hotfix if need
        if (Config.isEnable()) {
            Hotfix.loadDexIfNeed(this);
        }
    }

}
