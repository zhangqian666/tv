package com.iptv.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.iptv.common.data.EnumType.Platform;
import com.iptv.common.local.SysCacheFactory;

public class AtvUtils {

	public static Context sContext;

	public static String getMetroItemBackground(int color) {
		if (color > 0 && color < 9) {
			return String.format("drawable/home_common_itemview_bg_%s", color);
		}
		Random random = new Random();
		int ranColor = random.nextInt(8);
		return String.format("drawable/home_common_itemview_bg_%s", ranColor + 1);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	private static PackageInfo initPackInfo() {
		PackageManager packageManager = sContext.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(sContext.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return packInfo;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName() {
		PackageInfo info = initPackInfo();
		return info != null ? info.versionName : null;
	}

	public static int getAppVersionCode() {
		PackageInfo info = initPackInfo();
		return info != null ? info.versionCode : -1;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public static final String generateUUID() {
		String s = ((TelephonyManager) sContext.getSystemService("phone")).getDeviceId();
		if (s == null)
			s = "";
		String s1 = android.provider.Settings.Secure.getString(sContext.getContentResolver(), "android_id");
		if (s1 == null)
			s1 = "";
		String s2;
		String s3;
		WifiInfo wifiinfo;
		String s4;
		if (android.os.Build.VERSION.SDK_INT >= 9) {
			s2 = Build.SERIAL;
			if (s2 == null)
				s2 = "";
		} else {
			s2 = getDeviceSerial();
		}
		s3 = "";
		wifiinfo = ((WifiManager) sContext.getSystemService("wifi")).getConnectionInfo();
		if (wifiinfo != null) {
			s3 = wifiinfo.getMacAddress();
			if (s3 == null)
				s3 = "";
		}
		try {
			s4 = s + s1 + s2 + s3;
			if (s4.length() == 0) {
				s4 = new SysCacheFactory(sContext).getRandomUUID();
			}
			s4 = getMD5String(s4);
		} catch (NoSuchAlgorithmException nosuchalgorithmexception) {
			nosuchalgorithmexception.printStackTrace();
			return null;
		}
		return s4;
	}

	private static final String getDeviceSerial() {
		String s;
		try {
			Method method = Class.forName("android.os.Build")
					.getDeclaredMethod("getString",
							new Class[] { Class.forName("java.lang.String") });
			if (!method.isAccessible())
				method.setAccessible(true);
			s = (String) method.invoke(new Build(), "ro.serialno");
		} catch (ClassNotFoundException classnotfoundexception) {
			classnotfoundexception.printStackTrace();
			return "";
		} catch (NoSuchMethodException nosuchmethodexception) {
			nosuchmethodexception.printStackTrace();
			return "";
		} catch (InvocationTargetException invocationtargetexception) {
			invocationtargetexception.printStackTrace();
			return "";
		} catch (IllegalAccessException illegalaccessexception) {
			illegalaccessexception.printStackTrace();
			return "";
		}
		return s;
	}

	private static final String getMD5String(String s)
			throws NoSuchAlgorithmException {
		byte abyte0[] = MessageDigest.getInstance("SHA-1").digest(s.getBytes());
		Formatter formatter = new Formatter();
		try {
			int i = abyte0.length;
			for (int j = 0; j < i; j++) {
				byte byte0 = abyte0[j];
				formatter.format("%02x", byte0);
			}
			return formatter.toString();
		} finally {
			formatter.close();
		}
	}

	public static final String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public static String GetItem(String param, String paramString) {
		String str = "";
		int i = param.indexOf(paramString);
		if (i == -1) {
			// Log.i("LiveChannel", paramString + " is not exist");
			return str;
		}
		int j = param.indexOf("\"", i);// from i
		int k = param.indexOf("\"", j + 1);// from j+1
		if ((k > j) && (j > i)) {
			str = param.substring(j + 1, k);
			// if(paramString.equals("UserChannelID"))
			// {
			// Log.i("IPTV", str);
			// }
			return str;
		}
		// Log.i("LiveChannel", "str = null");
		return str;
	}

	public static int GetItemInt(String param, String paramString) {
		String str = GetItem(param, paramString);
		int item;
		if (str.equals("")) {
			// Log.i("LiveChannel", "str " + paramString + " is empty");
			item = 0;
		} else {
			try {
				item = Integer.parseInt(str);
			} catch (Exception e) {
				item = 0;
			}
		}
		// if(paramString.equals("UserChannelID"))
		// Log.i("IPTV", str + "=" + item);
		return item;
	}
	
	public static Platform GetPlatform(String param, String paramString) {
		String str = GetItem(param, paramString);
		Platform item;
		if (str.equals("")) {
			// Log.i("LiveChannel", "str " + paramString + " is empty");
			item = Platform.UNKNOW;
		} else {
			try {
				item = Platform.createPlatform(str);
			} catch (Exception e) {
				item = Platform.UNKNOW;
			}
		}
		return item;
	}
	
}
