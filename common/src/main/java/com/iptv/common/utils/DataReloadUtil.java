package com.iptv.common.utils;

import java.util.Vector;

public class DataReloadUtil {
    // 回看消息
    public static final int HomeRecChanViewID = 201;
    
    // 播放记录消息
    public static final int HomeHistoryViewID = 301;

    // 收藏消息
    public static final int HomeStoreViewID = 303;

    // 播放设置
    public static final int HomePlaySettingViewID = 401;
    // 用户设置
    public static final int HomeUserSettingViewID = 402;
    // 帐号
    public static final int HomeAccountViewID = 403;
    // 关于
    public static final int HomeAboutViewID = 404;
    // 个人中心
    public static final int HomePersonalViewID = 405;
    
    // 客房点餐
    public static final int HomeOrderViewID = 701;
    
    // 租车
    public static final int HomeTexiViewID = 702;
    
    // 打扫卫生
    public static final int HomeCleanViewID = 703;

    // 首页各view发生变化时,告诉首页reload的消息列表，如历史记录发生变化。
    private static Vector<Integer> sHomeReloadViewIDs = new Vector<Integer>();

//    public final static Vector<Integer> getReloadIds() {
//        return HomeReloadViewIDs;
//    }
    
    public static int size() {
    	return sHomeReloadViewIDs.size();
    }
    
    public static Integer get(int index) {
    	try {
    		return sHomeReloadViewIDs.get(index);
    	} catch (ArrayIndexOutOfBoundsException ex) {
    		ex.printStackTrace();
    	}
    	return null;
    }
    
    public static void addMessage(Integer key) {
    	if (!sHomeReloadViewIDs.contains(key)) {
    		sHomeReloadViewIDs.add(key);
    	}
    }
    
    public static boolean removeMessage(int index) {
    	try {
    		sHomeReloadViewIDs.removeElementAt(index);
    		return true;
    	} catch (ArrayIndexOutOfBoundsException ex) {
    		ex.printStackTrace();
    		return false;
    	}
    }
    
    public static void clear() {
    	sHomeReloadViewIDs.clear();
    }
    
}
