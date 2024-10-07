package ale.rains.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtils {
    private static final int CPU_COUNT = Math.max(Runtime.getRuntime().availableProcessors(), 8);
    private static final int CORE_POOL_SIZE = Math.max(4, Math.min(CPU_COUNT - 1, 4));
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 60;
    private static Handler sUiThreadHandler;
    private static Handler sWorkThreadHandler;
    private static ConcurrentHashMap<String, ExecutorService> sSingleThreadPoolMap = new ConcurrentHashMap<>();
    private volatile static ExecutorService sCachedThreadPool = null;
    private static ScheduledExecutorService sScheduledExecutorService;
    private static ExecutorService sWorkThreadPool = null;
    private static final BlockingQueue<Runnable> sWorkThreads = new LinkedBlockingQueue<>(128);
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "default#" + count.getAndIncrement());
        }
    };


    private static final ThreadFactory LOW_PRIORITYTF = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "low#" + count.getAndIncrement());
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    };

    public static Handler getUiThreadHandler() {
        if (sUiThreadHandler == null) {
            synchronized (ThreadPoolUtils.class) {
                if (sUiThreadHandler == null) {
                    sUiThreadHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sUiThreadHandler;
    }

    public static void postUi(Runnable r) {
        if (r == null) {
            return;
        }
        getUiThreadHandler().post(r);
    }

    public static void postUiDelay(Runnable r, long dealyMillis) {
        if (r == null) {
            return;
        }
        getUiThreadHandler().postDelayed(r, dealyMillis);
    }

    public static void removeUi(Runnable r) {
        getUiThreadHandler().removeCallbacks(r);
    }

    public static Handler getWorkThreadHandler() {
        if (sWorkThreadHandler == null) {
            synchronized (ThreadPoolUtils.class) {
                if (sWorkThreadHandler == null) {
                    HandlerThread workThread = new HandlerThread("workThread");
                    workThread.start();
                    Looper looper = workThread.getLooper();
                    if (looper != null) {
                        sWorkThreadHandler = new Handler(looper);
                    }
                }
            }
        }
        return sWorkThreadHandler;
    }

    public static void postWork(Runnable r) {
        if (r == null) {
            return;
        }
        if (getWorkThreadHandler() != null) {
            getWorkThreadHandler().post(r);
        }
    }

    public static void postWorkDelay(Runnable r, long dealyMillis) {
        if (r == null) {
            return;
        }
        if (getWorkThreadHandler() != null) {
            getWorkThreadHandler().postDelayed(r, dealyMillis);
        }
    }

    public static void removeWork(Runnable r) {
        if (getWorkThreadHandler() != null) {
            getWorkThreadHandler().removeCallbacks(r);
        }
    }

    public static synchronized ExecutorService getSingleExecutor(String key) {
        ExecutorService singleThreadPool = sSingleThreadPoolMap.get(key);
        if (singleThreadPool == null) {
            singleThreadPool = Executors.newSingleThreadExecutor();
            sSingleThreadPoolMap.put(key, singleThreadPool);
        }
        return singleThreadPool;
    }

    public static synchronized void removeSingleExecutor(String key) {
        if (key != null) {
            sSingleThreadPoolMap.remove(key);
        }
    }

    public static Executor getCachedExecutor() {
        if (sCachedThreadPool == null) {
            synchronized (ThreadPoolUtils.class) {
                if (sCachedThreadPool == null) {
                    sCachedThreadPool = Executors.newCachedThreadPool();
                }
            }
        }
        return sCachedThreadPool;
    }

    public static void postCachedThread(Runnable r) {
        if (getCachedExecutor() != null) {
            getCachedExecutor().execute(r);
        }
    }

    public static ScheduledExecutorService getScheduledExecutor() {
        if (sScheduledExecutorService == null) {
            synchronized (ThreadPoolUtils.class) {
                if (sScheduledExecutorService == null) {
                    sScheduledExecutorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE, LOW_PRIORITYTF);
                }
            }
        }
        return sScheduledExecutorService;
    }

    public static ScheduledFuture scheduleAtFixedRate(Runnable r, long delay, long period, TimeUnit unit) {
        return getScheduledExecutor().scheduleAtFixedRate(r, delay, period, unit);
    }

    public static ScheduledFuture schedule(Runnable r, long delay, TimeUnit unit) {
        return getScheduledExecutor().schedule(r, delay, unit);
    }

    public static Executor getWorkExecutor() {
        if (sWorkThreadPool == null) {
            synchronized (ThreadPoolUtils.class) {
                if (sWorkThreadPool == null) {
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(
                            CPU_COUNT, MAX_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                            sWorkThreads, DEFAULT_THREAD_FACTORY);
                    executor.allowCoreThreadTimeOut(true);
                    sWorkThreadPool = executor;
                }
            }
        }
        return sWorkThreadPool;
    }

    public void postWorkThread(Runnable r) {
        if (getWorkExecutor() != null) {
            getWorkExecutor().execute(r);
        }
    }
}
