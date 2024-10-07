package ale.rains.processors.logcat;

import java.util.Observable;

import ale.rains.processors.base.Analyser;
import ale.rains.processors.base.Command;
import ale.rains.processors.base.Invoker;

public class LogcatThread extends Observable implements Runnable {
    private ILogCallBack logCallback;
    private String logCommand;
    Analyser analyser;

    public LogcatThread(String logCommand, ILogCallBack logCallback) {
        this.logCommand = logCommand;
        this.logCallback = logCallback;
    }

    @Override
    public void run() {
        analyser = new LogcatAnalyser();
        Command command = new LogCommand(analyser);
        command.setParam(logCommand, logCallback);
        Invoker invoker = new Invoker();
        invoker.setCommand(command);
        invoker.invoke();
    }

    public void stop() {
        ((LogcatAnalyser) analyser).stopCatchLog();
        logCommand = null;
        logCallback = null;
    }
}