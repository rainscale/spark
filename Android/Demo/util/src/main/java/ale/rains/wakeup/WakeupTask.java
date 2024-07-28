package ale.rains.wakeup;

import android.content.Context;

import ale.rains.remote.RemoteManager;
import ale.rains.util.Logger;

/**
 * 唤醒远程服务任务
 */
public class WakeupTask implements Runnable {
    private Context mContext;
    private String mRemotePackageName;

    public WakeupTask(Context context, String remotePackageName) {
        mContext = context;
        mRemotePackageName = remotePackageName;
    }

    @Override
    public void run() {
        if (!WakeupManager.getInstance().isProcessRunning(mContext, mRemotePackageName)) {
            WakeupManager.getInstance().postUI(new Runnable() {
                @Override
                public void run() {
                    RemoteManager.getInstance().wakeup();
                }
            });
            Logger.d("wakeup remote service " + mRemotePackageName);
        }
    }
}