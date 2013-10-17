package com.baidu.bae.api.memcache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baidu.bae.api.memcache.internal.NsHead;
import com.baidu.bae.api.memcache.internal.ZcacheConf;
import com.baidu.gson.JsonElement;
import com.baidu.gson.JsonObject;
import com.baidu.mcpack.Mcpack;
import com.baidu.mcpack.McpackException;

public class McpackAndNShead {
	
	private static Mcpack mcpack = new Mcpack();
	private static Logger logger = Logger.getLogger(McpackAndNShead.class.getName());
	
	protected byte[] makePack(String pname, String token,String appid, long logid, String cmd, long expire, Map<String,Object> query_map) throws McpackException, IOException {
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("cmd", cmd);
		jsonObj.addProperty("pname", pname);
		jsonObj.addProperty("token", token);
		jsonObj.addProperty("logid", logid);
		if(null != appid && appid.trim().length()>0) {
			jsonObj.addProperty("appid", appid);
		}
		
		JsonObject content = new JsonObject();
		int query_num = query_map.size();
		content.addProperty("query_num", query_num);
		
		Iterator<String> iter = query_map.keySet().iterator();
		int num = 0;
		while (iter.hasNext()) {
			JsonObject query = new JsonObject();
			String key = iter.next();
			Object value = query_map.get(key);
			if (key.getBytes().length > ZcacheConf.KEY_MAX_LEN) {
				logger.log(Level.WARNING, "key length is larger than " +  ZcacheConf.KEY_MAX_LEN + " !!!");
				return null;
			}
			query.addProperty("key", key);
			if (value != null) {
				if (isBaseType(value)) {
					query.addProperty("value", value.toString());
				} else {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					(new ObjectOutputStream( bos )).writeObject( value );
					byte[] val = bos.toByteArray();
					String value_str = this.byteToString(val);
					if (value_str.getBytes().length > ZcacheConf.VALUE_MAX_LEN) {
						logger.log(Level.WARNING, "value length is larger than " +  ZcacheConf.VALUE_MAX_LEN + " !!!");
						return null;
					}
					query.addProperty("value", value_str);
				}
			}
			if (expire >= 0) {
				query.addProperty("delay_time", expire);
			}
			
			content.add("query" + (num++), query);
		}
		jsonObj.add("content", content);
		//System.out.println("request json is " + jsonObj);
		byte[] buffer = mcpack.toMcpack(ZcacheConf.defaultEncoding, jsonObj);
		
		NsHead nshead = new NsHead();
    	nshead.addContent(buffer);
    	
		return nshead.toByteArray();
	}
	
	protected JsonObject byteToJson(byte[] buff) throws McpackException {
		if (buff == null) {
			return null;
		}
		JsonElement jsonEle = mcpack.toJsonElement(ZcacheConf.defaultEncoding, buff);
        return jsonEle.getAsJsonObject();
	}

	protected byte[] makePackManager(String pname, String token, long logid, String cmd, Map<String,Long> userinfo) throws McpackException {
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("cmd", cmd);
		jsonObj.addProperty("pname", pname);
		jsonObj.addProperty("token", token);
		jsonObj.addProperty("logid", logid);
		if (userinfo != null) {
			Iterator<String> iter = userinfo.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				jsonObj.addProperty(key, userinfo.get(key));
			}
		}
		//System.out.println("request json is " + jsonObj);
		byte[] buffer = mcpack.toMcpack(ZcacheConf.defaultEncoding, jsonObj);
		
		NsHead nshead = new NsHead();
    	nshead.addContent(buffer);
    	
		return nshead.toByteArray();
	}
	protected String byteToString(byte[] buff) {
		StringBuffer sb = new StringBuffer();
		sb.append(buff[0]);
		for (int i=1;i<buff.length;i++) {
			sb.append("," + buff[i]);
		}
		return sb.toString();
	}
	protected byte[] stringToByte(String str) {
		String[] strs = str.split(",");
		byte[] buff = new byte[strs.length];
		for (int i=0;i<strs.length;i++) {
			buff[i] = Byte.parseByte(strs[i]);
		}
		return buff;
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
/*	public static void main(String[] agrs) {
		String str = "-84,-19,0,5,116,0,8,116,101,115,116,95,115,114,116";
		System.out.println(str);
		byte[] b = stringToByte(str);
		System.out.print(b[0]);
		for (int i=1;i<b.length;i++) {
			System.out.print("," + b[i]);
		}
	}*/
}
