package ale.rains.util;

import android.content.Context;

public class AppContext {
    private static AppContext sInstance;
    private Context context;

    public static synchronized AppContext getInstance() {
        if (sInstance == null) {
            sInstance = new AppContext();
        }
        return sInstance;
    }

    public void setContext(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }

    public Context getContext() {
        return context;
    }
}
