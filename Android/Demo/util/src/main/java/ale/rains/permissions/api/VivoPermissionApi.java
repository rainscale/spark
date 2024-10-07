package ale.rains.permissions.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import java.lang.reflect.Method;

import ale.rains.util.Logger;
import ale.rains.util.ThreadManager;

public class VivoPermissionApi extends BasePermissionApi {

    @Override
    public boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            try {
                return getFloatPermissionStatus(context) == 0;
            } catch (Exception e) {
                e.printStackTrace();

                // 通常方式去检查
                if (Build.VERSION.SDK_INT >= 23) {
                    try {
                        Class clazz = Settings.class;
                        Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                        return (Boolean) canDrawOverlays.invoke(null, context);
                    } catch (Exception e1) {
                        Logger.e(e);
                    }
                }
                return true;
            }
        }
        return true;
    }

    public boolean checkFloatWindowPermission23(Context context) {
        return checkFloatWindowPermission(context);
    }

    /**
     * 去i管家申请页面
     */
    @Override
    public void applyFloatWindowPermission23(Context context) {
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null) {
            try {
                appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
                ThreadManager.getInstance().postUITask(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "![CDATA[请进入\\\"应用管理->权限管理->悬浮窗\\\"页面开启权限]]", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                Logger.e(e);
                ThreadManager.getInstance().postUITask(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "![CDATA[请手动开启i管家，进入\\\"应用管理->权限管理->悬浮窗\\\"页面开启权限]]", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            ThreadManager.getInstance().postUITask(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "![CDATA[请手动开启i管家，进入\\\"应用管理->权限管理->悬浮窗\\\"页面开启权限]]", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 获取悬浮窗权限状态
     *
     * @return 1或其他是没有打开，0是打开，该状态的定义和{@link android.app.AppOpsManager#MODE_ALLOWED}，MODE_IGNORED等值差不多，自行查阅源码
     */
    public static int getFloatPermissionStatus(Context context) {
        String packageName = context.getPackageName();
        Uri uri = Uri.parse("content://com.iqoo.secure.provider.secureprovider/allowfloatwindowapp");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context.getContentResolver()
                .query(uri, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.getColumnNames();
            if (cursor.moveToFirst()) {
                @SuppressLint("Range")
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentlmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return getFloatPermissionStatus2(context);
            }

        } else {
            return getFloatPermissionStatus2(context);
        }
    }

    /**
     * vivo比较新的系统获取方法
     *
     * @return
     */
    private static int getFloatPermissionStatus2(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/float_window_apps");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri2, null, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range")
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return 1;
            }
        }
        return 1;
    }
}