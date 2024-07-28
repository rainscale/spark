package ale.rains.wakeup;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 重启保活管理类
 */
public class WakeupManager {
    private static volatile WakeupManager sInstance;
    private static ScheduledExecutorService scheduledExecutorService;
    private static Handler uiThreadHandler;

    public static WakeupManager getInstance() {
        if (sInstance == null) {
            synchronized (WakeupManager.class) {
                if (sInstance == null) {
                    sInstance = new WakeupManager();
                }
            }
        }
        return sInstance;
    }

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "Wakeup#" + mCount.getAndIncrement());
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    };

    private WakeupManager() {
        uiThreadHandler = new Handler(Looper.getMainLooper());
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
    }

    public ScheduledFuture postRepeatTask(Runnable task, long delay, long period, TimeUnit timeUnit) {
        return scheduledExecutorService.scheduleAtFixedRate(task, delay, period, timeUnit);
    }

    /**
     * 判断进程是否正在运行
     *
     * @param context     上下文
     * @param processName 进程名
     * @return true-正在运行，false-没有运行
     */
    public boolean isProcessRunning(Context context, String processName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : lists) {
                if (info.processName.equals(processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void postUI(Runnable task) {
        if (task == null) {
            return;
        }
        uiThreadHandler.post(task);
    }

    public void cancelTask(Runnable runnable) {
        uiThreadHandler.removeCallbacks(runnable);
    }
}