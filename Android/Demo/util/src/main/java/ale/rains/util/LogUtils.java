package ale.rains.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

public class LogUtils {
    private static final String DEFAULT_LOG_TAG = "Untag";
    private static String mLogTag;
    private static boolean mLogEnable = BuildConfig.DEBUG;
    private static String mClassName;
    private static ArrayList<String> mMethods;

    static {
        mClassName = LogUtils.class.getName();
        mMethods = new ArrayList<>();

        Method[] methods = LogUtils.class.getDeclaredMethods();
        for (Method method : methods) {
            mMethods.add(method.getName());
        }
    }

    public static void init(String tag, boolean logEnable) {
        mLogTag = tag;
        mLogEnable = logEnable;
    }

    public static void setTag(String tag) {
        mLogTag = tag;
    }

    public static String getTag() {
        if (!TextUtils.isEmpty(mLogTag)) {
            return mLogTag;
        }
        try {
            StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
            for (StackTraceElement st : trace) {
                if (mClassName.equals(st.getClassName()) || mMethods.contains(st.getMethodName())) {
                    continue;
                }
                String callingClass = st.getClassName();
                return callingClass.substring(callingClass.lastIndexOf('.') + 1);
            }
        } catch (Exception e) {
        }
        return DEFAULT_LOG_TAG;
    }

    public static String[] getMsgAndTag(String msg) {
        try {
            StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
            for (StackTraceElement st : trace) {
                if (mClassName.equals(st.getClassName()) || mMethods.contains(st.getMethodName())) {
                    continue;
                }
                int b = st.getClassName().lastIndexOf(".") + 1;
                if (TextUtils.isEmpty(mLogTag)) {
                    mLogTag = st.getClassName().substring(b);
                }
                String message = "[tid" + Thread.currentThread().getId() + " " + st.getMethodName() + "(" + st.getFileName() + ":" + st.getLineNumber() + ")] " + msg;
                return new String[]{mLogTag, message};

            }
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(mLogTag)) {
            return new String[]{DEFAULT_LOG_TAG, msg};
        } else {
            return new String[]{mLogTag, msg};
        }
    }

    public static void setLogEnable(boolean logEnable) {
        mLogEnable = logEnable;
    }

    public static String buildMessage(String msg) {
        try {
            StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
            for (StackTraceElement st : trace) {
                if (mClassName.equals(st.getClassName()) || mMethods.contains(st.getMethodName())) {
                    continue;
                }
                return String.format(Locale.US, "[tid%d %s(%s:%d)] %s", Thread.currentThread().getId(), st.getMethodName(), st.getFileName(), st.getLineNumber(), msg);
            }
        } catch (Exception e) {
        }
        return String.format(Locale.US, "[tid%d] %s", Thread.currentThread().getId(), msg);
    }

    public static String getFunctionName() {
        try {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            for (StackTraceElement st : trace) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (Thread.class.getName().equals(st.getClassName())) {
                    continue;
                }
                if (mClassName.equals(st.getClassName())) {
                    continue;
                }
                return "[thread:" + Thread.currentThread().getName() + " method:" + st.getClassName() + "." + st.getMethodName()
                        + "(" + st.getFileName() + ":" + st.getLineNumber() + ")" + "]";
            }

        } catch (Exception e) {
        }
        return "Unknow";
    }

    public static String getFormatMsg(String msg) {
        return getFunctionName() + " " + msg;
    }

    public static boolean isDebugBuild() {
        return "eng".equals(Build.TYPE) || "userdebug".equals(Build.TYPE);
    }

    public static boolean isLoggable(String tag, int level) {
        return Log.isLoggable(tag, level) || isDebugBuild() || mLogEnable;
    }

    public static void printStackTraceString() {
        d(getTag(), Log.getStackTraceString(new Throwable()));
    }

    public static void v(String tag, String msg) {
        if (isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    public static void v(String msg) {
        if (mLogEnable) {
            String[] tam = getMsgAndTag(msg);
            v(tam[0], tam[1]);
        }

    }

    public static void d(String tag, String msg) {
        if (isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (mLogEnable) {
            String[] tam = getMsgAndTag(msg);
            d(tam[0], tam[1]);
        }
    }

    public static void i(String tag, String msg) {
        if (isLoggable(tag, Log.INFO)) {
            Log.i(tag, msg);
        }
    }

    public static void i(String msg) {
        if (mLogEnable) {
            String[] tam = getMsgAndTag(msg);
            i(tam[0], tam[1]);
        }
    }

    public static void w(String tag, String msg) {
        if (isLoggable(tag, Log.WARN)) {
            Log.w(tag, msg);
        }
    }

    public static void w(String msg) {
        if (mLogEnable) {
            String[] tam = getMsgAndTag(msg);
            w(tam[0], tam[1]);
        }
    }

    public static void e(String tag, String msg) {
        if (isLoggable(tag, Log.ERROR)) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        if (mLogEnable) {
            String[] tam = getMsgAndTag(msg);
            e(tam[0], tam[1]);
        }
    }
}