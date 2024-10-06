package ale.rains.adb.cmd;

public interface IAdbCmd {

    void execCommand(String adbCommand);

    void execCommand(String adbCommand, long waitTime);

    String execCommandReturnResult(String adbCommand);

    String execCommandReturnResult(String adbCommand, long time);

    IAdbCmd startCommand(String adbCommand, OnReadLineListener lineListener);

    void stopCommand();

    boolean isClose();
}

