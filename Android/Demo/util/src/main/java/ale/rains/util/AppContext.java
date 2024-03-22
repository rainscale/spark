package ale.rains.util;

import android.content.Context;

public class AppContext {
    private static Context sContext;

    public static void init(Context context) {
        if (context == null) {
            return;
        }
        sContext = context.getApplicationContext();
    }

    public Context getContext() {
        return sContext;
    }
}
