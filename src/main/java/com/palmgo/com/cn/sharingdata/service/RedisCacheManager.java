package com.palmgo.com.cn.sharingdata.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import com.palmgo.com.cn.sharingdata.conf.Config;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import com.caits.lbs.exception.LBSException;
import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.JsonUtils;
import com.caits.lbs.framework.utils.ObjectSizeUtils;
import com.caits.lbs.framework.utils.StringUtils;
import com.lbs.service.redisUtils.ICacheService;

/**
 * redis客户端调用
 * <p>RedisCacheManager</p>
 * <p>TODO</p>
 *
 * @author		高鹏(gaopeng@chinatransinfo.com)
 * @version		0.0.0
 * <table style="border:1px solid gray;">
 * <tr>
 * <th width="100px">版本号</th><th width="100px">动作</th><th width="100px">修改人</th><th width="100px">修改时间</th>
 * </tr>
 * <!-- 以 Table 方式书写修改历史 -->
 * <tr>
 * <td>0.0.0</td><td>创建类</td><td>Administrator</td><td>2016年5月10日 下午2:55:58</td>
 * </tr>
 * <tr>
 * <td>XXX</td><td>XXX</td><td>XXX</td><td>XXX</td>
 * </tr>
 * </table>
 */
@Service
public class RedisCacheManager implements ICacheService {

	/** 日志记录 */
	protected static Logger log = CommonLogFactory.getLog();
	/** 采用单例模式 */
	protected static RedisCacheManager cacheManager = null;
	/** 创建全局连接池唯一实例 */
	private JedisCluster jedisCluster;
	/** 高速缓存服务地址，只需要写一个地址，其他的node自动查找加载 */
	//@Value("#{jdbcProperties.cacheServers}")
	//private String serverAddress;// = "192.168.100.181:7000";
	@Autowired
	private Config config;
	/** 服务器验证密码 */
	private String password;
	/** 网络超时时间阈值ms */
	private int soTimeout = 3 * 1000;
	

	/**
	 * 保护型构造方法
	 */
	protected RedisCacheManager() {
		if (cacheManager != null) {
			throw new IllegalStateException("实例已经初始化，本次构造放弃.");
		}
		cacheManager = this;
	}

