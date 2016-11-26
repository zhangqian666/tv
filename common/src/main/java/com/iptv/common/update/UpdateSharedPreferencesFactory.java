package com.iptv.common.update;

import android.content.Context;

import com.iptv.common.SharedPreferencesFactory;

public class UpdateSharedPreferencesFactory extends SharedPreferencesFactory {
	
	private static final String SHARED_NAME = "iptv_update_share";
	
	public UpdateSharedPreferencesFactory(Context context) {
		super(context, SHARED_NAME);
	}
	
	protected UpdateSharedPreferencesFactory(Context curContext, String key) {
		super(curContext, SHARED_NAME);
	}
	
	public int getInt(String key,int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }
    
    public long getLong(String key, long defaultValue){
    	return getSharedPreferences().getLong(key, defaultValue);
    }
    
    public void putLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).commit();
    }
    
    public String getString(String key, String defaultValue){
    	return getSharedPreferences().getString(key, defaultValue);
    }
    
    public void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }
    
    public Boolean getBoolean(String key, Boolean defaultValue){
    	return getSharedPreferences().getBoolean(key, defaultValue);
    }
    
    public void putBoolean(String key, Boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();
    }

}
