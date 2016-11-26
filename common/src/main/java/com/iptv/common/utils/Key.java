package com.iptv.common.utils;

import java.util.Date;
import java.util.Random;

/**
 * @author Nid java 处理无符号数太令人郁闷了
 */
public class Key
{
    static final int ENCRYPT_ROUNDS = 32;

    static final int DELTA = 0x9E3779B9;

    static final int FINAL_SUM = 0xC6EF3720;

    static final int BLOCK_SIZE_TWICE = (4 << 1) << 1;

    public static String getKey(long serverTime)
    {
        byte[] bytes = new byte[16];
        byte[] key = new byte[16];
        byte[] result = new byte[33];
        String keystr = "qqqqqww";

        for (int i = 0; i < 16; i++)
        {
            key[i] = i < keystr.length() ? (byte) keystr.charAt(i) : (byte) 0;
        }
        long timet = serverTime / 1000;
        timet -= 100;
        // System.out.println("timet=" + timet);
        Time2Str((int) timet, bytes, 16);
        Random rand = new Random();
        for (int i = 0; i < 16; i++)
        {
            if (bytes[i] == 0)
            {
                bytes[i] = (byte) rand.nextInt(256);
            }
        }

        TEncrypt(bytes, 16, key, 16);
        Str2Hex(bytes, 16, result, 33);
        String result_str = new String(result, 0, 32);
        return result_str;
    }
    
    
    public static String getKey()
    {
        byte[] bytes = new byte[16];
        byte[] key = new byte[16];
        byte[] result = new byte[33];
        String keystr = "qqqqqww";

        for (int i = 0; i < 16; i++)
        {
            key[i] = i < keystr.length() ? (byte) keystr.charAt(i) : (byte) 0;
        }
        long timet = (new Date()).getTime() / 1000;
        timet -= 100;
        // System.out.println("timet=" + timet);
        Time2Str((int) timet, bytes, 16);
        Random rand = new Random();
        for (int i = 0; i < 16; i++)
        {
            if (bytes[i] == 0)
            {
                bytes[i] = (byte) rand.nextInt(256);
            }
        }

        TEncrypt(bytes, 16, key, 16);
        Str2Hex(bytes, 16, result, 33);
        String result_str = new String(result, 0, 32);
        return result_str;
    }

    static int GetkeyFromstr(byte[] str, int len)
    {
        int key = 0;
        for (int i = 0; i < len; i++)
        {
            key ^= (int) (str[i]) << (i % 4 * 8);
        }
        return key;
    }

    static int Str2Hex(byte[] buffer, int buf_size, byte[] hexstr, int hs_size)
    {
        if (hs_size < 2 * buf_size + 1)
            return 0;

        for (int i = 0; i < buf_size; i++)
        {
            hexstr[2 * i] = (byte) (buffer[i] & 0xF);
            hexstr[2 * i + 1] = (byte) ((buffer[i] >> 4) & 0xF);
        }
        for (int i = 0; i < 2 * buf_size; i++)
        {
            hexstr[i] += (byte) (hexstr[i] > 9 ? 'a' - (char) 10 : '0');
        }
        hexstr[2 * buf_size] = (byte) 0;
        return 1;
    }

    static int Hex2Str(byte[] result, int hs_size, byte[] buffer, int buf_size)
    {
        if (2 * buf_size < hs_size)
            return 0;

        for (int i = 0; i < hs_size / 2; i++)
        {
            buffer[i] = (byte) ((result[2 * i] - (result[2 * i] > '9' ? 'a' - (char) 10 : '0')) | ((result[2 * i + 1] - (result[2 * i + 1] > '9' ? 'a' - (char) 10
                    : '0')) << 4));
        }
        return 1;
    }

    static void TEncrypt(byte[] buffer, int buf_size, byte[] key, int len)
    {
        long k0 = GetkeyFromstr(key, len);

        long k1 = 0;
        long k2 = 0;
        long k3 = 0;
        k1 = k0 << 8 | k0 >> 24;
        k2 = k0 << 16 | k0 >> 16;
        k3 = k0 << 24 | k0 >> 8;
        for (int i = 0; i + BLOCK_SIZE_TWICE <= buf_size; i += BLOCK_SIZE_TWICE)
        {
            long v0 = 0, v1 = 0, sum = 0;
            for (int k = 0; k < 4; k++)
            {
                v0 |= (long) (buffer[i + k] & 0xff) << (k * 8);
                v1 |= (long) (buffer[i + k + 4] & 0xff) << (k * 8);
            }

            for (int j = 0; j < ENCRYPT_ROUNDS; j++)
            {
                sum += DELTA;
                sum = getuint(sum);

                v0 += getuint(getuint(getuint(v1 << 4) + k0) ^ getuint(v1 + sum))
                        ^ getuint(getuint(v1 >> 5) + k1);
                v0 = getuint(v0);
                v1 += getuint(getuint(getuint(v0 << 4) + k2) ^ getuint(v0 + sum))
                        ^ getuint(getuint(v0 >> 5) + k3);
                v1 = getuint(v1);
            }

            for (int k = 0; k < 4; k++)
            {
                buffer[i + k] = (byte) ((v0 >> (k * 8)) & 0xFF);
                buffer[i + k + 4] = (byte) ((v1 >> (k * 8)) & 0xFF);
            }
        }
    }

    static long getuint(long l)
    {
        return l & 0xffffffffL;
    }

    static int Str2Time(byte[] str, int len)
    {
        int key = 0;

        for (int i = 0; i < len && i < 8; i++)
        {
            key |= (int) (str[i] - (str[i] > '9' ? 'a' - (char) 10 : '0')) << (28 - i % 8 * 4);
        }
        return key;
    }

    static void Time2Str(int timet, byte[] str, int len)
    {
        for (int i = 0; i < len && i < 8; i++)
        {
            str[i] = (byte) ((timet >> (28 - i % 8 * 4)) & 0xF);
            str[i] += (byte) (str[i] > 9 ? 'a' - (char) 10 : '0');
        }
    }

}
