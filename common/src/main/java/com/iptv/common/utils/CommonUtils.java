package com.iptv.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public final class CommonUtils {
	
	public static boolean tryParseInt(String s) {
		try {
			Pattern pattern = Pattern.compile("[0-9]*");
			return pattern.matcher(s).matches();
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static int parseInt(String s) {
		return parseInt(s, 0);
	}
	
	public static int parseInt(String s, int defaultInt) {
		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {
			return defaultInt;
		}
	}
	
	/**
     * 解析以字符串表示的长整数类型
     * 
     * @param s
     * @return
     */
    public static long parseLong(String s)
    {
        return parseLong(s, 0L);
    }

    /**
     * 解析以字符串表示的长整数类型，如果发生异常则返回默认值
     * 
     * @param s
     * @param defaultLong
     * @return
     */
    public static long parseLong(String s, long defaultLong)
    {
        try
        {
            return Long.parseLong(s);
        }
        catch (Exception ex)
        {
            // LogUtils.error(ex.getStackTrace().toString());
        }
        return defaultLong;
    }
	
    /**
     * 解析以字符串表示的单精度浮点类型
     * 
     * @param s
     * @return
     */
    public static float parseFloat(String s)
    {
        return parseFloat(s, 0.0F);
    }

    /**
     * 解析以字符串表示的单精度浮点类型，如果发生异常则返回默认值
     * 
     * @param s
     * @param defaultFloat
     * @return
     */
    public static float parseFloat(String s, float defaultFloat)
    {
        try
        {
            return Float.parseFloat(s);
        }
        catch (Exception ex)
        {
            // LogUtils.error(ex.getStackTrace().toString());
        }
        return defaultFloat;
    }
    
    /**
     * 解析以字符串表示的双精度浮点类型
     * 
     * @param s
     * @return
     */
    public static double parseDouble(String s)
    {
        return parseDouble(s, 0.0);
    }

    /**
     * 解析以字符串表示的双精度浮点类型，如果发生异常则返回默认值
     * 
     * @param s
     * @param defaultDouble
     * @return
     */
    public static double parseDouble(String s, double defaultDouble)
    {
        try
        {
            return Double.parseDouble(s);
        }
        catch (Exception ex)
        {
            // LogUtils.error(ex.getStackTrace().toString());
        }
        return defaultDouble;
    }

    /**
     * 解析以字符串表示的布尔类型
     * 
     * @param s
     * @return
     */
    public static boolean parseBoolean(String s)
    {
        return parseBoolean(s, false);
    }

    /**
     * 解析以字符串表示的布尔类型，如果发生异常则返回默认值
     * 
     * @param s
     * @param defaultBoolean
     * @return
     */
    public static boolean parseBoolean(String s, boolean defaultBoolean)
    {
        try
        {
            return Boolean.parseBoolean(s);
        }
        catch (Exception ex)
        {
            // LogUtils.error(ex.getStackTrace().toString());
        }
        return defaultBoolean;
    }

    /**
     * 指定日期格式，解析字符串日期为Date对象
     * 
     * @param time
     * @return
     */
    public static Date parseDate(String time, String dateFormat)
    {
        DateFormat inputDf = null;

        try
        {
            inputDf = new SimpleDateFormat(dateFormat);
        }
        catch (Exception e)
        { // 日期格式不合法
            LogUtils.warn(e.toString());
            return null;
        }

        Date result = null;
        try
        {
            result = inputDf.parse(time);
        }
        catch (Exception e)
        {
            System.out.println("This method should work for all date/" + "time strings you find in our data.");
            return null;
        }
        return result;
    }
}
