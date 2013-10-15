package com.baidu.bae.api.zcache;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.baidu.bae.api.factory.BaeFactory;
import com.baidu.bae.api.memcache.BaeCache;
import com.baidu.bae.api.memcache.BaeMemcachedClient;

public class LargeNumTest {

	private static String CACHEID = "yKtemTZfujgIBZpLZmhD";
	private static String MEMCACHEADDR = "10.237.21.15:20243";
	private static String USER = "EEwwBcHxjDBvqMuW8RGrsjO0";
	private static String PASSWORD = "zK2TSa9ALCiZ4L4AqThjFiDjtMNz0Gvw";
	
	//BaeMemcachedClient memcache = new BaeMemcachedClient(AK, SK, 0, new String[]{"10.237.4.20:8100"});
	BaeCache memcache = BaeFactory.getBaeCache(CACHEID, MEMCACHEADDR, USER, PASSWORD);
	
	
	@Test
	public void largeNumSetGet() {
		String key = "mxs_test_long_key";
		long value = 1234567890;
		memcache.set(key, value);
		assertEquals("check long num set get", value, memcache.get(key));
		String[] keys = new String[]{key};
		Map map = memcache.getMulti(keys);
		assertEquals("check getMulti", value, map.get(key));
		assertEquals("check getMultiArray", value, memcache.getMultiArray(keys)[0]);
	}
	@Test
	public void largeDoubleNumSetGet() {
		String key = "mxs_test_long_key";
		double value = 1234567890.0123456789;
		memcache.set(key, value);
		assertEquals("check long num set get", value, memcache.get(key));
		String[] keys = new String[]{key};
		Map map = memcache.getMulti(keys);
		assertEquals("check getMulti", value, map.get(key));
		assertEquals("check getMultiArray", value, memcache.getMultiArray(keys)[0]);
	}
}
