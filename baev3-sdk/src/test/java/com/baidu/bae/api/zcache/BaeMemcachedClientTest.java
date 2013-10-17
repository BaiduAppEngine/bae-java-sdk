package com.baidu.bae.api.zcache;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.baidu.bae.api.factory.BaeFactory;
import com.baidu.bae.api.memcache.BaeCache;
import com.baidu.bae.api.memcache.BaeMemcachedClient;



public class BaeMemcachedClientTest {

	private static String CACHEID = "yKtemTZfujgIBZpLZmhD";
	private static String MEMCACHEADDR = "10.237.21.15:20243";
	private static String USER = "EEwwBcHxjDBvqMuW8RGrsjO0";
	private static String PASSWORD = "zK2TSa9ALCiZ4L4AqThjFiDjtMNz0Gvw";
	
	//BaeMemcachedClient memcache = new BaeMemcachedClient(AK, SK, 0, new String[]{"10.237.4.20:8100"});
	BaeCache memcache = BaeFactory.getBaeCache(CACHEID, MEMCACHEADDR, USER, PASSWORD);
	
	
	@Test
	public void testTimeout() {
		memcache.setConnectTimeout(0);
		assertEquals(5, memcache.getConnectTimeout());
		memcache.setConnectTimeout(12);
		assertEquals(5, memcache.getConnectTimeout());
		memcache.setConnectTimeout(3);
		assertEquals(3, memcache.getConnectTimeout());
	}
	@Test
	public void testAddDelay() throws InterruptedException {
		String key = "mengxiansen_test_key_add_delay";
		String value = "value";
		memcache.delete(key);
		long time = 1000;
		memcache.add(key, value, time);
		assertEquals(value, memcache.get(key));
		Thread.sleep(time);
		assertNull(memcache.get(key));	
		memcache.delete(key);
		
		memcache.add(key, value, -1);
		assertEquals(value, memcache.get(key));
		memcache.delete(key);
	}
	@Test
	public void testAddString() {		
		String key = "mengxiansen_test_key_string";
		String value = "test_add_srt";
		memcache.delete(key);
		assertTrue(memcache.add(key, value));
		assertEquals(value, (String) memcache.get(key));
		
		String value_exist = "test_add_exist_key_str";
		assertFalse(memcache.add(key, value_exist));
		assertEquals(value, (String) memcache.get(key));
		memcache.delete(key);
		
		String key_null = null;
		assertFalse(memcache.add(key_null,value));
		String value_null = null;
		assertFalse(memcache.add(key, value_null));
		
		String key_nu = "";
		assertFalse(memcache.add(key_nu,value));
		String value_nu = "";
		assertFalse(memcache.add(key, value_nu));
	}
	
	@Test
	public void testAddStringObjectDate() throws InterruptedException {		
		String key = "mengxiansen_test_key_string_date";
		Date date_null = null;
		memcache.delete(key);
		String value = "test_add_srt_date";
		assertTrue(memcache.add(key, value, date_null));
		assertEquals(value, (String) memcache.get(key));
		memcache.delete(key);
		
		Date date_negative = new Date(-1000);
		assertTrue(memcache.add(key, value, date_negative));
		assertEquals(value, (String) memcache.get(key));
		memcache.delete(key);
		
		Date date_1000 = new Date(System.currentTimeMillis() + 1000);
		assertTrue(memcache.add(key, value, date_1000));
		assertEquals(value, (String) memcache.get(key));
		Thread.sleep(1100);
		assertNull((String) memcache.get(key));
		memcache.delete(key);
	}
	
	@Test
	public void testAddByte() {
		String key = "mengxiansen_test_key_byte";
		memcache.delete(key);
		byte value = 127;
		assertTrue(memcache.add(key, value));
		assertEquals(new Long(value), memcache.get(key));
		
		byte value_exist = -81;
		assertFalse(memcache.add(key, value_exist));
		assertEquals(new Long(value), memcache.get(key));
		memcache.delete(key);
	}
	public void testAddShort() {
		String key = "mengxiansen_test_key_short";
		memcache.delete(key);
		short value = 10;
		assertTrue(memcache.add(key, value));
		assertEquals(new Long(value), memcache.get(key));
		
		short value_exist = -81;
		assertFalse(memcache.add(key, value_exist));
		assertEquals(new Long(value), memcache.get(key));
		memcache.delete(key);
	}
	
