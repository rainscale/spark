package ale.rains.util;

public class ShellUtils {
    private static final String LF = "\n";

    public static String runAndWait(String command) {
        return run(command, ShellThread.WAIT_FOREVER);
    }

    public static String run(String command) {
        return run(command, ShellThread.WAIT_ONE_MINUTE);
    }

    public static String run(String command, long timeout) {
        StringBuilder sb = new StringBuilder();
        run(command, timeout, new ShellThread.OnReadListener() {
            @Override
            public void onRead(String line) {
                sb.append(line);
                sb.append(LF);
            }
        });
        return sb.toString();
    }

    public static void run(String command, long timeout, ShellThread.OnReadListener onnReadListener) {
        ShellThread thread = new ShellThread.Builder().setCommand(command).setTimeout(timeout).setOnReadListener(onnReadListener).create();
        thread.start();
    }
}
