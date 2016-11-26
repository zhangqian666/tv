package com.iptv.rocky.auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import android.util.Log;

public class AuthUtils {
	private static final String ALGORITHM = "DESede";

	private AuthUtils() {
	}
	
	public static String CTCGetAuthInfo(String EncryToken, String userId,
			String pwd) {
		return GetAuthInfo(EncryToken, userId, pwd, "CTC");
	}
	
	public static String CUGetAuthInfo(String EncryToken, String userId,
			String pwd) {
		return GetAuthInfo(EncryToken, userId, pwd, "CU");
	}

	public static String GetAuthInfo(String EncryToken, String userId,
			String pwd, String prefix) {
		Log.d("AuthUtils", "");
		//Log.i("Auth", "ET: " + EncryToken);
		// Security.addProvider(new com.sun.crypto.provider.SunJCE());
		// System.out.println("CTCGetAuthInfo start:test");
		// authVar.put("Auth_EncryToken", EncryToken);
		// final byte[] keyBytes1
		// ={'a','d','t','e','s','t','1','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};
		final byte[] keyBytes1 = addZeroForNum(pwd, 24).getBytes();
		// System.out.println("keyBytes:"+keyBytes1.toString());
		// String szSrc =
		// "04799110$"+EncryToken+"$adtest1$00100399010801000001A26573C0E479$192.168.35.99$a2:65:73:c0:e4:79$$CTC\1";
		Random localRandom = new Random();
		int ranInt = localRandom.nextInt(99999999);
		String ranS = String.valueOf(ranInt);
		// System.out.println("length of randomIn:"+ranS.length());
		String szSrc0 = ranS + "$" + EncryToken + "$" + userId + "$"
				+ CTCAuthService.getSTBID() + "$" + getLocalIpAddress() + "$"
				+ getMacAddress() + "$" + "$" + prefix;
		//Log.i("Auth", "ET: " + szSrc0.length());
		StringBuffer szSrc1 = new StringBuffer(szSrc0);
		//Log.i("Auth", "len: " + (int) (szSrc0.length() % 8));
		for (int tmp = (int) (szSrc0.length() % 8); tmp >= 0; tmp--) {
			//String str = String.valueOf((int) (szSrc0.length() % 8));
			//Log.i("Auth", "ET: " + str);
			szSrc1.append(String.valueOf((int) (szSrc0.length() % 8)));
		}
		String szSrc = szSrc1.toString();
		byte[] encoded = encryptMode(keyBytes1, szSrc.getBytes());

		return byte2hex(encoded);
	}

	// keybyte为加密密钥，长度为24字节
	// src为被加密的数据缓冲区（源）
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, ALGORITHM);

			// 加密
			Cipher c1 = Cipher.getInstance(ALGORITHM);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// 转换成十六进制字符串
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				// sb.append("0").append(str);// 左补0
				sb.append(str).append("0");// 右补0
				str = sb.toString();
				strLen = str.length();
			}
		}

		return str;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
					en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				if (intf.getName().toLowerCase().equals("eth0")) {
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); 
							enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							String ipaddress = inetAddress.getHostAddress().toString();
							if (!ipaddress.contains("::")) {// ipV6的地址
								return ipaddress;
							}
						}
					}
				} else {
					continue;
				}
			}
		} catch (Exception ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	public static String loadFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	public static String getMacAddress() {
		try {
			return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
