package ale.rains.comm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ale.rains.util.Logger;
import ale.rains.util.ReflectUtils;
import ale.rains.wakeup.HelpWakeupReceiver;
import ale.rains.wakeup.WakeupManager;
import ale.rains.wakeup.WakeupTask;

/**
 * 进程间通信的管理类
 */
public class CommunicateManager {
    private static final String WAKEUP_ACTION = "ale.rains.wakeup.restart"; // 服务连接中断后的重启广播
    private static final long DELAY = 5 * 60;
    private ICommunicateService mService = null;
    private static volatile CommunicateManager sInstance;
    private Context mContext;
    private String mCommunicatePackageName = ""; // 需要进程通信和守护的应用包名

    private List<IBindCommunicateServiceCallback> iBindCommunicateServiceCallbacks = new ArrayList<>();
    private List<IMsgCallback> iMsgCallbacks = new ArrayList<>(); // 发送消息后，消息经过服务处理后的回调
    private List<IMsgCallback> iLocalMsgCallbacks = new ArrayList<>(); // 本地接收的消息回调
    private IMsgCallback remoteMsgCallback;
    private IMsgCallback localMsgCallback;

    private ScheduledFuture mScheduledFuture;

    /**
     * 注册消息回执的监听
     *
     * @param iMessageCallback 消息回执的回调
     */
    public void registeremoteMsgCallback(IMsgCallback iMessageCallback) {
        if (iMessageCallback == null && !iMsgCallbacks.contains(remoteMsgCallback)) {
            iMsgCallbacks.add(remoteMsgCallback);
            return;
        }
        iMsgCallbacks.add(iMessageCallback);
    }

    /**
     * 反注册消息回执的监听
     *
     * @param iMessageCallback 消息回执的回调
     */
    public void unregisteremoteMsgCallback(IMsgCallback iMessageCallback) {
        if (iMessageCallback == null && iMsgCallbacks.contains(remoteMsgCallback)) {
            iMsgCallbacks.remove(remoteMsgCallback);
            return;
        }
        iMsgCallbacks.remove(iMessageCallback);
    }

    /**
     * 注册本地消息回执的监听
     *
     * @param iMsgCallback 消息回执的回调
     */
    public void registerLocalMsgCallback(IMsgCallback iMsgCallback) {
        if (iMsgCallback == null && !iLocalMsgCallbacks.contains(localMsgCallback)) {
            iLocalMsgCallbacks.add(localMsgCallback);
            return;
        }
        iLocalMsgCallbacks.add(iMsgCallback);
    }

    /**
     * 反注册本地消息回执的监听
     *
     * @param iMsgCallback 消息回执的回调
     */
    public void unregisterLocalMsgCallback(IMsgCallback iMsgCallback) {
        if (iMsgCallback == null && iLocalMsgCallbacks.contains(localMsgCallback)) {
            iLocalMsgCallbacks.remove(localMsgCallback);
            return;
        }
        iLocalMsgCallbacks.remove(iMsgCallback);
    }

    /**
     * 注册服务连接的监听
     *
     * @param callback 回调
     */
    public void registerBindCommunicateServiceCallbacks(IBindCommunicateServiceCallback callback) {
        iBindCommunicateServiceCallbacks.add(callback);
        callback.bindService(isBound());
    }

    /**
     * 注册服务连接的监听
     *
     * @param callback 回调
     */
    public void unregisterBindCommunicateServiceCallbacks(IBindCommunicateServiceCallback callback) {
        iBindCommunicateServiceCallbacks.remove(callback);
        callback.bindService(isBound());
    }

    public static CommunicateManager getInstance() {
        if (sInstance == null) {
            synchronized (CommunicateManager.class) {
                if (sInstance == null) {
                    sInstance = new CommunicateManager();
                }
            }
        }
        return sInstance;
    }

    private CommunicateManager() {
        remoteMsgCallback = new IMsgCallback() {
            @Override
            public void getMsgCallback(int type, String msg) {
                Logger.i("remote type: " + type + ", msg: " + msg);
            }
        };
        localMsgCallback = new IMsgCallback() {
            @Override
            public void getMsgCallback(int type, String msg) {
                Logger.i("local type: " + type + ", msg: " + msg);
            }
        };
    }

    public void init(Context context) {
        if (!isBound()) {
            mContext = context;
            try {
                PackageManager pm = mContext.getPackageManager();
                if (pm != null) {
                    ApplicationInfo appInfo = pm.getApplicationInfo(mContext.getPackageName(),
                            PackageManager.GET_META_DATA);
                    if (appInfo != null && appInfo.metaData != null) {
                        mCommunicatePackageName = appInfo.metaData.getString("ale.rains.communicate.package.name");
                        Logger.d("communicate packageName: " + mCommunicatePackageName);
                    }
                }
            } catch (Exception e) {
                Logger.e(e);
            }
            if (TextUtils.isEmpty(mCommunicatePackageName)) {
                Logger.e("communicate packageName is empty.");
            }
            onBindService();
        }
        if (mScheduledFuture == null) {
            final WakeupTask wakeupTask = new WakeupTask(mContext, mCommunicatePackageName);
            mScheduledFuture = WakeupManager.getInstance().postRepeatTask(wakeupTask, 0, DELAY, TimeUnit.SECONDS);
        }
    }

