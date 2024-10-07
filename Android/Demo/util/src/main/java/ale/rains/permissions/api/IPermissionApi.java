package ale.rains.permissions.api;

import android.content.Context;

public interface IPermissionApi {
    boolean checkFloatWindowPermission23(Context context); //检测悬浮窗权限 Build.VERSION.SDK_INT < 23

    boolean checkFloatWindowPermission(Context context); //检测悬浮窗权限 Build.VERSION.SDK_INT >= 23

    void applyFloatWindowPermission23(Context context); //跳转获取悬浮窗权限   Build.VERSION.SDK_INT < 23

    void applyFloatWindowPermission(Context context); //跳转获取悬浮窗权限   Build.VERSION.SDK_INT >= 23
}