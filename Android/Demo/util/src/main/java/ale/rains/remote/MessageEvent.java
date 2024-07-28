package ale.rains.remote;

/**
 * 消息事件结构体
 */
public class MessageEvent {
    /**
     * 消息类型
     */
    public int type;
    /**
     * 消息内容
     */
    public String msg;

    public MessageEvent(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}