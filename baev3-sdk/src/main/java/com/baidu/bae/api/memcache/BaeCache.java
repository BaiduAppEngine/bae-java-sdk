package com.baidu.bae.api.memcache;

import java.util.Date;
import java.util.Map;
/**
 * BaeMemcachedClient兼容了MemcacheClient的add、set、replace、get、delete等基本操作的接口，为开发者提供cache服务
 * 其中pname和token的大小不能超过64Byte
 * key的长度不能超过180Byte
 * value的长度不能超过1MB(在value被传输之前BaeMemcachedClient会对value进行一些处理，所以实际允许的value大小可能不到1MB)
 *
 */
public interface BaeCache {

	/**
	 * 获取cacheid
	 * @return cacheid	String
	 */
	public String getCacheId();
	/**
	 * 设置资源id
	 * @param cacheId	资源Id
	 */
	public void setCacheId(String cacheId);
	/**
	 * 获取cache服务地址
	 * @return cache服务地址和端口，例如cache.duapp.com:10240	String
	 */
	public String getMemcacheAddr();
	/**
	 * 设置cache服务器地址
	 * @param memcacheAddr cache服务器地址和端口，例如cache.duapp.com:10240
	 */
	public void setMemcacheAddr(String memcacheAddr);
	
	/**
	 * 获取user
	 * @return user
	 */
	public String getUser();
	/**
	 * 设置user
	 * @param user
	 */
	public void setUser(String user);
	/**
	 * 获取password
	 * @return password
	 */
	public String getPassword();
	/**
	 * 设置password
	 * @param password
	 */
	public void setPassword(String password);
	
	
	/**
	 * 获取连接超时时间，单位秒，默认5s，最小1s，最大10s
	 * @return connect_timeout
	 */
	public int getConnectTimeout();
	/**
	 * 获取连接超时时间，单位秒，默认5s，最小1s，最大10s
	 * @param connect_timeout
	 */
	public void setConnectTimeout(int connect_timeout);
	/** 
	 * 检查key是否在cache中存在 
	 * 
	 * @param key 要检测的key
	 * @return true 如果key在cache中， false 如果key不在cache中
	 */
	public boolean keyExists( String key );

	/**
	 * 给定key，从cache中将其对应的对象删除
	 *
	 * @param key 要删除的key
	 * @return <code>true</code>, 如果这个key被成功删除，或者key在cache中不存在
	 */
	public boolean delete( String key );
	
	/**
	 * 给定key，从cache中将其对应的对象删除，可以延时删除，此方法是用于兼容Memcached客户端的方法
	 * 
	 * 在进行延时删除时，比如设置的延迟删除时间是1s，那么在这1s内，对于add、replace和get操作是不会生效的，对于set操作是会生效的。
	 *
	 * @param key 要删除的key
	 * @param expiry 表示经过(expiry-当前时间)毫秒之后删除key，即在expiry的时间点删除key，如果小于或等于当前时间则立即删除
	 * @return <code>true</code>, 如果这个key被成功删除，或者key在cache中不存在
	 */
	public boolean delete( String key, Date expiry );
	/**
	 * 给定key，从cache中将其对应的对象删除，可以延时删除
	 * 
	 * 在进行延时删除时，比如设置的延迟删除时间是1s，那么在这1s内，对于add、replace和get操作是不会生效的，对于set操作是会生效的。
	 *
	 * @param key 要删除的key
	 * @param expiry 单位：毫秒，经过expiry毫秒之后删除key，如果为0或负值则立即删除
	 * @return <code>true</code>, 如果这个key被成功删除，或者key在cache中不存在
	 */
	public boolean delete( String key, long expiry );
    
	/**
	 * 将key-value存储在cache中，如果key已经存在，则旧的value将会被当前value所替代
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean set( String key, Object value );
	
	/**
	 * 将key-value存储在cache中，如果key已经存在，则旧的value将会被当前value所替代
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @param expiry 有效期时间，计算方法：用expiry-当前毫秒数
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean set( String key, Object value, Date expiry );
	/**
	 * 将key-value存储在cache中，如果key已经存在，则旧的value将会被当前value所替代
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @param expiry 有效期时间，单位：毫秒
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean set( String key, Object value, long expiry );

	/**
	 * 将key-value存储在cache中，如果key已经存在，则返回false，新的value不会替换旧的value
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean add( String key, Object value );

	/**
	 * 将key-value存储在cache中，如果key已经存在，则返回false，新的value不会替换旧的value
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @param expiry 有效期时间，计算方法：expiry-当前毫秒数
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean add( String key, Object value, Date expiry );
	/**
	 * 将key-value存储在cache中，如果key已经存在，则返回false，新的value不会替换旧的value
	 *
	 * @param key 存储的key
	 * @param value 存储的值
	 * @param expiry 有效期时间，单位：毫秒
	 * @return true, 如果存储成功，否则返回false
	 */
	public boolean add( String key, Object value, long expiry );
	/**
	 * 更新cache中key所对应的value，如果key不存在，则返回false
	 *
	 * @param key 要更新的key
	 * @param value 新值
	 * @return true, 如果更新成功，否则返回false
	 */
	public boolean replace( String key, Object value );
	/**
	 * 更新cache中key所对应的value，如果key不存在，则返回false
	 *
	 * @param key 要更新的key
	 * @param value 新值
	 * @param expiry 有效期时间，计算方法：expiry-当前毫秒数
	 * @return true, 如果更新成功，否则返回false
	 */
	public boolean replace( String key, Object value, Date expiry );
	/**
	 * 更新cache中key所对应的value，如果key不存在，则返回false
	 *
	 * @param key 要更新的key
	 * @param value 新值
	 * @param expiry 有效期时间，单位：毫秒
	 * @return true, 如果更新成功，否则返回false
	 */
	public boolean replace( String key, Object value, long expiry );
	/** 
	 * 在cache中存储一个计数器，counter为其初值，如果key已经存在，则用新的counter替换旧的counter
	 * 
	 * @param key 存储的key
	 * @param counter 存储的数值
	 * @return true，如果存储成功，否则放回false
	 */
	public boolean storeCounter( String key, Long counter );
	/** 
	 * 根据给定的key从cache的计数器中返回相应的值，返回的类型为long 
	 *
	 * @param key 存储的key
	 * @return counter 计数器的值，如果cache中不存在此key，则返回-1
	 */
	public long getCounter( String key );

