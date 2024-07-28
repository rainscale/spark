package ale.rains.comm;

import ale.rains.comm.IRemoteServiceCallback;

interface IRemoteService {
    void sendMessage(int type, String message);
    void registerCallback(IRemoteServiceCallback cb);
    void unregisterCallback(IRemoteServiceCallback cb);
}