package com.iptv.common.local;

import android.content.Context;

import com.iptv.common.SharedPreferencesFactory;

/**
 * 播放设置工厂类
 */
public class PlaySettingFactory extends SharedPreferencesFactory {
    private static final String PLAY_SETTING_KEY = "iptv_atv_play_setting";
    
    public PlaySettingFactory(Context curContext) {
        super(curContext, PLAY_SETTING_KEY);
    }

    public int getInt(String key,int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }
}
