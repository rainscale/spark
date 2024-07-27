package ale.rains.comm;

/**
 * 进程间通信的消息回执
 */
public interface IMsgCallback {
    void getMsgCallback(int type, String msg);
}