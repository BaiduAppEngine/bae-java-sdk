package com.baidu.bae.api.factory;

import com.baidu.bae.api.exception.BaeException;
import com.baidu.bae.api.image.BaeImageService;
import com.baidu.bae.api.image.internal.BaeImageServiceImpl;
import com.baidu.bae.api.log.schema.BaeLog;
import com.baidu.bae.api.memcache.BaeCache;
import com.baidu.bae.api.memcache.BaeMemcachedClient;
import com.baidu.bae.api.memcache.local.BaeMemcachedClientLocalImpl;

/**
 * 工厂类，获取bae各类服务的实例
 * 
 * @author mengxiansen
 * 
 */
public class BaeFactory {	
	/**
	 *  获取BaeImageService实例
	 * @param ak  管理平台上获取的ak
	 * @param sk  管理平台上获取的sk
	 * @param host  百度image服务API的域名，不包括http://
	 * @return  BaeImageService实例, 失败抛出BaeException异常
	 * @throws BaeException
	 */
	public static BaeImageService getBaeImageService(String ak,String sk,String host) throws BaeException {
		return new BaeImageServiceImpl(ak,sk,host);
	}
	
	public static BaeCache getBaeCache(String cacheId, String memcacheAddr, String user, String password) throws BaeException {		
		if (System.getProperty("baejavasdk.local") != null && System.getProperty("baejavasdk.local").equals("true") ) {
			return new BaeMemcachedClientLocalImpl(cacheId, memcacheAddr, user, password);
		} else {
			return new BaeMemcachedClient(cacheId, memcacheAddr, user, password);
		}
		
		
	}
	
	public static BaeLog getBaeLog() { 
		return new BaeLog();
	}
}
