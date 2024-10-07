package ale.rains.remote;

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
public class RemoteManager {
    private static final String WAKEUP_ACTION = "ale.rains.wakeup";
    private static final long DELAY = 5 * 60;
    private IRemoteService mService = null;
    private static volatile RemoteManager sInstance;
    private Context mContext;
    private String mRemotePackageName = "";

    private List<IRemoteServiceConnection> mOnServiceConnections = new ArrayList<>();
    private List<IMessageCallback> rMessageCallbacks = new ArrayList<>();
    private List<IMessageCallback> lMessageCallbacks = new ArrayList<>();
    private IMessageCallback defaultRemoteMessageCallback;
    private IMessageCallback defaultLocalMessageCallback;

    private ScheduledFuture mScheduledFuture;

    /**
     * 注册消息回执的监听
     *
     * @param cb 消息回执的回调
     */
    public void registerRemoteMessageCallback(IMessageCallback cb) {
        if (cb == null && !rMessageCallbacks.contains(defaultRemoteMessageCallback)) {
            rMessageCallbacks.add(defaultRemoteMessageCallback);
            return;
        }
        rMessageCallbacks.add(cb);
    }

    /**
     * 反注册消息回执的监听
     *
     * @param cb 消息回执的回调
     */
    public void unregisterRemoteMessageCallback(IMessageCallback cb) {
        if (cb == null && rMessageCallbacks.contains(defaultRemoteMessageCallback)) {
            rMessageCallbacks.remove(defaultRemoteMessageCallback);
            return;
        }
        rMessageCallbacks.remove(cb);
    }

    /**
     * 注册本地消息回执的监听
     *
     * @param cb 消息回执的回调
     */
    public void registerLocalMessageCallback(IMessageCallback cb) {
        if (cb == null && !lMessageCallbacks.contains(defaultLocalMessageCallback)) {
            lMessageCallbacks.add(defaultLocalMessageCallback);
            return;
        }
        lMessageCallbacks.add(cb);
    }

    /**
     * 反注册本地消息回执的监听
     *
     * @param cb 消息回执的回调
     */
    public void unregisterLocalMessageCallback(IMessageCallback cb) {
        if (cb == null && lMessageCallbacks.contains(defaultLocalMessageCallback)) {
            lMessageCallbacks.remove(defaultLocalMessageCallback);
            return;
        }
        lMessageCallbacks.remove(cb);
    }

    /**
     * 注册服务连接的监听
     *
     * @param cb 监听回调
     */
    public void registerOnServiceConnection(IRemoteServiceConnection cb) {
        mOnServiceConnections.add(cb);
        cb.onServiceConnect(isBound());
    }

    /**
     * 反注册服务连接的监听
     *
     * @param cb 回调
     */
    public void unregisterOnServiceConnection(IRemoteServiceConnection cb) {
        mOnServiceConnections.remove(cb);
        cb.onServiceConnect(isBound());
    }

    public static RemoteManager getInstance() {
        if (sInstance == null) {
            synchronized (RemoteManager.class) {
                if (sInstance == null) {
                    sInstance = new RemoteManager();
                }
            }
        }
        return sInstance;
    }

    private RemoteManager() {
        defaultRemoteMessageCallback = new IMessageCallback() {
            @Override
            public void replyMessage(int type, String message) {
                Logger.i("default remote reply " + type + ":" + message);
            }
        };
        defaultLocalMessageCallback = new IMessageCallback() {
            @Override
            public void replyMessage(int type, String message) {
                Logger.i("default local replay " + type + ":" + message);
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
                        mRemotePackageName = appInfo.metaData.getString("ale.rains.remote.package");
                        Logger.d("remote package " + mRemotePackageName);
                    }
                }
            } catch (Exception e) {
                Logger.e(e);
            }
            if (TextUtils.isEmpty(mRemotePackageName)) {
                Logger.e("remote package name is empty");
            } else {
                onBindService();
            }
        }
        if (mScheduledFuture == null) {
            final WakeupTask wakeupTask = new WakeupTask(mContext, mRemotePackageName);
            mScheduledFuture = WakeupManager.getInstance().postRepeatTask(wakeupTask, 0, DELAY, TimeUnit.SECONDS);
        }
    }

    /**
     * 绑定服务
     */
    private void onBindService() {
        final Intent intent = new Intent();
        ComponentName componentName = new ComponentName(mRemotePackageName,
                RemoteService.class.getName());
        intent.setComponent(componentName);
        try {
            Logger.d("bindService " + mRemotePackageName);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    // bindServiceAsUser(intent);
                    mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                }
            }, 3000);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    private void bindServiceAsUser(Intent intent) {
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
    }

    /**
     * 解绑服务
     */
    public void onUnbindService() {
        if (isBound()) {
            mContext.unbindService(mConnection);
            Logger.d("unbindService: " + mRemotePackageName);

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
     * @param type    消息类型，@see RemoteConstants.MsgType
     * @param message 消息内容
     * @return 消息是否发送成功
     */
    public boolean sendMessage(int type, String message) {
        boolean result = false;
        if (isBound()) {
            try {
                mService.sendMessage(type, message);
                result = true;
            } catch (RemoteException e) {
                Logger.e(e);
            }
        } else {
            Logger.e("service " + mRemotePackageName + " not connected");
        }

        return result;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IRemoteService.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
                Logger.d("service " + mRemotePackageName + " connected");
            } catch (RemoteException e) {
                Logger.e("service " + mRemotePackageName + " connect failed: " + e.toString());
            }

            // 服务连接成功状态回调
            for (IRemoteServiceConnection cb : mOnServiceConnections) {
                cb.onServiceConnect(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Logger.e("service " + mRemotePackageName + " disconnected");
            mService = null;

            // 1.通过广播拉活另一个进程。
            sendWakupBroadcast();
            // 2.通过绑定服务拉活另一个进程。
            onBindService();

            // 服务连接断开状态回调
            for (IRemoteServiceConnection cb : mOnServiceConnections) {
                cb.onServiceConnect(false);
            }
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Logger.d("service " + name + "onBindingDied");
        }
    };

    /**
     * 接收远程服务的消息回调
     */
    private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
        @Override
        public void replyMessage(int type, String message) {
            for (IMessageCallback cb : rMessageCallbacks) {
                if (cb != null) {
                    cb.replyMessage(type, message);
                }
            }
        }
    };

    /**
     * 发送唤醒广播一定要使用显示调用的广播，即不仅仅只有Action，还要指定包名和广播接收器的路径
     */
    private void sendWakupBroadcast() {
        Logger.d("wakeup service " + mRemotePackageName);
        Intent intent = new Intent(WAKEUP_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        ComponentName componentName = new ComponentName(mRemotePackageName,
                HelpWakeupReceiver.class.getName());
        intent.setComponent(componentName);
        try {
            Class<?> userHandle = Class.forName("android.os.UserHandle");
            UserHandle systemUserHandle = ReflectUtils.get(userHandle, "SYSTEM");
            mContext.sendBroadcastAsUser(intent, systemUserHandle);
        } catch (ClassNotFoundException e) {
            Logger.e(e);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    public void wakeup() {
        sendWakupBroadcast();
        onBindService();
    }

    public void replyMessage(int type, String message) {
        for (IMessageCallback callback : lMessageCallbacks) {
            if (callback != null) {
                callback.replyMessage(type, message);
            }
        }
    }
}