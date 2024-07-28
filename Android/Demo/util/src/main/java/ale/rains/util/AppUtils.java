package ale.rains.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public class AppUtils {
    private static Boolean sSystemApp = null;

    /**
     * 是否为系统应用
     */
    public static boolean isSystemApp() {
        if (sSystemApp == null) {
            int uid = getUid();
            sSystemApp = uid == 1000;
        }
        return sSystemApp;
    }

    /**
     * 通过包名获取进程id
     *
     * @return 进程id
     */
    public static int getPidByPackageName(String pkgName) {
        try {
            ActivityManager activityManager = (ActivityManager) AppContext.getContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> allRunningApps = activityManager.getRunningAppProcesses();
            Iterator iterator = allRunningApps.iterator();
            ActivityManager.RunningAppProcessInfo info;
            do {
                if (!iterator.hasNext()) {
                    return getPidByPs(pkgName);
                }
                info = (ActivityManager.RunningAppProcessInfo) iterator.next();
            } while (!info.processName.equals(pkgName));

            return info.pid;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 通过ps指令获取进程id
     *
     * @return 进程id
     */
    public static int getPidByPs(String packageName) {
        String cmd = "ps -A";
        BufferedReader reader = null;
        Process p = null;
        try {
            String line;
            p = Runtime.getRuntime().exec(cmd);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = reader.readLine()) != null) {
                if (line.contains(packageName)) {
                    String[] strings = line.trim().split(" +");
                    int size = strings.length;
                    if (size > 1) {
                        if (strings[size - 1].contains(packageName)) {
                            String pid = strings[1];
                            return Integer.parseInt(pid);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Logger.e(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Logger.e(e);
                }
            }
            if (p != null) {
                try {
                    p.destroy();
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
        }
        return 0;
    }

    /**
     * 获取当前应用的uid
     *
     * @return uid
     */
    public static int getUid() {
        ActivityManager activityManager = (ActivityManager) AppContext.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> allRunningApps = activityManager.getRunningAppProcesses();
        Iterator iterator = allRunningApps.iterator();
        ActivityManager.RunningAppProcessInfo info;
        do {
            if (!iterator.hasNext()) {
                return 0;
            }
            info = (ActivityManager.RunningAppProcessInfo) iterator.next();
        } while (!info.processName.equals(AppContext.getContext().getPackageName()));

        return info.uid;
    }

    /**
     * 判断包名对应的应用是否已安装
     *
     * @param packageName 应用包名
     * @return 是否已安装
     */
    public static boolean isAppExist(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        final PackageManager packageManager = AppContext.getContext().getPackageManager();
        List<PackageInfo> infos = packageManager.getInstalledPackages(0);
        if (infos == null || infos.isEmpty()) {
            return false;
        }
        for (PackageInfo packageInfo : infos) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前进程的进程名词
     *
     * @return 进程名词
     */
    public static String getProcessName() {
        try {
            File f = new File("/proc/" + android.os.Process.myPid() + "/cmdline");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String processName = br.readLine().trim();
            br.close();
            return processName;
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }
}