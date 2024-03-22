package ale.rains.util;

import android.content.Context;

public class InitManager {
    public static void init(Context context, String logTag, boolean logEnable) {
        AppContext.init(context);
        LogUtils.init(logTag, logEnable);
    }
}
