package com.baidu.bae.api.memcache.local;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.baidu.gson.JsonObject;

public class MemcachedClient {
	private static final String ERR_NO = "err_no";
	//private Logger logger =Logger.getLogger(MemcachedClient.class.getName());
	private ZcacheClient zcacheClient;
	private String pname;
	private String token;
	private String appid;
	private long login;
	private String[] hosts;
	private McpackAndNShead man = new McpackAndNShead();
	private int timeout = 5;
	/*
	 * errno : -1 server error
	 *         -2 sdk exception
	 *         -3 param error
	 */
	private int errno = 0;
	private String errMsg = "ok";
	
	protected MemcachedClient(String pname, String token,String appid, long login, String[] hosts, int timeout) {
		this.pname = pname;
		this.token = token;
		this.appid = appid;
		this.login = login;
		this.hosts = hosts;
		this.timeout = timeout;
		zcacheClient = new ZcacheClient(this.hosts, this.timeout);
	}
	
	
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	
	

	public String getAppid() {
		return appid;
	}


	public void setAppid(String appid) {
		this.appid = appid;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getLogin() {
		return login;
	}

	public void setLogin(long login) {
		this.login = login;
	}

	public String[] getHosts() {
		return hosts;
	}

	public void setHosts(String[] hosts) {
		this.hosts = hosts;
		zcacheClient.setHosts(hosts);
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
		zcacheClient.setTimeout(timeout);
	}


	/**
	 * Adds data to the server; the key, value, and an expiration time are specified.
	 *
	 * @param key key to store data under
	 * @param value value to store
	 * @param expiry when to expire the record
	 * @return true, if the data was successfully stored
	 */
	protected boolean add(String key, Object value, long expiry) {		
		try {
			JsonObject jsonObj = zcacheClient.addOne(pname, token,appid, login, key, value, expiry);
			//logger.log(Level.FINE, "add response jsonObj = " + jsonObj + "\n");
			if (jsonObj == null) {
				//logger.log(Level.WARNING, "sever error! return null value!!!\n");
				this.errno = -1;
				this.errMsg = "sever error! return null value!!!";
				return false;
			}
			int err_no = 0;
			err_no = jsonObj.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				//logger.log(Level.WARNING, "add for key '" + key + "' error!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return false;
			}
			
			JsonObject content = jsonObj.get("content").getAsJsonObject();
			JsonObject result0 = content.get("result0").getAsJsonObject();
			err_no = 0;
			err_no = result0.get(ERR_NO).getAsInt();
			//logger.log(Level.FINE, "result0's err_no is " + err_no);
			if (err_no != 0) {
				if (err_no == 4) {
					//logger.log(Level.WARNING, "add for key '" + key + "' warning!\n The key is exist!!!");
					this.errno = err_no;
					this.errMsg = "The key is exist!!!";
					return false;
				}
				//logger.log(Level.WARNING, "add for key '" + key + "' warning!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return false;
			}
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String ex_str = "" + e + "\n";
			for (int i=0;i<ste.length;i++) {
				ex_str +=  "		" + ste[i].toString() + "\n";
			}
			//logger.log(Level.WARNING, "add for key '" + key + "' error!\n" + "StackTrace : {[(\n" + ex_str + "\n)]}");
			this.errno = -2;
			this.errMsg = ex_str;
			return false;
		}
		return true;
	}
	/**
	 * Stores data on the server; the key, value, and an expiration time are specified.
	 *
	 * @param key key to store data under
	 * @param value value to store
	 * @param expiry when to expire the record
	 * @return true, if the data was successfully stored
	 */
	protected boolean set(String key, Object value, long expiry) {
		try {
			JsonObject jsonObj = zcacheClient.setOne(pname, token,appid, login, key, value, expiry);
			//logger.log(Level.FINE, "set response jsonObj = " + jsonObj + "\n");
			int err_no = 0;
			err_no = jsonObj.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				//logger.log(Level.WARNING, "set for key '" + key + "' error!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return false;
			} 
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String ex_str = "" + e + "\n";
			for (int i=0;i<ste.length;i++) {
				ex_str +=  "		" + ste[i].toString() + "\n";
			}
			//logger.log(Level.WARNING, "set for key '" + key + "' error!\n" + "StackTrace : {[(\n" + ex_str + "\n)]}");
			this.errno = -2;
			this.errMsg = ex_str;
			return false;
		}
		return true;
	}
	/**
	 * Updates data on the server; the key, value, and an expiration time are specified.
	 *
	 * @param key key to store data under
	 * @param value value to store
	 * @param expiry when to expire the record
	 * @return true, if the data was successfully stored
	 */
	protected boolean replace(String key, Object value, long expiry) {
		try {
			JsonObject jsonObj = zcacheClient.replaceOne(pname, token,appid, login, key, value, expiry);
			//logger.log(Level.FINE, "replace response jsonObj = " + jsonObj + "\n");
			int err_no = 0;
			err_no = jsonObj.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				//logger.log(Level.WARNING, "replace for key '" + key + "' error!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return false;
			}
			JsonObject content = jsonObj.get("content").getAsJsonObject();
			JsonObject result0 = content.get("result0").getAsJsonObject();
			err_no = 0;
			err_no = result0.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				if (err_no == 5) {
					//logger.log(Level.WARNING, "replace for key '" + key + "' warning! The key is not exist!\n");
					this.errno = err_no;
					this.errMsg = "The key is not exist!";
					return false;
				}
				//logger.log(Level.WARNING, "replace for key '" + key + "' error!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return false;
			}
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String ex_str = "" + e + "\n";
			for (int i=0;i<ste.length;i++) {
				ex_str +=  "		" + ste[i].toString() + "\n";
			}
			//logger.log(Level.WARNING, "replace for key '" + key + "' error!\n" + "StackTrace : {[(\n" + ex_str + "\n)]}");
			this.errno = -2;
			this.errMsg = ex_str;
			return false;
		}
		return true;
	}
	/** 
	 * Deletes an object from cache given cache key and expiration date. 
	 * 
	 * @param key the key to be removed
	 * @param expiry when to expire the record.
	 * @return <code>true</code>, if the data was deleted successfully
	 */
	protected boolean delete(String key, long expiry) {
		try {
			JsonObject jsonObj = zcacheClient.deleteOne(pname, token,appid, login, key, expiry);
			//logger.log(Level.FINE, "delete key '" + key + "' : response : " + jsonObj + "\n");
			int err_no = 0;
			err_no = jsonObj.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				//logger.log(Level.WARNING, "delete for key '" + key + "' error!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return false;
			} 
			JsonObject content = jsonObj.get("content").getAsJsonObject();
			JsonObject result0 = content.get("result0").getAsJsonObject();
			err_no = 0;
			err_no = result0.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				if (err_no == 5) {
					//logger.log(Level.WARNING, "delete for key '" + key + "' warning! The key is not exist!\n");
					this.errno = err_no;
					this.errMsg = "The key is not exist!";
					return false;
				}
				//logger.log(Level.WARNING, "delete for key '" + key + "' error!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return false;
			}
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String ex_str = "" + e + "\n";
			for (int i=0;i<ste.length;i++) {
				ex_str +=  "		" + ste[i].toString() + "\n";
			}
			//logger.log(Level.WARNING, "delete for key '" + key + "' error!\n" + "StackTrace : {[(\n" + ex_str + "\n)]}");
			this.errno = -2;
			this.errMsg = ex_str;
			return false;
		}
		return true;
	}
	/** 
	 * Increment the value at the specified key by passed in inc. 
	 * 
	 * @param key key where the data is stored
	 * @param inc how much to increment by
	 * @return -1, if the key is not found, the value after incrementing otherwise
	 */
	protected long increment(String key, long inc) {
		long res = -1;
		Object value = this.get(key);
		if (value == null) {
			//logger.log(Level.WARNING, "increment for key '" + key + "' warning! The key is not exist!\n");
			this.errno = 5;
			this.errMsg = "The key is not exist!";
			return res;
		}
		if (value instanceof Long) {
			res = (Long)value + inc;
		} else {
			res = inc;
		}
		this.set(key, res, 0);
		return res;
	}
	/**
	 * Decrement the value at the specified key by passed in value, and then return it.
	 *
	 * @param key key where the data is stored
	 * @param inc how much to increment by
	 * @return -1, if the key is not found, the value after incrementing otherwise
	 */
	protected long decrement(String key, long dec) {
		long res = 0;
		Object value = this.get(key);
		if (value == null) {
			//logger.log(Level.WARNING, "decrement for key '" + key + "' warning! The key is not exist!\n");
			this.errno = 5;
			this.errMsg = "The key is not exist!";
			return -1;
		}
		if (value instanceof Long) {
			res = (Long)value - dec;
		}
		if (res < 0) {
			res = 0;
		}
		this.set(key, res, 0);
		return res;
	}
	/**
	 * Retrieve a key from the server.
	 *
	 *  If the data was compressed or serialized when compressed, it will automatically<br/>
	 *  be decompressed or serialized, as appropriate. (Inclusive or)<br/>
	 *<br/>
	 *  Non-serialized(includes byte,short,integer and long) data will be returned as a long<br/>
	 *
	 * @param key key where data is stored
	 * @return the object that was previously stored, or null if it was not previously stored
	 */
	@SuppressWarnings("resource")
	protected Object get(String key) {
		ClassLoadingObjectInputStream ois;
		Object obj = null;
		int err_no = 0;
		try {
			JsonObject jsonObj = zcacheClient.getOne(pname, token,appid, login, key);
			//logger.log(Level.FINE, "get jsonObj = " + jsonObj + "\n");
			err_no = jsonObj.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				//logger.log(Level.WARNING, "get for key '" + key +"' error!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return null;
			}
			
			JsonObject content = jsonObj.get("content").getAsJsonObject();
			JsonObject result = content.get("result0").getAsJsonObject();
			err_no = result.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				if (err_no == 5) {
					//logger.log(Level.WARNING, "get for key '" + key +"' warning! The key is not exist!\n" + "message : {[(" + jsonObj + ")]}\n");
					this.errno = err_no;
					this.errMsg = jsonObj.toString();
					return null;
				} else if (err_no == 9) {
					//logger.log(Level.WARNING, "get for key '" + key +"' warning! The key is in state of delay delete!\n" + "message : {[(" + jsonObj + ")]}\n");
					this.errno = err_no;
					this.errMsg = jsonObj.toString();
				}
				//logger.log(Level.WARNING, "get for key '" + key +"' error!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return null;
			}
			String value_str = result.get("value").toString();
			if (value_str.startsWith("\"") && value_str.endsWith("\"")) {
				value_str = value_str.substring(1, value_str.length() - 1);
			}
			try {				
				//logger.log(Level.FINE, "get value = " + value_str + "\n");
				byte[] value = man.stringToByte(value_str);
				ois = new ClassLoadingObjectInputStream( new ByteArrayInputStream( value ) );
				obj = ois.readObject();
				//logger.log(Level.FINE, "in get(), unserialization obj is " + obj.toString() + "\n");
			}  catch (Exception e) {
				obj = Long.parseLong(value_str);
				//logger.log(Level.FINE, "in get(), cannot unserialization obj, obj is " + obj.toString() + "\n");
			} 
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String ex_str = "" + e + "\n";
			for (int i=0;i<ste.length;i++) {
				ex_str +=  "		" + ste[i].toString() + "\n";
			}
			//logger.log(Level.WARNING, "get for key '" + key + "' error!\n" + "StackTrace : {[(\n" + ex_str + "\n)]}");
			this.errno = -2;
			this.errMsg = ex_str;
			return null;
		}		
		return obj;
	}

	
	/**
	 * Retrieve multiple objects from the cache.
	 *
	 * @param keys String array of keys to retrieve
	 * @return a hashmap with entries for each key is found by the server,
	 *      keys that are not found are not entered into the hashmap, but attempting to
	 *      retrieve them from the hashmap gives you null.
	 */	
	@SuppressWarnings("resource")
	protected Map<String,Object> getMulti(String[] keys) {
		int len = keys.length;
		Map<String,Object> query_keys = new HashMap<String,Object>();
		for (int i=0;i<len;i++) {
			if (keys[i] == null || keys[i].equals("")) {
				//logger.log(Level.WARNING, "gerMulti for null key in number " + i + " !!!\n");
				continue;
			}
			query_keys.put(keys[i],null);
		}
		if (query_keys.size() <=0) {
			//logger.log(Level.WARNING, "gerMulti for null keys!!!\n");
			this.errno = -3;
			this.errMsg = "gerMulti for null key!!!";
			return null;
		}
		Map<String,Object> res_map = new HashMap<String,Object>();
		try {
			JsonObject jsonObj = zcacheClient.getMulti(pname, token,appid, login, query_keys);
			int err_no = jsonObj.get(ERR_NO).getAsInt();
			if (err_no != 0) {
				//logger.log(Level.WARNING, "getMulti error!!!\n" + "message : {[(" + jsonObj + ")]}\n");
				this.errno = err_no;
				this.errMsg = jsonObj.toString();
				return null;
			}
			
			JsonObject content = jsonObj.get("content").getAsJsonObject();
			Iterator<String> iter = query_keys.keySet().iterator();
			int i=0;
			while (iter.hasNext()) {
				JsonObject result = content.get("result" + i).getAsJsonObject();
				err_no = 0;
				err_no = result.get(ERR_NO).getAsInt();
				if (err_no != 0) {
					//logger.log(Level.WARNING, "getMulti on " + i + " key '" + query_keys.get(i) + "' error!\n" + "message : {[(" + result + ")]}\n");
					iter.next();
					continue;
				}
				String value_str = result.get("value").toString();
				if (value_str.startsWith("\"") && value_str.endsWith("\"")) {
					value_str = value_str.substring(1, value_str.length() - 1);
				}
				
				Object o = null;
				
				try {
					byte[] value = man.stringToByte(value_str);
					ClassLoadingObjectInputStream ois = new ClassLoadingObjectInputStream( new ByteArrayInputStream( value ) );
					o = ois.readObject();
				} catch (Exception e) {
					o = Long.parseLong(value_str);
				}
				//logger.log(Level.FINE, "in getMulti() value = " + o.toString() + "\n");
				res_map.put(iter.next(), o);
				i++;
			}
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String ex_str = "" + e + "\n";
			for (int i=0;i<ste.length;i++) {
				ex_str +=  "		" + ste[i].toString() + "\n";
			}
			//logger.log(Level.WARNING, "getMulti error!\n" + "StackTrace : {[(\n" + ex_str + "\n)]}\n");
			this.errno = -2;
			this.errMsg = ex_str;
			return null;
		}
		return res_map;
	}
	protected void setErrno(int errno) {
		this.errno = errno;
	}
	protected void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public int getErrno() {
		return errno;
	}
	public String getErrMsg() {
		return errMsg;
	}
}
