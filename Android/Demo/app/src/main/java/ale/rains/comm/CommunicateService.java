package ale.rains.comm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import ale.rains.demo.R;
import ale.rains.util.Logger;

public class CommunicateService extends Service {
    private static final String TAG = CommunicateService.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("onBind begin");
        //CommunicateManager.getInstance().init(getApplicationContext());
        // 前台服务被认为是用户主动意识到的一种服务，因此在内存不足时，系统也不会考虑将其终止。
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 8.0以上系统，一定要有channelId
            String channelId = TAG;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelId);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            builder = new Notification.Builder(this.getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(this.getApplicationContext());
        }

        builder.setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentTitle("进程间通信服务") // 设置下拉列表里的标题
                .setContentText("正在运行...") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Notification notification = builder.build(); // 获取构建好的Notification
            startForeground(9999, notification);
        }
        Logger.d("onBind end");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        stopForeground(true);
    }

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        Logger.i("dump");
        pw.append("Communicate Service");
        pw.flush();
        try {
            mBinder.dump(fd, args);
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            pw.flush();
        }
    }

    private final ICommunicateService.Stub mBinder = new ICommunicateService.Stub() {
        private ICommunicateServiceCallback callback;

        @Override
        public void doSomething(String aString) throws RemoteException {
            if (callback != null) {
                callback.replyStringMessage(CommunicateConstants.MsgType.TEST,
                        "你好，客户端，我是" + TAG + "。收到你的String：" + aString);
            }
        }

        @Override
        public void sendStringMessage(int type, String message) throws RemoteException {
            CommunicateManager.getInstance().replyStringMessage(type, message);
            if (callback != null) {
                callback.replyStringMessage(type, message);
            } else {
                Logger.d("callback is null");
            }
            Logger.d("type: " + type + ", message: " + message);
        }

        @Override
        public void registerCallback(ICommunicateServiceCallback cb) throws RemoteException {
            Logger.d("registerCallback");
            callback = cb;
        }

        @Override
        public void unregisterCallback(ICommunicateServiceCallback cb) throws RemoteException {
            Logger.d("unregisterCallback");
            callback = null;
        }
    };
}