package ale.rains.comm;

import ale.rains.comm.ICommunicateServiceCallback;

interface ICommunicateService {
    void doSomething(String message);
    void sendStringMessage(int type, String message);
    void registerCallback(ICommunicateServiceCallback cb);
    void unregisterCallback(ICommunicateServiceCallback cb);
}