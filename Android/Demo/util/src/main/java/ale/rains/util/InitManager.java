package ale.rains.util;

import android.content.Context;

public class InitManager {
    private static InitManager sInstance;

    public static synchronized InitManager getInstance() {
        if (sInstance == null) {
            sInstance = new InitManager();
        }
        return sInstance;
    }

    public void init(Context context, String logTag, boolean logEnable) {
        AppContext.getInstance().setContext(context);
        LogUtils.init(logTag, logEnable);
    }
}