	/**
	 * 初始化方法, 在applicationContext-redis.xml 中 <code>
	 * <![CDATA[
	 * <bean id="redisCacheServer" class="com.caits.lbs.framework.services.memcache.RedisCacheManager"
	 * init-method="init"\> 
	 * <property name="config" ref="jedisPoolConfig"><\/property>
	 * <property name="serverAddress" value="192.168.2.186:6379,192.168.4.115:6379"><\/property> 
	 * <\/bean>
	 * ]]>
	 * </code>
	 * */
	//@PostConstruct
	public void init() {
		if (jedisCluster != null) {
			throw new IllegalStateException("实例连接jedisCluster已经初始化，放弃本次执行.");
		}
		/** 设置与缓存服务器的连接 ,启用主备模式 */
		try {
			// edis Cluster will attempt to discover cluster nodes automatically
			Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
			//String[] serverArr = StringUtils.splitToArray(serverAddress, ",", true);
			List<String> serverArr = config.getCacheServer();
			Assert.isTrue(serverArr.size() > 0, "方法init的serverAddress参数错误");
			for (String server : serverArr) {
				String[] hostPort = StringUtils.splitToArray(server, ":", true);
				if (hostPort.length > 1) {
					jedisClusterNodes.add(new HostAndPort(hostPort[0], Integer.valueOf(hostPort[1])));
				}
			}
			jedisCluster = new JedisCluster(jedisClusterNodes, soTimeout,5,new GenericObjectPoolConfig(),getPassword());
			//log.info("RedisCacheManager initialization successful!" + serverAddress);
			log.info("RedisCacheManager initialization successful!" + JsonUtils.getJsonStringFromObject(serverArr));
		} catch (Exception e) {
			log.error("初始化RedisCacheManager连接失败:" + e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 添加
	 * */
	public boolean set(String key, int exp, String value) {//throws LBSException {
		if (key == null || value == null) {
//			throw new LBSException("方法put传入的对象为空");
			log.error("方法put传入的对象为空");
			return false;
		}
		try {
			String result = null;
			if(exp>0){
				result = jedisCluster.setex(key, exp, value);
			} else {
				result = jedisCluster.set(key, value);
			}
			log.debug("set key"+key+" result="+result);
			return true;
		} catch (Exception e) {
			String message = "设置Redis缓存异常,key=" + key;
			log.error(message, e);
//			throw new LBSException(message, e);
			return false;
		} finally {
			// 释放对象池
		}
	}

	public boolean set(String key, int exp, Object value) {//throws LBSException {
		if (key == null || value == null) {
//			throw new LBSException("方法put传入的对象为空");
			log.error("方法put传入的对象为空");
			return false;
		}
		try {
			byte[] bytes = ObjectSizeUtils.serialize(value);
			String result = null;
			if(exp>0){
				result = jedisCluster.setex(SafeEncoder.encode(key), exp, bytes);
			} else {
				result = jedisCluster.set(SafeEncoder.encode(key), bytes);
			}
			log.debug("set key"+key+" result="+result);
			return true;
		} catch (Exception e) {
			String message = "设置Redis缓存异常,key=" + key;
			log.error(message, e);
//			throw new LBSException(message, e);
			return false;
		} finally {
			// 释放对象池
		}
	}
	
	/**
	 * 删除
	 * */
	public void delete(String key) throws LBSException {
		jedisCluster.del(key);
	}
	
	/**
	 * 删除
	 * */
	public void deleteO(String key) throws LBSException {
		jedisCluster.del(SafeEncoder.encode(key));
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.caits.lbs.framework.services.memcache.IMemcacheService#set(java.lang
	 * .String, int, java.lang.Object, long)
	 */
	public boolean set(String key, int exp, Object value, long expiry) throws LBSException {
		return set(key, exp, value);
	}

	/**
	 * 根据指定的关键字获取对象. 返回值的类型是对象，不是简单字符串，须经过反序列化
	 * 
	 * @param key
	 * @return
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public Object getO(String key) throws LBSException {
		if (key == null) {
			throw new LBSException("方法get传入的对象为空");
		}
		try {
			byte[] bytes = jedisCluster.get(SafeEncoder.encode(key));
			if(bytes!=null) {
				Object result = ObjectSizeUtils.unserialize(bytes);
				return result;
			}
		} catch (Exception e) {
			String message = "获取Redis缓存对象异常,key=" + key;
			log.error(message, e);
			throw new LBSException(message, e);
		} finally {
			// 释放对象池
		}
		return null;
	}

	public Object get(String key){// throws LBSException {
		if (key == null) {
//			throw new LBSException("方法get传入的对象为空");
			log.error("方法get传入的对象为空");
			return null;
		}
		try {
			String result = jedisCluster.get(key);
			return result;
		} catch (Exception e) {
			String message = "获取Redis缓存对象异常,key=" + key;
			log.error(message, e);
//			throw new LBSException(message, e);
			return null;
		} finally {
			// 释放对象池
		}
	}
	
	@Override
	public boolean set(byte[] key, int exp, byte[] value, long expiry) throws LBSException {
		if (key == null || value == null) {
			throw new LBSException("方法put传入的对象为空");
		}
		// ShardedJedis jedis =null;
		try {
			String result=null;
			if(exp>0)
				result = jedisCluster.setex(key, exp, value);
			else
				result = jedisCluster.set(key, value);
			log.debug("set key"+key+" result="+result);
			return true;
		} catch (Exception e) {
			String message = "设置Redis缓存异常,key=" + key;
			log.error(message, e);
			throw new LBSException(message, e);

		} finally {
			// 释放对象池

		}

	}

	@Override
	public byte[] get(byte[] key) throws LBSException {
		if (key == null) {
			throw new LBSException("方法get传入的对象为空");
		}
		try {
			byte[] bytes = jedisCluster.get(key);
			return bytes;
		} catch (Exception e) {
			String message = "获取Redis缓存对象异常,key=" + key;
			log.error(message, e);
			throw new LBSException(message, e);
		} finally {
			// 释放对象池
		}

	}

	/**
	 * 获取变量<code>soTimeout</code>的值
	 * 
	 * @return 返回的数据类型是<code>int</code>
	 */
	public int getSoTimeout() {
		return soTimeout;
	}

	/**
	 * 设置变量<code> soTimeout</code> 的值
	 * 
	 * @param soTimeout
	 *            <code>soTimeout</code> 参数类型是<code>int</code>
	 */
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	/**
	 * 获取变量<code>password</code>的值
	 * 
	 * @return 返回的数据类型是<code>String</code>
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置变量<code> password</code> 的值
	 * 
	 * @param password
	 *            <code>password</code> 参数类型是<code>String</code>
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.caits.lbs.framework.services.memcache.IMemcacheService#sendMessage
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(String head, String body) {
		try {
			Assert.notNull(jedisCluster, "消息发送对象jedisCluster不能为空.");
			Assert.notNull(body, "消息内容msg不能为空.");
			 jedisCluster.publish(head, body);
			log.info("消息发送成功,head:" + head + ",body:" + body);
		} catch (Exception e) {
			log.error("消息发送失败,head:" + head + ",body:" + body + ",msg=" + e.getLocalizedMessage(), e);
		} finally {

		}
	}

	/**
	 * 获取集群模式的连接对象
	 * 
	 * @return
	 */
	public static synchronized JedisCluster getCLusterConnection() {
		Assert.notNull(cacheManager, "jedisCluster连接对象不能为空");
		return cacheManager.jedisCluster;
	}

	/**
	 * 直接获取redis的连接实例.
	 * 
	 * @return
	 */
	public static synchronized JedisCluster getConnection() {
		return getCLusterConnection();
	}

	/**
	 * 获取本对象实例
	 * 
	 * @return
	 */
	public static RedisCacheManager getInstance() {
		return cacheManager;
	}
	
    public static void main(String[] args) {
    	JedisCluster jedisCluster = null;
    	String serverAddress = "192.168.100.181:7000";
    	int soTimeout = 3000;
    	try {
			// edis Cluster will attempt to discover cluster nodes automatically
			Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
			String[] serverArr = StringUtils.splitToArray(serverAddress, ",", true);
			Assert.isTrue(serverArr.length > 0, "方法init的serverAddress参数错误");
			for (String server : serverArr) {
				String[] hostPort = StringUtils.splitToArray(server, ":", true);
				if (hostPort.length > 1) {
					jedisClusterNodes.add(new HostAndPort(hostPort[0], Integer.valueOf(hostPort[1])));
				}
			}
			jedisCluster = new JedisCluster(jedisClusterNodes, soTimeout,5,new GenericObjectPoolConfig(),null);
			log.info("RedisCacheManager initialization successful!" + serverAddress);
		} catch (Exception e) {
			log.error("初始化RedisCacheManager连接失败:" + e.getLocalizedMessage(), e);
		}
    	Map<String, JedisPool> map = jedisCluster.getClusterNodes();
    	
    	System.out.println(map);
    	JedisPool j = map.get("192.168.100.181:7000");
    	Jedis r = j.getResource();
//    	System.out.println(j.getNumActive());
//    	System.out.println(j.toString());
//    	System.out.println(j.getNumIdle());
//    	System.out.println(j.getNumWaiters());
//    	System.out.println(r);
//    	System.out.println(r.asking());
//    	System.out.println(r.clientGetname());
    	System.out.println(r.clientList());
    	System.out.println("-------------------------");
    	System.out.println(r.clusterInfo());
    	System.out.println("****************");
    	System.out.println(r.info());
    	System.out.println("****************");
    	System.out.println(r.info("Replication"));
//    	System.out.println(r.clusterNodes());
//    	System.out.println(r.clusterSaveConfig());
//    	System.out.println(r.clusterSlots());
//    	System.out.println(r.bgsave());
//    	System.out.println(r.bgrewriteaof());
    	
//        jedis.set("foo", "bar");  
//        String value = jedis.get("foo");  
//        System.out.println(value);
        
//    	Jedis jedis = new Jedis("192.168.100.181",6379);  
//        System.out.println(jedis.get("graphic_targetid_1100_0001"));
//        
//        
//        Jedis jedis0 = new Jedis("192.168.100.181",7005);  
//        System.out.println(jedis0.get("graphic_targetid_1100_0001"));
//        
//        Jedis jedis1 = new Jedis("192.168.100.181",7000);  
//        System.out.println(jedis1.get("graphic_targetid_1100_0001"));
//        
//        Jedis jedis2 = new Jedis("192.168.100.181",7001);  
//        System.out.println(jedis2.get("graphic_targetid_1100_0001"));
//        
//        Jedis jedis3 = new Jedis("192.168.100.181",7002);  
//        System.out.println(jedis3.get("graphic_targetid_1100_0001"));
//        
//        Jedis jedis4 = new Jedis("192.168.100.181",7003);  
//        System.out.println(jedis4.get("graphic_targetid_1100_0001"));
//        
//        Jedis jedis5 = new Jedis("192.168.100.181",7004);  
//        System.out.println(jedis5.get("graphic_targetid_1100_0001"));
        
//        116.431067,39.893542,101169320_1
        
        /*
        BaseLineEvent o = new BaseLineEvent();
        o.setControlType((byte) 1);
        o.setDistControl((byte) 2);
        System.out.println("o1="+o);
        
//        String objectJson = JsonUtils.getJsonStringFromObject(o);
//        jedis.set("aac", objectJson);
//        System.out.println(jedis.get("aac"));
        
        
        jedis.set("good".getBytes(), SerializeUtil.serialize(o));
		byte[] value1 = jedis.get("good".getBytes());
		Object object = SerializeUtil.unserialize(value1);
		System.out.println("o2="+o);
		if (object != null) {
			BaseLineEvent goods = (BaseLineEvent) object;
			System.out.println(goods.getControlType());
			System.out.println(goods.getDistControl());
		}
		System.out.println(jedis.del("good".getBytes()));*/
    }  
}
