package com.iptv.common.local;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;

import com.iptv.common.SharedPreferencesFactory;

/**
 * 系统缓存工厂类
 */
public class SysCacheFactory extends SharedPreferencesFactory {
    private static final String SYS_CACHE_KEY = "iptv_atv_sys_cache";

    public SysCacheFactory(Context curContext) {
        super(curContext, SYS_CACHE_KEY);
    }

    public String getRandomUUID() {
        SharedPreferences sp = getSharedPreferences();
        String uuid = sp.getString("random_uuid","");
        if (uuid.length() == 0) {
            SharedPreferences.Editor editor = sp.edit();
            uuid = UUID.randomUUID().toString();
            editor.putString("random_uuid", uuid);
            editor.commit();
        }
        return uuid;
    }
}
