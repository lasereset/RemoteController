package com.dmrjkj.remotecontroller.cache;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dmrjkj.remotecontroller.AppApplication;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

import java.util.HashMap;
import java.util.Map;

public class ShareprefrenceCache {

    private static SharedPreferences sp = null;
    private static ShareprefrenceCache instance = null;

    public static synchronized ShareprefrenceCache getInstance() {
        if (instance == null) {
            instance = new ShareprefrenceCache();
        }
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(AppApplication.getInstance());
        }
        return instance;
    }
    
    public void PInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    public void PBool(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public void PFloat(String key, float value) {
        sp.edit().putFloat(key, value).apply();
    }

    public float GFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public int GInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public boolean GBool(String key, boolean defualtValue) {
        return sp.getBoolean(key, defualtValue);
    }

    public void PString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public String GString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public Map<String, String> getAllDeviceCache() {
        String mapDatas = GString(BLE_DEVICES_MAP, "");
        if (ToolUtils.isEmpty(mapDatas)) {
            return null;
        }
        return JSON.parseObject(mapDatas, new TypeReference<Map<String, String>>(){});
    }

    public boolean saveDeviceToCache(String mac, String name) {
        if (ToolUtils.isEmpty(mac)) {
            return false;
        }
        Map<String, String> data = getAllDeviceCache();
        if (ToolUtils.isEmpty(data)) {
            data = new HashMap<>();
        }
        boolean exist = data.containsKey(mac);
        data.put(mac, name);
        PString(BLE_DEVICES_MAP, JSON.toJSONString(data));
        return exist;
    }

    public boolean removeDevice(String mac) {
        Map<String, String> data = getAllDeviceCache();
        if (ToolUtils.isEmpty(data)) {
            return false;
        }
        if (data.containsKey(mac)) {
            data.remove(mac);
            PString(BLE_DEVICES_MAP, JSON.toJSONString(data));
            return true;
        }
        return false;
    }

    public static final String BLE_DEVICES_MAP = "BLE_DEVICES_MAP";
    public static final String FEEDBACK_TOUCH_MODEL = "FEEDBACK_TOUCH_MODEL";
}
