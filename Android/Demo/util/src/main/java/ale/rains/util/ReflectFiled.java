package ale.rains.util;

import java.lang.reflect.Field;

public class ReflectFiled<Type> {
    private static final String TAG = "ReflectFiled";
    private Class<?> mClazz;
    private String mFieldName;

    private boolean mInit;
    private Field mField;

    public ReflectFiled(Class<?> clazz, String fieldName) {
        if (clazz == null || fieldName == null || fieldName.length() == 0) {
            throw new IllegalArgumentException("Both of invoker and fieldName can not be null or nil!");
        }
        this.mClazz = clazz;
        this.mFieldName = fieldName;
    }

    private synchronized void prepare() {
        if (mInit) {
            return;
        }
        Class<?> clazz = mClazz;
        while (clazz != null) {
            try {
                Field f = clazz.getDeclaredField(mFieldName);
                f.setAccessible(true);
                mField = f;
                break;
            } catch (Exception e) {
            }
            clazz = clazz.getSuperclass();
        }
        mInit = true;
    }

    public synchronized Type get() throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {
        return get(false);
    }

    @SuppressWarnings("unchecked")
    public synchronized Type get(boolean ignoreFieldNoExist)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (mField == null) {
            if (!ignoreFieldNoExist) {
                throw new NoSuchFieldException();
            }
            Logger.w(TAG, String.format("field: %s is no exists.", mFieldName));
            return null;
        }
        Type fieldVal = null;
        try {
            fieldVal = (Type) mField.get(null);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("unable to cast object");
        }
        return fieldVal;
    }

    public synchronized Type get(boolean ignoreFieldNoExist, Object instance)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (mField == null) {
            if (!ignoreFieldNoExist) {
                throw new NoSuchFieldException();
            }
            Logger.w(TAG, String.format("field: %s is no exists.", mFieldName));
            return null;
        }
        Type fieldVal = null;
        try {
            fieldVal = (Type) mField.get(instance);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("unable to cast object");
        }
        return fieldVal;
    }

    public synchronized Type get(Object instance) throws NoSuchFieldException, IllegalAccessException {
        return get(false, instance);
    }

    public synchronized Type getWithoutThrow(Object instance) {
        Type fieldVal = null;
        try {
            fieldVal = get(true, instance);
        } catch (NoSuchFieldException e) {
            Logger.e(TAG, e);
        } catch (IllegalAccessException e) {
            Logger.e(TAG, e);
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e);
        }
        return fieldVal;
    }

    public synchronized Type getWithoutThrow() {
        Type fieldVal = null;
        try {
            fieldVal = get(true);
        } catch (NoSuchFieldException e) {
            Logger.e(TAG, e);
        } catch (IllegalAccessException e) {
            Logger.e(TAG, e);
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e);
        }
        return fieldVal;
    }

    public synchronized boolean set(Object instance, Type val) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {
        return set(instance, val, false);
    }

    public synchronized boolean set(Object instance, Type val, boolean ignoreFieldNoExist)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (mField == null) {
            if (!ignoreFieldNoExist) {
                throw new NoSuchFieldException("method: " + mFieldName + " is not exists.");
            }
            Logger.w(TAG, String.format("field: %s is no exists.", mFieldName));
            return false;
        }
        mField.set(instance, val);
        return true;
    }


    public synchronized boolean setWithoutThrow(Object instance, Type val) {
        boolean result = false;
        try {
            result = set(instance, val, true);
        } catch (NoSuchFieldException e) {
            Logger.e(TAG, e);
        } catch (IllegalAccessException e) {
            Logger.e(TAG, e);
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e);
        }
        return result;
    }

    public synchronized boolean set(Type val) throws NoSuchFieldException, IllegalAccessException {
        return set(null, val, false);
    }

    public synchronized boolean setWithoutThrow(Type val) {
        boolean result = false;
        try {
            result = set(null, val, true);
        } catch (NoSuchFieldException e) {
            Logger.e(TAG, e);
        } catch (IllegalAccessException e) {
            Logger.e(TAG, e);
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e);
        }
        return result;
    }
}