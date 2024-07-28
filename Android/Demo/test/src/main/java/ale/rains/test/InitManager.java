package ale.rains.test;

import android.content.Context;

import ale.rains.test.constant.Constants;
import ale.rains.util.AppContext;
import ale.rains.util.Logger;

public class InitManager {
    public static void init(Context context) {
        if (context == null) {
            Logger.e("context is null");
            return;
        }
        init(context, Constants.APP_TAG, context.getCacheDir().getAbsolutePath(), 1);
    }

    public static void init(Context context, String logTag, String logSavePath) {
        init(context, logTag, logSavePath, 1);
    }

    public static void init(Context context, String logTag, String logSavePath, int logCacheSize) {
        if (context == null) {
            Logger.e("context is null");
            return;
        }
        AppContext.init(context, logTag);
        Logger.init(logTag, logSavePath, logCacheSize);
    }
}