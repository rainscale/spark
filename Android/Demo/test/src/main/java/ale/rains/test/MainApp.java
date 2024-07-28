package ale.rains.test;

import android.app.Application;

import ale.rains.remote.RemoteManager;
import ale.rains.util.AppUtils;
import ale.rains.util.Logger;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitManager.init(this);
        Logger.i("uid: " + AppUtils.getUid());
//        String processName = AppUtils.getProcessName();
//        if (!StringUtils.equals(processName, getPackageName())) {
//            Logger.i("not main process");
//            return;
//        }
//        Logger.i("sourceDir: " + FitaContext.getContext().getApplicationInfo().sourceDir);
//        Logger.i("publicSourceDir: " + FitaContext.getContext().getApplicationInfo().publicSourceDir);
//        Logger.i("nativeLibraryDir: " + FitaContext.getContext().getApplicationInfo().nativeLibraryDir);
        initCommunication();
    }

    public void initCommunication() {
        RemoteManager.getInstance().init(getApplicationContext());
        Logger.i("remote service " + (RemoteManager.getInstance().isBound() ? "connected" : "disconnected"));
        RemoteManager.getInstance().registerOnServiceConnection(
                state -> Logger.i("remote service " + (state ? "connected" : "disconnected")));
    }
}