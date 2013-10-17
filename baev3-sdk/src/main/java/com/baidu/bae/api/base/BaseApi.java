package com.baidu.bae.api.base;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.baidu.bae.api.util.Utils;
import com.baidu.bae.api.exception.BaeException;


public class BaseApi {

	
	private static Logger logger = Logger.getLogger(BaseApi.class
			.getName());
	
	protected static String TIMESTAMP = "timestamp";
	protected static String EXPIRES = "expires";
	protected static int EXPIRES_SECONDE = 10 * 60;
	protected static String METHOD = "method";
	protected static String SIGN = "sign";
	protected static String CLIENT_ID = "client_id";
	protected static String APP_ID = "app_id";
	
	protected String appId;
	protected String clientId;
	protected String clientSecret;
	protected String host;
	
	protected long requestId;
	
	
	
	/**
	 * 向指定的url发送数据，将结果以json对象返回
	 * @param postEncode
	 * @param url
	 * @return
	 */
	public  JSONObject postData(Map<String, Object> postEncode, String url) throws BaeException {
		requestId = 0;
		
		
		HttpResponse response;
		int statusCode = 0;
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost post = new HttpPost(url);
		genRequestHeader(post);
		

		StringBuilder postStr = new StringBuilder();
		for (String key : postEncode.keySet()) {
			String value = String.valueOf(postEncode.get(key));
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			postStr.append(key).append("=").append(value).append("&");
		}

			if (postStr.length() > 0)
				postStr.deleteCharAt(postStr.length() - 1);
			String resp = null;
			try {
				post.setEntity(new StringEntity(postStr.toString(), "UTF-8"));
				response = httpclient.execute(post);
				statusCode = response.getStatusLine().getStatusCode();

				resp = "";
				InputStream is = response.getEntity().getContent();
				int size = 0;
				byte[] bs = new byte[1024];
				while ((size = is.read(bs)) > 0) {
					resp += new String(bs, 0, size, "UTF-8");
				}
			} catch (Exception e1) {
				logger.log(Level.SEVERE, "java sdk error!", e1);
				throw new BaeException(BaeException.SDK_ERROR,e1);
			}
			if (200 == statusCode) {
				JSONObject json = null;
				try {
					json = Utils.toJson(resp);
					if(json.has("request_id")) {
						requestId = json.getLong("request_id");
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE, "http status is ok, but the body returned is not a json string!", e);
					throw new BaeException(BaeException.HTTP_OK_BUT_JSON__ERROR,e);
				}
				return json;
			}
			
			JSONObject json = null;
			try {
				json = Utils.toJson(resp);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "http status is error, and the body returned is not a json string!", e);
				throw new BaeException(BaeException.HTTP_ERROR_AND_JSON_ERROR,e);
			}
			String error_msg = json.getString("error_msg");
			int errno = json.getInt("error_code");
			if(json.has("request_id")) {
				requestId = json.getLong("request_id");
			}
			String msg =  "requestId is " + requestId + ", errno is " + errno + ", errmsg is " + error_msg;
			logger.log(Level.SEVERE,msg);
			throw new BaeException(errno,error_msg);
	}
	
	private static void genRequestHeader(HttpRequestBase requestBase) {
		requestBase.setHeader("User-Agent", "Baidu javasdk Client");
		requestBase.setHeader("Content-Type",
				"application/x-www-form-urlencoded");
	}

	public long getRequestId() {
		return requestId;
	}
	
	protected Map<String, Object> getParams(String method, String url,
			Map<String, Object> postData) {
		if (null == postData) {
			postData = new HashMap<String, Object>();
		}
		postData.put(METHOD, method);
		postData.put(APP_ID, this.appId);
		postData.put(CLIENT_ID, this.clientId);
		postData.put(TIMESTAMP, System.currentTimeMillis() / 1000);
		postData.put(EXPIRES, System.currentTimeMillis() / 1000
				+ EXPIRES_SECONDE);
		String sign = Utils.getSign("POST", url, postData, this.clientSecret);
		postData.put(SIGN, sign);
		
		return postData;
	}
	
	protected void checkInt(int dest,int min,int max) {
		if(dest >= min && dest <= max) {
			return;
		}
		throw new BaeException(BaeException.PARAM_ERROR, "param must between " + min + " and " + max);
	}
	
	
	
	
}
