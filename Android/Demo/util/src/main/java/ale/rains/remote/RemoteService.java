package ale.rains.remote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import ale.rains.util.R;
import ale.rains.util.Logger;

public class RemoteService extends Service {
    private static final String TAG = RemoteService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("begin");
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = TAG;
            NotificationChannel nc = new NotificationChannel(channelId, channelId,
                    NotificationManager.IMPORTANCE_DEFAULT);
            nc.setDescription(channelId);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.createNotificationChannel(nc);
            }
            builder = new Notification.Builder(this.getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(this.getApplicationContext());
        }

        builder.setSmallIcon(R.color.transparent) // 设置状态栏内的小图标
                .setContentTitle("进程间通信服务") // 设置下拉列表里的标题
                .setContentText("正在运行...") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Notification notification = builder.build(); // 获取构建好的Notification
            startForeground(666, notification);
        }
        Logger.d("end");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        stopForeground(true);
    }

    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        private IRemoteServiceCallback callback;

        @Override
        public void sendMessage(int type, String message) throws RemoteException {
            RemoteManager.getInstance().replyMessage(type, message);
            if (callback != null) {
                callback.replyMessage(type, message);
            } else {
                Logger.d("callback is null");
            }
            Logger.d(type + ":" + message);
        }

        @Override
        public void registerCallback(IRemoteServiceCallback cb) throws RemoteException {
            Logger.d("registerCallback");
            callback = cb;
        }

        @Override
        public void unregisterCallback(IRemoteServiceCallback cb) throws RemoteException {
            Logger.d("unregisterCallback");
            callback = null;
        }
    };
}