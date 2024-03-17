package ale.rains.demo;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import ale.rains.demo.activity.MainActivity;
import ale.rains.util.LogUtils;

public class HelloService extends Service {
    // 渠道ID
    private static final String CHANNEL_ID = "normal";
    // 通知的唯一ID
    private static final int NOTIFICATION_UNIQUE_ID = 1000;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
        LogUtils.d("onCreate");
    }

    /*
    START_NOT_STICKY
    如果系统在 onStartCommand() 返回后终止服务，则除非有待传递的挂起 Intent，否则系统不会重建服务。
    这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。
    START_STICKY
    如果系统在 onStartCommand() 返回后终止服务，则其会重建服务并调用 onStartCommand()，
    但不会重新传递最后一个 Intent。相反，除非有挂起 Intent 要启动服务，
    否则系统会调用包含空 Intent 的 onStartCommand()。在此情况下，系统会传递这些 Intent。
    此常量适用于不执行命令、但无限期运行并等待作业的媒体播放器（或类似服务）。
    START_REDELIVER_INTENT
    如果系统在 onStartCommand() 返回后终止服务，则其会重建服务，
    并通过传递给服务的最后一个 Intent 调用 onStartCommand()。
    所有挂起 Intent 均依次传递。此常量适用于主动执行应立即恢复的作业（例如下载文件）的服务。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("onStartCommand");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
//        Message msg = serviceHandler.obtainMessage();
//        msg.arg1 = startId;
//        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
//        return START_STICKY;

        // 在API11之后构建Notification的方式
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext(), CHANNEL_ID); //获取一个Notification构造器
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher_round)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("下拉列表中的Title") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("要显示的内容") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(NOTIFICATION_UNIQUE_ID, notification);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d("onBind");
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        LogUtils.d("onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        stopForeground(true);
    }
}