	@Test
	public void testAddBoolean() {
		String key = "mengxiansen_test_key_boolean";
		memcache.delete(key);
		boolean value = true;
		assertTrue(memcache.add(key, value));
		Boolean res = (Boolean) memcache.get(key);
		assertEquals(value, res);
		
		boolean value_exist = false;
		assertFalse(memcache.add(key, value_exist));
		assertEquals(value, (Boolean) memcache.get(key));
		memcache.delete(key);
	}
	@Test
	public void testAddCharacter() {
		String key = "mengxiansen_test_key_char";
		memcache.delete(key);
		char value = 'a';
		assertTrue(memcache.add(key, value));
		Character res = (Character) memcache.get(key);
		assertEquals((Character)value, res);
		
		char value_exist = 'E';
		assertFalse(memcache.add(key, value_exist));
		assertEquals((Character)value, (Character) memcache.get(key));
		memcache.delete(key);
	}

	@Test
	public void testAddInteger() {
		String key = "mengxiansen_test_key_int";
		memcache.delete(key);
		int value = 99;
		assertTrue(memcache.add(key, value));
		assertEquals(new Long(value), (Long) memcache.get(key));
		
		int value_exist = 1024;
		assertFalse(memcache.add(key, value_exist));
		assertEquals(new Long(value), (Long) memcache.get(key));
		memcache.delete(key);
	}

	@Test
	public void testAddFloat() {
		String key = "mengxiansen_test_key_float";
		memcache.delete(key);
		float value = 99.99F;
		assertTrue(memcache.add(key, value));
		Float res = (Float) memcache.get(key);
		assertEquals((Float)value, res);
		
		float value_exist = 10.24F;
		assertFalse(memcache.add(key, value_exist));
		assertEquals((Float)value, (Float) memcache.get(key));
		memcache.delete(key);
	}
	
	@Test
	public void testAddObject() {
		String key = "mengxiansen_test_key_object";
		memcache.delete(key);
		ObjectTest value = new ObjectTest();
		value.setB((byte)1);
		value.setBool(true);
		value.setC('C');
		value.setD(1.1);
		value.setF(1.3F);
		value.setI(2);
		value.setL(3);
		value.setS((short)4);
		value.setStr("object test string");
		ObjectTest.innerClass.in_int = 12;
		ObjectTest.innerClass.in_str = "inner Class string";
		
		assertTrue(memcache.add(key, value));
		ObjectTest res = (ObjectTest) memcache.get(key);
		
		assertEquals(value.getB(), res.getB());
		assertEquals(value.getC(), res.getC());
		assertEquals(new Double(value.getD()), new Double(res.getD()));
		assertEquals(new Float(value.getF()), new Float(res.getF()));
		assertEquals(value.getI(), res.getI());
		assertEquals(value.getL(), res.getL());
		assertEquals(value.getS(), res.getS());
		assertEquals(value.getStr(), res.getStr());
		assertEquals(value.getInnerInt(), res.getInnerInt());
		assertEquals(value.getInnerStr(), res.getInnerStr());
		
		ObjectTest value_exist = new ObjectTest();
		value_exist.setI(1099);
		assertFalse(memcache.add(key, value_exist));
		assertEquals(value.getI(), ((ObjectTest) memcache.get(key)).getI());
		memcache.delete(key);

	}

