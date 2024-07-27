package ale.rains.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SPUtils {
    private static final String DEFAULT_SP_FILE_NAME = "default";

    /**
     * 保存多组给定字符串的值到缺省文件
     *
     * @param map 多组map值，字符串的类型自识别
     * @return 成功返回true，否则false
     */
    public static boolean put(Map<String, Object> map) {
        return put(null, map);
    }

    /**
     * 保存多组给定字符串的值
     *
     * @param fileName 文件名称
     * @param map      多组map值，字符串的类型自识别
     * @return 成功返回true，否则false
     */
    public static boolean put(String fileName, Map<String, Object> map) {
        if (null == map) {
            return false;
        }
        SharedPreferences.Editor editor = getSp(fileName).edit();
        Set<Map.Entry<String, Object>> set = map.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            Object value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (value instanceof String) {
                editor.putString(entry.getKey(), (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(entry.getKey(), (Integer) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(entry.getKey(), (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(entry.getKey(), (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(entry.getKey(), (Long) value);
            } else if (value instanceof Double) {
                editor.putLong(entry.getKey(), Double.doubleToLongBits((Double) value));
            } else {
                editor.putString(entry.getKey(), value.toString());
            }
        }
        return editor.commit();
    }

    /**
     * 保存指定字符串的值到缺省文件
     *
     * @param key   字符串名称
     * @param value 字符串值
     * @return 成功返回true，否则false
     */
    public static boolean put(String key, Object value) {
        return put(null, key, value);
    }

    /**
     * 保存指定字符串的值
     *
     * @param fileName 文件名称
     * @param key      字符串名称
     * @param value    字符串值
     * @return 成功返回true，否则false
     */
    public static boolean put(String fileName, String key, Object value) {
        SharedPreferences.Editor editor = getSp(fileName).edit();
        if (value == null) {
            value = "";
        }
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Double) {
            // Double以Long的形式保存，get的时候需转换
            editor.putLong(key, Double.doubleToLongBits((Double) value));
        } else {
            editor.putString(key, value.toString());
        }
        return editor.commit();
    }

    /**
     * 获取缺省文件字符串保存的整型值
     *
     * @param key      字符串名称
     * @param defValue 缺省值，类型为int
     * @return 返回字符串保持的整型值，读取失败返回缺省值
     */
    public static int getInt(String key, int defValue) {
        return getInt(null, key, defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的整型值
     *
     * @param fileName 文件名称
     * @param key      字符串名称
     * @param defValue 缺省值，类型为int
     * @return 返回字符串保持的整型值，读取失败返回缺省值
     */
    public static int getInt(String fileName, String key, int defValue) {
        SharedPreferences sp = getSp(fileName);
        return sp.getInt(key, defValue);
    }

    /**
     * 获取缺省文件字符串保存的布尔值
     *
     * @param key      字符串名称
     * @param defValue 缺省值，类型为boolean
     * @return 返回字符串保持的布尔值，读取失败返回缺省值
     */
    public static Boolean getBoolean(String key, Boolean defValue) {
        return getBoolean(null, key, defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的布尔值
     *
     * @param fileName 指定文件名称
     * @param key      字符串名称
     * @param defValue 缺省值，类型为boolean
     * @return 返回字符串保持的布尔值，读取失败返回缺省值
     */
    public static Boolean getBoolean(String fileName, String key, Boolean defValue) {
        SharedPreferences sp = getSp(fileName);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 获取缺省文件字符串保存的浮点值
     *
     * @param key      字符串名称
     * @param defValue 缺省值，类型为float
     * @return 返回字符串保持的浮点值，读取失败返回缺省值
     */
    public static float getFloat(String key, float defValue) {
        return getFloat(null, key, defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的浮点值
     *
     * @param fileName 文件名称
     * @param key      字符串名称
     * @param defValue 缺省值，类型为float
     * @return 返回字符串保持的浮点值，读取失败返回缺省值
     */
    public static float getFloat(String fileName, String key, float defValue) {
        SharedPreferences sp = getSp(fileName);
        return sp.getFloat(key, defValue);
    }

    /**
     * 获取缺省文件字符串保存的长整型值
     *
     * @param key      字符串名称
     * @param defValue 缺省值，类型为long
     * @return 返回字符串保持的长整型值，读取失败返回缺省值
     */
    public static Long getLong(String key, Long defValue) {
        return getLong(null, key, defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的长整型值
     *
     * @param fileName 文件名称
     * @param key      字符串名称
     * @param defValue 缺省值，类型为long
     * @return 返回字符串保持的长整型值，读取失败返回缺省值
     */
    public static Long getLong(String fileName, String key, Long defValue) {
        SharedPreferences sp = getSp(fileName);
        return sp.getLong(key, defValue);
    }

    /**
     * 获取缺省文件字符串保存的字符串值
     *
     * @param key      字符串名称
     * @param defValue 缺省值，类型为string
     * @return 返回字符串保持的字符串值，读取失败返回缺省值
     */
    public static String getString(String key, String defValue) {
        return getString(null, key, defValue);
    }

    /**
     * 获取指定文件里面，字符串保存的字符串值
     *
     * @param fileName 文件名称
     * @param key      字符串名称
     * @param defValue 缺省值，类型为String
     * @return 返回字符串保持的字符串值，读取失败返回缺省值
     */
    public static String getString(String fileName, String key, String defValue) {
        SharedPreferences sp = getSp(fileName);
        return sp.getString(key, defValue);
    }

    /**
     * 保存对象到缺省文件
     *
     * @param key    字符串名称
     * @param object 对象实体
     * @return 成功返回true，否则false
     */
    public static boolean putObject(String key, Object object) {
        return putObject(null, key, object);
    }

    /**
     * 保存对象到指定文件
     *
     * @param fileName 文件名称
     * @param key      字符串名称
     * @param object   对象实体
     * @return 成功返回true，否则false
     */
    public static boolean putObject(String fileName, String key, Object object) {
        SharedPreferences.Editor editor = getSp(fileName).edit();
        if (object == null) {
            editor.remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            Logger.e(e);
            return false;
        }
        // 将对象放到OutputStream中转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            Logger.e(e);
        }
        // 将编码后的字符串写到指定文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }

    /**
     * 获取缺省文件中的对象
     *
     * @param key 字符串名称
     * @return 成功返回对象，否则为null
     */
    public static Object getObject(String key) {
        return getObject(null, key);
    }

    /**
     * 获取指定文件中的对象
     *
     * @param fileName 文件名称
     * @param key      字符串名称
     * @return 成功返回对象，否则为null
     */
    public static Object getObject(String fileName, String key) {
        SharedPreferences sp = getSp(fileName);
        try {
            String base64Str = sp.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (base64Str.equals("")) {
                // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(base64Str.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * 保存List到缺省文件
     *
     * @param key      对象在文件中的名称
     * @param datalist 要保存的List实体
     */
    public static <T> void putDataList(String key, List<T> datalist) {
        putDataList(null, key, datalist);
    }

    /**
     * 保存List到指定文件
     *
     * @param fileName 文件名称
     * @param key      对象在文件中的名称
     * @param datalist 要保存的List实体
     */
    public static <T> void putDataList(String fileName, String key, List<T> datalist) {
        if (null == datalist || datalist.size() == 0)
            return;
        SharedPreferences.Editor editor = getSp(fileName).edit();
        Gson gson = new Gson();
        // 转换成json数据，再保存
        String json = gson.toJson(datalist);
        editor.putString(key, json);
        editor.apply();
    }

    /**
     * 获取缺省文件中的List
     *
     * @param key  List在文件中的名称
     * @param type 类型
     * @return 成功返回List，否则为null
     */
    public static <T> List<T> getDataList(String key, Type type) {
        return getDataList(null, key, type);
    }

    /**
     * 获取指定文件中的List
     *
     * @param fileName 文件名称
     * @param key      List在文件中的名称
     * @param type     类型
     * @return 成功返回List，否则为null
     */
    public static <T> List<T> getDataList(String fileName, String key, Type type) {
        if (null == key) return new ArrayList<>();
        SharedPreferences sp = getSp(fileName);

        List<T> datalist = new ArrayList<>();
        String jsonStr = sp.getString(key, null);
        if (null == jsonStr) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(jsonStr, type);
        return datalist;
    }

    /**
     * 移除缺省文件的指定字符串数据
     *
     * @param key 字符串名称
     * @return 成功返回true，否则 false
     */
    public static boolean remove(String key) {
        return remove(null, key);
    }

    /**
     * 移除指定文件的指定字符串数据
     *
     * @param fileName 文件名称
     * @param key      字符串名称
     * @return 成功返回true，否则 false
     */
    public static boolean remove(String fileName, String key) {
        SharedPreferences.Editor editor = getSp(fileName).edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 清空缺省文件中保存的所有数据
     *
     * @return 成功返回true，否则false
     */
    public static boolean clear() {
        return clear(null);
    }

    /**
     * 清空指定文件中保存的所有数据
     *
     * @param fileName 文件名称
     * @return 成功返回true，否则false
     */
    public static boolean clear(String fileName) {
        SharedPreferences.Editor editor = getSp(fileName).edit();
        editor.clear();
        return editor.commit();
    }

    private static SharedPreferences getSp(String fileName) {
        SharedPreferences sp;
        if (TextUtils.isEmpty(fileName)) {
            sp = AppContext.getContext().getSharedPreferences(DEFAULT_SP_FILE_NAME, Context.MODE_PRIVATE);
        } else {
            sp = AppContext.getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return sp;
    }
}