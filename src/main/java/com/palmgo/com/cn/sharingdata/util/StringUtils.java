package com.palmgo.com.cn.sharingdata.util;

/**
 * <p>文件名:		StringUtils.java</p>
 * <p>版权:		CopyrightTag</p>
 * <p>公司:		CompanyTag</p>
 * @author		周华彬(zhouhuabin@ctfo.com)
 */


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.caits.lbs.framework.log.CommonLogFactory;

/**
 * <p>
 * StringUtils
 * </p>
 * <p>
 * 字符串类通用工具类
 * </p>
 * 
 * @author 周华彬(zhouhuabin@ctfo.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th>
 *          <th width="100px">动作</th>
 *          <th * width="100px">修改人</th>
 *          <th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>周华彬</td>
 *          <td>2008-7-11 下午05:00:41</td>
 *          </tr>
 *          <tr>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          </tr>
 *          </table>
 */
public abstract class StringUtils {

	protected static Logger log = CommonLogFactory.getLog();

	/**
	 * prefix of ascii string of native character
	 */
	private static String ASCII_PREFIX = "\\u";
	
	static String key1 = "\\$\\{(.)+\\}";

	/**
	 * 判断输入字符串是否满足以下非空条件：<br>
	 * 不为空，剪头去尾后长度大于0，不论大小写字符串都不等于null
	 * 
	 * @param str
	 *            输入要判断的串
	 * @return 根据依据判断出的布尔结果
	 */
	public static boolean isNullOrBlank(String str) {
		if (str == null) {
			return true;
		}
		if (str.trim().length() <= 0) {
			return true;
		}
		if (str.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 非null而且内容不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean notNullOrBlank(String str) {
		return !isNullOrBlank(str);
	}

	/**
	 * 安全的trim方式，剪头去尾，在输入为空的情况下也能安全剪除字符串两端的空白
	 * 
	 * @param str
	 * @return 返回绝不为空，至少为长度为0的串
	 */
	public static String trim(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 获得字符串的数组表示
	 * 
	 * @param srcStr
	 *            使用(delim)符号分割的字符串
	 * @param delim
	 *            分割符，如果符号不是打印符号，则使用空格替代
	 * @param zeroSize
	 *            决定当拆不出任何数组的时候，是返回null还是零长度的对象，如果为true则返回0长度对象，否则直接null
	 * @return
	 */
	public static String[] splitToArray(String srcStr, String delim, boolean zeroSize) {
		String[] strArray = null;
		if (!isNullOrBlank(srcStr)) {
			// 如果分割符不是特殊符号，则默认使用空格分割
			if (isNullOrBlank(delim)) {
				delim = " ";
			}
			StringTokenizer stringTokenizer = new StringTokenizer(srcStr, delim);
			strArray = new String[stringTokenizer.countTokens()];
			for (int i = 0; stringTokenizer.hasMoreTokens(); i++) {
				strArray[i] = stringTokenizer.nextToken();
			}
		}
		if (zeroSize && strArray == null) {
			// 为了节省空间，需要指定长度为0
			strArray = new String[0];
		}
		return strArray;
	}

	/**
	 * null convert to blank
	 * 
	 * @param string
	 * @return
	 */
	public static String null2blank(String string) {
		return string == null ? "" : string;
	}

	/**
	 * null convert to default string <br>
	 * the expression is
	 * <code>return string == null ? defaultVal : string</code>
	 * 
	 * @param string
	 *            to be checked string
	 * @param defaultVal
	 *            if string is null,will return defaultVal
	 * @return string or defaultVal
	 */
	public static String null2default(String string, String defaultVal) {
		return string == null ? defaultVal : string;
	}

	/**
	 * FIXME
	 * 
	 * @param src
	 * @return
	 */
	public static String removeSpace(String src) {
		if (src == null) {
			return "";
		}
		char[] ss = src.toCharArray();
		int j = 0;
		for (int i = 0; i < ss.length; i++) {
			if (ss[i] != ' ') {
				ss[j++] = ss[i];
			}
		}
		return new String(ss).substring(0, j);
	}

	/**
	 * FIXME
	 * 
	 * @param e
	 * @param l
	 * @return 决不返回空
	 */
	public static List<String> stacks(Throwable e, List<String> l) {
		if (e == null) {
			return new LinkedList<String>();
		}
		if (l == null) {
			l = new LinkedList<String>();
		}

		l.add(e.getMessage());
		StackTraceElement[] sts = e.getStackTrace();
		if (sts != null && sts.length > 0) {
			for (StackTraceElement s : sts) {
				l.add(s.toString());
			}
		}
		Throwable cause = e.getCause();
		if (cause != null) {
			stacks(cause, l);
		}
		return l;
	}

	/**
	 * 合并数组到字符串
	 * 
	 * @param arr
	 * @param sep
	 *            分割符
	 * @return
	 */
	public static String joinArrays(final String[] arr, String sep) {
		if (arr == null || arr.length < 1)
			return "";
		StringBuilder sb = new StringBuilder(arr[0]);
		for (int i = 1; i < arr.length; i++) {
			String str = arr[i];
			sb.append(sep + str);
		}
		return sb.toString();
	}

	/**
	 * 
	 * 将map各元素追加到字符串后
	 * 
	 * @param builder
	 * @param map
	 * @return
	 */
	public static StringBuilder map2String(StringBuilder builder, String mapName, Map<?, ?> map) {
		if (map != null && !map.isEmpty()) {
			builder.append(mapName + ": {");
			for (Object key : map.keySet()) {
				builder.append(key);
				builder.append(':');
				builder.append(map.get(key));
				builder.append(", \r\n");
			}
			builder.append("}");
		} else {
			builder.append(mapName + ": ");
			builder.append(map);
		}
		return builder;
	}

	/**
	 * 
	 * 将列表各元素追加到字符串后
	 * 
	 * @param builder
	 *            被追加的地方
	 * @param listName
	 *            列表名称
	 * @param list
	 *            列表
	 * @return
	 */
	public static StringBuilder list2String(StringBuilder builder, String listName, List<?> list) {
		if (list != null && !list.isEmpty()) {
			builder.append(listName + ": (");
			for (Object value : list) {
				builder.append(value);
				builder.append(", ");
			}
			builder.append(")");
		} else {
			builder.append(listName + ": ");
			builder.append(list);
		}
		return builder;
	}

	/**
	 * 
	 * 将集合各元素追加到字符串后
	 * 
	 * @param builder
	 * @param setName
	 * @param set
	 * @return
	 */
	public static StringBuilder set2String(StringBuilder builder, String setName, Set<?> set) {
		if (set != null && !set.isEmpty()) {
			builder.append(setName + ": [");
			for (Object value : set) {
				builder.append(value);
				builder.append(", ");
			}
			builder.append("]");
		} else {
			builder.append(setName + ": ");
			builder.append(set);
		}
		return builder;
	}

	/**
	 * 取出某个字符串{}中的内容
	 * 
	 * @param string
	 * @return
	 */
	public static String takeOutString(String string) {
		if (string.indexOf('{') == -1) {
			return "";
		}
		string = string.substring(1);
		string = string.substring(0, string.length() - 1);
		if (notNullOrBlank(string)) {
			string = string + ", ";
		}
		return string;
	}

	/**
	 * 从数组里查找指定的属性是否存在
	 * 
	 * @param attributeName
	 * @param attributes
	 * @return
	 */
	public static boolean findAttributes(String attributeName, String[] attributes) {
		if (attributes == null || attributeName == null)
			return false;
		for (String attribute : attributes) {
			if (attributeName.equalsIgnoreCase(attribute))
				return true;
		}
		return false;
	}

	/**
	 * 将A编码字符串转换成B编码字符串
	 * 
	 * @param str
	 *            要转换的字符串
	 * @param sourceEncoding
	 *            原始编码
	 * @param destEncoding
	 *            目标编码
	 * @return 转换后的字符串
	 */
	public static String convertCharset(String str, String sourceEncoding, String destEncoding) {
		if (str != null && sourceEncoding != null && destEncoding != null) {

			try {
				if (sourceEncoding.equalsIgnoreCase(destEncoding))
					return str;
				return new String(str.getBytes(sourceEncoding), destEncoding);
			} catch (UnsupportedEncodingException e) {
				log.error("convertCharset error,msg=" + e.getLocalizedMessage(), e);
			}
		}
		return "";
	}

	/**
	 * 字节数组转换成指定编码的字符串
	 * 
	 * @param bytes
	 *            原始字节数组
	 * @param skip
	 *            开始位置
	 * @param charsetName
	 *            目标编码
	 * @return 字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String getString(byte[] bytes, int skip, String charsetName) throws UnsupportedEncodingException {
		int start = skip;
		for (int i = 0; i < bytes.length - 1; i++) {
			if (bytes[i] < 0) {
				int sum = ((bytes[i] & 0xff) << 8) + (bytes[i + 1] & 0xff);
				if (sum >= 0x8140 && sum <= 0xFEFE) {
					if (++i == start) {
						start++; // 位置正好占半个汉字, 多跳过一个, 如果写成start--, 则表示少跳过一个
					}
				}
			}
			if (i >= start) {
				break;
			}
		}
		return new String(bytes, start, bytes.length - start, charsetName);
	}

	/**
	 * 将字符串s转化为html代码
	 * 
	 * @param s
	 *            要转化的字符串
	 * @return 转化之后的字符串
	 */
	public static String toHtml(String s) {
		s = s.replace("&", "&amp;");
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		s = s.replace("\t", "    ");
		s = s.replace("\r\n", "\n");
		s = s.replace("\n", "<br>");
		s = s.replace(" ", "&nbsp;");
		s = s.replace("'", "&#39;");
		s = s.replace("\\", "&#92;");
		return s;
	}

	/**
	 * 将html代码转化为 显示字符串s
	 * 
	 * @param s
	 * @return
	 */
	public static String htmlTo(String s) {
		s = s.replace("&amp;", "&");
		s = s.replace("&lt;", "<");
		s = s.replace("&gt;", ">");
		s = s.replace("    ", "\t");
		s = s.replace("\n", "\r\n");
		s = s.replace("<br>", "\n");
		s = s.replace("&nbsp;", " ");
		s = s.replace("&#39;", "'");
		s = s.replace("&#92;", "\\");
		return s;
	}

	/**
	 * Native to ascii string. It's same as execut native2ascii.exe.
	 * 
	 * @param str
	 *            native string
	 * @return ascii string
	 */
	public static String native2Ascii(String str) {
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			sb.append(char2Ascii(chars[i]));
		}
		return sb.toString();
	}

	/**
	 * Native character to ascii string.
	 * 
	 * @param c
	 *            native character
	 * @return ascii string
	 */
	private static String char2Ascii(char c) {
		if (c > 255) {
			StringBuilder sb = new StringBuilder();
			sb.append(ASCII_PREFIX);
			int code = (c >> 8);
			String tmp = Integer.toHexString(code);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
			code = (c & 0xFF);
			tmp = Integer.toHexString(code);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
			return sb.toString();
		} else {
			return Character.toString(c);
		}
	}

	/**
	 * Ascii to native string. It's same as execut native2ascii.exe -reverse.
	 * 
	 * @param str
	 *            ascii string
	 * @return native string
	 */
	public static String ascii2Native(String str) {
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int index = str.indexOf(ASCII_PREFIX);
		while (index != -1) {
			sb.append(str.substring(begin, index));
			sb.append(ascii2Char(str.substring(index, index + 6)));
			begin = index + 6;
			index = str.indexOf(ASCII_PREFIX, begin);
		}
		sb.append(str.substring(begin));
		return sb.toString();
	}

	/**
	 * Ascii to native character. String regEx = "[\\u4e00-\\u9fa5]"; 汉字
	 * 
	 * @param str
	 *            ascii string
	 * @return native character
	 */
	private static char ascii2Char(String str) {
		if (str.length() != 6) {
			throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
		}
		if (!ASCII_PREFIX.equals(str.substring(0, 2))) {
			throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
		}
		String tmp = str.substring(2, 4);
		int code = Integer.parseInt(tmp, 16) << 8;
		tmp = str.substring(4, 6);
		code += Integer.parseInt(tmp, 16);
		return (char) code;
	}

	/**
	 * 判断字符串是否包含${xxx}形式的子串
	 * 
	 * @param sourceStr
	 *            判断字符串
	 * @return
	 */
	public static boolean getValueByName(String sourceStr) {
		return sourceStr.matches("(.)*\\$\\{(.)+\\}(.)*");
	}

	/**
	 * 变量替换 ，将${xxxx_yyy}中的xxxx_yyy从map中实际值代替
	 * 
	 * @param oldStr
	 * @param valueMap
	 *            提供值的map
	 * @return
	 */
	public static String replaceVars(String oldStr, Map<String, String> valueMap) {
		String resultString = oldStr;
		try {
			Pattern regex = Pattern.compile(key1, Pattern.CASE_INSENSITIVE);
			Matcher matcher = regex.matcher(oldStr);
			StringBuffer sb = new StringBuffer();
			// 使用find()方法查找第一个匹配的对象
			// 使用循环找出模式匹配的内容替换之,再将内容加到sb里
			while (matcher.find()) {
				String temp = matcher.group();
				int count = matcher.groupCount();
				String key = temp.substring(2, temp.length() - 1);
				String newValue = null2default(valueMap.get(key), "xxx");
				matcher.appendReplacement(sb, newValue);
			}
			// 最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
			matcher.appendTail(sb);
			resultString = sb.toString();
		} catch (IllegalArgumentException ex) {
			log.info("get error,msg=" + ex.getLocalizedMessage(), ex);
		} catch (IndexOutOfBoundsException ex) {
			log.info("get error,msg=" + ex.getLocalizedMessage(), ex);
		}
		return resultString;
	}
	
	
	
	public static Map<String, String> getVars(String oldStr) {
		Map<String, String> valueMap = new HashMap<>();
		try {
			Pattern regex = Pattern.compile(key1, Pattern.CASE_INSENSITIVE);
			Matcher matcher = regex.matcher(oldStr);
			StringBuffer sb = new StringBuffer();
			// 使用find()方法查找第一个匹配的对象
			// 使用循环找出模式匹配的内容替换之,再将内容加到sb里
			while (matcher.find()) {
				String temp = matcher.group();
				int count = matcher.groupCount();
				String key = temp.substring(2, temp.length() - 1);
				valueMap.put(key, "");
			}
			// 最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
			matcher.appendTail(sb);
		} catch (IllegalArgumentException ex) {
			log.info("get error,msg=" + ex.getLocalizedMessage(), ex);
		} catch (IndexOutOfBoundsException ex) {
			log.info("get error,msg=" + ex.getLocalizedMessage(), ex);
		}
		return valueMap;
	}
	

	/**
	 * 变量替换 ，将${xxxx_yyy}中的xxxx_yyy从map中实际值代替
	 * 
	 * @param oldStr
	 * @param valueObj
	 *            提供值的实例
	 * @return
	 */
	public static String replaceVars(String oldStr, Object valueObj) {
		String resultString = oldStr;
		try {
			UnsafeExcerpt unsafe = new UnsafeExcerpt();
			Pattern regex = Pattern.compile(key1, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
			Matcher matcher = regex.matcher(oldStr);
			StringBuffer sb = new StringBuffer();
			// 使用find()方法查找第一个匹配的对象
			boolean result = matcher.find();
			// 使用循环找出模式匹配的内容替换之,再将内容加到sb里
			while (result) {
				String temp = matcher.group(1);
				String key = temp.substring(2, temp.length() - 1);
				Object value = unsafe.getFieldObject(valueObj, key);
				String newValue = value != null ? (value + "") : "xxx";
				matcher.appendReplacement(sb, newValue);
				result = matcher.find();
			}
			// 最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
			matcher.appendTail(sb);
			resultString = sb.toString();
		} catch (IllegalArgumentException ex) {
			log.info("get error,msg=" + ex.getLocalizedMessage(), ex);
		} catch (IndexOutOfBoundsException ex) {
			log.info("get error,msg=" + ex.getLocalizedMessage(), ex);
		} catch (SecurityException ex) {
			log.info("get error,msg=" + ex.getLocalizedMessage(), ex);
		} catch (NoSuchFieldException ex) {
			log.info("get error,msg=" + ex.getLocalizedMessage(), ex);
		}
		return resultString;
	}

	/**
	 * 截取源字符串中从开始字符到另指定结束字符中间的串 如果开始字符和结束字符有多处，则只取第一处
	 * 
	 * @param source
	 *            源字符串
	 * @param startStr
	 *            开始串
	 * @param endStr
	 *            结束串,不存在的串则传xxx，自动获取到原始串的结尾
	 * @return 中间的字符串
	 */
	public static String subStringBySatrtEndStr(String source, String startStr, String endStr) {
		String result = null;
		try {
			int startPos = source.indexOf(startStr);
			Assert.isTrue(startPos > -1, source + "中不包含" + startStr + ",将不截取");
			int endPos = source.indexOf(endStr, startPos + startStr.length());
			if (endPos == -1)
				result = source.substring(startPos + startStr.length());
			else
				result = source.substring(startPos + startStr.length(), endPos);
		} catch (Exception ex) {
			log.info("字符串截取异常,source=" + source + ",startStr=" + startStr, ex);
		}
		return result;
	}

	/**
	 * 全角转半角的 转换函数
	 * @param fullStr
	 *            全角字符
	 * @return 半角字符
	 */
	public static final String convertFull2Half(String fullStr) {
		StringBuilder outStrBuf = new StringBuilder("");
		String Tstr = "";
		byte[] b = null;
		try {
			for (int i = 0; i < fullStr.length(); i++) {
				Tstr = fullStr.substring(i, i + 1);
				// 全角空格转换成半角空格
				if (Tstr.equals("　")) {
					outStrBuf.append(" ");
					continue;
				}
				b = Tstr.getBytes("unicode");
				// 得到 unicode 字节数据
				if (b[2] == -1) {
					// 表示全角？
					b[3] = (byte) (b[3] + 32);
					b[2] = 0;
					outStrBuf.append(new String(b, "unicode"));
				} else {
					outStrBuf.append(Tstr);
				}
			} // end for.
		} catch (Exception e) {
			log.error("全角转换半角异常", e);
		}
		return outStrBuf.toString();
	}

	/**
	 * 半角转全角
	 * 
	 * @param halfStr
	 * @return 全角字符
	 */
	public static final String convertHalf2Full(String halfStr) {
		StringBuilder outStrBuf = new StringBuilder("");
		String Tstr = "";
		byte[] b = null;
		try {
			for (int i = 0; i < halfStr.length(); i++) {
				Tstr = halfStr.substring(i, i + 1);
				if (Tstr.equals(" ")) {
					// 半角空格
					outStrBuf.append(Tstr);
					continue;
				}
				b = Tstr.getBytes("unicode");
				if (b[2] == 0) {
					// 半角?
					b[3] = (byte) (b[3] - 32);
					b[2] = -1;
					outStrBuf.append(new String(b, "unicode"));
				} else {
					outStrBuf.append(Tstr);
				}
			}
		} catch (Exception e) {
			log.error("半角转换全角异常", e);
		}
		return outStrBuf.toString();
	}
    /**
     * 根据Unicode编码完美的判断中文汉字和符号 
     * @param c	要判断的字符
     * @return
     */
    public static boolean charIsChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
	/**
	 * FIXME
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> valMap = new HashMap<String, String>();
		valMap.put("roadName", "二环路");
		valMap.put("jamLength", "300米");// ${jamLength}
		System.out.println("replaceVars(String, Object):" + replaceVars("${roadName}拥堵xxxx${jamLength}", valMap));
		System.out.println("Htmlto:" + htmlTo("&lt;a&gt;ttt&lt;/a&gt;"));
		System.out.println("subStringBySatrtEndStr:" + subStringBySatrtEndStr("前方300米处拥堵约400米,通过需要约2分钟", "通过需要约", "xxx"));
		System.out.println("subStringBySatrtEndStr-other:" + subStringBySatrtEndStr("\"涞宝路\"", "\"", "\""));
		// System.out.println(encodeConvert(encodeConvert("a中华人民共和国b","utf-8","gbk"),"gbk","utf-8"));
		String QJstr = "hello!！ 全角转换，ＤＡＯ ５３２３２　-Ｓ３２６";
		System.out.println(convertFull2Half(QJstr));
		String str = "java 汽车 召回 2345";
		System.out.println(convertHalf2Full(str));
		System.out.println("中文判断:"+charIsChinese('Y'));
		System.out.println(":"+JsonUtils.getJsonStringFromMap(getVars("${roadName}拥堵xxxx${jamLength}")));
		System.exit(0);
	}
}