	@Test
	public void testKeyExists() {
		String key = "mengxiansen_test_key_string";
		memcache.delete(key);
		assertFalse(memcache.keyExists(key));
		
		String value = "test_add_srt";
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.keyExists(key));
		memcache.delete(key);
	}

	@Test
	public void testDeleteString() {
		assertFalse(memcache.delete(null));
		assertFalse(memcache.delete(""));
		
		String key = "mengxiansen_test_key_delete";
		String value = "test_del_srt";
		assertTrue(memcache.delete(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.delete(key));
	}

	@Test
	public void testDeleteStringDate() throws InterruptedException {		
		String key = "mengxiansen_test_key_delete_date";
		String value = "test_del_srt";
		
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.delete(key, null));
		assertNull(memcache.get(key));
		
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.delete(key, new Date(-1000)));
		assertNull(memcache.get(key));
		
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.delete(key, -1));
		assertNull(memcache.get(key));
		
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.delete(key, new Date(1000)));
		assertNull(memcache.get(key));
		Thread.sleep(1100);
		assertNull(memcache.get(key));
		
		String new_value = "new_value";
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.delete(key, new Date(System.currentTimeMillis() + 1000)));
		//Thread.sleep(2000);
		//assertNull(memcache.get(key));
		assertFalse(memcache.add(key, new_value));
		//assertNull(memcache.get(key));
		Thread.sleep(1100);
		assertNull(memcache.get(key));
		
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.delete(key, new Date(System.currentTimeMillis() + 9000)));
		//assertNull(memcache.get(key));
		assertTrue(memcache.replace(key, new_value));
		assertEquals(new_value, memcache.get(key));
		Thread.sleep(1100);
		assertEquals(new_value, memcache.get(key));

		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.delete(key, new Date(1000)));
		assertNull(memcache.get(key));
		assertTrue(memcache.set(key, new_value));
		assertEquals(new_value, memcache.get(key));
		Thread.sleep(1100);
		assertEquals(new_value, memcache.get(key));
	}

	@Test
	public void testSetStringObject() {
		assertFalse(memcache.set(null, "abc"));
		assertFalse(memcache.set("", "abc"));
		assertFalse(memcache.set("set_key", null));
		assertFalse(memcache.set("set_key", ""));
				
		String key = "mengxiansen_test_key_set";
		String value = "test_set_srt";
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		value = "test_exist_key_set";
		assertTrue(memcache.set(key, value));
		assertEquals(value, (String) memcache.get(key));
		
		key = "set_not_exist_key";
		value = "set_not_exist_value";
		memcache.delete(key);
		assertTrue(memcache.set(key, value));
		assertEquals(value, (String) memcache.get(key));
		memcache.delete(key);
	}

	@Test
	public void testSetStringObjectDate() throws InterruptedException {
		String key = "mengxiansen_test_key_set_date";
		String value = "test_set_srt";
		
		Date date_null = null;
		assertTrue(memcache.set(key, value, date_null));
		assertEquals(value, memcache.get(key));
		
		value = "test_set_date";
		assertTrue(memcache.set(key, value, new Date(-1000)));
		assertEquals(value, memcache.get(key));
		
		value = "test_set_long";
		assertTrue(memcache.set(key, value, -1));
		assertEquals(value, memcache.get(key));
		
		String value_1000 = "test_set_1000";
		assertTrue(memcache.set(key, value_1000, new Date(System.currentTimeMillis() + 1000)));
		assertEquals(value_1000, memcache.get(key));
		
		Thread.sleep(1100);
		assertNull(memcache.get(key));
		memcache.delete(key);
		
		assertTrue(memcache.add(key, value));
		assertTrue(memcache.set(key, value_1000, new Date(System.currentTimeMillis() + 1000)));
		assertEquals(value_1000, memcache.get(key));
		Thread.sleep(1100);
		assertNull(memcache.get(key));
		memcache.delete(key);
	}
	
	@Test
	public void testReplaceStringObject() {
		assertFalse(memcache.replace(null, "abc"));
		assertFalse(memcache.replace("", "abc"));
		assertFalse(memcache.replace("set_key", null));
		assertFalse(memcache.replace("set_key", ""));
		
		assertFalse(memcache.replace("set_key", "abc"));
				
		String key = "mengxiansen_test_key_replace";
		String value = "test_replace_srt";
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		String new_value = "test_exist_key_replace";
		assertTrue(memcache.replace(key, new_value));
		assertEquals(new_value, (String) memcache.get(key));
		assertTrue(memcache.delete(key));
	}

	@Test
	public void testReplaceStringObjectDate() throws InterruptedException {
		String key = "mengxiansen_test_key_replace_date";
		String value = "test_replace_srt";
		
		memcache.delete(key);
		assertTrue(memcache.add(key, value));
		String value_new = "test_replace_new_value";
		assertTrue(memcache.replace(key, value_new));
		assertEquals(value_new, memcache.get(key));
		
		String value_date = "test_replace_date";
		assertTrue(memcache.replace(key, value_date, new Date(-1000)));
		assertEquals(value_date, memcache.get(key));
		
		value_date = "test_replace_long";
		assertTrue(memcache.replace(key, value_date, -1));
		assertEquals(value_date, memcache.get(key));
		
		String value_1000 = "test_replace_1000";
		assertTrue(memcache.replace(key, value_1000, new Date(System.currentTimeMillis() + 1000)));
		assertEquals(value_1000, memcache.get(key));
		
		Thread.sleep(1100);
		assertNull(memcache.get(key));
		memcache.delete(key);
	}

	@Test
	public void testGet() {
		String key = "mengxiansen_test_key_get";
		String value = "test_get_srt";
		assertNull(memcache.get(null));
		assertNull(memcache.get(""));
		
		assertTrue(memcache.delete(key));
		assertNull(memcache.get(key));
		
		assertTrue(memcache.add(key, value));
		assertEquals(value, memcache.get(key));
		assertTrue(memcache.delete(key));
		assertNull(memcache.get(key));
	}

	@Test
	public void testStoreCounterStringLong() {
		String key = "mengxiansen_test_key_storeCounter";
		long counter = 99;
		
		assertFalse(memcache.storeCounter(null, counter));
		assertFalse(memcache.storeCounter("", counter));
		assertFalse(memcache.storeCounter(key, null));
		
		long counter_negative = -38;
		Long res_zero = new Long(0);
		assertTrue(memcache.delete(key));
		assertTrue(memcache.storeCounter(key, counter_negative));
		assertEquals((Long)counter_negative, (Long)memcache.get(key));
		assertTrue(memcache.delete(key));
		
		long counter_zero = 0;
		assertTrue(memcache.storeCounter(key, counter_zero));
		assertEquals(res_zero, (Long)memcache.get(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.storeCounter(key, counter));
		assertEquals((Long)counter, (Long)memcache.get(key));
		assertTrue(memcache.delete(key));
	}

	@Test
	public void testGetCounter() {
		String key_str = "mengxiansen_test_key_getCounter_str";
		String value_str = "not_long_type";
		
		String key_long_negative = "mengxiansen_test_key_getCounter_long_negative";
		long value_long_negative = -30;
		
		String key_long_zero = "mengxiansen_test_key_getCounter_long_zero";
		long value_long_zero = 0;
		
		String key_long = "mengxiansen_test_key_getCounter_long";
		long value_long = 10;		
		
		assertEquals(-1L, memcache.getCounter(null));
		assertEquals(-1L, memcache.getCounter(""));
		
		assertTrue(memcache.set(key_str, value_str));
		assertEquals(-1L, memcache.getCounter(key_str));
		
		assertTrue(memcache.set(key_long_negative, value_long_negative));
		assertEquals(-1L, memcache.getCounter(key_long_negative));
		
		assertTrue(memcache.set(key_long_zero, value_long_zero));
		assertEquals(value_long_zero, memcache.getCounter(key_long_zero));
		
		assertTrue(memcache.set(key_long, value_long));
		assertEquals(value_long, memcache.getCounter(key_long));
		assertTrue(memcache.delete(key_long));
	}
	
	@Test
	public void testAddOrIncrString() {
		String key = "mengxiansen_test_key_addOrIncr";
		long value = 10;
		
		assertEquals(-1L, memcache.addOrIncr(null));
		assertEquals(-1L, memcache.addOrIncr(""));
		
		assertTrue(memcache.delete(key));
		for (int i=0;i<11;i++) {
			assertEquals(0L, memcache.addOrIncr(key));
		}
		
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		assertEquals(value, memcache.addOrIncr(key));
		assertTrue(memcache.delete(key));
	}


	@Test
	public void testAddOrIncrStringLong() {
		String key = "mengxiansen_test_key_addOrIncr_twoParameter";
		
		assertEquals(-1L, memcache.addOrIncr(null, 3));
		assertEquals(-1L, memcache.addOrIncr("", 3));
		
		assertTrue(memcache.delete(key));
		assertEquals(3L, memcache.addOrIncr(key, 3));
		assertEquals(16L, memcache.addOrIncr(key, 13));
		assertEquals(16L, memcache.addOrIncr(key, 0));
		assertEquals(6L, memcache.addOrIncr(key, -10));		
		assertTrue(memcache.delete(key));
	}
	@Test
	public void testAddOrDecrString() {
		String key = "mengxiansen_test_key_addOrDecr";
		long value = 10;
		
		assertEquals(-1L, memcache.addOrDecr(null));
		assertEquals(-1L, memcache.addOrDecr(""));
		
		assertTrue(memcache.delete(key));
		for (int i=0;i<11;i++) {
			assertEquals(0L, memcache.addOrDecr(key));
		}
		
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		assertEquals(value, memcache.addOrDecr(key));
		assertTrue(memcache.delete(key));
	}

	@Test
	public void testAddOrDecrStringLong() {
		String key = "mengxiansen_test_key_addOrDecr_twoParameter";
		
		assertEquals(-1L, memcache.addOrDecr(null, 3));
		assertEquals(-1L, memcache.addOrDecr("", 3));
		
		assertTrue(memcache.delete(key));
		assertEquals(16L, memcache.addOrDecr(key, 16));
		assertEquals(3L, memcache.addOrDecr(key, 13));
		assertEquals(3L, memcache.addOrDecr(key, 0));
		assertEquals(0L, memcache.addOrDecr(key, 6));		
		assertTrue(memcache.delete(key));
		
		assertEquals(16L, memcache.addOrDecr(key, 16));
		assertEquals(26L, memcache.addOrDecr(key, -10));
		assertTrue(memcache.delete(key));
	}

	@Test
	public void testIncrString() {
		assertEquals(-1L, memcache.incr(null));
		assertEquals(-1L, memcache.incr(""));
		
		String key = "mengxiansen_test_increment_key";
		long value = 10;
		assertTrue(memcache.delete(key));
		assertEquals(-1L, memcache.incr(key));
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		assertEquals(11L, memcache.incr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, "test_incr_value"));
		assertEquals(1L, memcache.incr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, "99"));
		assertEquals(1L, memcache.incr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, -88));
		assertEquals(-87L, memcache.incr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, 1.5));
		assertEquals(1L, memcache.incr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, -1));
		assertEquals(0L, memcache.incr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, 0));
		assertEquals(1L, memcache.incr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, 1));
		assertEquals(2L, memcache.incr(key));
		assertTrue(memcache.delete(key));
	}
	

	@Test
	public void testIncrStringLong() {
		String key = "mengxiansen_test_increment_key";
		long value = 10;
		assertTrue(memcache.delete(key));
		assertEquals(-1L, memcache.incr(key, 3));
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		assertEquals(13L, memcache.incr(key, 3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, "test_incr_value"));
		assertEquals(3L, memcache.incr(key, 3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, "99"));
		assertEquals(3L, memcache.incr(key, 3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, -88));
		assertEquals(-85L, memcache.incr(key, 3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, 1.5));
		assertEquals(3L, memcache.incr(key, 3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, value));
		assertEquals(10L, memcache.incr(key, 0));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, value));
		assertEquals(9L, memcache.incr(key, -1));
		assertTrue(memcache.delete(key));
	}

	@Test
	public void testDecrString() {
		assertEquals(-1L, memcache.decr(null));
		assertEquals(-1L, memcache.decr(""));
		
		String key = "mengxiansen_test_decrement_key";
		long value = 10;
		assertTrue(memcache.delete(key));
		assertEquals(-1L, memcache.decr(key));
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		assertEquals(9L, memcache.decr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, 0));
		assertEquals(0L, memcache.decr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, -88));
		assertEquals(0L, memcache.decr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, "test_decr_value"));
		assertEquals(0L, memcache.decr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, "99"));
		assertEquals(0L, memcache.decr(key));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, 1.5));
		memcache.get(key);
		assertEquals(0L, memcache.decr(key));
		memcache.get(key);
		assertTrue(memcache.delete(key));
	}

	@Test
	public void testDecrStringLong() {
		String key = "mengxiansen_test_increment_key";
		long value = 10;
		assertTrue(memcache.delete(key));
		assertEquals(-1L, memcache.decr(key, 3));
		assertTrue(memcache.delete(key));
		assertTrue(memcache.add(key, value));
		assertEquals(7L, memcache.decr(key, 3));
		assertEquals(10L, memcache.decr(key, -3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, "test_incr_value"));
		assertEquals(0L, memcache.decr(key, 3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, "99"));
		assertEquals(0L, memcache.decr(key, 3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, -88));
		assertEquals(0L, memcache.decr(key, 3));
		assertTrue(memcache.delete(key));
		
		assertTrue(memcache.add(key, 1.5));
		assertEquals(0L, memcache.decr(key, 3));
		assertTrue(memcache.delete(key));
	}
	
	@Test
	public void testGetMultiArray() {
		String keys[] = new String[64];
		String values[] = new String[64];
		
		assertNull(memcache.getMultiArray(null));
		assertNull(memcache.getMultiArray(keys));
		
		int N = 5;
		for (int i=0;i<N;i++) {
			keys[i] = "mengxiansen_test_getMutilArray_key" + i;
			values[i] = "value" + i;
			assertTrue(memcache.set(keys[i], values[i]));
			assertEquals(values[i], memcache.get(keys[i]));
		}
		
		keys[N] = null;
		keys[N+1] = "";
		values[N] = null;
		values[N+1] = null;
		Object res[] = memcache.getMultiArray(keys);
		
		for (int i=0;i<N+2;i++) {
			assertEquals(values[i], res[i]);
		}
		for (int i=0;i<N+2;i++) {
			memcache.delete(keys[i]);
		}
		
		String large_keys[] = new String[100];
		int M = 65;
		for (int i=0;i<M;i++) {
			large_keys[i] = "mengxiansen_test_getMutilArray_key" + i;
		}
		assertNull(memcache.getMultiArray(large_keys));		
	}

	@Test
	public void testGetMulti() {
		String keys[] = new String[64];
		String values[] = new String[64];
		
		assertNull(memcache.getMulti(null));
		assertNull(memcache.getMulti(keys));
		
		int N = 62;
		for (int i=0;i<N;i++) {
			keys[i] = "mengxiansen_test_getMutil_key" + i;
			values[i] = "value" + i;
			System.out.println();
			System.out.println("**********************************");
			long b1 = System.currentTimeMillis();
			memcache.set(keys[i], values[i]);
			long b2 = System.currentTimeMillis();
			System.out.println("          " + i + ". set once time is " + (b2-b1) + "ms");
			System.out.println("**********************************");
			System.out.println();
		}
		
		keys[N] = null;
		keys[N+1] = "";
		values[N] = null;
		values[N+1] = null;		
		Map<String,Object> res = memcache.getMulti(keys);
		
		for (int i=0;i<N+2;i++) {
			assertEquals(values[i], res.get(keys[i]));
		}
		for (int i=0;i<N+2;i++) {
			memcache.delete(keys[i]);
		}
		
		String large_keys[] = new String[100];
		int M = 65;
		for (int i=0;i<M;i++) {
			large_keys[i] = "mengxiansen_test_getMutil_key" + i;
		}
		assertNull(memcache.getMulti(large_keys));	
	}
	
}
