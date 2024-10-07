package ale.rains.util;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RomUtils {
    private static Boolean isRoot = null;

    /**
     * 判断当前手机是否有ROOT权限
     */
    public static boolean isRooted() {
        boolean ret = false;

        // 避免重复查找文件
        if (isRoot != null) {
            return isRoot;
        }
        try {
            if (new File("/system/bin/su").exists()) {
                ret = true;
            } else if (new File("/system/xbin/su").exists()) {
                ret = true;
            } else if (new File("/su/bin/su").exists()) {
                ret = true;
            }
            Logger.d("isRooted: " + ret);
        } catch (Exception e) {
            Logger.e(e);
        }
        isRoot = ret;
        return ret;
    }

    /**
     * 获取华为emui版本号
     */
    public static double getEmuiVersion() {
        try {
            String emuiVersion = getSystemProperty("ro.build.version.emui");
            if (TextUtils.isEmpty(emuiVersion)) {
                return 0;
            }
            String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
            return Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 4.0;
    }

    private static Boolean isSony = null;

    /**
     * 判断是否是sony
     */
    public static boolean isSonySystem() {
        if (isSony == null) {
            isSony = Build.MANUFACTURER.toLowerCase().contains("sony");
        }
        return isSony;
    }

    private static Boolean isSamSung = null;

    /**
     * 判断是否是三星
     */
    public static boolean isSamsungSystem() {
        if (isSamSung == null) {
            isSamSung = Build.MANUFACTURER.toLowerCase().contains("samsung");
        }
        return isSamSung;
    }

    private static Boolean isRealme = null;

    /**
     * 判断是否是realme
     */
    public static boolean isRealmeSystem() {
        if (isRealme == null) {
            isRealme = Build.MANUFACTURER.toLowerCase().contains("realme");
        }
        return isRealme;
    }

    private static Boolean isOppo = null;

    /**
     * 判断是否是oppo
     */
    public static boolean isOppoSystem() {
        if (isOppo == null) {
            isOppo = Build.MANUFACTURER.toLowerCase().contains("oppo");
            if (!isOppo) {
                isOppo = Build.BRAND.toLowerCase().contains("oppo");
            }
        }
        return isOppo;
    }

    private static Boolean isOnePlus = null;

    public static boolean isOnePlusSystem() {
        if (isOnePlus == null) {
            isOnePlus = Build.BRAND.toLowerCase().contains("oneplus");
        }
        return isOnePlus;
    }

    private static Boolean isVivo = null;

    public static boolean isVivoSystem() {
        if (isVivo == null) {
            isVivo = Build.MANUFACTURER.toLowerCase().contains("vivo");
        }
        return isVivo;
    }

    private static Boolean isGoogle = null;

    public static boolean isGoogleSystem() {
        if (isGoogle == null) {
            isGoogle = Build.MANUFACTURER.toLowerCase().contains("google");
        }
        return isGoogle;
    }

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    public static int getMiuiVersion() {
        String version = getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                LogUtils.e("get miui version code error, version : " + version);
            }
        }
        return -1;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException e) {
            Logger.e(e);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Logger.e(e);
                }
            }
        }
        return line;
    }

    private static Boolean isHuawei = null;

    public static boolean isHuaweiSystem() {
        if (isHuawei == null) {
            isHuawei = Build.MANUFACTURER.toLowerCase().contains("huawei");
        }
        return isHuawei;
    }

    private static Boolean isMiui = null;

    /**
     * check if is miui ROM
     */
    public static boolean isMiuiSystem() {
        if (isMiui == null) {
            isMiui = !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
        }
        return isMiui;
    }

    private static Boolean isBlackShark = null; //黑鲨

    /**
     * check if is miui ROM
     */
    public static boolean isBlackSharkSystem() {
        if (isBlackShark == null) {
            isBlackShark = Build.MANUFACTURER.toLowerCase().contains("blackshark");
        }
        return isBlackShark;
    }


    private static Boolean isMeizu = null;

    public static boolean isMeizuSystem() {
        //return Build.MANUFACTURER.toLowerCase().contains("meizu");
        if (isMeizu == null) {
            String flymeOSFlag = getSystemProperty("ro.build.display.id");
            if (TextUtils.isEmpty(flymeOSFlag)) {
                isMeizu = false;
            } else {
                isMeizu = flymeOSFlag.toLowerCase().contains("flyme");
            }
        }
        return isMeizu;
    }

    private static Boolean is360 = null;

    public static boolean is360System() {
        if (is360 == null) {
            is360 = Build.MANUFACTURER.toLowerCase().contains("qiku")
                    || Build.MANUFACTURER.contains("360");
        }
        return is360;
    }

    public static String getDeviceSerialNO() {
        try {
            return getSystemProperty("ro.serialno");
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }
}