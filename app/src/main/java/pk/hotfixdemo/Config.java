package pk.hotfixdemo;

import android.content.SharedPreferences;

/**
 * @author zijiao
 * @version 2016/2/19
 * @Mark
 */
public class Config {

    private static final String NAME = "Config";
    private static final String ENABLE = "ENABLE";
    private static final SharedPreferences sp;

    static {
        sp = HotfixApplication.sContext.getSharedPreferences(NAME, 0);
    }

    public static void setEnable(boolean enable) {
        sp.edit().putBoolean(ENABLE, enable).commit();
    }

    public static boolean isEnable() {
        return sp.getBoolean(ENABLE, false);
    }

}
