package ale.rains.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        long i = 0L;
        do {
            LogUtils.d("loop: " + i);
            runShellCommand(mCommand);
            try {
                Thread.sleep(mSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        } while (isLoop);
        LogUtils.i("finish");
    }

    @Override
    public void start() {
        LogUtils.d("timeout: " + mTimeout);
        super.start();
        if (mTimeout != WAIT_ASYNC) {
            try {
                this.join(mTimeout);
            } catch (InterruptedException e) {
                LogUtils.e(e.getMessage());
            } finally {
                stopShellProcess();
            }
        }
    }

    private void runShellCommand(String command) {
        LogUtils.i("command: " + command);
        BufferedReader reader = null;
        try {
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
            LogUtils.i("exit value: " + result);
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        } catch (InterruptedException e) {
            LogUtils.e(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    LogUtils.e(e.getMessage());
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
        void onRead(String line);
    }
}
