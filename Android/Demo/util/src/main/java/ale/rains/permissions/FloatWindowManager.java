package ale.rains.permissions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;

import ale.rains.permissions.api.PermissionApi;
import ale.rains.util.Logger;
import ale.rains.util.R;

public class FloatWindowManager {
    private static volatile FloatWindowManager sInstance;
    private Dialog mDialog;
    private PermissionApi mPermissionApi;

    public static FloatWindowManager getInstance() {
        if (sInstance == null) {
            synchronized (FloatWindowManager.class) {
                if (sInstance == null) {
                    sInstance = new FloatWindowManager();
                }
            }
        }
        return sInstance;
    }

    private FloatWindowManager() {
        mPermissionApi = new PermissionApi();
    }

    boolean checkFloatPermission(Context context) {
        if (checkPermission(context)) {
            return true;
        } else {
            applyPermission(context);
            return false;
        }
    }

    boolean checkPermission(Context context) {
        // 6.0版本之后由于google增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            return mPermissionApi.checkFloatWindowPermission23(context);
        }
        return mPermissionApi.checkFloatWindowPermission(context);
    }


    private void applyPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            showConfirmDialog(context, confirm -> {
                if (confirm) {
                    mPermissionApi.applyFloatWindowPermission23(context);
                }
            });
        } else {
            commonROMPermissionApply(context);
        }
    }

    /**
     * 直接去申请权限
     */
    void applyPermissionDirect(Context context) {

        if (Build.VERSION.SDK_INT < 23) {
            mPermissionApi.applyFloatWindowPermission23(context);
        } else {
            mPermissionApi.applyFloatWindowPermission(context);
        }
    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            showConfirmDialog(context, confirm -> {
                if (confirm) {
                    try {
                        mPermissionApi.applyFloatWindowPermission(context);
                    } catch (Exception e) {
                        Logger.e(e);
                    }
                }
            });
        }
    }


    private void showConfirmDialog(Context context, final OnConfirmResult result) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = new AlertDialog.Builder(context).setCancelable(true).setTitle("")
                .setMessage("您的手机没有授予悬浮窗权限，请开启后再试")
                .setPositiveButton(R.string.float_go_to_grant,
                        (dialog, which) -> {
                            result.confirmResult(true);
                            dialog.dismiss();
                        }).setNegativeButton(R.string.float_no_grant_now,
                        (dialog, which) -> {
                            result.confirmResult(false);
                            dialog.dismiss();
                        }).create();
        mDialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }
}