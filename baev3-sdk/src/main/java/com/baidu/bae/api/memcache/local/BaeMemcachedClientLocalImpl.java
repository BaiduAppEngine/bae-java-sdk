package com.baidu.bae.api.memcache.local;

import java.util.Date;
import java.util.Map;

import com.baidu.bae.api.memcache.BaeCache;
import com.baidu.bae.api.memcache.internal.ZcacheConf;



/**
 * BaeMemcachedClient兼容了MemcacheClient的add、set、replace、get、delete等基本操作的接口，为开发者提供cache服务
 * 其中pname和token的大小不能超过64Byte
 * key的长度不能超过180Byte
 * value的长度不能超过1MB(在value被传输之前BaeMemcachedClient会对value进行一些处理，所以实际允许的value大小可能不到1MB)
 *
 */
public class BaeMemcachedClientLocalImpl  implements BaeCache {
	//private Logger logger = Logger.getLogger(BaeMemcachedClient.class.getName());
	//缓存客户端
	private MemcachedClient cacheClient;
	private String user;
	private String password;
	private String cacheId;
	private long logid;
	private String memcacheAddr;
	private int connect_timeout = 5;
	private static final int MAX_TIMEOUT = 10;
	private static final int MIN_TIMEOUT = 1;
	private static final int DEFAULT_TIMEOUT = 5;

	/**
	 * 创建一个BaeMemCachedClient实例
	 *
	 * @param cacheId	资源id
	 * @param memcacheAddr  服务器地址和端口，例如cache.duapp.com:10240
	 * @param user  用户名
	 * @param password  密码
	 */
	public BaeMemcachedClientLocalImpl(String cacheId, String memcacheAddr, String user, String password) {
		init();
	}

	/** 
	 * 用默认值初始化客户端，默认key进行URL编码
	 */
	private void init() {
		
		cacheClient = new MemcachedClient(this.user, this.password,this.cacheId, this.logid, new String[] {this.memcacheAddr}, this.connect_timeout);
	}

