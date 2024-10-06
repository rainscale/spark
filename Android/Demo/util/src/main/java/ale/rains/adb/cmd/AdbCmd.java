package ale.rains.adb.cmd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ale.rains.adb.constant.AdbConstant;
import ale.rains.util.Logger;
import ale.rains.util.StringUtils;
import ale.rains.util.ThreadManager;

public class AdbCmd implements IAdbCmd {
    protected boolean isStop;
    private volatile CmdLine cmdLine;
    protected static ExecutorService sAdbThreadPool = new ThreadPoolExecutor(0, 5,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), new ThreadManager.CustomThreadFactory()); //IO线程池

    public AdbCmd() {
    }

    @Override
    public void execCommand(String adbCommand) {
        execCommandReturnResult(adbCommand);
    }

    @Override
    public void execCommand(String adbCommand, long waitTime) {
        execCommandReturnResult(adbCommand, waitTime);
    }

    @Override
    public String execCommandReturnResult(String adbCommand) {
        return execCommandReturnResult(adbCommand, AdbConstant.DEFAULT_TIME_OUT);
    }

    @Override
    public String execCommandReturnResult(String adbCommand, long waitTime) {
        if (StringUtils.isBlank(adbCommand)) {
            return "";
        }
        if (adbCommand.contains("adb shell")) {
            adbCommand = adbCommand.replaceAll("adb shell", "").trim();
        }
        if (StringUtils.isBlank(adbCommand)) {
            return "";
        }
        if (adbCommand.length() > 1) {
            if (adbCommand.startsWith("\"") && adbCommand.endsWith("\"")) {
                adbCommand = adbCommand.substring(1, adbCommand.length() - 1);
            } else if (adbCommand.startsWith("'") && adbCommand.endsWith("'")) {
                adbCommand = adbCommand.substring(1, adbCommand.length() - 1);
            }
        }
        Logger.i("adbCommand: " + adbCommand + ", waitTime: " + waitTime);
        if (CmdTools.isInitialized()) {
            return CmdTools.execAdbCmd(adbCommand, waitTime);
        } else {
            if (CmdTools.generateConnection()) {
                return CmdTools.execAdbCmd(adbCommand, waitTime);
            } else {
                Logger.e("adbLib init failed, please check adb permission");
                return AdbConstant.ADB_DISCONNECTED;
            }
        }
    }

    @Override
    public IAdbCmd startCommand(String adbCommand, OnReadLineListener lineListener) {
        try {
            if (cmdLine != null) {
                cmdLine.close();
            }
            sAdbThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    cmdLine = CmdTools.openAdbStream("shell:");
                    String realAdbCommand;
                    if (!adbCommand.endsWith("\n")) {
                        realAdbCommand = adbCommand + "\n";
                    } else {
                        realAdbCommand = adbCommand;
                    }
                    cmdLine.writeCommand(realAdbCommand);
                    ThreadManager.sleep(1000);
                    cmdLine.readOutput((line) -> {
                        if (lineListener != null) {
                            lineListener.onReadLine(line);
                        }
                    }, AdbConstant.DEFAULT_TIME_OUT);
                }
            });
        } catch (Exception e) {
            Logger.e(e);
        }
        return this;
    }

    @Override
    public void stopCommand() {
        isStop = true;
        try {
            if (cmdLine != null) {
                cmdLine.close();
            }
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    @Override
    public boolean isClose() {
        return isStop;
    }
}