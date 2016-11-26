package com.iptv.common.utils;

import java.io.UnsupportedEncodingException;

public class PPTVBase64Encoding
{
    public static String decode(String url, String key) throws UnsupportedEncodingException
    {
        String urlTemp = url;
        byte[] bin = Base64.decode(urlTemp.getBytes("utf-8"));

        byte[] bout = new byte[bin.length];
        byte[] bkey = key.getBytes();
        for (int i = 0; i < bin.length; i++)
        {
            bout[i] = (byte) (bin[i] - bkey[(i % bkey.length)]);
        }
        return new String(bout, 0, bout.length, "utf-8");
    }

    public static String encode(String url, String key) throws UnsupportedEncodingException
    {
        byte[] bin = url.getBytes("utf-8");
        byte[] bout = new byte[bin.length];
        byte[] bkey = key.getBytes();
        for (int i = 0; i < bin.length; i++)
        {
            bout[i] = (byte) (bin[i] + bkey[(i % bkey.length)]);
        }

        String stringPlayLink = new String(Base64.encode(bout));
        return stringPlayLink;
    }

}
