package ale.rains.util;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ReflectUtils {
    public static <T> T get(Class<?> clazz, String fieldName) throws Exception {
        return new ReflectFiled<T>(clazz, fieldName).get();
    }

    public static <T> T get(Class<?> clazz, String fieldName, Object instance) throws Exception {
        return new ReflectFiled<T>(clazz, fieldName).get(instance);
    }

    public static boolean set(Class<?> clazz, String fieldName, Object object) throws Exception {
        return new ReflectFiled(clazz, fieldName).set(object);
    }

    public static boolean set(Class<?> clazz, String fieldName, Object instance, Object value) throws Exception {
        return new ReflectFiled(clazz, fieldName).set(instance, value);
    }

    public static <T> T invoke(Class<?> clazz, String methodName, Object instance, Object... args) throws Exception {
        return new ReflectMethod(clazz, methodName).invoke(instance, args);
    }

    /**
     * 适用参数中包含匿名内部类的方法调用
     *
     * @param clazz
     * @param methodName
     * @param parametersType
     * @param instance
     * @param args
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     */
    public static <T> T invokeHideMethod(Class<?> clazz, String methodName, Class<?>[] parametersType, Object instance, Object... args) throws IllegalAccessException, NoSuchFieldException, InvocationTargetException {

//        method.invoke(mContext, intent, mConnection, Context.BIND_AUTO_CREATE, systemUserHandle);
        return new ReflectMethod(clazz, methodName, parametersType).invoke(instance, args);
    }

    /**
     * 反射执行无参数方法，不支持有参数的方法
     *
     * @param clazz      类
     * @param obj        对象
     * @param methodName 方法名
     * @return 返回值
     */
    public static Object invokeMethodWithoutParam(Class<?> clazz, Object obj, String methodName) {
        if (clazz == null || obj == null || TextUtils.isEmpty(methodName)) {
            return null;
        }

        try {
            Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(obj);
        } catch (NoSuchMethodException e) {
            Logger.e(e);
        } catch (IllegalAccessException e) {
            Logger.e(e);
        } catch (InvocationTargetException e) {
            Logger.e(e);
        }
        return null;

    }

    /**
     * 反射执行任意方法
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param instance
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T invoke(Class<?> clazz, String methodName, Object instance, Class[] paramType, Object[] paramValue) {

        T result = null;
        try {
            result = new ReflectMethod(clazz, methodName, paramType).invoke(instance, paramValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 反射执行带一个boolean参数的方法，不支持其他参数的方法
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param obj        对象
     * @return 返回值
     */
    public static Object invokeMethodWithboo(Class<?> clazz, String methodName, Object obj, boolean param) {
        if (clazz == null || obj == null || TextUtils.isEmpty(methodName)) {
            return null;
        }

        try {
            Method method = clazz.getDeclaredMethod(methodName, boolean.class);
            method.setAccessible(true);
            return method.invoke(obj, param);
        } catch (NoSuchMethodException e) {
            Logger.e(e);
        } catch (IllegalAccessException e) {
            Logger.e(e);
        } catch (InvocationTargetException e) {
            Logger.e(e);
        }
        return null;

    }

    /**
     * 反射执行带一个int参数的方法，不支持其他参数的方法
     *
     * @param clazz      类
     * @param obj        对象
     * @param methodName 方法名
     * @return 返回值
     */
    public static Object invokeMethodWithint(Class<?> clazz, Object obj, String methodName, int param) {
        if (clazz == null || obj == null || TextUtils.isEmpty(methodName)) {
            return null;
        }

        try {
            Method method = clazz.getDeclaredMethod(methodName, int.class);
            method.setAccessible(true);
            return method.invoke(obj, param);
        } catch (NoSuchMethodException e) {
            Logger.e(e);
        } catch (IllegalAccessException e) {
            Logger.e(e);
        } catch (InvocationTargetException e) {
            Logger.e(e);
        }
        return null;

    }

    /**
     * 通过类名获取Class对象
     *
     * @param className 类名
     * @return Class对象  如果找不到类，则返回null
     */
    public static Class<?> getClass(String className) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Logger.e(e);
        }

        return null;
    }

    /**
     * 通过newInstance获取Object对象,注意：如果类的无参构造私有化了就无法使用该方法了
     *
     * @param clazz Class对象
     * @return Object  获取Class类对应的一个对象，具体使用时再做强转
     */
    public static Object getInstance(Class<?> clazz) {
        if (clazz == null) {
            Logger.e("ReflectUtils : getClass : clazz == null");
            return null;
        }
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException e) {
            Logger.e(e);
        } catch (InstantiationException e) {
            Logger.e(e);
        }

        return null;
    }

    /**
     * 通过反射获得对应字段
     *
     * @param cl    class对象
     * @param obj   Object类对象
     * @param field 字段名
     * @return 字段值，具体使用时需要强转
     */
    public static Object getField(Class<?> cl, Object obj, String field) {
        try {
            Field localField = cl.getDeclaredField(field);
            localField.setAccessible(true);
            return localField.get(obj);
        } catch (IllegalAccessException e) {
            Logger.e(e);
        } catch (NoSuchFieldException e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * 通过反射修改字段值
     *
     * @param cl    class对象
     * @param obj   待修改对象
     * @param field 待修改值的字段名
     * @param value 修改值
     */
    public static void setField(Class<?> cl, Object obj, String field, Object value) {
        try {
            Field localField = cl.getDeclaredField(field);
            localField.setAccessible(true);
            localField.set(obj, value);
        } catch (NoSuchFieldException e) {
            Logger.e(e);
        } catch (IllegalAccessException e) {
            Logger.e(e);
        }
    }
}