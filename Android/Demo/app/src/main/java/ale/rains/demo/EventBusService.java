package ale.rains.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ale.rains.demo.model.EventMessage;
import ale.rains.util.Logger;

public class EventBusService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        //注册数据监听
        EventBus.getDefault().register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Subscribe
    public void onMsgEventReceived(String msg) {
        Logger.i("msg: " + msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 1)
    public void onMsgEventReceived(EventMessage event) {
        Logger.i("MsgEvent msg: " + event.getMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解注册数据监听
        EventBus.getDefault().unregister(this);
    }
}