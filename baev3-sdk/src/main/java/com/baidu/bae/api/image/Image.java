package com.baidu.bae.api.image;

import java.util.regex.Pattern;

import com.baidu.bae.api.exception.BaeException;
/**
 * 图片数据源类<br/>
 * 暂时只支持url的形式
 *
 */
public class Image {

	private String url = null;
	private boolean isURL = false;
	private byte[] data = null;
	
	/**
	 * 获取Image实例
	 *
	 */
	public Image()
	{
		
	}
	/**
	 * 获取Image实例
	 * @param url 图片的URL,<b>长度范围:</b>1-2048. 支持http协议
	 */
	public Image(String url){
		setURL(url);
		
	}
	/*
	public BaeImageSourceImpl(byte[] imageData){
		
	}*/
	/**
	 * 设置图片URL
	 * @param url 图片的URL,<b>长度范围:</b>1-2048. 支持http协议
	 */
	public void setURL(String url) {
		checkURL(url);
		this.url = url;
		this.isURL = true;

	}
	
	/*public void setData(byte[] imageData) {
		// TODO Auto-generated method stub

	}*/
	/**
	 * 获取图片的URL
	 * @return 返回图片的URL
	 */
	public String getURL() {
		return this.url;
	}
	
	/*public byte[] getData() {
		// TODO Auto-generated method stub
		return null;
	}*/
	/**
	 * 判断是否为URL
	 * @return 返回true表示图片源为URL, false表示图片源为二进制数据
	 */
	public boolean isURL() {
		return this.isURL;
	}
	
    private void checkURL(String url)
    {
    	this.checkString(url, "url", 0, 2048);
    	String pattern = "^(http|https)://.*";
    	// regular match
    	if(!Pattern.matches(pattern, url)){
    		throw new BaeException(BaeException.PARAM_ERROR,
    				"invalid image source url[" + url + "]");
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
