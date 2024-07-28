package ale.rains.remote;

/**
 * 进程间通信的消息回执
 */
public interface IMessageCallback {
    void replyMessage(int type, String message);
}