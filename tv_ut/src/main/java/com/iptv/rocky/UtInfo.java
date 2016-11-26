package com.iptv.rocky;

import java.io.FileInputStream;
import java.io.IOException;

import android.os.SystemProperties;

public class UtInfo {

	public String getStbMacAdd(){
		
		String stbMacAdd= null;
		String path = "sys/class/net/eth0/address";
		FileInputStream fis_name;
		try {
			fis_name = new FileInputStream(path);
			byte[] buffer_name = new byte[1024 * 8];
			int byteCount_name = fis_name.read(buffer_name);
			if ( byteCount_name > 0 )
			{
				stbMacAdd = new String(buffer_name, 0, byteCount_name, "utf-8");
			}
			
		} catch ( IOException e) {
			
			e.printStackTrace();
		}
		return stbMacAdd;
	}
	
	public String getStbId(){
		String stbid = SystemProperties.get("ro.serialno");
		return stbid;
	}
	
}
