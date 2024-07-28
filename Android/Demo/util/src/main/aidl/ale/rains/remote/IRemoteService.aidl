package ale.rains.remote;

import ale.rains.remote.IRemoteServiceCallback;

interface IRemoteService {
    void sendMessage(int type, String message);
    void registerCallback(IRemoteServiceCallback cb);
    void unregisterCallback(IRemoteServiceCallback cb);
}