package ale.rains.util;

import ale.rains.adb.cmd.AdbCmd;
import ale.rains.adb.cmd.IAdbCmd;
import ale.rains.adb.cmd.OnReadLineListener;
import ale.rains.adb.constant.AdbConstant;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdbUtils {
    /**
     * 执行进程命令
     *
     * @param adbCommand 待执行指令
     */
    public static void execCommand(String adbCommand) {
        new AdbCmd().execCommand(adbCommand);
    }

    /**
     * 执行进程命令
     *
     * @param adbCommand 待执行指令
     */
    public static void execCommand(String adbCommand, long waitTime) {
        new AdbCmd().execCommand(adbCommand, waitTime);
    }


    public static void execCommand(String adbCommand, ICommonCallback callback) {
        execCommand(adbCommand, AdbConstant.DEFAULT_TIME_OUT, callback);
    }

    /**
     * 建议需要返回结果的都使用这个方法
     *
     * @param adbCommand adb指令
     * @param callback   主线程回调
     */
    public static void execCommand(String adbCommand, long waitTime, ICommonCallback callback) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> emitter.onNext(execCommandReturnResult(adbCommand, waitTime)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String info) {
                        if (callback != null) {
                            callback.onSuccess(info);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) {
                            callback.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 执行进程命令（带返回值）
     *
     * @param adbCommand 待执行指令
     * @return String   命令执行后返回的结果
     */
    public static String execCommandReturnResult(String adbCommand) {
        return new AdbCmd().execCommandReturnResult(adbCommand);
    }

    /**
     * 执行进程命令（带返回值）
     *
     * @param adbCommand 待执行指令
     * @param waitTime   0表示一直等待
     * @return String   命令执行后返回的结果
     */
    public static String execCommandReturnResult(String adbCommand, long waitTime) {
        return new AdbCmd().execCommandReturnResult(adbCommand, waitTime);
    }

    /**
     * 执行进程命令（带返回值）
     *
     * @param adbCommand 待执行指令
     * @return String   命令执行后返回的结果
     */
    public static IAdbCmd startCommand(String adbCommand, OnReadLineListener lineListener) {
        return new AdbCmd().startCommand(adbCommand, lineListener);
    }

}
