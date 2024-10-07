package ale.rains.processors.logcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ale.rains.processors.base.Analyser;
import ale.rains.processors.base.ICallback;
import ale.rains.util.Logger;

public class LogcatAnalyser extends Analyser {
    private Process process = null;

    public LogcatAnalyser() {
    }

    public LogcatAnalyser(ICallback callback) {
        super(callback);
    }

    @Override
    protected Object operation(Object... params) {
        String logCommand = (String) params[0];
        ILogCallBack logCallBack = (ILogCallBack) params[1];
        catchLog(logCommand, logCallBack);
        return null;
    }

    public void stopCatchLog() {
        if (process != null) {
            process.destroy();
        }
    }

    private void catchLog(String logCommand, ILogCallBack logCallBack) {
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec(logCommand);
            InputStream in = process.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                if (logCallBack != null) {
                    logCallBack.getLine(line);
                }
            }
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Logger.e(e);
                }
            }
        }
    }
}