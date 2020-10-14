package com.palmgo.com.cn.sharingdata.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class DateUitls {


	private static Logger log = Logger.getLogger(DateUitls.class);

	/** 1分钟包含的秒数 */
	public static final int ONE_MINUTE_SECONDS = 60;
	
	/** 1小时包含的分钟数 */
	public static final int ONE_HOUR_MINUTES = 60;

	/** 1天包含的小时数 */
	public static final int ONE_DAY_HOURS = 24;
	
	/** 1天包含的秒数 */
	public static final int ONE_DAY_SECONDS = ONE_DAY_HOURS*ONE_HOUR_MINUTES*ONE_MINUTE_SECONDS;
	
	/** 1周包含的天数 */
	public static final int ONE_WEEK_DAYS = 7; 
	
	/** 日志记录器 */
	/** 日期格式化器 */
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	/** 小时描述格式 */
	static Map<String, String> hourdesc = new HashMap<String, String>() {
		/** TODO */
		private static final long serialVersionUID = 1L;

		{
			put("00", "00:00--01:00");
			put("01", "01:00--02:00");

			put("02", "02:00--03:00");
			put("03", "03:00--04:00");

			put("04", "04:00--05:00");
			put("05", "05:00--06:00");

			put("06", "06:00--07:00");
			put("07", "07:00--08:00");

			put("08", "08:00--09:00");
			put("09", "09:00--10:00");

			put("10", "10:00--11:00");
			put("11", "11:00--12:00");

			put("12", "12:00--13:00");
			put("13", "13:00--14:00");

			put("14", "14:00--15:00");
			put("15", "15:00--16:00");

			put("16", "16:00--17:00");
			put("17", "17:00--18:00");

			put("18", "18:00--19:00");
			put("19", "19:00--20:00");

			put("20", "20:00--21:00");
			put("21", "21:00--22:00");

			put("22", "22:00--23:00");
			put("23", "23:00--00:00");
		}
	};

	/**
	 * 判断是否为闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean runNian(int year)// 判断是否为闰年的方法
	{
		boolean t = false;
		if (year % 4 == 0) {
			if (year % 100 != 0) {
				t = true;
			} else if (year % 400 == 0) {
				t = true;
			}
		}
		return t;
	}

	/**
	 * 获取指定timeid的起始时间小时与分钟HHMM
	 * 
	 * @param timeId
	 *            原始id
	 * @param interval
	 *            间隔分钟
	 * @return 小时分钟
	 */
	public static String getHHMM(int timeId, double interval) {
		DecimalFormat deciformat = new DecimalFormat("00");
		String strPublishTime = deciformat
				.format((int) ((timeId) * interval / ONE_HOUR_MINUTES))
				+ ":"
				+ deciformat.format((timeId) * interval % ONE_HOUR_MINUTES);
		return strPublishTime;
	}

	/**
	 * 获得小时范围标记
	 * 
	 * @param key	两位小时数
	 * @return
	 */
	public static String getHourDesc(String key) {
		String hourDesc = hourdesc.get(key);
		return hourDesc == null ? key : hourDesc;
	}

	/**
	 * 获取时间的串描述
	 * 
	 * @param time
	 *            时间值，是同一时间时的毫秒表示
	 * @param pattern
	 *            时间输出模式，如果为空那么默认输出为满足的yyyy-MM-dd HH:mm:ss.SSS<br>
	 *            <blockquote>
	 *            <table border=0 cellspacing=3 cellpadding=0 * summary="Examples of date and time patterns interpreted in the U.S. * locale">
	 *            <tr bgcolor="#ccccff">
	 *            <th align=left>模式串
	 *            <th align=left>输出结果
	 *            <tr>
	 *            <td><code>"yyyy.MM.dd G 'at' HH:mm:ss z"</code>
	 *            <td><code>2001.07.04 AD at 12:08:56 PDT</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>"EEE, MMM d, ''yy"</code>
	 *            <td><code>Wed, Jul 4, '01</code>
	 *            <tr>
	 *            <td><code>"h:mm a"</code>
	 *            <td><code>12:08 PM</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>"hh 'o''clock' a, zzzz"</code>
	 *            <td><code>12 o'clock PM, Pacific Daylight Time</code>
	 *            <tr>
	 *            <td><code>"K:mm a, z"</code>
	 *            <td><code>0:08 PM, PDT</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>"yyyyy.MMMMM.dd GGG hh:mm aaa"</code>
	 *            <td><code>02001.July.04 AD 12:08 PM</code>
	 *            <tr>
	 *            <td><code>"EEE, d MMM yyyy HH:mm:ss Z"</code>
	 *            <td><code>Wed, 4 Jul 2001 12:08:56 -0700</code>
	 *            <tr bgcolor="#eeeeff">
	 *            <td><code>"yyMMddHHmmssZ"</code>
	 *            <td><code>010704120856-0700</code>
	 *            <tr>
	 *            <td><code>"yyyy-MM-dd HH:mm:ss.SSS"</code>
	 *            <td><code>2001-07-04 12:08:56.235</code>
	 *            </table>
	 *            </blockquote>
	 * 
	 * @return 返回的时间串描述，决不为空
	 */
	public static String simpleDateFormat(long time, String pattern) {
		if (time < 0) {
			time = 0;
		}
		if (pattern == null) {
			pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		return new SimpleDateFormat(pattern).format(new Date(time));
	}

	/**
	 * 将日期按照样式格式化
	 * 
	 * @param time
	 * @param pattern eg:yyyy-MM-dd HH:mm:ss.SSS
	 * @return
	 */
	public static String simpleDateFormat(Date time, String pattern) {
		if (time == null) {
			return null;
		}
		return simpleDateFormat(time.getTime(),pattern);
	}

	/**
	 * 将日期转换为制定格式的字符串
	 * 
	 * @param date
	 *            日期Calendar对象
	 * @param pattern
	 *            格式样式是字符串format的形式，例如yyyy-MM-dd HH:mm:ss.SSS
	 * @return
	 */
	public static String simpleDateFormat(Calendar date, String pattern) {
		return simpleDateFormat(date.getTime(),pattern);

	}

	/**
	 * 根据日期串和格式串返回日期对象 ,yyyyMMdd HH:mm:ss
	 * 
	 * @param strDate
	 *            日期字符串
	 * @param pattern
	 *            日期样式  eg:yyyy-MM-dd HH:mm:ss.SSS
	 * @return
	 */
	public static Date getDateByPattern(String strDate, String pattern) {
		Date d = new Date();
		try {
			d = new SimpleDateFormat(pattern).parse(strDate);
		} catch (ParseException e) {
			log.error(String.format("解析日期格式错误,str=%s,pattern=%s ,将返回当前时间",
					strDate, pattern), e);
		}
		return d;
	}

	/**
	 * 日期类型转换为utc值 秒
	 * 
	 * @param date
	 * @return
	 */
	public static long date2utc(Date date) {
		return date.getTime() / 1000;
	}

	/**
	 * 将utc秒转换为日期对象
	 * 
	 * @param utc
	 * @return utc所表示的日期
	 */
	public static Date utc2date(long utc) {
		return new Date(utc * 1000);
	}

	/**
	 * 获取日期中某个段的值
	 * 
	 * @param d
	 * @param field
	 * @return
	 */
	public static int getDateFieldByCalendar(Date d, int field) {
		int ret = -1;
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			ret = c.get(field);
		} catch (Exception e) {
			log.error(String.format("解析日期格式错误,str=%s,pattern=%s ,将返回当前时间", d,
					field), e);
		}
		return ret;
	}

	/**
	 * 根据日期值获取指定时间的timeid 默认时间间隔的分钟
	 * 
	 * @param date
	 *            指定的日期
	 * @return 日期时间所代表的timeid
	 */
	public static int getTimeIDByDate(Date date, double interval) {
		return getTimeIDByMillis(date.getTime(), interval);
	}

	/**
	 * 根据日期毫秒值获取指定时间的timeid，最后一个整5分钟?
	 * 
	 * @param millis
	 *            日期毫秒
	 * @return timeid 0-288
	 */
	public static int getTimeIDByMillis(long millis, double interval) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		int minutes = cal.get(Calendar.MINUTE);
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int second = cal.get(Calendar.SECOND);
		int timeid = (int) ((hours * ONE_HOUR_MINUTES * ONE_HOUR_MINUTES
				+ minutes * ONE_HOUR_MINUTES + second) / (interval * ONE_HOUR_MINUTES));

		return timeid;
	}

	/**
	 * 切断日期的小时部分
	 * 
	 * @param second
	 *            代表日期的秒
	 * @return 去掉小时的秒数
	 */
	public static long truncatTimeFromDate(long second) {
		long day = (second + TimeZone.getDefault().getRawOffset() / 1000) / 86400;
		return (day * 86400 - TimeZone.getDefault().getRawOffset() / 1000);
	}

	/**
	 * 切断日期的小时部分
	 * 
	 * @param date
	 *            原始日期
	 * @return 去掉小时的日期
	 */
	public static Date truncatTimeFromDate(Date date) {
		long second = truncatTimeFromDate(date.getTime() / 1000);
		date.setTime(second * 1000);
		return date;
	}

	public static String getNowDateTime(){
		Date time = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(time);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*Calendar call = Calendar.getInstance();
		// call.setTimeInMillis(1268720708 * 1000);
		Date date = call.getTime();

		System.out.println("系统原始日期是"
				+ simpleDateFormat(call, "%1$tY-%1$tm-%1$te"));
		long utc = date2utc(date);
		System.out.println("date2utc时间秒数是" + utc);
		System.out.println("utc2date值是" + utc2date(utc));
		System.out.println("getDateByPattern值是"
				+ getDateByPattern("20111031 12:32:34", "yyyyMMdd kk:mm:ss"));

		System.out.println("simpleDateFormat结果是"
				+ simpleDateFormat(utc * 1000, "yyyyMMdd"));
		System.out.println("simpleDateFormat2结果是"
				+ simpleDateFormat(date, "yyyyMMdd"));

		System.out.println("getDateFieldByCalendar结果是"
				+ getDateFieldByCalendar(date, Calendar.DAY_OF_WEEK));
		System.out.println("truncatTimeFromDate-utc结果是"
				+ truncatTimeFromDate(utc));
		System.out.println("truncatTimeFromDate-date结果是"
				+ truncatTimeFromDate(date));*/
		
		String s = "1523168453";
    	String format = "HH:mm:ss";
    	System.out.println(stampToDate(s, format,1000));;
	}

	/* 
     * 将时间戳转换为时间
     * yyyy-MM-dd HH:mm:ss
     */
    public static String stampToDate(String s , String format, int num){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(s);
        Date date = new Date(lt*num);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    /***
	 * 将时间转换为时间戳
	 * @param s
	 * @param format yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
    public static String dateToStamp(String s , String format) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime() / 1000L;
        res = String.valueOf(ts);
        return res;
    }
    
    /** 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String seconds, String format){
    	 if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
    		             return "";  
         }  
          if(format == null || format.isEmpty()){
             format = "yyyy-MM-dd HH:mm:ss";
         }   
         SimpleDateFormat sdf = new SimpleDateFormat(format);  
         return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    
    /**
     * 根据指定时间戳 和 需要添加的分钟数，返回增加分钟数后的结果
     * @param utc
     * @param utc_time
     * @param format
     * @return
     */
    public static String Dateyanshi(String utc , int utc_time , String format){
    	String datetime = String.valueOf(Long.valueOf(utc) + Long.valueOf(utc_time));
    	return stampToDate(datetime,format);
    }
    
    /**
     * 
     * @param date 根据指定时间 和 需要添加的分钟数，返回增加分钟数后的结果
     * @param utc_time
     * @param format
     * @return
     */
    public static String Dateyanshi_(String date , int utc_time , String format){
    	String utc = null;
		try {
			utc = dateToStamp(date,format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	String datetime = String.valueOf(Long.valueOf(utc) + Long.valueOf(utc_time));
    	return stampToDate(datetime,format);
    }
    
    /**
     * 获取系统当前时间
     * @param format yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getNowDate(String format){
    	 SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
         return df.format(new Date());
    }
    
    
    /**
     * 获取时间 
     * @param format
     * @param num 把日期往后增加一天.整数往后推,负数往前移动
     * @return
     */
    public static String sysDate(String format , int num){
    	 Date date=new Date();//取时间
    	 Calendar calendar = new GregorianCalendar();
    	 calendar.setTime(date);
    	 calendar.add(calendar.DATE,num);//把日期往后增加一天.整数往后推,负数往前移动
    	 date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
    	 SimpleDateFormat formatter = new SimpleDateFormat(format);
    	 String dateString = formatter.format(date);
    	 return dateString;
    }
  
}
