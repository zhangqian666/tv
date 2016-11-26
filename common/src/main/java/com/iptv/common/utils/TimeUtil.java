package com.iptv.common.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class TimeUtil
{
    protected static final double ONE_THOUSAND = 1000.0;

    protected static final int SIXTY = 60;

    /**
     * 获取现在时间
     * 
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     * 
     * @return 返回短时间格式 yyyy-MM-dd
     */
    public static Date getNowDateShort()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     * 
     * @return 返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     * 
     * @return 返回短时间字符串格式 yyyy-MM-dd
     */
    public static String getStringDateShort()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static boolean longerThanOneDay(long time)
    {
        String specDateTime = TimeUtil.getSpecStringDateTime(time);
        String currentDate = TimeUtil.getStringDate();
        long days = TimeUtil.getDays(currentDate, specDateTime);

        if (days >= 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String getDateTimeToShow(String strTime)
    {
        String dateTimeToShow;
        String specDateTime = TimeUtil.getSpecStringDateTime(strTime);
        String currentDate = TimeUtil.getStringDate();
        long days = TimeUtil.getDays(currentDate, specDateTime);
        if (days == 0)
        {
            String[] array = specDateTime.split(" ");
            dateTimeToShow = array[array.length - 1];
        }
        else if (days == 1)
        {
            String[] array = specDateTime.split(" ");
            dateTimeToShow = "昨天 " + array[array.length - 1];
        }
        else
        {
            dateTimeToShow = specDateTime;
        }
        return dateTimeToShow;
    }

    /**
     * 根据某个数值转化成具体的时间
     * 
     * @param strTime
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getSpecStringDateTime(String strTime)
    {
        long times = CommonUtils.parseLong(strTime);
        Date d = new Date(times);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String specStrDateTime = sd.format(d);
        return specStrDateTime;
    }

    public static String getSpecStringDateTime(long time)
    {
        Date d = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String specStrDateTime = sd.format(d);
        return specStrDateTime;
    }

    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     * 
     * @return
     */
    public static String getTimeShort()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取时间 小时:分 HH:mm
     * 
     * @return
     */
    public static String getTimeShortHm()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     * 
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    // /**
    // * 将长时间格式字符串转换为时间HH:mm:ss
    // *
    // * @param strDate
    // * @return
    // */
    // public static Date strToDateLong(String strDate) {
    // SimpleDateFormat formatter = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // ParsePosition pos = new ParsePosition(0);
    // Date strtodate = formatter.parse(strDate, pos);
    // return strtodate;
    // }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     * 
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(java.util.Date dateDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     * 
     * @param dateDate
     * @param k
     * @return
     */
    public static String dateToStr(java.util.Date dateDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }
    
    public static String dateToStr(java.util.Date dateDate, String sformat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     * 
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 得到现在时间
     * 
     * @return
     */
    public static Date getNow()
    {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 提取一个月中的最后一天
     * 
     * @param day
     * @return
     */
    public static Date getLastDate(long day)
    {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    /**
     * 得到现在时间
     * 
     * @return 字符串 yyyyMMdd HHmmss
     */
    public static String getStringToday()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 得到现在小时
     */
    public static String getHour()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     * 
     * @return
     */
    public static String getTime()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     * 
     * @param sformat yyyyMMddhhmmss
     * @return
     */
    public static String getUserDate(String sformat)
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
     */
    public static String getTwoHour(String st1, String st2)
    {
        String[] kk = null;
        String[] jj = null;
        kk = st1.split(":");
        jj = st2.split(":");
        if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
            return "0";
        else
        {
            double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
            double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
            if ((y - u) > 0)
                return y - u + "";
            else
                return "0";
        }
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2)
    {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try
        {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        }
        catch (Exception e)
        {
            return "";
        }
        return day + "";
    }

    /**
     * 时间前推或后推分钟,其中JJ表示分钟.
     */
    public static String getPreTime(String sj1, String jj)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mydate1 = "";
        try
        {
            Date date1 = format.parse(sj1);
            long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
            date1.setTime(Time * 1000);
            mydate1 = format.format(date1);
        }
        catch (Exception e)
        {
        }
        return mydate1;
    }

    /**
     * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
     */
    public static String getNextDay(String nowdate, String delay)
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String mdate = "";
            Date d = strToDate(nowdate);
            long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        }
        catch (Exception e)
        {
            return "";
        }
    }
    
    /**
     * 得到一个时间延后或前移几天的时间,delay为前移或后延的天数
     */
    public static String getNextDay(int delay)
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String mdate = "";
            Date currentTime = new Date();
            long myTime = (currentTime.getTime() / 1000) + delay * 24 * 60 * 60;
            currentTime.setTime(myTime * 1000);
            mdate = format.format(currentTime);
            return mdate;
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /**
     * 判断是否润年
     * 
     * @param ddate
     * @return
     */
    public static boolean isLeapYear(String ddate)
    {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        Date d = strToDate(ddate);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);
        if ((year % 400) == 0)
            return true;
        else if ((year % 4) == 0)
        {
            if ((year % 100) == 0)
                return false;
            else
                return true;
        }
        else
            return false;
    }

    /**
     * 返回美国时间格式 26 Apr 2006
     * 
     * @param str
     * @return
     */
    public static String getEDate(String str)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(str, pos);
        String j = strtodate.toString();
        String[] k = j.split(" ");
        return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
    }

    /**
     * 获取一个月的最后一天
     * 
     * @param dat
     * @return
     */
    public static String getEndDateOfMonth(String dat)
    {// yyyy-MM-dd
        String str = dat.substring(0, 8);
        String month = dat.substring(5, 7);
        int mon = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12)
        {
            str += "31";
        }
        else if (mon == 4 || mon == 6 || mon == 9 || mon == 11)
        {
            str += "30";
        }
        else
        {
            if (isLeapYear(dat))
            {
                str += "29";
            }
            else
            {
                str += "28";
            }
        }
        return str;
    }

    /**
     * 判断二个时间是否在同一个周
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear)
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        else if (1 == subYear && 11 == cal2.get(Calendar.MONTH))
        {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH))
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     * 
     * @return
     */
    public static String getSeqWeek()
    {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1)
            week = "0" + week;
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    /**
     * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
     * 
     * @param sdate
     * @param num
     * @return
     */
    public static String getWeek(String sdate, String num)
    {
        // 再转换为时间
        Date dd = TimeUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(dd);
        if (num.equals("1")) // 返回星期一所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        else if (num.equals("2")) // 返回星期二所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        else if (num.equals("3")) // 返回星期三所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        else if (num.equals("4")) // 返回星期四所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        else if (num.equals("5")) // 返回星期五所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        else if (num.equals("6")) // 返回星期六所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        else if (num.equals("0")) // 返回星期日所在的日期
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     * 
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate)
    {
        // 再转换为时间
        Date date = TimeUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    public static String getWeekStr(String sdate)
    {
        String str = "";
        str = TimeUtil.getWeek(sdate);
        if ("1".equals(str))
        {
            str = "星期日";
        }
        else if ("2".equals(str))
        {
            str = "星期一";
        }
        else if ("3".equals(str))
        {
            str = "星期二";
        }
        else if ("4".equals(str))
        {
            str = "星期三";
        }
        else if ("5".equals(str))
        {
            str = "星期四";
        }
        else if ("6".equals(str))
        {
            str = "星期五";
        }
        else if ("7".equals(str))
        {
            str = "星期六";
        }
        return str;
    }

    /**
     * 将形如 HH:MM:SS的字符串表达形式转化成具体秒数 
     * 
     * @param time
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getSeconds(String time)
    {
        int timeInt = 0;
        if (time == null || time.equals(""))
        {
            return 0;
        }

        String[] timeSplits = time.split(":");
        int size = timeSplits.length;

        for (int i = size - 1; i >= 0; i--)
        {
            int timeSplit = CommonUtils.parseInt(timeSplits[i]);
            int multiple = size - 1 - i;
            timeInt += timeSplit * Math.pow(60, multiple);
        }
        return timeInt;
    }

    /**
     * 两个时间之间的天数
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2)
    {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        java.util.Date mydate = null;
        try
        {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        }
        catch (Exception e)
        {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    /**
     * 形成如下的日历 ， 根据传入的一个时间返回一个结构 星期日 星期一 星期二 星期三 星期四 星期五 星期六 下面是当月的各个时间
     * 此函数返回该日历第一行星期日所在的日期
     * 
     * @param sdate
     * @return
     */
    public static String getNowMonth(String sdate)
    {
        // 取该时间所在月的一号
        sdate = sdate.substring(0, 8) + "01";

        // 得到这个月的1号是星期几
        Date date = TimeUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int u = c.get(Calendar.DAY_OF_WEEK);
        String newday = TimeUtil.getNextDay(sdate, (1 - u) + "");
        return newday;
    }

    /**
     * 取得数据库主键 生成格式为yyyymmddhhmmss+k位随机数
     * 
     * @param k 表示是取几位随机数，可以自己定
     */

    public static String getNo(int k)
    {
        return getUserDate("yyyyMMddhhmmss") + getRandom(k);
    }

    /**
     * 返回一个随机数
     * 
     * @param i
     * @return
     */
    public static String getRandom(int i)
    {
        Random jjj = new Random();
        // int suiJiShu = jjj.nextInt(9);
        if (i == 0)
            return "";
        String jj = "";
        for (int k = 0; k < i; k++)
        {
            jj = jj + jjj.nextInt(9);
        }
        return jj;
    }

    /**
     * @param args
     */
    public static boolean RightDate(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
        if (date == null)
            return false;
        if (date.length() > 10)
        {
            sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
        else
        {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        try
        {
            sdf.parse(date);
        }
        catch (ParseException pe)
        {
            return false;
        }
        return true;
    }

    /**
     * 获得时间字符串HMS
     * 
     * @param timeMs 传入时间
     * @return 时间对应的字符串
     * @see [类、类#方法、类#成员]
     */
    public static String stringForHMS(double timeMs)
    {
        int totalSeconds = (int)(timeMs / ONE_THOUSAND);

        int seconds = totalSeconds % SIXTY;
        int minutes = (totalSeconds / SIXTY) % SIXTY;
        int hours = totalSeconds / (SIXTY * SIXTY);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * 获得时间MS字符串
     * 
     * @param timeMs 传入时间
     * @return 时间对应的字符串
     * @see [类、类#方法、类#成员]
     */
    public static String stringForMS(double timeMs, boolean withsign)
    {
        String sign = "";
        if (withsign)
        {
            if (timeMs < 0)
            {
                timeMs = -timeMs;
                sign = "-";
            }
            else
            {
                sign = "+";
            }
        }

        int totalSeconds = (int)(timeMs / ONE_THOUSAND);

        int seconds = totalSeconds % SIXTY;
        int minutes = totalSeconds / SIXTY;
        
        return String.format("%s%02d:%02d", sign, minutes, seconds);
    }
    
    // /***************************************************************************
    // * 
    // nd=1表示返回的值中包含年度 
    // yf=1表示返回的值中包含月份 
    // rq=1表示返回的值中包含日期 
    // format表示返回的格式
    // 1 以年月日中文返回  
    // 2 以横线-返回 
    // 3 以斜线/返回 
    // 4 以缩写不带其它符号形式返回 
    // 5 以点号.返回
    // *
    // **************************************************************************/
    // public static String getStringDateMonth(String sdate, String nd, String
    // yf, String rq, String format) {
    // Date currentTime = new Date();
    // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    // String dateString = formatter.format(currentTime);
    // String s_nd = dateString.substring(0, 4); // 年份
    // String s_yf = dateString.substring(5, 7); // 月份
    // String s_rq = dateString.substring(8, 10); // 日期
    // String sreturn = "";
    // roc.util.MyChar mc = new roc.util.MyChar();
    // if (sdate == null || sdate.equals("") || !mc.Isdate(sdate)) { // 处理空值情况
    // if (nd.equals("1")) {
    // sreturn = s_nd;
    // // 处理间隔符
    // if (format.equals("1"))
    // sreturn = sreturn + "年";
    // else if (format.equals("2"))
    // sreturn = sreturn + "-";
    // else if (format.equals("3"))
    // sreturn = sreturn + "/";
    // else if (format.equals("5"))
    // sreturn = sreturn + ".";
    // }
    // // 处理月份
    // if (yf.equals("1")) {
    // sreturn = sreturn + s_yf;
    // if (format.equals("1"))
    // sreturn = sreturn + "月";
    // else if (format.equals("2"))
    // sreturn = sreturn + "-";
    // else if (format.equals("3"))
    // sreturn = sreturn + "/";
    // else if (format.equals("5"))
    // sreturn = sreturn + ".";
    // }
    // // 处理日期
    // if (rq.equals("1")) {
    // sreturn = sreturn + s_rq;
    // if (format.equals("1"))
    // sreturn = sreturn + "日";
    // }
    // } else {
    // // 不是空值，也是一个合法的日期值，则先将其转换为标准的时间格式
    // sdate = roc.util.RocDate.getOKDate(sdate);
    // s_nd = sdate.substring(0, 4); // 年份
    // s_yf = sdate.substring(5, 7); // 月份
    // s_rq = sdate.substring(8, 10); // 日期
    // if (nd.equals("1")) {
    // sreturn = s_nd;
    // // 处理间隔符
    // if (format.equals("1"))
    // sreturn = sreturn + "年";
    // else if (format.equals("2"))
    // sreturn = sreturn + "-";
    // else if (format.equals("3"))
    // sreturn = sreturn + "/";
    // else if (format.equals("5"))
    // sreturn = sreturn + ".";
    // }
    // // 处理月份
    // if (yf.equals("1")) {
    // sreturn = sreturn + s_yf;
    // if (format.equals("1"))
    // sreturn = sreturn + "月";
    // else if (format.equals("2"))
    // sreturn = sreturn + "-";
    // else if (format.equals("3"))
    // sreturn = sreturn + "/";
    // else if (format.equals("5"))
    // sreturn = sreturn + ".";
    // }
    // // 处理日期
    // if (rq.equals("1")) {
    // sreturn = sreturn + s_rq;
    // if (format.equals("1"))
    // sreturn = sreturn + "日";
    // }
    // }
    // return sreturn;
    // }

    // public static String getNextMonthDay(String sdate, int m) {
    // sdate = getOKDate(sdate);
    // int year = Integer.parseInt(sdate.substring(0, 4));
    // int month = Integer.parseInt(sdate.substring(5, 7));
    // month = month + m;
    // if (month < 0) {
    // month = month + 12;
    // year = year - 1;
    // } else if (month > 12) {
    // month = month - 12;
    // year = year + 1;
    // }
    // String smonth = "";
    // if (month < 10)
    // smonth = "0" + month;
    // else
    // smonth = "" + month;
    // return year + "-" + smonth + "-10";
    // }

    // public static String getOKDate(String sdate) {
    // if (sdate == null || sdate.equals(""))
    // return getStringDateShort();
    //
    // if (!VeStr.Isdate(sdate)) {
    // sdate = getStringDateShort();
    // }
    // // 将“/”转换为“-”
    // sdate = VeStr.Replace(sdate, "/", "-");
    // // 如果只有8位长度，则要进行转换
    // if (sdate.length() == 8)
    // sdate = sdate.substring(0, 4) + "-" + sdate.substring(4, 6) + "-" +
    // sdate.substring(6, 8);
    // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    // ParsePosition pos = new ParsePosition(0);
    // Date strtodate = formatter.parse(sdate, pos);
    // String dateString = formatter.format(strtodate);
    // return dateString;
    // }
    //
    // public static void main(String[] args) throws Exception {
    // try {
    // //System.out.print(Integer.valueOf(getTwoDay("2006-11-03 12:22:10",
    // "2006-11-02 11:22:09")));
    // } catch (Exception e) {
    // throw new Exception();
    // }
    // //System.out.println("sss");
    // }
}
