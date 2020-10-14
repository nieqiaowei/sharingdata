/*
 * Date: 2012-10-12
 * author: Fsr  
 *
 */
package com.palmgo.com.cn.sharingdata.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.caits.lbs.framework.bean.common.GeoPoint;
import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.FileUtils;
import com.caits.lbs.framework.utils.GeoUtils;


/**
 * @author Fsr <br>
 *         Create Time：2012-10-12 下午5:53:02<br>
 *         import org.apache.http.Header; import org.apache.http.HttpEntity; import org.apache.http.HttpResponse; import org.apache.http.client.ClientProtocolException; import
 *         org.apache.http.client.HttpClient; import org.apache.http.client.methods.HttpGet; import org.apache.http.client.methods.HttpPost; import
 *         org.apache.http.client.methods.HttpUriRequest; import org.apache.http.entity.StringEntity; import org.apache.http.impl.client.DefaultHttpClient;
 */
public class CommonUtil {
	 protected static Logger log = CommonLogFactory.getLog();
	/**
	 * 检查坐标格式
	 * @param request
	 * @param response
	 * @param parameterName
	 * @param coordinate
	 * @return boolean
	 * @throws
	 */
	public static boolean checkCoords(String coordinate) {
		coordinate = coordinate.trim();
		String[] temps = coordinate.split(",");
		if (temps.length != 2) {
			return false;
		}
		String[] coords = coordinate.split(",");
		if (!isDecimal(coords[0]) || !isDecimal(coords[1])) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是小数
	 * @param str
	 * @return
	 */
	public static boolean isDecimal(String str) {
		str = str.trim();
		int x = 0;
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				if (str.charAt(i) == '.') {
					x++;
				} else {
					return false;
				}
				if (x > 1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		str = str.trim();
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @Description: 坐标偏移
	 * @param response
	 * @param parameterName
	 * @param coordinate
	 * @return String
	 * @throws
	 */
	public static String encryPoint(String coordinate) {
		coordinate = coordinate.trim();
		String[] coords = coordinate.split(",");
		GeoPoint newPoint = GeoUtils.encryPoint(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
		if (newPoint == null) {
			// logger.error("{}:{}坐标偏移错误", parameterName, coordinate);
			return null;
		}
		return newPoint.getLatitude() + "," + newPoint.getLongitude();
	}

	/**
	 * 读取指定的属性文件
	 * @param dataPath 文件路径
	 * @param charset 字符集
	 * @return 属性集合
	 */
	public static Properties readPropertyFile(String dataPath, String charset) {
		Properties props = new Properties();
		BufferedReader reader = null;
		try {
			// 从属性文件中读取配置信息
			reader = FileUtils.createBufferedReader(dataPath, 2000, charset, FileUtils.CONST_BUFFERSIZE_10K);
			props.load(reader);
		} catch (Exception e) {
			log.error("读取属性文件异常,msg=" + e.getLocalizedMessage(), e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				log.error("关闭属性文件异常,msg=" + e.getLocalizedMessage());
			}
		}
		return props;
	}

	/**
	 * 产生新的license
	 */
	public static String generateLicense() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 将长度转换成文字描述
	 * @param length 单位为米
	 * @return
	 */
	public static String length2Desc(int length) {
		String desc = null;
		int km = length / 1000;
		int hundred = (int) Math.round((length % 1000) * 1.0 / 100);
		if (hundred == 10) {
			km++;
			hundred = 0;
		}
		if (km == 0 && hundred == 0)
			desc = "";
		else if (km == 0 && hundred > 0)
			desc = hundred + "00米";
		else if (km > 0 && hundred == 0)
			desc = km + "公里";
		else
			desc = km + "." + hundred + "公里";
		return desc;
	}

	/**
	 * 将时间秒数转换为描述
	 * @param secondTime
	 * @return
	 */
	public static String secondTime2Desc(int secondTime) {
		String desc = null;
		int hour = secondTime / 3600;
		int minute = (int) ((secondTime % 3600) / 60);
		if (hour == 0 && minute == 0)
			desc = "<1分钟";
		else if (hour == 0 && minute > 0)
			desc = minute + "分钟";
		else if (hour > 0 && minute == 0)
			desc = hour + "小时";
		else
			desc = hour + "小时" + minute + "分钟";
		return desc;
	}

	/**
	 * 获取客户端真实ip
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取web应用类路径 
	 * @return String
	 */
	public static String getWebClassesPath() {
		String path = CommonUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		return path;
	}

	/**
	 * 获取web-info的路径 
	 * @return
	 * @throws IllegalAccessException String
	 */
	public static String getWebInfPath() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0)
			path = path.substring(0, path.indexOf("WEB-INF") + 8);
		else {
			throw new IllegalAccessException("路径获取错误");
		}
		return path;
	}

	/**
	 * 获取应用war的路径 
	 * @return
	 * @throws IllegalAccessException String
	 */
	public static String getWebRoot() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0)
			path = path.substring(0, path.indexOf("WEB-INF"));
		else {
			throw new IllegalAccessException("路径获取错误");
		}
		return path;
	}
	
	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == ' ') {
                 c[i] = '\u3000';
               } else if (c[i] < '\177') {
                 c[i] = (char) (c[i] + 65248);

               }
             }
             return new String(c);
    }
    
    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == '\u3000') {
                 c[i] = ' ';
               } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                 c[i] = (char) (c[i] - 65248);

               }
             }
        String returnString = new String(c);
        
             return returnString;
    }
    
    public static boolean isChinese(String str){
    	boolean temp = false;
    	Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    	Matcher m = p.matcher(str);
    	if(m.find()){
    		temp = true;
    	}
    	return temp;
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
    public static String NowDate(String format){
    	 SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
         System.out.println();// new Date()为获取当前系统时间
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
    
    /**
     * 内容追加到文件
     * @param fileName
     * @param content
     */
    public static void addcontentToFile(String fileName, String content) {
    	boolean fristwriter = false;
    	//检查文件是否存在不存在则创键件文件
    	File file = new File(fileName);
    	if(!file.exists()){
    		try {
    			file.createNewFile();
    			log.info("创建文件成功！"+fileName);
    			fristwriter = true;
			} catch (Exception e) {
				log.error("创建文件失败！"+e.getLocalizedMessage(),e);
			}
    	}
        FileWriter writer = null;  
        try {     
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(fileName, true);
            if(fristwriter){
            	 writer.write(content);
            }else{
            	 writer.write("\r\n"+content);
            }
            writer.flush();
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
    }
    
    /**
     * 新写入覆盖
     * @param fileName
     * @param content
     */
    public static void addcontentToFileNew(String fileName, String content) {   
        FileWriter writer = null;  
        try {     
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(fileName, false);     
            writer.write(content+"\r\n");
            writer.flush();
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
    }
    
    /**
	 * 获取文件当前路径
	 * @return
	 */
	public static String getCurrentPath(){
		File directory = new File(".");
		File newPath = null;
		try {
			newPath = new File(directory.getCanonicalPath());
			newPath.mkdir();
		} catch (Exception exp) {
			log.error("获取文件路径错误,msg="+exp.getMessage(),exp);
		}
		if (newPath != null)
			return newPath.getPath() + File.separator;
		else
			return File.separator;
	}
	
	/**
	 * 数组转换为16进制字符串
	 * 
	 * @param block
	 * @return
	 */
	public static String bytes2HexString(byte[] block) {
		StringBuffer buf = new StringBuffer();
		int len = block.length;
		for (int i = 0; i < len; i++) {
			byte2HexString(block[i], buf);
		}
		return buf.toString();
	}
	
	/**
	 * 将单个字符转换为16进制并追加到buffer中 Converts a byte to hex digit and writes to the
	 * supplied buffer
	 * 
	 * @param b
	 * @param buf
	 */
	public static void byte2HexString(byte b, StringBuffer buf) {
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}
	
	/**
	 * gzip 解压
	 * @param bytes
	 * @return
	 * @throws Exception 
	 */
	public static byte[] gzipuncompress(byte[] bytes) throws Exception {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream ungzip = null;
        ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        byte[] data = out.toByteArray();
        ungzip.close();
        in.close();
        out.close();
        return data;
    }
	
	
	
	
	public static String getDate(String str) {
    	SimpleDateFormat sdf1= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    	SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			return sdf2.format(sdf1.parse(str));
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}
    	return null;
    }
	
	
    public static void main(String[] args) {
    	String time = Dateyanshi_("2017-09-20T11:41:41.777".replaceAll("T", " "),60 * 30,"yyyy-MM-dd HH:mm:ss");
    	System.out.println(time);
    	System.out.println(sysDate("yyyy-MM-dd",0));
    	time  = Dateyanshi_("2018-5-12 6:42:00", 0, "yyyy-MM-dd HH:mm:ss");
    	System.out.println(time);
	}
}
