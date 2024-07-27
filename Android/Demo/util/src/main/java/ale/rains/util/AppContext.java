package ale.rains.util;

import android.content.Context;
import android.text.TextUtils;

public class AppContext {
    private static Context sContext;
    private static String sTag = AppContext.class.getSimpleName();

    public static void init(Context context, String tag) {
        if (context == null) {
            Logger.e("context is null");
            return;
        }
        sContext = context.getApplicationContext();
        if (!TextUtils.isEmpty(tag)) {
            sTag = tag;
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static String getTag() {
        return sTag;
    }
}
