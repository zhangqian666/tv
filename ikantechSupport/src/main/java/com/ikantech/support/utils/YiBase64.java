package com.ikantech.support.utils;

import it.sauronsoftware.base64.Base64;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class YiBase64
{
	private static String chars;

	private static char pad;

	private static Base64 base64;

	static
	{
		chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		pad = '=';
		base64 = new Base64(chars, pad);
	}

	public static void setChars(String chars)
	{
		setCharsAndPad(chars, pad);
	}

	public static void setPad(char pad)
	{
		setCharsAndPad(chars, pad);
	}

	public static void setCharsAndPad(String chars, char pad)
	{
		if (chars == null)
		{
			throw new NullPointerException("chars non-null");
		}
		YiBase64.chars = chars;
		YiBase64.pad = pad;
		base64 = new Base64(chars, pad);
	}

	public static String encode(String str) throws RuntimeException
	{
		return base64.encode(str);
	}

	public static String encode(String str, String charset)
			throws RuntimeException
	{
		return base64.encode(str, charset);
	}

	public static byte[] encode(byte[] bytes) throws RuntimeException
	{
		return base64.encode(bytes);
	}

	public static byte[] encode(byte[] bytes, int wrapAt)
			throws RuntimeException
	{
		return base64.encode(bytes, wrapAt);
	}

	public static void encode(InputStream inputStream, OutputStream outputStream)
			throws IOException
	{
		base64.encode(inputStream, outputStream);
	}

	public static void encode(InputStream inputStream,
			OutputStream outputStream, int wrapAt) throws IOException
	{
		base64.encode(inputStream, outputStream, wrapAt);
	}

	public static void encode(File source, File target, int wrapAt)
			throws IOException
	{
		base64.encode(source, target, wrapAt);
	}

	public static void encode(File source, File target) throws IOException
	{
		base64.encode(source, target);
	}

	public static String decode(String str) throws RuntimeException
	{
		return base64.decode(str);
	}

	public static String decode(String str, String charset)
			throws RuntimeException
	{
		return base64.decode(str, charset);
	}

	public static void decode(File source, File target) throws IOException
	{
		base64.decode(source, target);
	}

	public static void decode(InputStream inputStream, OutputStream outputStream)
			throws IOException
	{
		base64.decode(inputStream, outputStream);
	}

	public static byte[] decode(byte[] bytes) throws RuntimeException
	{
		return base64.decode(bytes);
	}
}
