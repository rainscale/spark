package ale.rains.adb.cmd;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import ale.rains.util.Logger;

/**
 * 检测adb的工具
 */
public class AdbChecker {
    /**
     * 连接成功后的action
     */
    public interface Action {
        void action();
    }

    private ArrayList<Action> mActions;
    private AtomicBoolean isChecking;

    public AdbChecker() {
        mActions = new ArrayList<>();
        isChecking = new AtomicBoolean(false);
    }

    public void addAction(Action action) {
        mActions.add(action);
    }

    public void removeAction(Action action) {
        mActions.remove(action);
    }

    public void check() {
        if (isChecking.get()) {
            Logger.d("is checking...");
            return;
        }
        Thread checkThread = new Thread(() -> {
            isChecking.set(true);
            while (!CmdTools.isInitialized()) {
                try {
                    if (!isChecking.get()) {
                        break;
                    }
                    Thread.sleep(10000);
                    if (!CmdTools.isInitialized()) {
                        Logger.d("please run adb tcpip 5555");
                    }
                } catch (InterruptedException e) {
                    Logger.e(e);
                }
            }
            callback();
        });
        checkThread.start();
    }

    private void callback() {
        isChecking.set(false);
        if (mActions != null && !mActions.isEmpty()) {
            for (Action action : mActions) {
                action.action();
            }
            mActions.clear();
        }
    }

    public void destroy() {
        if (mActions != null) {
            mActions.clear();
            mActions = null;
        }
        isChecking.set(false);
    }
}