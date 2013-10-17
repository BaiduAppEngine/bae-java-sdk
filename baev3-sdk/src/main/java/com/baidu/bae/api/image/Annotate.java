package com.baidu.bae.api.image;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.baidu.bae.api.exception.BaeException;
/**
 * 文字水印功能类<br/>
 * 
 * 可自定义文字的大小、样式、颜色、位置等信息
 *
 */
public class Annotate {

	
	private Map<String, Object> data;
	private String text;
	
	/**
	 *  获取Annotate对象
	 *
	 */
	public Annotate(){
		this.data= new HashMap<String, Object>();
	}
	
	/**
	 * 获取Annotate对象
	 * @param text 待添加水印的文字,UTF-8编码,<b>范围:</b>1-500字符
	 */
	public Annotate(String text){
		this.data= new HashMap<String, Object>();
		setText(text);
	}
	
	/**
	 * 获取水印操作参数
	 * @return 返回水印操作参数
	 */
	public Map<String, Object> getOperations() {
		return this.data;
	}
	
	/**
	 * 设置水印文本
	 * 
	 * @param text
	 *            待添加水印的文字,UTF-8编码,<b>范围:</b>1-500字符
	 */
	public void setText(String text) {
		checkString(text, "text length", 1,500);
		this.text = text;
		data.put("text", text);

	}

	/**
	 * 获得水印的文字信息
	 * 
	 * @return 水印的文字信息
	 */
	public String getText() {
		return text;
	}


	/**
	 * 设置文字透明度
	 * 
	 * @param opacity 透明度大小,<b>范围:</b>0-1, 默认为0
	 * 
	 */
	public void setOpacity(float opacity) {
		checkFloat(opacity, "opacity", 0.0f, 1.0f);
		data.put("opacity", opacity);
		

	}

	/**
	 * 设置水印字体样式
	 * 
	 * @param name 字体样式,支持宋体（默认）、楷体、黑体、微软雅黑、Arial（该字体不支持中文）.取值请参考ImageConstant
	 * @param size
	 *            字体大小,<b>范围:</b>0-1000,默认为25
	 * @param color
	 *            字体颜色,<b>范围:</b>标准6位RGB色,默认为黑色('000000')
	 */
	public void setFont(int name, int size, String color) {
		checkInt(name, "font name", 0, 4);
		checkInt(size, "font size", 0, 1000);
		checkRGB(color);
		data.put("font_name", name);
		data.put("font_size", size);
		data.put("font_color", color);

	}
	
	/**
	 * 设置水印字体样式
	 * 
	 * @param name 字体样式,支持宋体、楷体、黑体、微软雅黑、Arial（该字体不支持中文）.取值请参考ImageConstant
	 * @param size
	 *            字体大小,<b>范围:</b>0-1000,默认为25
	 * <br/>字体颜色默认为黑色('000000')
	 */
	public void setFont(int name, int size){
		setFont(name, size, "000000");
	}

	/**
	 * 设置水印文字位置
	 * 
	 * @param x_offset
	 *            X坐标位置,<b>范围:</b>0-图片宽度, 默认为0
	 * @param y_offset
	 *            Y坐标位置,<b>范围:</b>0-图片高度, 默认为0
	 */
	public void setPos(int x_offset, int y_offset) {
		checkInt(x_offset, "x_offset", 0, Integer.MAX_VALUE);
		checkInt(y_offset, "y_offset", 0, Integer.MAX_VALUE);
		data.put("x_offset", x_offset);
		data.put("y_offset", y_offset);

	}

	/**
	 * 设置图片输出格式
	 * 
	 * @param outputType
	 *            支持JPG（默认）、GIF、BMP、PNG、WEBP.取值请参考ImageConstant.
	 */
	public void setOutputType(int outputType) {
		checkInt(outputType, "output type", 0, 4);
		data.put("desttype", outputType);

	}

	/**
	 * 图片压缩质量
	 * 
	 * @param quality
	 *            <b>范围:</b>0-100, 默认为80
	 */
	public void setQuality(int quality) {
		checkInt(quality, "quality", 0, 100);
		data.put("quality", quality);

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
	  
	private void checkFloat (float dest, String prompt, float min, float max) 
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
