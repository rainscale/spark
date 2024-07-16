package ale.rains.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {
    private static ConcurrentHashMap<String, ExecutorService> sSingleThreadPoolMap = new ConcurrentHashMap<>();
    private volatile static ExecutorService sCachedThreadPool = null;

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
}
