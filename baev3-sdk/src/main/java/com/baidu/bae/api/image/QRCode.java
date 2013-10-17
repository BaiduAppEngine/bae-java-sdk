package com.baidu.bae.api.image;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.baidu.bae.api.exception.BaeException;
/**
 * 二维码功能类<br/>
 * 
 * 可自定义二维码的文字、大小、版本、纠错级别等信息
 *
 */
public class QRCode {

	private Map<String, Object> data;
	private String text;
	
	/**
	 * 获取QRCode实例
	 * 
	 */
	public QRCode(){
		this.data= new HashMap<String, Object>();
	}
	/**
	 * 获取QRCode实例
	 * @param text 待生成二维码的文字, UTF-8编码,<b>范围:</b>1-500个字符 
	 */
	public QRCode(String text){
		this.data= new HashMap<String, Object>();
		setText(text);
	}
	
	/**
	 * 设置二维码文本信息
	 * @param text  待生成二维码的文字, UTF-8编码,<b>范围:</b>1-500个字符 
	 */
	public void setText(String text) {
		checkString(text, "text length", 1,500);
		this.text = text;
		data.put("text", text);
	}

	/**
	 * 获取二维码的文字信息
	 * @return 返回二维码文字信息
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * 获取二维码操作参数
	 * @return 返回二维码操作参数
	 */
	public Map<String,Object> getOperations() {
		return this.data;
	}

	/**
	 * 设置二维码的版本信息
	 * @param version 版本大小, <b>范围:</b>0-30, 默认0
	 */
	public void setVersion(int version) {
		checkInt(version, "version", 0, 30);
		data.put("version", version);

	}

	/**
	 * 设置生成二维码的尺寸
	 * @param size 尺寸大小,<b>范围:</b>1-100, 默认3
	 */
	public void setSize(int size) {
		checkInt(size, "size", 1, 100);
		data.put("size", size);

	}

	/**
	 * 设置二维码的纠错级别
	 * @param level 纠错级别,<b>范围:</b>1-4, 默认2
	 */
	public void setLevel(int level) {
		checkInt(level, "level", 1, 4);
		data.put("level", level);
	}

	/**
	 * 设置二维码的边缘宽度
	 * @param margin 边缘大小,<b>范围:</b>1-100, 默认4
	 */
	public void setMargin(int margin) {
		checkInt(margin, "margin", 1, 100);
		data.put("margin", margin);
	}

	/**
	 * 设置二维码的前景颜色
	 * @param foreground 标准6位RGB色,默认是黑色('000000')
	 */
	public void setForeground(String foreground) {
		checkRGB(foreground);
		data.put("foreground", foreground);

	}

	/**
	 * 设置二维码的背景颜色
	 * @param background 标准6位RGB色,默认是白色('FFFFFF')
	 */
	public void setBackground(String background) {
		checkRGB(background);
		data.put("background", background);
	}

	/**
	 * 清除操作
	 */
	public void clearOperations() {
		data.clear();
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
	
	private void checkRGB(String RGB){
		checkString(RGB, "RGB", 6, 6);
	    String pattern = "[AaBbCcDdEeFf0123456789]{6}";
    	// regular match
	    if(!Pattern.matches(pattern, RGB)){
    		throw new BaeException(BaeException.PARAM_ERROR,
    				"invalid RGB color[" + RGB + "]");
    	}
	}

}