    /**
     * 绑定服务
     */
    private void onBindService() {
        final Intent intent = new Intent();
        ComponentName componentName = new ComponentName(mCommunicatePackageName,
                CommunicateService.class.getName());
        intent.setComponent(componentName);
        try {
            Logger.d("bindService from: " + mCommunicatePackageName);

            // 延时3000ms，再进行服务绑定。
            // 原因：如果服务杀掉后立马重启，太快了，系统会检测到上一次的状态，认为服务已启动，从而重启失败。
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Logger.d("begin bindService from: " + mCommunicatePackageName);

                    try {
                        Class<?> userHandle = Class.forName("android.os.UserHandle");
                        UserHandle systemUserHandle = ReflectUtils.get(userHandle, "CURRENT");
                        Object[] parameters = new Object[]{intent, mConnection, 1, systemUserHandle};
                        Class<?>[] paramType = new Class[]{intent.getClass(), ServiceConnection.class, Integer.TYPE, systemUserHandle.getClass()};
                        ReflectUtils.invokeHideMethod(mContext.getClass(), "bindServiceAsUser", paramType, mContext, parameters);
                    } catch (ClassNotFoundException e) {
                        Logger.e(e);
                    } catch (Exception e) {
                        Logger.e(e);
                    }
                    Logger.d("end bindService from: " + mCommunicatePackageName);
                }
            }, 3000);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 解绑服务
     */
    public void onUnbindService() {
        if (isBound()) {
            mContext.unbindService(mConnection);
            Logger.d("unbindService: " + mCommunicatePackageName);

            if (mScheduledFuture != null) {
                mScheduledFuture.cancel(true);
                mScheduledFuture = null;
            }
        }
    }

    public boolean isBound() {
        return mService != null;
    }

    /**
     * 发送字符串消息
     *
     * @param type    消息类型，见：RemoteConstants.MsgType
     * @param message 消息
     * @return 消息是否发送成功
     */
    public boolean sendRemoteMessage(int type, String message) {
        boolean success = false;
        if (isBound()) {
            try {
                mService.sendStringMessage(type, message);
                success = true;
            } catch (RemoteException e) {
                Logger.e("Send Remote Message failed: " + mCommunicatePackageName + ". The reason is: " + e.toString());
            }
        } else {
            Logger.e("Service has not connected: " + mCommunicatePackageName);
        }

        return success;
    }

    /**
     * 发送字符串消息。测试用
     *
     * @param message 消息
     */
    public void doSomething(String message) {
        if (isBound()) {
            try {
                mService.doSomething(message);
            } catch (RemoteException e) {
                Logger.e("Send Remote Message failed: " + mCommunicatePackageName + ". The reason is: " + e.toString());
            }
        } else {
            Logger.e("Service has not connected: " + mCommunicatePackageName);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ICommunicateService.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
                Logger.d("Service has connected: " + mCommunicatePackageName);
            } catch (RemoteException e) {
                Logger.e("Service connect failed: " + mCommunicatePackageName + ". The reason is: " + e.toString());
            }

            // 服务连接成功回调
            for (IBindCommunicateServiceCallback callback : iBindCommunicateServiceCallbacks) {
                callback.bindService(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Logger.e("Service has unexpectedly disconnected: " + mCommunicatePackageName);
            mService = null;

            // 方案1和2都加上，做强大些，避免有些手机有些场景拉活不了。
            // R17：方案1可以在切到任务列表-全部清理时，做到A、B都拉活。R11s：方案1拉活不了。
            // 方案2，只能做到一个进程挂掉时，另一个进程拉活他。

            // 方案1：通过广播拉活另一个进程。
            helpRestartBroadcast();
            // 方案2：通过绑定服务拉活另一个进程。
            onBindService();

            for (IBindCommunicateServiceCallback callback : iBindCommunicateServiceCallbacks) {
                callback.bindService(false);
            }
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Logger.d("onBindingDied " + name);
        }
    };

    private ICommunicateServiceCallback mCallback = new ICommunicateServiceCallback.Stub() { // 接收远程服务的消息回调
        @Override
        public void replyStringMessage(int type, String message) {
            for (IMsgCallback callback : iMsgCallbacks) {
                if (callback != null) {
                    callback.getMsgCallback(type, message);
                }
            }
        }
    };

    /**
     * 发送拉活广播。
     * 一定要使用显示调用的广播，即不仅仅只有Action，还要指定包名和广播接收器的路径。
     * Android 5.0后隐式调用失效，为不安全的调用方式。
     *
     * @author tangwei
     */
    private void helpRestartBroadcast() {
        Logger.d("helpRestartBroadcast from: " + mCommunicatePackageName);
        Intent intent = new Intent(WAKEUP_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        ComponentName componentName = new ComponentName(mCommunicatePackageName,
                HelpWakeupReceiver.class.getName());
        intent.setComponent(componentName);
        try {
            Class<?> userHandle = Class.forName("android.os.UserHandle");
            UserHandle systemUserHandle = ReflectUtils.get(userHandle, "SYSTEM");
            mContext.sendBroadcastAsUser(intent, systemUserHandle);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拉活方法
     */
    public void helpRestart() {
        helpRestartBroadcast();
        onBindService();
    }

    public void replyStringMessage(int type, String message) {
        for (IMsgCallback callback : iLocalMsgCallbacks) {
            if (callback != null) {
                callback.getMsgCallback(type, message);
            }
        }
    }
}