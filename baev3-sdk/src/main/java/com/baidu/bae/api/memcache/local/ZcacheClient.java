package com.baidu.bae.api.memcache.local;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baidu.bae.api.memcache.internal.ZcacheConf;
import com.baidu.gson.JsonObject;
import com.baidu.mcpack.McpackException;

public class ZcacheClient {
	private static Logger logger = Logger.getLogger(ZcacheClient.class.getName());
	/**
	 * <key,<<value,object>,<time,nowtime>,<expiry,time(ms)>>
	 */
	private static HashMap<String, HashMap<String,Object>> cacheMap = new HashMap<String, HashMap<String,Object>>();
	
	private boolean isblock = false;
	
	private String[] hosts;
	private int timeout;
	
	protected ZcacheClient(String[] hosts, int timeout) {
		this.hosts = hosts;
		this.timeout = timeout;
		
	}
	
	
	
	public String[] getHosts() {
		return hosts;
	}
	public void setHosts(String[] hosts) {
		this.hosts = hosts;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	protected boolean isBaseType(Object value) {
		return (
				value instanceof Byte            ||
				value instanceof Integer         ||
				value instanceof Long            ||
				value instanceof Short
				)
			? true
			: false;
	}
	protected String byteToString(byte[] buff) {
		StringBuffer sb = new StringBuffer();
		sb.append(buff[0]);
		for (int i=1;i<buff.length;i++) {
			sb.append("," + buff[i]);
		}
		return sb.toString();
	}
	private boolean storeKeyValue(String key, Object value, long expiry) throws IOException {
		long time = System.currentTimeMillis();
		if (key.getBytes().length > ZcacheConf.KEY_MAX_LEN) {
			logger.log(Level.WARNING, "key length is larger than " +  ZcacheConf.KEY_MAX_LEN + " !!!");
			
			return false;
		}
		HashMap<String,Object> addValue = new HashMap<String,Object>();
		if (value != null) {
			if (isBaseType(value)) {
				addValue.put("value", value.toString());
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				(new ObjectOutputStream( bos )).writeObject( value );
				byte[] val = bos.toByteArray();
				String value_str = this.byteToString(val);
				if (value_str.getBytes().length > ZcacheConf.VALUE_MAX_LEN) {
					logger.log(Level.WARNING, "value length is larger than " +  ZcacheConf.VALUE_MAX_LEN + " !!!");
					return false;
				}
				
				addValue.put("value", value_str);
			}
		}
		addValue.put("time", time);
		addValue.put("expiry", expiry);
		addValue.put("isdel", false);
		cacheMap.put(key, addValue);
		return true;
	}
	private void checkKey(String key) {
		this.isblock = false;
		if (cacheMap.containsKey(key)) {
			
			HashMap<String,Object> tmp = cacheMap.get(key);
			long time = (Long) tmp.get("time");
			long expriy = (Long) tmp.get("expiry");
			boolean isdel = (Boolean) tmp.get("isdel");
			//是否有延迟策略
			if (expriy <= 0) {
				return;
			}
			//是否延迟删除
			if (isdel) {
				if (time + expriy <= System.currentTimeMillis()) {
					cacheMap.remove(key);
				} else {
					this.isblock = true;
				}
			} else {
				//是否存储到期
				if (time + expriy <= System.currentTimeMillis()) {
					cacheMap.remove(key);
				}
			}
		}
	}
	//{[({"err_no":0,"error":"ZCACHE_OK","content":{"result0":{"err_no":9,"error":"ZCACHE_ERR_FROZEN_DELETE"}}})]}
	protected JsonObject addOne(String pname, String token,String appid, long login, String key, Object value, long expiry) throws McpackException, IOException {
		checkKey(key);
		JsonObject json = new JsonObject();
		json.addProperty("err_no", 0);
		json.addProperty("error", "ZCACHE_OK");
		if (cacheMap.containsKey(key)) {
			JsonObject content = new JsonObject();
			JsonObject result0 = new JsonObject();
			if (this.isblock) {
				result0.addProperty("err_no", 9);
				result0.addProperty("error", "ZCACHE_ERR_FROZEN_DELETE");
			} else {
				result0.addProperty("err_no", 4);
				result0.addProperty("error", "ZCACHE_ERR_EXIST");
			}
			content.add("result0", result0);
			json.add("content", content);
		} else {
			if (this.storeKeyValue(key,value,expiry)) {
				JsonObject result0 = new JsonObject();
				result0.addProperty("err_no", 0);
				result0.addProperty("error", "ZCACHE_OK");
				JsonObject content = new JsonObject();
				content.add("result0", result0);
				json.add("content", content);
			} else {
				JsonObject result0 = new JsonObject();
				result0.addProperty("err_no", 101);
				result0.addProperty("error", "ZCACHE_SDK_ERROR");
				JsonObject content = new JsonObject();
				content.add("result0", result0);
				json.add("content", content);
			}
		}
		return json;
	}
	
	protected JsonObject setOne(String pname, String token,String appid, long login, String key, Object value, long expiry) throws McpackException, IOException {
		JsonObject json = new JsonObject();
		json.addProperty("err_no", 0);
		json.addProperty("error", "ZCACHE_OK");
		if (this.storeKeyValue(key,value,expiry)) {
			JsonObject result0 = new JsonObject();
			result0.addProperty("err_no", 0);
			result0.addProperty("error", "ZCACHE_OK");
			JsonObject content = new JsonObject();
			content.add("result0", result0);
			json.add("content", content);
		} else {
			JsonObject result0 = new JsonObject();
			result0.addProperty("err_no", 101);
			result0.addProperty("error", "ZCACHE_SDK_ERROR");
			JsonObject content = new JsonObject();
			content.add("result0", result0);
			json.add("content", content);
		}
		
		return json;
	}
	
	protected JsonObject replaceOne(String pname, String token,String appid, long login, String key, Object value, long expiry) throws McpackException, IOException {
		checkKey(key);
		JsonObject json = new JsonObject();	
		json.addProperty("err_no", 0);
		json.addProperty("error", "ZCACHE_OK");
		if (!cacheMap.containsKey(key)) {
			JsonObject result0 = new JsonObject();
			result0.addProperty("err_no", 5);
			result0.addProperty("error", "ZCACHE_ERR_NOT_EXIST");
			JsonObject content = new JsonObject();
			content.add("result0", result0);
			json.add("content", content);		
		} else {
			if (this.storeKeyValue(key,value,expiry)) {
				JsonObject result0 = new JsonObject();
				result0.addProperty("err_no", 0);
				result0.addProperty("error", "ZCACHE_OK");
				JsonObject content = new JsonObject();
				content.add("result0", result0);
				json.add("content", content);
			} else {
				JsonObject result0 = new JsonObject();
				result0.addProperty("err_no", 101);
				result0.addProperty("error", "ZCACHE_SDK_ERROR");
				JsonObject content = new JsonObject();
				content.add("result0", result0);
				json.add("content", content);
			}
		}
		return json;
	}
	//{[({"err_no":0,"error":"ZCACHE_OK","content":{"result0":{"err_no":9,"error":"ZCACHE_ERR_FROZEN_DELETE"}}})]}
	protected JsonObject deleteOne(String pname, String token,String appid, long login, String key, long expiry) throws McpackException, IOException {
		checkKey(key);
		JsonObject json = new JsonObject();
		json.addProperty("err_no", 0);
		json.addProperty("error", "ZCACHE_OK");
				
		if (cacheMap.containsKey(key)) {
			if (this.isblock) {
				JsonObject result0 = new JsonObject();
				JsonObject content = new JsonObject();
				result0.addProperty("err_no", 9);
				result0.addProperty("error", "ZCACHE_ERR_FROZEN_DELETE");
				content.add("result0", result0);
				json.add("content",content);				
			} else {
				if (expiry > 0) {
					HashMap<String, Object> tmp = cacheMap.get(key);
					tmp.put("expiry", expiry);
					tmp.put("isdel", true);
					tmp.put("time", System.currentTimeMillis());
					cacheMap.put(key, tmp);
				} else {
					cacheMap.remove(key);
				}
				JsonObject result0 = new JsonObject();
				JsonObject content = new JsonObject();
				result0.addProperty("err_no", 0);
				result0.addProperty("error", "ZCACHE_OK");
				content.add("result0", result0);
				json.add("content",content);
			}
		} else {
			JsonObject result0 = new JsonObject();
			result0.addProperty("err_no", 0);
			result0.addProperty("error", "ZCACHE_OK");
			JsonObject content = new JsonObject();
			content.add("result0", result0);
			json.add("content",content);
		}
		return json;
	}
	
	protected JsonObject getOne(String pname, String token,String appid, long login, String key) throws McpackException, IOException {
		checkKey(key);
		JsonObject json = new JsonObject();
		json.addProperty("err_no", 0);
		json.addProperty("error", "ZCACHE_OK");	
		if (cacheMap.containsKey(key)) {
			JsonObject content = new JsonObject();
			JsonObject result0 = new JsonObject();
			if (this.isblock) {
				result0.addProperty("err_no", 9);
				result0.addProperty("error", "ZCACHE_ERR_FROZEN_DELETE");
			} else {
				result0.addProperty("err_no", 0);
				result0.addProperty("error", "ZCACHE_OK");
				result0.addProperty("value", (String) cacheMap.get(key).get("value"));
			}
			content.add("result0", result0);
			json.add("content", content);
		} else {
			JsonObject content = new JsonObject();
			JsonObject result0 = new JsonObject();
			result0.addProperty("err_no", 5);
			result0.addProperty("error", "ZCACHE_ERR_NOT_EXIST");
			content.add("result0", result0);
			json.add("content", content);
		}
		
		return json;
	}
	
	protected JsonObject getMulti(String pname, String token,String appid, long login, Map<String,Object> keys) throws McpackException, IOException {
		JsonObject json = new JsonObject();
		json.addProperty("err_no", 0);
		json.addProperty("error", "ZCACHE_OK");	
		JsonObject content = new JsonObject();
		Iterator<String> iters = keys.keySet().iterator();
		int c = 0;
		while (iters.hasNext()) {
			String key = iters.next();
			JsonObject res = this.getOne(pname, token, appid, login, key);
			JsonObject result = res.getAsJsonObject("content").getAsJsonObject("result0");
			content.add("result" + c,result);
			c++;
		}
		json.add("content", content);
		return json;
	}	
	
}
