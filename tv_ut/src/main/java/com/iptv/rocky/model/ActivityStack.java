package com.iptv.rocky.model;

import java.lang.ref.WeakReference;
import java.util.Stack;

import android.app.Activity;

public final class ActivityStack {
	
	private static Stack<WeakReference<Activity>> activityStack = new Stack<WeakReference<Activity>>();
	
	/**
	 * 返回栈大小
	 * @return
	 */
	public static int size() {
		return activityStack.size();
	}
	
	/**
	 * 返回栈顶项
	 * @return
	 */
	public static Activity getFront() {
		if (activityStack.size() > 0) {
			return activityStack.get(0).get();
		} 
		return null;
	}
	
	/**
	 * 当前栈顶项出栈
	 */
	public static void popCurrent() {
		WeakReference<Activity> activity = topActivity();
		popActivity(activity); 
	}
	
	
	/**
	 * 指定项以上的栈顶所有项出栈
	 * @param currentActivity 指定项
	 */
	public static void popFront(Class<? extends Activity> currentActivity) {
		while (true) {
			WeakReference<Activity> activity = topActivity();
			if (activity != null 
					&& !activity.getClass().equals(currentActivity)) {
				popActivity(activity);   
			} else {
				break;
			}
		}
	}
	
	/**
	 * 除栈底项外的所有项出栈
	 */
	public static void popAllFront() {
		while (true) {
			if (activityStack.size() > 1) {
				popCurrent();
			} else {
				break;
			}
		}
	}
	
	/**
	 * 指定项以下的栈底所有项出栈
	 * @param currentActivity 指定项
	 */
	public static void popEnd(Activity currentActivity) {
		while (true) {
			WeakReference<Activity> activity = bottomActivity();   
            if(activity == null 
               || activity.get() == currentActivity) {   
                break;   
            }   
            popActivity(activity);   
		}
	}
	
	/**
	 * 所有项出栈
	 */
	public static void popAll() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (activityStack.get(i) != null) {
            	Activity activity = activityStack.get(i).get();
                if (activity != null) {
                    activity.finish();
                }
            }
        }
        activityStack.clear();
	}
	
	/**
	 * 新项入栈
	 * @param activity
	 */
	public static void pushActivity(Activity activity) {   
        activityStack.add(new WeakReference<Activity>(activity));   
    }   
	
	/**
	 * 指定项出栈
	 * @param activity
	 */
	public static void popActivity(WeakReference<Activity> activity) {   
        if(activity != null) {  
        	if (activity.get() != null) {
        		activity.get().finish();   
        	}
            activityStack.remove(activity);   
            activity = null;   
        }   
    }   
	
	/**
	 * 返回栈顶项
	 * @return
	 */
	public static WeakReference<Activity> topActivity() {   
    	if (activityStack.size() > 0) {
    		return activityStack.lastElement();    
    	}
    	return null;
    }   
	
	/**
	 * 返回栈底项
	 * @return
	 */
	public static WeakReference<Activity> bottomActivity() {
		if (activityStack.size() > 0) {
    		return activityStack.firstElement();    
    	}
    	return null;
	}
}
