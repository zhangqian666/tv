package com.iptv.common.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.iptv.common.SharedPreferencesFactory;
import com.iptv.common.passport.UserInfo;

/**
 * 登录用户用户信息工厂类
 */
public class UserInfoFactory extends SharedPreferencesFactory {
    private static final String USER_INFO_KEY = "iptv_atv_user_info";

    public UserInfoFactory(Context curContext) {
        super(curContext, USER_INFO_KEY);
    }

    public void saveLoginedUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("username", userInfo.username);
        editor.putString("nickname", userInfo.nickname);
        editor.putString("token", userInfo.token);
        editor.putInt("vipType", userInfo.vipType);
        editor.putBoolean("isYearVip", userInfo.isYearVip);
        editor.putString("vipValidDate", userInfo.vipValidDate == null ? "" : userInfo.vipValidDate);
        editor.putBoolean("isVipValid", userInfo.isVipValid);
        editor.putBoolean("isLogouted", false);
        editor.commit();
    }

    public UserInfo getLoginedUserInfo() {
        UserInfo userInfo = null;
        SharedPreferences pre = getSharedPreferences();
        boolean isLogouted = pre.getBoolean("isLogouted", true);
        if (isLogouted) {
            return userInfo;
        }
        userInfo = new UserInfo();
        userInfo.username = pre.getString("username", "");
        userInfo.nickname = pre.getString("nickname", "");
        userInfo.token = pre.getString("token", "");
        userInfo.vipType = pre.getInt("vipType", 0);
        userInfo.isYearVip = pre.getBoolean("isYearVip", false);
        userInfo.vipValidDate = pre.getString("vipValidDate", "");
        userInfo.isVipValid = pre.getBoolean("isVipValid", false);
        return userInfo;
    }

    public void logout() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean("isLogouted", true);
        editor.commit();
    }
    
    public boolean isVip(UserInfo info) {
    	if (info == null) {
    		return false;
    	}
    	
		return info.isVipValid;
    }
}
