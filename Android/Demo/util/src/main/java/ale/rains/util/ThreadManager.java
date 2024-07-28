package ale.rains.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理
 * <p>
 * 提供主线程接口，工作线程池，IO线程池
 */
public class ThreadManager {
    private static final int BACKGROUND_THREAD_POOL_MAX_SIZE = 2;
    private volatile static ThreadManager sInstance = null;
    private Handler mMainHandler = null;
    private ExecutorService mWorkThreadPool = null;
    private ExecutorService mIOThreadPool = null;

    public static class CustomThreadFactory implements ThreadFactory {
        @SuppressLint("DefaultLocale")
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName(String.format("%s#%d", "CustomThread", t.getId()));
            return t;
        }
    }

    private ThreadManager() {
        initMainHandler();
        initWorkThreadPool();
        initIOThreadPool();
    }

    public static ThreadManager getInstance() {
        if (sInstance == null) {
            synchronized (ThreadManager.class) {
                if (sInstance == null) {
                    sInstance = new ThreadManager();
                }
            }
        }
        return sInstance;
    }

    private void initMainHandler() {
        if (mMainHandler == null) {
            synchronized (this) {
                if (mMainHandler == null) {
                    mMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
    }

    /**
     * 获取当前的处理器个数
     */
    private int getAvailableProcessorsCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    private void initWorkThreadPool() {
        if (mWorkThreadPool == null) {
            synchronized (this) {
                if (mWorkThreadPool == null) {
                    mWorkThreadPool = new ThreadPoolExecutor(getAvailableProcessorsCount(), getAvailableProcessorsCount() * 2 + 1,
                            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), new CustomThreadFactory());
                }
            }
        }
    }

    private void initIOThreadPool() {
        if (mIOThreadPool == null) {
            synchronized (this) {
                mIOThreadPool = new ThreadPoolExecutor(0, BACKGROUND_THREAD_POOL_MAX_SIZE,
                        60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), new CustomThreadFactory());
            }
        }
    }

    public void postUITask(Runnable runnable) {
        postUITaskDelayed(runnable, 0);
    }

    public void postUITaskDelayed(Runnable runnable, long delayMillis) {
        if (mMainHandler != null) {
            mMainHandler.postDelayed(runnable, delayMillis);
        }
    }

    /**
     * 优先执行
     */
    public void postUITaskFront(Runnable runnable) {
        if (mMainHandler != null) {
            mMainHandler.postAtFrontOfQueue(runnable);
        }
    }

    public void removeUITask(Runnable runnable) {
        if (mMainHandler != null) {
            mMainHandler.removeCallbacks(runnable);
        }
    }

    public ExecutorService getWorkThreadPool() {
        return mWorkThreadPool;
    }

    public void postWorkTask(Runnable runnable) {
        if (mWorkThreadPool != null) {
            mWorkThreadPool.execute(runnable);
        }
    }

    public void postIOTask(Runnable runnable) {
        if (mIOThreadPool != null) {
            mIOThreadPool.execute(runnable);
        }
    }

    /**
     * 是否是主线程
     *
     * @return true-主线程 false-非主线程
     */
    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 睡眠
     *
     * @param millis 休眠时间(ms)
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            Logger.e(e);
        }
    }
}