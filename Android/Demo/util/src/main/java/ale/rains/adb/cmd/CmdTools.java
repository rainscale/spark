package ale.rains.adb.cmd;

import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ale.rains.adb.constant.AdbConstant;
import ale.rains.adb.lib.AdbBase64;
import ale.rains.adb.lib.AdbConnection;
import ale.rains.adb.lib.AdbCrypto;
import ale.rains.adb.lib.AdbStream;
import ale.rains.util.AppContext;
import ale.rains.util.Logger;
import ale.rains.util.StringUtils;

public class CmdTools {
    private static volatile AdbConnection connection;
    private static List<AdbStream> streams = new ArrayList<>();
    public static String DEVICE_ID = null;
    public static final String ERROR_NO_CONNECTION = "ERROR_NO_CONNECTION";
    public static final String ERROR_CONNECTION_ILLEGAL_STATE = "ERROR_CONNECTION_ILLEGAL_STATE";
    public static final String ERROR_CONNECTION_COMMON_EXCEPTION = "ERROR_CONNECTION_COMMON_EXCEPTION";
    private static ExecutorService cachedExecutor = Executors.newCachedThreadPool();
    private static ScheduledExecutorService scheduledExecutorService;
    private static volatile long LAST_RUNNING_TIME = 0;

    static {
        generateConnection();
    }

    /**
     * 是否已初始化
     *
     * @return
     */
    public static boolean isInitialized() {
        return connection != null;
    }

    public static String execAdbExtCmd(final String cmd, final int wait) {
        if (connection == null) {
            Logger.e("no connection");
            return "";
        }

        try {
            AdbStream stream = connection.open(cmd);
            Logger.i(stream.getLocalId() + "@" + cmd);
            streams.add(stream);

            // 当wait为0，每个10ms观察一次stream状况，直到shutdown
            if (wait == 0) {
                while (!stream.isClosed()) {
                    Thread.sleep(10);
                }
            } else {
                // 等待wait毫秒后强制退出
                Thread.sleep(wait);
                stream.close();
            }

            // 获取stream所有输出
            InputStream adbInputStream = stream.getInputStream();
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[128];
            int readCount = -1;
            while ((readCount = adbInputStream.read(buffer, 0, 128)) > -1) {
                sb.append(new String(buffer, 0, readCount));
            }
            streams.remove(stream);
            return sb.toString();
        } catch (IllegalStateException e) {
            Logger.e(e);
            if (connection != null) {
                connection.setFine(false);
            }
            boolean result = generateConnection();
            if (result) {
                return retryExecAdb(cmd, wait);
            } else {
                Logger.e("regenerateConnection failed");
                return "";
            }
        } catch (Exception e) {
            Logger.e(e);
            return "";
        }
    }

    public static AdbBase64 getBase64Impl() {
        return new AdbBase64() {
            @Override
            public String encodeToString(byte[] arg0) {
                // new sun.misc.BASE64Encoder().encode(arg0);
                return Base64.getEncoder().encodeToString(arg0);
            }
        };
    }

    /**
     * 生成Adb连接，由所在文件生成，或创建并保存到相应文件
     */
    public static synchronized boolean generateConnection() {
        if (connection != null && connection.isFine()) {
            return true;
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e1) {
                Logger.e(e1);
            } finally {
                connection = null;
            }
        }

        Socket sock;
        AdbCrypto crypto;
        AdbBase64 base64 = getBase64Impl();
        // getfileDirs()路径为/data/user/0/ale.rains.demo/files
        Logger.i("getfileDirs(): " + AppContext.getContext().getFilesDir());
        // 获取连接公私钥
        File privKey = new File(AppContext.getContext().getFilesDir(), AdbConstant.KEY_PATH_PRIVATE);
        File pubKey = new File(AppContext.getContext().getFilesDir(), AdbConstant.KEY_PATH_PUBLIC);