	/** 
	 *  增加一个计数器
	 * 
	 * @param key 存储的key
	 * @return value 返回计数器的值，如果不存在则将计数器加入cache后返回0
	 */
	public long addOrIncr( String key );
	/** 
	 * 将计数器增加inc，如果不存在，则加入一个初值为inc的计数器 
	 * 
	 * @param key 存储的key
	 * @param inc 要增加的值或存储的值
	 * @return value 返回计数器的值
	 */
	public long addOrIncr( String key, long inc );

	/** 
	 *  增加一个计数器
	 * 
	 * @param key 存储的key
	 * @return value 返回计数器的值，如果不存在则将计数器加入cache后返回0
	 */
	public long addOrDecr( String key );
	/** 
	 * 将计数器减少inc，如果不存在，则加入一个初值为inc的计数器 
	 * 
	 * @param key 存储的key
	 * @param inc 要减少的值或存储的值
	 * @return value 返回计数器的值
	 */
	public long addOrDecr( String key, long inc );

	/**
	 * 如果key存在则加1，并返回；如果key对应的值不是数值类型(byte, short, int, long中的一种), 则将其看做0，加1后返回；如果key不存在则返回-1
	 *
	 * @param key 存储的key
	 * @return -1, 如果key不存在；如果key存在，则返回增加后的值
	 */
	public long incr( String key );
	/**
	 * 如果key存在则加inc，并返回；如果key对应的值不是数值类型(byte, short, int, long中的一种), 则将其看做0，加inc后返回；如果key不存在则返回-1
	 *
	 * @param key 存储的key
	 * @param inc 要增加的值
	 * @return -1, 如果key不存在；如果key存在，则返回增加后的值
	 */
	public long incr( String key, long inc );
	/**
	 * 如果key存在则减1；如果key对应的值不是数值类型(byte, short, int, long中的一种), 则返回0；如果key不存在则返回-1
	 * 只有当key不存在时decr才会返回-1；如果操作完之后的数值为负数则返回0
	 *
	 * @param key 存储的key
	 * @return -1, 如果key不存在；如果key存在，则返回减少后的值，如果减少后是负值，则返回0
	 */
	public long decr( String key );
	/**
	 * 如果key存在则减inc；如果key对应的值不是数值类型(byte, short, int, long中的一种), 则返回0；如果key不存在则返回-1
	 * 只有当key不存在时decr才会返回-1；如果操作完之后的数值为负数则返回0
	 *
	 * @param key 存储的key
	 * @param inc 要减少的值
	 * @return -1, 如果key不存在；如果key存在，则返回减少后的值，如果减少后是负值，则返回0
	 */
	public long decr( String key, long inc );
	/**
	 * 从cache中取回key对应的值，如果key不存在则返回null。
	 * 如果存储的value是byte, short, int, long中一种，则返回long类型
	 *
	 * @param key 存储的key
	 * @return 之前存储的对象；若不存在则返回null
	 */
	public Object get( String key );
	/** 
	 * 从cache中获取多个对象，一次最多64个
	 *
	 * @param keys 存储的keys
	 * @return 获取的对象，与传入的keys的顺序保持一致
	 */
	public Object[] getMultiArray( String[] keys );
	/** 
	 * 从cache中获取多个对象，一次最多64个
	 *
	 * @param keys 存储的keys
	 * @return 返回HashMap<key,value>
	 */
	public Map<String,Object> getMulti( String[] keys );
	
	/**
	 * 获取错误码
	 * @return 返回int型错误码
	 */
	public int getErrno();
	
	/**
	 * 获取错误信息
	 * @return 获取String型错误信息
	 */
	public String getErrMsg();
	

	
}