	public String getCacheId() {
		return cacheId;
	}
	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
		cacheClient.setAppid(cacheId);
	}

	/**
	 * 获取user
	 * @return user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * 设置user
	 * @param user
	 */
	public void setUser(String user) {
		if (user.getBytes().length > ZcacheConf.PNAME_MAX_LEN) {
			//logger.log(Level.WARNING, "ak length is larger than " + ZcacheConf.PNAME_MAX_LEN + " !!!");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("ak length is larger than " + ZcacheConf.PNAME_MAX_LEN + " !!!");
			return;
		}
		this.user = user;
		cacheClient.setPname(user);
	}
	/**
	 * 获取password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置password
	 * @param password
	 */
	public void setPassword(String password) {
		if (password.getBytes().length > ZcacheConf.TOKEN_MAX_LEN) {
			//logger.log(Level.WARNING, "sk length is larger than " + ZcacheConf.TOKEN_MAX_LEN + " !!!");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("sk length is larger than " + ZcacheConf.PNAME_MAX_LEN + " !!!");
			return;
		}
		this.password = password;
		cacheClient.setToken(password);
	}
	/**
	 * 获取cache服务地址
	 * @return cache服务地址和端口，例如cache.duapp.com:10240	String
	 */
	public String getMemcacheAddr() {
		return this.memcacheAddr;
	}
	/**
	 * 设置cache服务器地址
	 * @param hosts 例如：{"192.168.0.18:3399","192.168.0.99:3396"}
	 */
	public void setMemcacheAddr(String memcacheAddr) {
		this.memcacheAddr = memcacheAddr;
	}
	/**
	 * 获取连接超时时间，单位秒，默认5s，最小1s，最大10s
	 * @return connect_timeout
	 */
	public int getConnectTimeout() {
		return connect_timeout;
	}
	/**
	 * 获取连接超时时间，单位秒，默认5s，最小1s，最大10s
	 * @param connect_timeout
	 */
	public void setConnectTimeout(int connect_timeout) {
		if (connect_timeout < BaeMemcachedClientLocalImpl.MIN_TIMEOUT || connect_timeout > BaeMemcachedClientLocalImpl.MAX_TIMEOUT) {
			connect_timeout = BaeMemcachedClientLocalImpl.DEFAULT_TIMEOUT;
		}
		this.connect_timeout = connect_timeout;
		cacheClient.setTimeout(connect_timeout);
	}
	/** 
	 * 检查key是否在cache中存在 
	 * 
	 * @param key 要检测的key
	 * @return true 如果key在cache中， false 如果key不在cache中
	 */
	public boolean keyExists( String key ) {
		return ( get(key) != null );
	}

	/**
	 * 给定key，从cache中将其对应的对象删除
	 *
	 * @param key 要删除的key
	 * @return <code>true</code>, 如果这个key被成功删除，或者key在cache中不存在
	 */
	public boolean delete( String key ) {
		return delete( key, 0 );
	}
	
	/**
	 * 给定key，从cache中将其对应的对象删除，可以延时删除，此方法是用于兼容Memcached客户端的方法
	 * 
	 * 在进行延时删除时，比如设置的延迟删除时间是1s，那么在这1s内，对于add、replace和get操作是不会生效的，对于set操作是会生效的。
	 *
	 * @param key 要删除的key
	 * @param expiry 表示经过(expiry-当前时间)毫秒之后删除key，即在expiry的时间点删除key，如果小于或等于当前时间则立即删除
	 * @return <code>true</code>, 如果这个key被成功删除，或者key在cache中不存在
	 */
	public boolean delete( String key, Date expiry ) {
		long expire = 0;
		if (expiry != null) {
			expire = expiry.getTime() - System.currentTimeMillis();
		}
		if (expire < 0) {
			expire = 0;
		}
		return delete(key, expire);
	}
	/**
	 * 给定key，从cache中将其对应的对象删除，可以延时删除
	 * 
	 * 在进行延时删除时，比如设置的延迟删除时间是1s，那么在这1s内，对于add、replace和get操作是不会生效的，对于set操作是会生效的。
	 *
	 * @param key 要删除的key
	 * @param expiry 单位：毫秒，经过expiry毫秒之后删除key，如果为0或负值则立即删除
	 * @return <code>true</code>, 如果这个key被成功删除，或者key在cache中不存在
	 */
	public boolean delete( String key, long expiry ) {
		if (key == null || key.equals("")) {
			//logger.log(Level.WARNING, "delete for null key!!!\n");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("delete for null key!!!");
			return false;
		}
		if (expiry < 0) {
			expiry = 0;
		}
		/*try {
			key = sanitizeKey( key );
		} catch ( UnsupportedEncodingException e ) {
			StackTraceElement[] ste = e.getStackTrace();
			String ex_str = "" + e + "\n";
			for (int i=0;i<ste.length;i++) {
				ex_str +=  "		" + ste[i].toString() + "\n";
			}
			logger.log(Level.WARNING, "failed to sanitize your key!\n" + "StackTrace : {[(\n" + ex_str + "\n)]}");
			return false;
		}*/
		return cacheClient.delete(key, expiry);
	}
    
	/**
	 * 将key-value存储在cache中，如果key已经存在，则旧的value将会被当前value所替代
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean set( String key, Object value ) {
		return set( key, value, 0 );
	}
	
	/**
	 * 将key-value存储在cache中，如果key已经存在，则旧的value将会被当前value所替代
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @param expiry 有效期时间，计算方法：用expiry-当前毫秒数
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean set( String key, Object value, Date expiry ) {
		long expire = 0;
		if (expiry != null) {
			expire = expiry.getTime() - System.currentTimeMillis();
		}
		if (expire < 0) {
			expire = 0;
		}
		return set(key, value, expire);
	}
	/**
	 * 将key-value存储在cache中，如果key已经存在，则旧的value将会被当前value所替代
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @param expiry 有效期时间，单位：毫秒
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean set( String key, Object value, long expiry ) {
		if (key == null || key.equals("") || value == null || value.equals("")) {
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key and value can not be null!!!");
			return false;
		}
		long expire = expiry;
		if (expire < 0) {
			expire = 0;
		}
		return cacheClient.set(key, value, expire);
	}

	/**
	 * 将key-value存储在cache中，如果key已经存在，则返回false，新的value不会替换旧的value
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean add( String key, Object value ) {
		return add( key, value, 0 );
	}

	/**
	 * 将key-value存储在cache中，如果key已经存在，则返回false，新的value不会替换旧的value
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @param expiry 有效期时间，计算方法：expiry-当前毫秒数
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean add( String key, Object value, Date expiry ) {
		long expire = 0;
		if (expiry != null) {
			expire = expiry.getTime() - System.currentTimeMillis();
		}
		if (expire < 0) {
			expire = 0;
		}		
		return add(key, value, expire);
	}
	/**
	 * 将key-value存储在cache中，如果key已经存在，则返回false，新的value不会替换旧的value
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @param expiry 有效期时间，单位：毫秒
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean add( String key, Object value, long expiry ) {
		if (key == null || key.equals("") || value == null || value.equals("")) {
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key and value can not be null!!!");
			return false;
		}
		if (expiry < 0) {
			expiry = 0;
		}
		
		return cacheClient.add(key, value, expiry);
	}
	/**
	 * 更新cache中key所对应的value，如果key不存在，则返回false
	 *
	 * @param key 要更新的key
	 * @param value 新值
	 * @return true, 如果更新成功，否则返回false
	 */
	public boolean replace( String key, Object value ) {
		return replace( key, value, 0 );
	}
	/**
	 * 更新cache中key所对应的value，如果key不存在，则返回false
	 *
	 * @param key 要更新的key
	 * @param value 新值
	 * @param expiry 有效期时间，计算方法：expiry-当前毫秒数
	 * @return true, 如果更新成功，否则返回false
	 */
	public boolean replace( String key, Object value, Date expiry ) {
		long expire = 0;
		if (expiry != null) {
			expire = expiry.getTime() - System.currentTimeMillis();
		}
		if (expire < 0) {
			expire = 0;
		}
		return replace(key, value, expire);
	}
	/**
	 * 更新cache中key所对应的value，如果key不存在，则返回false
	 *
	 * @param key 要更新的key
	 * @param value 新值
	 * @param expiry 有效期时间，单位：毫秒
	 * @return true, 如果更新成功，否则返回false
	 */
	public boolean replace( String key, Object value, long expiry ) {
		if (key == null || key.equals("") || value == null || value.equals("")) {
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key and value can not be null!!!");
			return false;
		}
		
		if (expiry < 0) {
			expiry = 0;
		}
		return cacheClient.replace(key, value, expiry);
	}
	/** 
	 * 在cache中存储一个计数器，counter为其初值，如果key已经存在，则用新的counter替换旧的counter
	 * 
	 * @param key 存储的key
	 * @param counter 存储的数值
	 * @return true，如果存储成功，否则放回false
	 */
	public boolean storeCounter( String key, Long counter ) {
		if (key == null || key.equals("") || counter == null) {
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key and counter can not be null!!!");
			return false;
		}
		
		return cacheClient.set( key, counter, 0 );
	}
	/** 
	 * 根据给定的key从cache的计数器中返回相应的值，返回的类型为long 
	 *
	 * @param key 存储的key
	 * @return counter 计数器的值，如果cache中不存在此key，则返回-1
	 */
	public long getCounter( String key ) {
		if (key == null || key.equals("")) {
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key can not be null!!!");
			return -1;
		}
		
		long counter = -1;
		try {
			Object obj = cacheClient.get( key );
			if (obj instanceof Long) {
				counter = (Long) cacheClient.get( key );
			}
			if (counter < 0) {
				counter = -1;
			}
		} catch ( Exception ex ) {
			//logger.log(Level.WARNING, String.format( "getCounter failed!!! Failed to parse Long value for key: %s", key ) );
			cacheClient.setErrno(-2);
			cacheClient.setErrMsg(String.format( "getCounter failed!!! Failed to parse Long value for key: %s", key ));
		}		
		return counter;
	}

	/** 
	 *  增加一个计数器
	 * 
	 * @param key 存储的key
	 * @return value 返回计数器的值，如果不存在则将计数器加入cache后返回0
	 */
	public long addOrIncr( String key ) {
		return addOrIncr( key, 0 );
	}
	/** 
	 * 将计数器增加inc，如果不存在，则加入一个初值为inc的计数器 
	 * 
	 * @param key 存储的key
	 * @param inc 要增加的值或存储的值
	 * @return value 返回计数器的值
	 */
	public long addOrIncr( String key, long inc ) {
		if (key == null || key.equals("")) {
			//logger.log(Level.WARNING, "add or increment for null key!!!\n");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key can not be null!!!");
			return -1;
		}
		
		boolean ret = cacheClient.add( key, new Long( inc ), 0 );

		if ( ret ) {
			return inc;
		}
		else {
			return cacheClient.increment( key, inc );
		}
	}

	/** 
	 *  增加一个计数器
	 * 
	 * @param key 存储的key
	 * @return value 返回计数器的值，如果不存在则将计数器加入cache后返回0
	 */
	public long addOrDecr( String key ) {
		return addOrDecr( key, 0 );
	}
	/** 
	 * 将计数器减少inc，如果不存在，则加入一个初值为inc的计数器 
	 * 
	 * @param key 存储的key
	 * @param inc 要减少的值或存储的值
	 * @return value 返回计数器的值
	 */
	public long addOrDecr( String key, long inc ) {
		if (key == null || key.equals("")) {
			//logger.log(Level.WARNING, "decrement for null key!!!\n");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key can not be null!!!");
			return -1;
		}
		
		boolean ret = cacheClient.add( key, new Long( inc ), 0);

		if ( ret ) {
			return inc;
		}
		else {
			return cacheClient.decrement( key, inc );
		}
	}

	/**
	 * 如果key存在则加1，并返回；如果key对应的值不是数值类型(byte, short, int, long中的一种), 则将其看做0，加1后返回；如果key不存在则返回-1
	 *
	 * @param key 存储的key
	 * @return -1, 如果key不存在；如果key存在，则返回增加后的值
	 */
	public long incr( String key ) {
		return incr( key, 1 );
	}
	/**
	 * 如果key存在则加inc，并返回；如果key对应的值不是数值类型(byte, short, int, long中的一种), 则将其看做0，加inc后返回；如果key不存在则返回-1
	 *
	 * @param key 存储的key
	 * @param inc 要增加的值
	 * @return -1, 如果key不存在；如果key存在，则返回增加后的值
	 */
	public long incr( String key, long inc ) {
		if (key == null || key.equals("")) {
			//logger.log(Level.WARNING, "increment for null key!!!\n");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key can not be null!!!");
			return -1;
		}
		return cacheClient.increment( key, inc );
	}
	/**
	 * 如果key存在则减1；如果key对应的值不是数值类型(byte, short, int, long中的一种), 则返回0；如果key不存在则返回-1
	 * 只有当key不存在时decr才会返回-1；如果操作完之后的数值为负数则返回0
	 *
	 * @param key 存储的key
	 * @return -1, 如果key不存在；如果key存在，则返回减少后的值，如果减少后是负值，则返回0
	 */
	public long decr( String key ) {
		return decr( key, 1 );
	}
	/**
	 * 如果key存在则减inc；如果key对应的值不是数值类型(byte, short, int, long中的一种), 则返回0；如果key不存在则返回-1
	 * 只有当key不存在时decr才会返回-1；如果操作完之后的数值为负数则返回0
	 *
	 * @param key 存储的key
	 * @param inc 要减少的值
	 * @return -1, 如果key不存在；如果key存在，则返回减少后的值，如果减少后是负值，则返回0
	 */
	public long decr( String key, long inc ) {
		if (key == null || key.equals("")) {
			//logger.log(Level.WARNING, "increment for null key!!!\n");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key can not be null!!!");
			return -1;
		}
		return cacheClient.decrement( key, inc );
	}
	/**
	 * 从cache中取回key对应的值，如果key不存在则返回null。
	 * 如果存储的value是byte, short, int, long中一种，则返回long类型
	 *
	 * @param key 存储的key
	 * @return 之前存储的对象；若不存在则返回null
	 */
	public Object get( String key ) {
		if (key == null || key.equals("")) {
			//logger.log(Level.WARNING, "get for null key!!!\n");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key can not be null!!!");
			return null;
		}
		return cacheClient.get( key );
	}
	/** 
	 * 从cache中获取多个对象，一次最多64个
	 *
	 * @param keys 存储的keys
	 * @return 获取的对象，与传入的keys的顺序保持一致
	 */
	public Object[] getMultiArray( String[] keys ) {
		Map<String,Object> data = getMulti( keys );

		if ( data == null )
			return null;

		Object[] res = new Object[ keys.length ];
		for ( int i = 0; i < keys.length; i++ ) {
			res[i] = data.get( keys[i] );
		}

		return res;
	}
	/** 
	 * 从cache中获取多个对象，一次最多64个
	 *
	 * @param keys 存储的keys
	 * @return 返回HashMap<key,value>
	 */
	public Map<String,Object> getMulti( String[] keys ) {
		if (keys == null) {
			//logger.log(Level.WARNING, "getMulti for null keys!!!\n");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("key can not be null!!!");
			return null;
		}
		int len = keys.length;
		if (len == 0 || len > 64) {
			//logger.log(Level.WARNING, "getMulti error! the keys is 0 or large than max size 64!!!\n");
			cacheClient.setErrno(-3);
			cacheClient.setErrMsg("the keys num must >0 and <64");
			return null;
		}
		return cacheClient.getMulti( keys );
	}
	
	
	public int getErrno() {
		return cacheClient.getErrno();
	}
	public String getErrMsg() {
		return cacheClient.getErrMsg();
	}
	

	/*private String sanitizeKey( String key ) throws UnsupportedEncodingException {
		return ( sanitizeKeys ) ? URLEncoder.encode( key, ZcacheConf.defaultEncoding ) : key;
	}*/
	
	/*public static void main(String[] args) throws UnsupportedEncodingException {
		BaeMemcachedClient mc = new BaeMemcachedClient("DF6af6608b5bf9e81c5dca2cd849d89c", "06399db0c751ee2a0bef0e10bfd37932", 0, new String[]{"10.81.11.99:8243"});
		mc.storeCounter("meng_key", new Long(-1));
		System.out.println(mc.getCounter("meng_key"));
		String key = new String("啥中  s	ad文".getBytes(), "GBK");
		String value = "阿罗迪斯jfmengxiansen中文value";
		mc.delete(key);
		mc.add(key, value);
		System.out.println(mc.get(key));
	}*/
}