        if (!privKey.exists() || !pubKey.exists()) {
            try {
                crypto = AdbCrypto.generateAdbKeyPair(base64);
                privKey.delete();
                pubKey.delete();
                crypto.saveAdbKeyPair(privKey, pubKey);
            } catch (NoSuchAlgorithmException | IOException e) {
                Logger.e(e);
                return false;
            }
        } else {
            try {
                crypto = AdbCrypto.loadAdbKeyPair(base64, privKey, pubKey);
            } catch (Exception e) {
                Logger.e(e);
                try {
                    crypto = AdbCrypto.generateAdbKeyPair(base64);
                    privKey.delete();
                    pubKey.delete();
                    crypto.saveAdbKeyPair(privKey, pubKey);
                } catch (NoSuchAlgorithmException | IOException ex) {
                    Logger.e(ex);
                    return false;
                }
            }
        }

        // 开始连接adb
        Logger.i("Socket connecting...");
        try {
            String server = AdbConstant.KEY_IP;
            String[] split = server.split(":");
            sock = new Socket(split[0], Integer.parseInt(split[1]));
            sock.setReuseAddress(true);
        } catch (IOException e) {
            Logger.e(e);
            return false;
        }
        Logger.i("Socket connected");

        AdbConnection conn;
        try {
            conn = AdbConnection.create(sock, crypto);
            Logger.i("ADB connecting...");

            // 10s超时
            conn.connect(10 * 1000);
        } catch (Exception e) {
            Logger.e(e);
            // socket关闭
            if (sock.isConnected()) {
                try {
                    sock.close();
                } catch (IOException e1) {
                    Logger.e(e);
                }
            }
            return false;
        }
        connection = conn;
        Logger.i("ADB connected");

        if (DEVICE_ID == null) {
            DEVICE_ID = StringUtils.trim(execAdbCmd("getprop ro.serialno", 0));
            AdbConstant.KEY_SERIAL_ID = DEVICE_ID;
        }

