package ale.rains.util;

public class ShellUtils {
    private static final String LF = "\n";

    /**
     * 执行Shell命令，一直阻塞直到线程任务结束
     *
     * @param command 命令字符串
     * @return 命令执行结果
     */
    public static String runAndWait(String command) {
        return run(command, ShellThread.WAIT_FOREVER);
    }

    /**
     * 执行Shell命令，超时时间1分钟，适合快速执行结束的指令
     *
     * @param command 命令字符串
     * @return 命令执行结果
     */
    public static String run(String command) {
        return run(command, ShellThread.WAIT_ONE_MINUTE);
    }

    /**
     * 执行Shell命令，自定义超时时间>0未返回结果阻塞
     *
     * @param command 命令字符串
     * @param timeout 超时时间
     * @return 命令执行结果
     */
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

    /**
     * 执行Shell命令，自定义超时时间>0未返回结果阻塞，-1异步执行
     *
     * @param command        命令字符串
     * @param timeout        超时时间
     * @param onReadListener 命令执行结果回调
     */
    public static void run(String command, long timeout, ShellThread.OnReadListener onReadListener) {
        ShellThread t = new ShellThread.Builder().setCommand(command).setTimeout(timeout).setOnReadListener(onReadListener).create();
        t.start();
    }
}
