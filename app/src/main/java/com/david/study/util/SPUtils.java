package com.david.study.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.david.study.app.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * SharedPreferences工具类
 * Created by DavidChen on 2015/12/19.
 */
public class SPUtils {

    /**
     * 保存在手机里的文件名
     */
    public static final String FILE_NAME = Config.SP_FILE_NAME;

    /**
     * 保存数据的方法，根据保存数据类型的不同，调用不同的保存方法
     * @param context context
     * @param key   名称
     * @param object    值
     */
    public static String put(Context context, String key, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
        return key;
    }

    /**
     * 得到保存数据的方法，根据默认值类型的不同，调用不同的get方法
     * @param context context
     * @param key key
     * @param defaultObject 默认值
     * @return 需要的值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除key对应的值
     * @param context context
     * @param key key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 移除所有数据
     * @param context context
     */
    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否存在
     * @param context context
     * @param key key
     * @return true，存在，false，不存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有键值对
     * @param context context
     * @return 所有键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }

    /**
     * 创建一个解决所有SharedPreferencesCompat.apply方法的一个兼容类
     */
    public static class SharedPreferencesCompat
    {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         * @return 方法
         */
        private static Method findApplyMethod() {
            try {
                Class clazz = SharedPreferences.Editor.class;
                return clazz.getMethod("apply");
            } catch (NoSuchMethodException e)
            {

            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         * @param editor editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e){
            }
            editor.commit();
        }
    }
}
