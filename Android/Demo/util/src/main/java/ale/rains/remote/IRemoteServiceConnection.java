package ale.rains.remote;

/**
 * 进程间服务连接状态回调
 */
public interface IRemoteServiceConnection {
    void onServiceConnect(boolean state);
}