        // ADB成功连接后，开启ADB状态监测
        startAdbStatusCheck();
        return true;
    }

    /**
     * 开始检查ADB状态
     */
    private static void startAdbStatusCheck() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                // 防止重复运行，14s内只能执行一次
                if (currentTime - LAST_RUNNING_TIME < 14 * 1000) {
                    return;
                }

                LAST_RUNNING_TIME = currentTime;
                String result = null;
                try {
                    result = execAdbCmd("echo '1'", 5000);
                } catch (Exception e) {
                    Logger.e(e);
                }

                if (!StringUtils.equals("1", StringUtils.trim(result))) {
                    // 等2s再检验一次
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Logger.e(e);
                    }

                    boolean genResult = false;

                    // double check机制，防止单次偶然失败带来重连
                    String doubleCheck = null;
                    try {
                        doubleCheck = execAdbCmd("echo '1'", 5000);
                    } catch (Exception e) {
                        Logger.e(e);
                    }
                    if (!StringUtils.equals("1", StringUtils.trim(doubleCheck))) {
                        // 尝试恢复3次
                        for (int i = 0; i < 3; i++) {
                            // 关停无用连接
                            if (connection != null && connection.isFine()) {
                                try {
                                    connection.close();
                                } catch (IOException e) {
                                    Logger.e(e);
                                } finally {
                                    connection = null;
                                }
                            }

                            // 清理下当前已连接进程
                            clearProcesses();

                            // 尝试重连
                            genResult = generateConnection();
                            if (genResult) {
                                break;
                            }
                        }

                        // 恢复失败
                        if (!genResult) {
                            boolean res = CmdTools.generateConnection();
                            if (!res) {
                                Logger.i("ADB未恢复，请连接PC执行\'adb tcpip 5555\'开启端口");
                            }
                            return;
                        }
                    }
                }

                // 15S 检查一次
                scheduledExecutorService.schedule(this, 15, TimeUnit.SECONDS);
            }
        }, 15, TimeUnit.SECONDS);
    }

    public static void clearProcesses() {
        try {
            for (AdbStream stream : streams) {
                Logger.i("stop stream: " + stream.toString());

                try {
                    stream.close();
                } catch (Exception e) {
                    Logger.e(e);
                }
            }
            streams.clear();
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 执行Adb命令，对外<br/>
     * <b>注意：主线程执行的话超时时间会强制设置为5S以内，防止ANR</b>
     *
     * @param cmd      对应命令
     * @param waitTime 等待执行时间，0表示一直等待
     * @return 命令行输出
     */
    public static String execAdbCmd(final String cmd, long waitTime) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (waitTime > 5000 || waitTime == 0) {
                Logger.w("主线程等待时间" + waitTime + "ms过长， 修改为5s");
                waitTime = 5000;
            }
            final long finalWaitTime = waitTime;
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() {
                    return _execAdbCmd(cmd, finalWaitTime);
                }
            };
            Future<String> result = cachedExecutor.submit(callable);
            try {
                return result.get();
            } catch (ExecutionException e) {
                Logger.e(e);
            } catch (InterruptedException e) {
                Logger.e(e);
            }
            return null;
        }
        return _execAdbCmd(cmd, waitTime);
    }

    /**
     * 执行Adb命令
     *
     * @param cmd      对应命令
     * @param waitTime 等待执行时间，0表示一直等待
     * @return 命令行输出
     */
    public static String _execAdbCmd(final String cmd, final long waitTime) {
        if (connection == null) {
            Logger.e("no connection when execAdbCmd");
            return "";
        }

        try {
            AdbStream stream = connection.open("shell:" + cmd);
            Logger.i(stream.getLocalId() + "@" + "shell:" + cmd);
            streams.add(stream);

            // 当waitTime为0，每个10ms观察一次stream状况，直到shutdown
            if (waitTime == 0) {
                while (!stream.isClosed()) {
                    Thread.sleep(10);
                }
            } else {
                // 等待最长wait毫秒后强制退出
                long start = System.currentTimeMillis();
                while (!stream.isClosed() && System.currentTimeMillis() - start < waitTime) {
                    Thread.sleep(10);
                }
                if (!stream.isClosed()) {
                    stream.close();
                }
            }

            // 获取stream所有输出
            Queue<byte[]> results = stream.getReadQueue();
            StringBuilder sb = new StringBuilder();
            for (byte[] bytes : results) {
                if (bytes != null) {
                    sb.append(new String(bytes));
                }
            }
            streams.remove(stream);
            return sb.toString();
        } catch (IllegalStateException e) {
            Logger.e(e);
            if (connection != null) {
                connection.setFine(false);
            }
            boolean result = generateConnection();
            if (result) {
                return retryExecAdb(cmd, waitTime);
            } else {
                Logger.e("regenerateConnection failed");
                return "";
            }
        } catch (Exception e) {
            Logger.e(e);
            return "";
        }
    }

    /**
     * 打开ADB Stream
     *
     * @return
     */
    public static CmdLine openAdbStream(String cmd) {
        if (connection == null) {
            Logger.e("no connection");
            return null;
        }
        try {
            AdbStream stream = connection.open(cmd);
            Logger.i(stream.getLocalId() + "@" + cmd);
            streams.add(stream);
            CmdLine cmdLine = new CmdLine(stream);

            // 记录tag
            cmdLine.cmdTag = stream.getLocalId() + "@" + cmd.split(":")[0] + ":";
            return cmdLine;
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }


    /**
     * 执行adb命令，在超时时间范围内
     *
     * @param cmd
     * @param timeout 超时时间（必大于0）
     * @return
     * @deprecated Use {link #execHighPrivilegeCmd(String, int)}
     */
    public static String execShellCmdWithTimeout(final String cmd, final long timeout) {
        if (connection == null) {
            Logger.i("connection is null");
            return "";
        }

        try {
            long startTime = System.currentTimeMillis();
            AdbStream stream = connection.open("shell:" + cmd);
            Logger.i(stream.getLocalId() + "@shell:" + cmd);
            streams.add(stream);

            while (!stream.isClosed() && System.currentTimeMillis() - startTime < timeout) {
                Thread.sleep(10);
            }

            if (!stream.isClosed()) {
                stream.close();
            }

            // 获取stream所有输出
            Queue<byte[]> results = stream.getReadQueue();
            StringBuilder sb = new StringBuilder();
            for (byte[] bytes : results) {
                if (bytes != null) {
                    sb.append(new String(bytes));
                }
            }
            streams.remove(stream);
            return sb.toString();
        } catch (IllegalStateException e) {
            Logger.e(e);

            if (connection != null) {
                connection.setFine(false);
            }
            boolean result = generateConnection();
            if (result) {
                return retryExecAdb(cmd, timeout);
            } else {
                return "";
            }
        } catch (Exception e) {
            Logger.e(e);
            return "";
        }
    }

    private static String retryExecAdb(String cmd, long waitTime) {
        AdbStream stream = null;
        try {
            stream = connection.open("shell:" + cmd);
            Logger.i(stream.getLocalId() + "@shell:" + cmd);
            streams.add(stream);

            // 当waitTime为0，每个10ms观察一次stream状况，直到shutdown
            if (waitTime == 0) {
                while (!stream.isClosed()) {
                    Thread.sleep(10);
                }
            } else {
                // 等待wait毫秒后强制退出
                long start = System.currentTimeMillis();
                while (!stream.isClosed() && System.currentTimeMillis() - start < waitTime) {
                    Thread.sleep(10);
                }
                if (!stream.isClosed()) {
                    stream.close();
                }
            }

            // 获取stream所有输出
            Queue<byte[]> results = stream.getReadQueue();
            StringBuilder sb = new StringBuilder();
            for (byte[] bytes : results) {
                if (bytes != null) {
                    sb.append(new String(bytes));
                }
            }
            streams.remove(stream);
            return sb.toString();
        } catch (Exception e) {
            Logger.e(e);
        }
        return "";
    }

    public static String execSafeCmd(String cmd, int retryCount) {
        String result = "";
        while (retryCount-- > 0) {
            result = execAdbCmdWithStatus(cmd, 0);
            Logger.w("execSafeCmd result: " + result);
            if (ERROR_NO_CONNECTION.equals(result) || ERROR_CONNECTION_ILLEGAL_STATE.equals(result)) {
                generateConnection();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Logger.e(e);
                }
            } else if (ERROR_CONNECTION_COMMON_EXCEPTION.equals(result)) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Logger.e(e);
                }
            } else {
                break;
            }
        }
        return result;
    }

    public static String execAdbCmdWithStatus(final String cmd, final long waitTime) {
        if (connection == null) {
            return ERROR_NO_CONNECTION;
        }
        try {
            AdbStream stream = connection.open("shell:" + cmd);
            Logger.i(stream.getLocalId() + "@shell:" + cmd);
            streams.add(stream);

            // 当waitTime为0，每个10ms观察一次stream状况，直到shutdown
            if (waitTime == 0) {
                while (!stream.isClosed()) {
                    Thread.sleep(10);
                }
            } else {
                // 等待wait毫秒后强制退出
                long start = System.currentTimeMillis();
                while (!stream.isClosed() && System.currentTimeMillis() - start < waitTime) {
                    Thread.sleep(10);
                }
                if (!stream.isClosed()) {
                    stream.close();
                }
            }

            // 获取stream所有输出
            Queue<byte[]> results = stream.getReadQueue();
            StringBuilder sb = new StringBuilder();
            for (byte[] bytes : results) {
                if (bytes != null) {
                    sb.append(new String(bytes));
                }
            }
            streams.remove(stream);
            return sb.toString();
        } catch (IllegalStateException e) {
            return ERROR_CONNECTION_ILLEGAL_STATE;
        } catch (Exception e) {
            return ERROR_CONNECTION_COMMON_EXCEPTION;
        }
    }

    public static String execAdbCmd(String cmd) {
        return execAdbCmd(cmd, 0);
    }
}