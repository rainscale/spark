package ale.rains.comm;

/**
 * 字符串消息事件
 */
public class StringMsgEvent {
    /**
     * 消息类型
     */
    public int type;
    /**
     * 消息内容
     */
    public String msg;

    public StringMsgEvent(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}