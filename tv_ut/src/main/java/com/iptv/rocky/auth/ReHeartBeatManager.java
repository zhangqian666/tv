package com.iptv.rocky.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.iptv.common.utils.LogUtils;

public class ReHeartBeatManager {
	private static final int MAX_HEART_BEAT_TIME = 3;

	private AuthService mAuthService;

	private int mHeartBeatFailedTime;

	public ReHeartBeatManager(AuthService authService) {
		mAuthService = authService;
		mHeartBeatFailedTime = 1;
	}

	public synchronized void start() {
		mHeartBeatFailedTime++;
		if (mHeartBeatFailedTime > MAX_HEART_BEAT_TIME) {
			mHeartBeatFailedTime = 1;
			checkExchange();
		} else {
			mAuthService.startHeartBeat(false);
		}
	}

	public synchronized void reset() {
		mHeartBeatFailedTime = 1;
	}
	
	
	/**
	 * 通过 Ping 检查服务器是否可用
	 */
	private boolean checkExchange(){
		 // 增加先检查服务器是否通，目的是确认楼道交换机端口已经起来了
		String pingNum ="1";
		String m_strForNetAddress="10.144.0.2";
		boolean result =false;
		Process p;
		try {
			p = Runtime.getRuntime().exec("/system/bin/ping -c "+ pingNum + " " + m_strForNetAddress);
			int status = p.waitFor(); 
	        if (status == 0) {  
	            result=true; 
	        }    
	        else 
	        { 
	            result=false; 
	        }
	        String lost = new String();  
	        String delay = new String();  
	        BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
	         
	        String str = new String();  

	        //读出所有信息并显示          
	        String tv_PingInfo="";
	        while((str=buf.readLine())!=null){  
	            str = str + "\r\n";
	            LogUtils.debug("输出:"+str);
	            //tv_PingInfo.append(str);
	        }
			
		} catch (IOException e) {

			e.printStackTrace();
		} 
		catch (InterruptedException e) {

			e.printStackTrace();
		}
		return result;
	}
	
}
