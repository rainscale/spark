package ale.rains.wakeup;

import android.content.Context;

import ale.rains.comm.CommunicateManager;
import ale.rains.util.Logger;

/**
 * 重启保活任务
 */
public class WakeupTask implements Runnable {
    private Context mContext;
    private String mCommunicatePackageName;

    public WakeupTask(Context context, String communicatePackageName) {
        this.mContext = context;
        this.mCommunicatePackageName = communicatePackageName;
    }

    @Override
    public void run() {
        if (!WakeupManager.getInstance().isProcessRunning(mContext, mCommunicatePackageName)) {
            WakeupManager.getInstance().postUI(new Runnable() {
                @Override
                public void run() {
                    CommunicateManager.getInstance().helpRestart();
                }
            });
            Logger.d("restart communicate packageName: " + mCommunicatePackageName);
        }
    }
}