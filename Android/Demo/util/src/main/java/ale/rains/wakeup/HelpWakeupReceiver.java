package ale.rains.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ale.rains.remote.RemoteManager;
import ale.rains.util.Logger;

/**
 * 重启保活的广播接收器
 * 需要注册广播：ale.rains.wakeup
 */
public class HelpWakeupReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        helpWakeup();
    }

    /**
     * 启动服务绑定
     */
    private void helpWakeup() {
        RemoteManager.getInstance().init(mContext.getApplicationContext());
        Logger.i("start remote service");
    }
}