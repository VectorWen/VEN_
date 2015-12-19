package com.vector.ven.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Map;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * @author vector.huang
 * @version V1.0
 * @Description: 存储、读取APP 需要缓存的数据
 * @date 2015/5/7.
 */
public class DataPool {

    private Context context;
    /**
     * sp 文件默认使用的文件名
     */
    public static final String defaultDataCachePoolName = "vector";
    public static final String SETTING = "setting";

    private String dataCachePoolName;
    private SharedPreferences sp;

    public DataPool(Context context) {
        this(defaultDataCachePoolName, context);
    }

    public DataPool(String dataCachePoolName, Context context) {
        this.dataCachePoolName = dataCachePoolName;
        this.context = context;
        sp = context.getSharedPreferences(this.dataCachePoolName,
                Context.MODE_PRIVATE);
    }

    /**
     * add a key(String)-value(Serializable object) into SharedPreference
     *
     * @param key
     * @param value
     * @return true if add successfully
     */
    public boolean put(String key, Serializable value) {
        boolean flag = false;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            String base64String = Base64.encodeToString(bos.toByteArray(),
                    Base64.DEFAULT);
            sp.edit().putString(key, base64String).apply();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * add a key(String)-value(String) into SharedPreference
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }
    /**
     * add a key(String)-value(long) into SharedPreference
     * @param key
     * @param value
     */
    public void putLong(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    /**
     * add a key(String,default is "temp")-value(object) into SharedPreference
     *
     * @param value value of this pair ,with the defalut key="temp"
     * @return true if add successfully
     */
    public boolean put(Serializable value) {
        return put("temp", value);
    }

    /**
     * get value(Serializable Object) from DataPool(SharedPreference) with the
     * given Key
     *
     * @param key key of this pair ,with the defalut key="temp"
     * @return one Serializable Object
     */
    public Serializable get(String key) {
        if (!contains(key))
            return null;
        String base64String = sp.getString(key, "");
        byte[] buf = Base64.decode(base64String, Base64.DEFAULT);
        ByteArrayInputStream bis = new ByteArrayInputStream(buf);
        ObjectInputStream ois = null;
        Serializable result = null;
        try {
            ois = new ObjectInputStream(bis);
            result = (Serializable) ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return result;
    }

    /**
     * get value(String) from DataPool(SharedPreference) with the
     * given Key
     *
     * @param key
     * @return String,if key not exist return "" string
     */
    public String getString(String key) {
         return sp.getString(key,"");
    }

    /**
     * get value(long) from DataPool(SharedPreference) with the
     * given Key
     *
     * @param key
     * @return String,if key not exist return 0 long
     */
    public long getLong(String key) {
         return sp.getLong(key,0);
    }

    /**
     * check if DataPool(SharedPreference) contain the given key
     *
     * @param key the given key
     * @return true if it contains
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     * check if DataPool(SharedPreference) is empty
     *
     * @return true if it's empty
     */
    public boolean isEmpty() {
        return sp.getAll().size() == 0;
    }

    /**
     * remove a key-value of this pair
     *
     * @param key the key of this pair
     * @return true if it removes successfully
     */
    public void remove(String key) {
        if (!contains(key))
            return;
        sp.edit().remove(key).apply();
    }

    /**
     * remove all the key-value of this pair
     *
     * @return
     */
    public void removeAll() {
        if (isEmpty())
            return;
        Map<String, ?> map = sp.getAll();
        for (String key : map.keySet()) {
            remove(key);
        }
    }

    /**
     * update the key-value
     *
     * @param key   key of this pair
     * @param value value of this pair
     * @return true if set successfully
     */
    public boolean set(String key, Serializable value) {
        if (!contains(key))
            return false;
        return put(key, value);
    }




}
