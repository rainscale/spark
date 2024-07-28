package ale.rains.comm;

interface IRemoteServiceCallback {
    void replyMessage(int type, String message);
}