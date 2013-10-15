package com.baidu.bae.api.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 工具类
 * 
 * @author chenkui
 * 
 */
public class Utils {

	/**
	 * 取得当前时间，格式为yyyy-MM-dd kk:mm:ss
	 * 
	 * @return 返回当前时间
	 */
	public static String getTimeStamp() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 取得签名值
	 * 
	 * @param type
	 *            http请求类型，post,get
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param clientSecret
	 *            sk值
	 * @return
	 */
	public static String getSign(String type, String url,
			Map<String, Object> params, String clientSecret) {
		StringBuilder content = new StringBuilder();
		content.append(type).append(url);
		Set<String> mapkey = params.keySet();
		String[] keys = new String[params.size()];
		int pos = 0;
		for (String k : mapkey) {
			keys[pos++] = k;
		}

		Arrays.sort(keys);
		for (String key : keys) {
			content.append(key).append("=").append(params.get(key));
		}
		content.append(clientSecret);
		String base = null;
		try {
			base = URLEncoder.encode(content.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return md5(base);
	}

	/**
	 * 将map转化为json串
	 * 
	 * @param map
	 * @return
	 */
	public static String toJsonString(Map<String, Object> map) {
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}

	public static String toJsonString(String[] arr) {
		JSONArray json = JSONArray.fromObject(arr);
		return json.toString();
	}

	/**
	 * 将json串转为JSONObject
	 * 
	 * @param str
	 *            json串
	 * @return
	 */
	public static JSONObject toJson(String str) {
		JSONObject json = JSONObject.fromObject(str);
		return json;
	}

	private static String md5(String plaintext) {

		byte[] source = null;
		try {
			source = plaintext.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
															// >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	@SuppressWarnings("restriction")
	public static byte[] base64Decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bt;
	}

}
