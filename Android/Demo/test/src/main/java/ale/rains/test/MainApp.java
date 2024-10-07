package ale.rains.test;

import android.app.Application;
import android.os.Environment;

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
        // getCacheDir(): /data/user/0/ale.rains.test/cache
        Logger.i("getCacheDir(): " + AppContext.getContext().getCacheDir());
        // getFilesDir(): /data/user/0/ale.rains.test/files
        Logger.i("getFilesDir(): " + AppContext.getContext().getFilesDir());
        // getDatabasePath(): /data/user/0/ale.rains.test/databases/db
        Logger.i("getDatabasePath(): " + AppContext.getContext().getDatabasePath("db"));
        // getExternalCacheDir(): /storage/emulated/0/Android/data/ale.rains.test/cache
        Logger.i("getExternalCacheDir(): " + AppContext.getContext().getExternalCacheDir());
        // getExternalFilesDir(): /storage/emulated/0/Android/data/ale.rains.test/files
        Logger.i("getExternalFilesDir(): " + AppContext.getContext().getExternalFilesDir(null));
        // getExternalStorageDirectory(): /storage/emulated/0
        Logger.i("getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory());
        // getExternalStoragePublicDirectory(): /storage/emulated/0/Download
        Logger.i("getExternalStoragePublicDirectory(): " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        initCommunication();
    }

    public void initCommunication() {
        RemoteManager.getInstance().init(getApplicationContext());
        Logger.i("remote service " + (RemoteManager.getInstance().isBound() ? "connected" : "disconnected"));
        RemoteManager.getInstance().registerOnServiceConnection(
                state -> Logger.i("remote service " + (state ? "connected" : "disconnected")));
    }
}