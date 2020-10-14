package com.palmgo.com.cn.sharingdata.util;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import sun.misc.Unsafe;

import com.caits.lbs.framework.log.CommonLogFactory;

/**
 * <p>
 * UnsafeExcerpt
 * </p>
 * <p>
 * 用途：不安全的高效操作对象的方法，在性能要求高的时候调用
 * 直接操作内存，获取和设置对象的值
 * </p>
 * 
 * @author 周华彬(zhouhuabin@ctfo.com, zhou_hua_bin@163.com)
 * @version 0.0.1 2012-11-1
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th>
 *          <th width="100px">动作</th>
 *          <th width="100px">修改人</th>
 *          <th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>zhb</td>
 *          <td>2012-11-1 下午5:56:22</td>
 *          </tr>
 *          <tr>
 *          <td>0.0.1</td>
 *          <td>创建类</td>
 *          <td>zhb</td>
 *          <td>2012-11-1 下午5:56:22</td>
 *          <td>0.0.2</td>
 *          <td>修改类</td>
 *          <td>xxx</td>
 *          <td>x年x月x日</td>
 *          </tr>
 *          </table>
 */
@SuppressWarnings("restriction")
public class UnsafeExcerpt {
	protected Logger log = CommonLogFactory.getLog();

	public UnsafeExcerpt() {
		super();
	}

	// RandomDataInput

	/**
	 * 比较整数值，一致则替换，否则不替换 
	 * @param obj	要操作的对象
	 * @param ageOffset	Field的偏移值
	 * @param expect	预期的值
	 * @param update	新值
	 * @return true:替换成功,false:未替换
	 */
	public boolean compareAndSwapInt(Object obj, long ageOffset, int expect, int update) {
		return UNSAFE.compareAndSwapInt(obj, ageOffset, expect, update);
	}

	/**
	 * 给字段设置新值 
	 * @param obj	要操作的对象
	 * @param ageOffset	字段的偏移值
	 * @param value	新值
	 */
	public void setFieldObject(Object obj, long ageOffset, Object value) {
		UNSAFE.putObject(obj, ageOffset, value);
	}

	/**
	 * 获取字段的值 
	 * @param obj	要操作的对象
	 * @param ageOffset	字段的偏移值
	 * @return	字段的值
	 */
	public Object getFieldObject(Object obj, long ageOffset) {
		return UNSAFE.getObject(obj, ageOffset);
	}

	/**
	 * 获取指定类，指定字段的偏移值 
	 * @param clazz	类名
	 * @param fieldName	字段名称，注意大小写
	 * @return	字段的偏移值
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public long getFieldOffset(Class<?> clazz, String fieldName) throws SecurityException, NoSuchFieldException {
		return UNSAFE.objectFieldOffset(clazz.getDeclaredField(fieldName));
	}

	/**
	 * 根据对象和字段名获取值 
	 * @param obj	对象
	 * @param fieldName	属性名
	 * @return
	 */
	public Object getFieldObject(Object obj, String fieldName) throws SecurityException, NoSuchFieldException  {
		long offset=getFieldOffset(obj.getClass(),fieldName);
		return UNSAFE.getObject(obj, offset);
	}
	/**
	 * 根据对象和字段名设置对象的值 
	 * @param obj	对象
	 * @param fieldName	属性名
	 * @param value 对象的值
	 * @return
	 */
	public void setFieldObject(Object obj, String fieldName,Object value) throws SecurityException, NoSuchFieldException  {
		long offset=getFieldOffset(obj.getClass(),fieldName);
		UNSAFE.putObject(obj, offset, value);
	}
	/**
	 * *** Access the Unsafe class *****
	 */
	private static final Unsafe UNSAFE;

	static {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			UNSAFE = (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * 测试内存操作对象的方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UnsafeExcerpt unsafe = new UnsafeExcerpt();
			Person person = unsafe.new Person();
			unsafe.setFieldObject(person, "age", 25);
			unsafe.setFieldObject(person, "name", "zhb");
			unsafe.log.info("person:" + unsafe.getFieldObject(person, "name"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	class Person {
		String name;
		int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
}