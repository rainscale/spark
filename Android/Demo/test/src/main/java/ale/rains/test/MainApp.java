package ale.rains.test;

import android.app.Application;

import ale.rains.remote.RemoteManager;
import ale.rains.util.AppContext;
import ale.rains.util.AppUtils;
import ale.rains.util.Logger;
import ale.rains.util.StringUtils;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitManager.init(this);
        Logger.i("uid: " + AppUtils.getUid());
        String processName = AppUtils.getProcessName();
        if (!StringUtils.equals(processName, getPackageName())) {
            Logger.i("not main process");
            return;
        }
        // sourceDir: /data/app/~~XkGa9g37ne_i8zBCTDYNnw==/ale.rains.test-9iiSSpkX9JUgFDzjePKing==/base.apk
        Logger.i("sourceDir: " + AppContext.getContext().getApplicationInfo().sourceDir);
        // publicSourceDir: /data/app/~~XkGa9g37ne_i8zBCTDYNnw==/ale.rains.test-9iiSSpkX9JUgFDzjePKing==/base.apk
        Logger.i("publicSourceDir: " + AppContext.getContext().getApplicationInfo().publicSourceDir);
        // nativeLibraryDir: /data/app/~~XkGa9g37ne_i8zBCTDYNnw==/ale.rains.test-9iiSSpkX9JUgFDzjePKing==/lib/arm64
        Logger.i("nativeLibraryDir: " + AppContext.getContext().getApplicationInfo().nativeLibraryDir);
        initCommunication();
    }

    public void initCommunication() {
        RemoteManager.getInstance().init(getApplicationContext());
        Logger.i("remote service " + (RemoteManager.getInstance().isBound() ? "connected" : "disconnected"));
        RemoteManager.getInstance().registerOnServiceConnection(
                state -> Logger.i("remote service " + (state ? "connected" : "disconnected")));
    }
}