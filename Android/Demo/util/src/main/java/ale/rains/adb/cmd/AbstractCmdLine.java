package ale.rains.adb.cmd;

import java.io.IOException;

public interface AbstractCmdLine {
    void writeCommand(String cmd);
    String readOutput();
    void close() throws IOException;
    boolean isClosed();
}
