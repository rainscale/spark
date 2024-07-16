package ale.rains.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    public static class DateTimePattern {
        public static final String DATE_TIME_TYPE = "yyyy-MM-dd";

        public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

        public static final String FILE_TIME_TYPE = "yyyy_MM_dd_HH_mm_ss";

        public static final String LOG_FILE_TIME_TYPE = "yyyyMMdd_HHmmss";

        public static final String LOG_TIME_TYPE = "yyyy_MM_dd_HHmmss";

        public static final String LONG_TIME_TYPE = "yyyy-MM-dd HH:mm:ss";

        public static final String MICRO_TIME_TYPE = "yyyy-MM-dd HH:mm:ss";

        public static final String SHORT_TIME_TYPE = "HH:mm:ss";
    }

    public static long getCurDateTimeStamp() {
        return System.currentTimeMillis() / 86400000L * 86400000L;
    }

    public static long getCurMinuteTimeStamp() {
        return System.currentTimeMillis() / 60000L * 60000L;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String getDateTime() {
        return getDateTimeStringByFormat("yyyy-MM-dd");
    }

    public static String getDateTimeStringByFormat(long paramLong, String pattern) {
        if (TextUtils.isEmpty(pattern))
            return "";
        if (paramLong == 0L)
            paramLong = System.currentTimeMillis();
        return (new SimpleDateFormat(pattern)).format(Long.valueOf(paramLong));
    }

    public static String getDateTimeStringByFormat(String paramString) {
        if (TextUtils.isEmpty(paramString))
            return "";
        long l = System.currentTimeMillis();
        return (new SimpleDateFormat(paramString, Locale.ENGLISH)).format(Long.valueOf(l));
    }

    public static String getDefaultTime() {
        return getDateTimeStringByFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String getFileTypeTime() {
        return getDateTimeStringByFormat("yyyy_MM_dd_HH_mm_ss");
    }

    public static String getLogFileTypeTime() {
        return getDateTimeStringByFormat("yyyyMMdd_HHmmss");
    }

    public static String getLogTypeTime() {
        return getDateTimeStringByFormat("yyyy_MM_dd_HHmmss");
    }

    public static long getLogcatTimeStamp(String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        try {
            return simpleDateFormat.parse(source).getTime();
        } catch (ParseException e) {
            LogUtils.e(e.toString());
            return 0L;
        }
    }

    public static String getLongTime() {
        return getDateTimeStringByFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String getMicroTime() {
        return getDateTimeStringByFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String getShortTime() {
        return getDateTimeStringByFormat("HH:mm:ss");
    }

    public static String getTimeSpanString(long start, long end) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(Long.valueOf(end - start));
    }

    public static long getTimeStamp(String pattern, String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(source).getTime();
        } catch (ParseException e) {
            LogUtils.e(e.toString());
            return 0L;
        }
    }

    public static int getYear() {
        return Calendar.getInstance().get(1);
    }

    /**
     * 判断时间是否超过指定的间隔
     * @param begin 开始时间
     * @param interval 时间间隔
     * @return
     */
    public static boolean isTimeExceed(long begin, long interval) {
        long passTime = System.currentTimeMillis() - begin;
        return passTime  > (1000L * interval);
    }
}
