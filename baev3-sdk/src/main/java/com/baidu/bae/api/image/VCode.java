package com.baidu.bae.api.image;

import java.util.HashMap;
import java.util.Map;

import com.baidu.bae.api.exception.BaeException;
/**
 * 验证码功能类<br/>
 * 可自定义验证码的长度,干扰程度
 *
 */
public class VCode {

	//private int len;
	//private int pattern;
	private String secret;
	private String input;
	private Map<String, Object> data;
	
	/**
	 * 获取VCode实例
	 * 
	 */
	public VCode(){
		this(4, 0);
	}
	
	/**
	 * 获取VCode实例
	 * @param len 验证码的位数,<b>范围:</b>4-5
	 * @param pattern 验证码干扰程度,<b>范围:</b>0-3
	 */
	public VCode(int len, int pattern) {
		//this.len = len;
		//this.pattern = pattern;
		this.data = new HashMap<String,Object>();
		this.setLen(len);
		this.setPattern(pattern);
	}
	
	/**
	 * 获取验证码操作参数
	 * @return 返回验证码操作的参数
	 */
	public Map<String, Object> getOperations() {
		return this.data;
	}
	
	/**
	 * 设置验证码的位数
	 * @param len 验证码的位数,<b>范围:</b>4-5
	 */
	public void setLen(int len) {
		this.checkInt(len, "len", 4, 5);
		this.data.put("len", len);

	}
	
	/**
	 * 设置验证码的干扰程度
	 * @param pattern 验证码干扰程度,<b>范围:</b>0-3
	 */
	public void setPattern(int pattern) {
		this.checkInt(pattern, "pattern", 0, 3);
		this.data.put("setno", pattern);

	}

	/**
	 * 设置验证码的输入
	 * @param input 输入信息,<b>范围:</b>4-5
	 */
	public void setInput(String input) {
		this.checkString(input, "input", 4, 5);
		this.data.put("input", input);

	}
	
	/**
	 * 获取输入
	 * @return 返回用户输入
	 */
	public String getInput() {
		return this.input;
	}

	/**
	 * 设置验证码的密文
	 * @param secret 密文信息
	 */
	public void setSecret(String secret) {
		checkString(secret, "secret", 368, 368);
		this.data.put("vcode", secret);

	}

	/**
	 * 获取验证码密文
	 * @return 返回验证码密文信息
	 */
	public String getSecret() {
		return this.secret;
	}
	
	/**
	 * 清除操作
	 */
	public void clearOperations() {
		this.data.clear();

	}
	
	private void checkInt(int dest, String prompt, int min, int max)
	{
		if(dest < min || dest > max) {
			throw new BaeException(BaeException.PARAM_ERROR, "["
					+ prompt + "]" + " must between " + min + " and " + max);
		}
		
	}
	
	private void checkString(String str,String prompt, int min, int max)
	{
		if(str.length() < min || str.length() > max){
			throw new BaeException(BaeException.PARAM_ERROR, "[" +prompt + "]" + 
					" must between " + min + " and " + max);
		}
		
	}

}
