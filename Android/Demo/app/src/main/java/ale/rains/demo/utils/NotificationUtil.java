package ale.rains.demo.utils;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import ale.rains.demo.R;

public class NotificationUtil {
    public static boolean isNotificationEnabled(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager.areNotificationsEnabled();
    }

    private static void showDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.setIcon(R.mipmap.ic_launcher_round)
                .setTitle("系统提示：")
                .setMessage(msg)
                .setNegativeButton("取消", (DialogInterface dialog, int which) -> {
                    Toast.makeText(context, "您点击了取消按钮", Toast.LENGTH_SHORT).show();
                })
                .setPositiveButton("去设置", (DialogInterface dialog, int which) -> {
                    Toast.makeText(context, "您点击了确定按钮", Toast.LENGTH_SHORT).show();
                    gotoSet(context);
                }).create();
        alertDialog.show();
    }

    private static void gotoSet(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openNotificationSetting(Context context, OnNextListener onNextListener) {
        if (!isNotificationEnabled(context)) {
//            gotoSet(context);
            String msg = "通知权限未开启，需要开启权限！";
            showDialog(context, msg);
        } else {
            if (onNextListener != null) {
                onNextListener.onNext();
            }
        }
    }

    public interface OnNextListener {
        void onNext();
    }
}