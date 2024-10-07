package ale.rains.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ale.rains.adb.cmd.CmdTools;
import ale.rains.util.Logger;
import ale.rains.util.R;
import ale.rains.util.ThreadManager;

public class PermissionUtils {
    private static OnPermissionCallback sCallback;

    /**
     * 开始请求权限
     */
    public static void requestPermissions(Context context, String permission, @NonNull final OnPermissionCallback callback) {
        if (!TextUtils.isEmpty(permission)) {
            List<String> permissions = new ArrayList<>();
            permissions.add(permission);
            requestPermissions(context, permissions, callback);
        } else {
            callback.onPermissionResult(false, "permission is empty");
        }
    }

    /**
     * 开始请求权限
     */
    public static void requestPermissions(Context context, final List<String> permissions, @NonNull final OnPermissionCallback callback) {
        ThreadManager.getInstance().postUITask(() -> {
            if (PermissionDialogActivity.sRunningStatus) {
                callback.onPermissionResult(false, "request other permission now");
                return;
            }

            if (permissions == null || permissions.size() == 0) {
                callback.onPermissionResult(true, "permission is empty");
                return;
            }

            final Intent intent = new Intent(context, PermissionDialogActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (permissions instanceof ArrayList) {
                intent.putStringArrayListExtra(PermissionDialogActivity.PERMISSIONS_KEY, (ArrayList<String>) permissions);
            } else {
                intent.putStringArrayListExtra(PermissionDialogActivity.PERMISSIONS_KEY, new ArrayList<>(permissions));
            }

            context.startActivity(intent);
            sCallback = callback;
        });
    }

    /**
     * 处理权限
     */
    static void onPermissionResult(boolean result, String reason) {
        if (sCallback == null) {
            return;
        }
        ThreadManager.getInstance().postUITask(() -> {
            sCallback.onPermissionResult(result, reason);
            sCallback = null;
        });
    }

    /**
     * 检查Accessibility权限
     */
    public static boolean isAccessibilitySettingsOn(Context context) {
        if (context == null) {
            return false;
        }
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Logger.e(e);
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }

    /**
     * 检查悬浮窗权限
     */
    public static boolean isFloatWindowPermissionOn(Context context) {
        return FloatWindowManager.getInstance().checkFloatPermission(context);
    }

    /**
     * 检查使用状态权限
     */
    public static boolean isUsageStatPermissionOn(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    /**
     * 检查是否已有高权限
     */
    public static boolean grantHighPrivilegePermission(Context context) {
        if (context == null) {
            return false;
        }
        if (!CmdTools.isInitialized()) {
            ThreadManager.getInstance().postWorkTask(() -> CmdTools.generateConnection());
            Toast.makeText(context, context.getString(R.string.permission_no_root_need_adb), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static void grantHighPrivilegePermissionAsync(final GrantPermissionCallback callback) {
        if (!CmdTools.isInitialized()) {
            ThreadManager.getInstance().postWorkTask(() -> {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                //使用Callable接口作为构造参数
                FutureTask<Boolean> future =
                        new FutureTask<>(() -> CmdTools.generateConnection());
                executor.execute(future);
                try {
                    if (future.get(5, TimeUnit.SECONDS)) {
                        callback.onGrantSuccess();
                    } else {
                        callback.onGrantFail("execute failed");
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    // future.cancel(true);
                    callback.onGrantFail("execute exeception");
                } finally {
                    executor.shutdown();
                }
            });
        } else {
            callback.onGrantSuccess();
        }
    }

    /**
     * 检查需动态申请的权限，对未获取的权限进行申请
     *
     * @return 是否已全部获取
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkAndGrantDynamicPermissionIfNeeded(Activity activity, String[] neededPermissions) {
        String[] notGrantedPermissions = new String[neededPermissions.length];
        int index = 0;

        // 检查每项权限是否已经获得，未获得的加入待申请队列
        for (String permission : neededPermissions) {
            if (permission != null && ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions[index++] = permission;
            }
        }

        // 当存在未获得的权限，进行动态申请
        if (index > 0) {
            ActivityCompat.requestPermissions(activity, notGrantedPermissions, 0);
            return false;
        }
        return true;
    }

    /**
     * 检查需动态申请的权限，对未获取的权限进行申请
     *
     * @return 是否已全部获取
     */
    @TargetApi(Build.VERSION_CODES.M)
    static List<String> checkUngrantedPermission(Activity activity, String[] neededPermissions) {
        List<String> notGrantedPermissions = new ArrayList<>();

        // 检查每项权限是否已经获得，未获得的加入待申请队列
        for (String permission : neededPermissions) {
            if (permission != null && ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions.add(permission);
            }
        }

        return notGrantedPermissions;
    }

    public static void clear() {
        sCallback = null;
    }

    public interface OnPermissionCallback {
        void onPermissionResult(boolean result, String reason);
    }

    public interface GrantPermissionCallback {
        void onGrantSuccess();

        void onGrantFail(String msg);
    }
}