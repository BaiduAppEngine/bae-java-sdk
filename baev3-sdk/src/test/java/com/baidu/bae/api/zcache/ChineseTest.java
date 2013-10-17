package com.baidu.bae.api.zcache;

import static org.junit.Assert.*;

import org.junit.Test;

import com.baidu.bae.api.factory.BaeFactory;
import com.baidu.bae.api.memcache.BaeCache;
import com.baidu.bae.api.memcache.BaeMemcachedClient;

public class ChineseTest {

	private static String CACHEID = "yKtemTZfujgIBZpLZmhD";
	private static String MEMCACHEADDR = "10.237.21.15:20243";
	private static String USER = "EEwwBcHxjDBvqMuW8RGrsjO0";
	private static String PASSWORD = "zK2TSa9ALCiZ4L4AqThjFiDjtMNz0Gvw";
	
	//BaeMemcachedClient memcache = new BaeMemcachedClient(AK, SK, 0, new String[]{"10.237.4.20:8100"});
	BaeCache memcache = BaeFactory.getBaeCache(CACHEID, MEMCACHEADDR, USER, PASSWORD);
	
	
	@Test
	public void testChinses() {
		String key = "测试中文";
		String value = "中文的值";
		memcache.delete(key);
		memcache.add(key, value);
		assertEquals("check zhongwen add", value, memcache.get(key));
		value = "替换中文";
		memcache.replace(key, value);
		assertEquals("check zhongwen replace", value, memcache.get(key));
		value = "确实是中文";
		memcache.set(key, value);
		assertEquals("check zhongwen set", value, memcache.get(key));
	}
	@Test
	public void testSpecialChar() {
		String key = "中文    空格	tab@Y&*T&*!&(特殊符号";
		String value = "变态的key";
		memcache.delete(key);
		memcache.add(key, value);
		assertEquals("check space add", value, memcache.get(key));
		value = "替换中文";
		memcache.replace(key, value);
		assertEquals("check space replace", value, memcache.get(key));
		value = "确实是中文";
		memcache.set(key, value);
		assertEquals("check space set", value, memcache.get(key));
	}
	@Test
	public void testSpace() {
		String key = " ";
		String value = "变态的key";
		memcache.delete(key);
		memcache.add(key, value);
		assertEquals("check space add", value, memcache.get(key));
		value = "替换中文";
		memcache.replace(key, value);
		assertEquals("check space replace", value, memcache.get(key));
		value = "确实是中文";
		memcache.set(key, value);
		assertEquals("check space set", value, memcache.get(key));
	}
	@Test
	public void testTab() {
		String key = "	";
		String value = "变态的key";
		memcache.delete(key);
		memcache.add(key, value);
		assertEquals("check space add", value, memcache.get(key));
		value = "替换中文";
		memcache.replace(key, value);
		assertEquals("check space replace", value, memcache.get(key));
		value = "确实是中文";
		memcache.set(key, value);
		assertEquals("check space set", value, memcache.get(key));
	}
}
