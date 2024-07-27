package ale.rains.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Shell执行线程，默认为非阻塞
 */
public class ShellThread extends Thread {
    public static final int WAIT_ASYNC = -1;
    public static final int WAIT_FOREVER = 0;
    public static final int WAIT_ONE_MINUTE = 60000;
    private String mCommand;
    private boolean isLoop;
    private OnReadListener mOnReadListener;
    private Process mProcess = null;
    private long mSleepTime;
    private long mTimeout;

    private ShellThread(Builder builder) {
        mCommand = builder.command;
        isLoop = builder.isLoop;
        mOnReadListener = builder.onReadListener;
        mSleepTime = builder.sleepTime;
        mTimeout = builder.timeout;
    }

    @Override
    public void run() {
        long tid = getId();
        long i = 0L;
        Logger.i("begin: " + tid);
        do {
            Logger.i("loop: " + i);
            runShellCommand(mCommand);
            try {
                Thread.sleep(mSleepTime);
            } catch (InterruptedException e) {
                Logger.e(e);
            }
            i++;
        } while (isLoop);
        Logger.i("end: " + tid);
    }

    @Override
    public void start() {
        Logger.d("timeout: " + mTimeout);
        super.start();
        if (mTimeout != WAIT_ASYNC) {
            try {
                this.join(mTimeout);
            } catch (InterruptedException e) {
                Logger.e(e);
            } finally {
                stopShellProcess();
            }
        }
    }

    private void runShellCommand(String command) {
        Logger.i("command: " + command);
        BufferedReader reader = null;
        try {
            // 错误输出流合并到标准输出流
            mProcess = new ProcessBuilder().redirectErrorStream(true).command(command.split("\\s+")).start();
            // mProcess = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (mOnReadListener != null) {
                    mOnReadListener.onRead(line);
                }
            }
            int result = mProcess.waitFor();
            Logger.i("exit: " + result);
        } catch (IOException e) {
            Logger.e(e);
        } catch (InterruptedException e) {
            Logger.e(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    Logger.e(e);
                }
            }
            if (mProcess != null) {
                mProcess.destroy();
                mProcess = null;
            }
        }
    }

    public void stopShellProcess() {
        isLoop = false;
        if (mProcess != null) {
            mProcess.destroy();
            // 针对sleep的情况
            interrupt();
            mProcess = null;
        }
    }

    public static class Builder {
        private String command;
        private boolean isLoop = false;
        private ShellThread.OnReadListener onReadListener;
        private long sleepTime = 0L;
        private long timeout = -1L;

        public ShellThread create() {
            return new ShellThread(this);
        }

        public Builder setCommand(String command) {
            this.command = command;
            return this;
        }

        public Builder setLoop(boolean isLoop) {
            this.isLoop = isLoop;
            return this;
        }

        public Builder setOnReadListener(ShellThread.OnReadListener onReadListener) {
            this.onReadListener = onReadListener;
            return this;
        }

        public Builder setSleepTime(long sleepTime) {
            this.sleepTime = sleepTime;
            return this;
        }

        public Builder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }
    }

    public static interface OnReadListener {
        /**
         * 读取命令执行返回的一行信息
         *
         * @param line 命令返回的一行字符串
         */
        void onRead(String line);
    }
}
