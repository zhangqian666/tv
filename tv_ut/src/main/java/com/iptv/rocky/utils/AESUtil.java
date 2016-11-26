package com.iptv.rocky.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

	static final String algorithmStr = "AES/ECB/PKCS5Padding";

	private static final String keyBytes = "abcdefgabcdefg12";

	private static String code = "HBVIRGIN";

	private static byte[] encrypt(String content, String password) throws Exception {
		byte[] keyStr = getKey(password);
		SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
		Cipher cipher = Cipher.getInstance(algorithmStr);// algorithmStr
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);// ʼ
		byte[] result = cipher.doFinal(byteContent);
		return result; //
	}

	private static byte[] decrypt(byte[] content, String password) throws Exception {
		byte[] keyStr = getKey(password);
		SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
		Cipher cipher = Cipher.getInstance(algorithmStr);// algorithmStr
		cipher.init(Cipher.DECRYPT_MODE, key);// ʼ
		byte[] result = cipher.doFinal(content);
		return result; //
	}

	private static byte[] getKey(String password) {
		byte[] rByte = null;
		if (password != null) {
			rByte = password.getBytes();
		} else {
			rByte = new byte[24];
		}
		return rByte;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 加密
	 * 
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String encrypt() throws Exception {
		// 加密之后的字节数组,转成16进制的字符串形式输出
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		return parseByte2HexStr(encrypt((code + ":" + calendar.getTime().getTime()), keyBytes));
	}

	/**
	 * 解密
	 * 
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static boolean decrypt(String content) throws Exception {
		// 解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
		byte[] b = decrypt(parseHexStr2Byte(content), keyBytes);
		Object[] codes = new String(b).split(":");
		if (codes[0].toString().equals(code)) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			String pStr = encrypt();
			System.out.println("加密后:" + pStr);
			boolean postStr = decrypt(pStr);
			System.out.println("解密后：" + postStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
