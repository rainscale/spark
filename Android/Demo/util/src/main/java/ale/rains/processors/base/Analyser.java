package ale.rains.processors.base;

import java.util.ArrayList;
import java.util.List;

public abstract class Analyser {
    private Object result = null;
    private Object[] params = null;

    private List<ICallback> callbacks = new ArrayList<>();

    public Analyser() {
    }

    public Analyser(ICallback callback) {
        callbacks.add(callback);
    }

    public void addCallback(ICallback callback) {
        callbacks.add(callback);
    }

    public List<ICallback> getCallbacks() {
        return callbacks;
    }

    public void removeCallback(ICallback callback) {
        callbacks.remove(callback);
    }

    protected abstract Object operation(Object... params);

    public Object getResult() {
        return result;
    }

    protected void onResult(Object object) {
    }

    /**
     * 内部调用。
     *
     * @param objects
     */
    protected void internalOperation(Object... objects) {
        params = objects;

        result = operation(params);

        onResult(getResult());

        //把数据传递给注册到Analyser的回调接口。
        triggerResultCallback(getResult());
    }

    /**
     * 触发所有注册到Analyser的回调接口，传递结果。
     * 如果开发者在Object operation(Object param)中在普通Java线程的run线程体内产生结果，然后打算回调给Callback，
     * 那么需要在Object operation(Object param)中手动调用triggerResultCallback。
     * 如果Object operation(Object param)以正常的同步方式返回结果则无需再次手动调用triggerResultCallback。
     *
     * @param object
     */
    public void triggerResultCallback(Object object) {
        if (!callbacks.isEmpty()) {
            for (ICallback callback : callbacks) {
                callback.call(object);
            }
        }
    }
}