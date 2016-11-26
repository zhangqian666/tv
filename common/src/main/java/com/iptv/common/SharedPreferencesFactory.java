package com.iptv.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 操作SharedPreferences 工厂类
 */
public class SharedPreferencesFactory {
    private String key;
    private Context mContext;
    private SharedPreferences sharedPreferences;

    protected SharedPreferencesFactory(Context context, String key) {
        this.key = key;
        this.mContext = context;
    }

    protected SharedPreferences getSharedPreferences() {
        if (this.sharedPreferences == null) {
            this.sharedPreferences = this.mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }
}
