package ale.rains.demo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import ale.rains.remote.RemoteManager;
import ale.rains.toast.Toaster;
import ale.rains.toast.style.WhiteToastStyle;
import ale.rains.util.InitManager;
import ale.rains.util.Logger;

public class MyApplication extends Application {
    // 渠道名
    private static final String CHANNEL_NAME = "普通通知";
    // 渠道ID
    private static final String CHANNEL_ID = "normal";
    // 渠道描述
    private static final String CHANNEL_DESCRIPTION = "这是一个用来测试的通知";

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InitManager.init(this, "Demo", true);
        initCommunication();
        // 初始化吐司工具类
        Toaster.init(this, new WhiteToastStyle());
        createNotificationChannel();
        Logger.d("onCreate");
    }

    public void initCommunication() {
        RemoteManager.getInstance().init(getApplicationContext());
        Logger.i("remote service " + (RemoteManager.getInstance().isBound() ? "connected" : "disconnected"));
        RemoteManager.getInstance().registerOnServiceConnection(
                state -> Logger.i("remote service " + (state ? "connected" : "disconnected")));
    }
}
