package ale.rains.processors.logcat;

import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import ale.rains.util.FileUtils;
import ale.rains.util.Logger;
import ale.rains.util.ThreadPoolUtils;

public class LogcatProcessors {
    private static final String DEFAULT_LOG_COMMAND = "logcat";
    private static volatile LogcatProcessors instance;
    private BufferedWriter bufferedWriter;
    private LogcatThread thread;
    private String logCommand;
    private String logPath;

    private LogcatProcessors() {
    }

    public static LogcatProcessors getInstance() {
        if (instance == null) {
            synchronized (LogcatProcessors.class) {
                if (instance == null) {
                    instance = new LogcatProcessors();
                }
            }
        }
        return instance;
    }

    public String getLogCommand() {
        return logCommand;
    }

    public void setLogCommand(String logCommand) {
        this.logCommand = logCommand;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        Logger.i("logPath: " + logPath);
        this.logPath = logPath;
        if (!TextUtils.isEmpty(logPath)) {
            String parentPath = new File(logPath).getParent();
            if (parentPath != null) {
                new File(parentPath).mkdirs();
            }
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                bufferedWriter = new BufferedWriter(new FileWriter(logPath, true));
            } catch (IOException e) {
                Logger.e(e);
            }
        }
    }

    public void start() {
        if (TextUtils.isEmpty(logCommand)) {
            logCommand = DEFAULT_LOG_COMMAND;
        }
        thread = new LogcatThread(logCommand, new ILogCallBack() {
            @Override
            public void getLine(String line) {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.write(line + "\n");
                    } catch (IOException e) {
                    }
                }
            }
        });
        ThreadPoolUtils.postWork(thread);
        thread.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
            }
        });
    }

    public void stop() {
        if (thread != null) {
            thread.deleteObservers();
            thread.stop();
            ThreadPoolUtils.removeWork(thread);
        }
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                Logger.e(e);
            }
        }
        if (!TextUtils.isEmpty(logPath)) {
            FileUtils.deleteFile(logPath);
        }
    }
}