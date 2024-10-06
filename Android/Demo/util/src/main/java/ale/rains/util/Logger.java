package ale.rains.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 日志操作工具类
 */
public class Logger {
    private static String TAG = "Logger";
    private static final String FILE_SPLIT_SEPARATOR = "\\.";
    private static final String FILE_NEWLINE = "\r\n";
    private static final long MAX_FILE_SIZE = 1024L * 1024 * 1024;
    private static final long MIN_FILE_SIZE = 5L * 1024 * 1024;
    private static long sThreshold = 50L * 1024 * 1024;
    private static long sLogFileSize;
    private static int sMaxCount = 100;
    private static int sCacheSize = 4;
    private static boolean isDebug = BuildConfig.DEBUG;
    private static boolean isSave = true;
    private static boolean isPrint = true;
    private static ExecutorService sLogExecutor = null;
    private static String DEFAULT_LOG_FOLDER = Environment.getExternalStorageDirectory() + File.separator;
    private static String DEFAULT_LOG_FILE_NAME = "default";
    private static List<String> sCacheLog = new ArrayList<>();
    /**
     * 日志文件夹
     */
    private static String sLogFolder = DEFAULT_LOG_FOLDER;
    /**
     * 日志文件名
     */
    private static String sLogFileName = DEFAULT_LOG_FILE_NAME;
    /**
     * 日志文件完整路径
     */
    private static String sLogPath;

    /**
     * 设置debug开关
     *
     * @param v 是否debug
     */
    public static void setDebugSwitch(boolean v) {
        isDebug = v;
    }

    /**
     * 获取debug开关
     */
    public static boolean getDebugSwitch() {
        return isDebug;
    }

    /**
     * 设置保存日志
     *
     * @param v 是否保存日志
     */
    public static void setSaveSwitch(boolean v) {
        isSave = v;
    }

    /**
     * 设置是否打印日志
     *
     * @param v 是否打印
     */
    public static void setPrintSwitch(boolean v) {
        isPrint = v;
    }

    /**
     * 初始化
     * AppContext调用
     *
     * @param tag     日志标记
     * @param logPath 日志存储位置
     */
    public static void init(String tag, String logPath) {
        init(tag, logPath, 1);
    }

    /**
     * 初始化
     * AppContext调用
     *
     * @param tag       日志标记
     * @param logPath   日志存储位置
     * @param cacheSize 缓存日志条数
     */
    public static void init(String tag, String logPath, int cacheSize) {
        if (!TextUtils.isEmpty(logPath)) {
            File file = new File(logPath);
            if (file.isDirectory()) {
                init(tag, logPath, DEFAULT_LOG_FILE_NAME, cacheSize);
            } else {
                init(tag, file.getParent(), file.getName(), cacheSize);
            }
        }
    }

    /**
     * 初始化
     * AppContext调用
     *
     * @param tag         日志标记
     * @param logFolder   日志文件夹
     * @param logFileName 日志文件名
     * @param cacheSize   缓存日志条数
     */
    public static void init(String tag, String logFolder, String logFileName, int cacheSize) {
        Log.i(TAG, "tag: " + tag + ", logFolder: " + logFolder + ", logFileName: " + logFileName + ", cacheSize: " + cacheSize);
        if (!TextUtils.isEmpty(tag)) {
            TAG = tag;
        }
        sCacheSize = cacheSize;
        setSavePath(logFolder, logFileName);
        initLogExecutor();
    }

    private static void initLogExecutor() {
        if (sLogExecutor == null) {
            sLogExecutor = new ThreadPoolExecutor(1, 1,
                    60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(5000), new LogThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    public static class LogThreadFactory implements ThreadFactory {
        @SuppressLint("DefaultLocale")
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(String.format("%s#%d", "Logger", thread.getId()));
            return thread;
        }
    }

    /**
     * 设置日志文件路径
     *
     * @param path 日志文件路径
     */
    public static void setSavePath(String path) {
        if (!TextUtils.isEmpty(path)) {
            File f = new File(path);
            if (f.isDirectory()) {
                setSavePath(path, DEFAULT_LOG_FILE_NAME);
            } else {
                setSavePath(f.getParent(), f.getName());
            }
        } else {
            Logger.e(TAG, "setSavePath failed: path is empty!");
        }
    }

    /**
     * 设置日志保存文件路径
     *
     * @param logFolder   日志文件夹
     * @param logFileName 日志文件名(不包括扩展名）
     */
    public static void setSavePath(String logFolder, String logFileName) {
        synchronized (Logger.class) {
            if (!TextUtils.isEmpty(logFolder) && !TextUtils.isEmpty(logFileName)) {
                sLogFolder = logFolder;
                sLogFileName = logFileName;
                sLogPath = "";
            }
        }
    }

    /**
     * 获取日志保存文件夹路径
     *
     * @return 日志保存文件夹路径
     */
    public static String getLogFolder() {
        return sLogFolder;
    }

    /**
     * 设置日志保存的文件路径
     *
     * @param path 当前正在写入的文件路径
     */
    public static void setLogPath(String path) {
        sLogPath = path;
    }

    /**
     * 获取日志保存的文件路径
     *
     * @return 当前正在写入的文件路径
     */
    public static String getLogPath() {
        return sLogPath;
    }

    /**
     * 设置日志文件大小阈值
     *
     * @param threshold 文件大小阈值(Byte)
     */
    static void setThreshold(long threshold) {
        if (threshold > 0) {
            sThreshold = Math.max(threshold, MIN_FILE_SIZE);
            sThreshold = Math.min(sThreshold, MAX_FILE_SIZE);
        }
    }

    /**
     * 获取日志文件大小阈值
     *
     * @return 文件大小阈值(Byte)
     */
    static long getThreshold() {
        return sThreshold;
    }

    /**
     * 设置最大日志文件数量
     *
     * @param count 最大日志文件数量
     */
    static void setMaxCount(int count) {
        if (count > 0) {
            sMaxCount = count;
        }
    }

    /**
     * 获取当前最大日志文件数量
     *
     * @return 最大日志文件数量
     */
    static int getMaxCount() {
        return sMaxCount;
    }

    /**
     * 保存日志
     *
     * @param log 日志信息
     */
    private static void saveLog(String log) {
        int currentPid = Process.myPid();
        // long currentThreadId = Thread.currentThread().getId();
        long currentThreadId = Process.myTid();

        String msg = TimeUtils.getDateTimeStringByFormat(TimeUtils.
                DateTimePattern.DEFAULT_TIME_TYPE) + " " + currentPid + "-" + currentThreadId + " " + log + FILE_NEWLINE;

        if (sLogExecutor == null) {
            initLogExecutor();
        }
        sLogExecutor.execute(() -> saveLog(msg, true));
    }

    /**
     * 强制将当前缓存日志写入文件
     */
    public static void flush() {
        saveLog(null, false);
    }

    private static void saveLog(String log, boolean isCache) {
        if (!TextUtils.isEmpty(log)) {
            sCacheLog.add(log);
        }

        // 未达到缓存阈值
        if (isCache && sCacheLog.size() < sCacheSize) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String s : sCacheLog) {
            sb.append(s);
        }
        synchronized (Logger.class) {
            if (TextUtils.isEmpty(sLogPath)) {
                writeLog(sLogFolder, sLogFileName, sb.toString());
            } else {
                writeLog(sLogPath, sb.toString());
            }
        }
        sCacheLog.clear();
    }

    /**
     * 获取初始的日志文件路径
     *
     * @return 初始的日志文件路径
     */
    private static String getInitialLogPath() {
        return sLogFolder + File.separator + sLogFileName + "_"
                + TimeUtils.getDateTimeStringByFormat(TimeUtils.DateTimePattern.LOG_FILE_TIME_TYPE) + "-0.txt";
    }

    /**
     * 获取日志文件名词匹配的正则表达式
     *
     * @param isCurrent true：匹配当前可写入的日志文件   false：匹配所有日志文件
     * @return 正则表达式字符串
     */
    private static String getLogFilePattern(boolean isCurrent) {
        return sLogFileName + "_\\d{8}_\\d{6}-" + (isCurrent ? "0" : "\\d*") + ".";
    }

    /**
     * 根据文件夹和文件名写日志
     *
     * @param logFolder   日志文件夹
     * @param logFileName 日志文件名
     * @param log         日志内容
     */
    public static void writeLog(String logFolder, String logFileName, String log) {
        File parentFile = new File(logFolder);
        if (!parentFile.exists()) {
            try {
                parentFile.mkdirs();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        if (!parentFile.isDirectory()) {
            Log.e(TAG, "logFolder is not directory");
            return;
        }

        sLogFolder = logFolder;
        sLogFileName = logFileName;
        File[] logFiles = parentFile.listFiles();
        Pattern pattern = Pattern.compile(getLogFilePattern(true));
        if (logFiles != null) {
            for (File f : logFiles) {
                if (f.isFile() && pattern.matcher(f.getName()).find()) {
                    sLogPath = f.getAbsolutePath();
                    sLogFileSize = f.length();
                    writeLog(sLogPath, log);
                    return;
                }
            }
        }
        // 在目录下未找到符合条件的文件时，创建新文件
        sLogFileSize = 0;
        sLogPath = getInitialLogPath();
        FileUtils.writeFile(sLogPath, log);
    }

    /**
     * 根据文件路径写日志
     *
     * @param logPath 日志文件路径
     * @param log     日志内容
     */
    public static void writeLog(String logPath, String log) {
        File f = new File(logPath);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        if (!f.isFile()) {
            Log.e(TAG, "writeLog failed: is not a file!");
            return;
        }
        if (sLogFileSize == 0) {
            sLogFileSize = f.length();
        }
        sLogFileSize += log.length();
        if (sLogFileSize > sThreshold) {
            changeLogFileName();
            sLogPath = getInitialLogPath();
            sLogFileSize = 0;
        }
        FileUtils.writeFile(sLogPath, log);
    }

    /**
     * 遍历目录下所有文件，修改文件序号
     */
    private static void changeLogFileName() {
        File parentFile = new File(sLogFolder);
        if (!parentFile.exists() || !parentFile.isDirectory()) {
            Log.e(TAG, "logFolder is not directory");
            return;
        }
        File[] logFiles = parentFile.listFiles();
        Pattern pattern = Pattern.compile(getLogFilePattern(false));
        if (logFiles == null) {
            return;
        }
        for (File f : logFiles) {
            if (f.isFile() && pattern.matcher(f.getName()).find()) {
                String fileName = f.getName();
                try {
                    int fileId = Integer.parseInt(fileName.substring(fileName.lastIndexOf("-") + 1, fileName.lastIndexOf(".")));
                    if (fileId >= sMaxCount) {
                        // 删除序号超过最大文件数量的文件
                        FileUtils.deleteFile(f.getAbsolutePath());
                    } else {
                        // 文件序号 + 1
                        String newFileName = fileName.replace("-" + fileId, "-" + (++fileId));
                        File newFile = new File(sLogFolder + File.separator + newFileName);
                        if (!f.renameTo(newFile)) {
                            Log.e(TAG, "rename " + fileName + "to" + newFileName + " failed!");
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    }

    private static String getLevelText(int level) {
        switch (level) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.INFO:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            case Log.ASSERT:
                return "A";
        }
        return "N";
    }

    /**
     * 日志文件打印和保存
     * isSave-保存到指定文件
     * isPrint-打印到控制台
     * isDebug-打印debug级别日志
     *
     * @param level 日志等级
     * @param tag   日志标记
     * @param log   日志信息
     */
    private static void log(int level, String tag, String log) {
        int index = 4;
        if (AppContext.getTag().equals(tag)) {
            index = 5;
        }
        String[] clazzNames = Thread.currentThread().getStackTrace()[index].getClassName()
                .split(FILE_SPLIT_SEPARATOR);
        String methodName = Thread.currentThread().getStackTrace()[index].getMethodName();
        String className = clazzNames[clazzNames.length - 1];
        String msg = className + "@" + methodName + ": " + log;
        if (isSave) {
            saveLog(tag + " " + getLevelText(level) + " " + msg);
        }
        if (!isPrint) {
            return;
        }
        switch (level) {
            case Log.VERBOSE:
                if (isDebug) {
                    Log.v(tag, msg);
                }
                break;
            case Log.DEBUG:
                if (isDebug) {
                    Log.d(tag, msg);
                }
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.ERROR:
            case Log.ASSERT:
                Log.e(tag, msg);
                break;
            default:
                break;
        }

    }

    public static String getThrowableString(Throwable e) {
        if (e == null) {
            return "";
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        while (e != null) {
            e.printStackTrace(printWriter);
            e = e.getCause();
        }
        String text = writer.toString();
        printWriter.close();
        return text;
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        log(Log.VERBOSE, tag, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        log(Log.INFO, tag, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        log(Log.ERROR, tag, msg);
    }

    public static void e(Throwable e) {
        e(getThrowableString(e));
    }

    public static void e(String tag, Throwable e) {
        e(tag, getThrowableString(e));
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        log(Log.DEBUG, tag, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        log(Log.WARN, tag, msg);
    }
}