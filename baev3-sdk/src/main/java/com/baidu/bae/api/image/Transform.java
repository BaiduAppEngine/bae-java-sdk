package com.baidu.bae.api.image;

import java.util.HashMap;
import java.util.Map;

import com.baidu.bae.api.exception.BaeException;
/**
 * 图形变换功能类<br/>
 * 可对图片执行例如剪裁、缩放、翻转、设置灰度、设置饱和度等操作.支持在一次请求中，同时将多种类型的变换施加于图片上
 *
 */
public class Transform {

	private Map<String, Object> data;
	
	/**
	 * 获取Transform实例
	 *
	 */
	public Transform(){
		this.data= new HashMap<String, Object>();
	}
	
	/**
	 * 获取变换操作参数
	 * @return 返回变换操作的参数
	 */
	public Map<String, Object> getOperations(){
		return this.data;
	}
	
	/**
	 * 设置缩放图片
	 * @param zoomType 可按宽度、高度、像素以及非等比例缩放,取值参考ImageConstant
	 * @param widthValue 缩放值,根据缩放类型的不同，取值范围不同.<br/>
	 * <b>宽度范围:</b>0-10000 <br/> 
	 * <b>高度范围:</b>0-10000 <br/>
	 * <b>像素范围:</b>0-100000000 <br/>
	 * <b>非等比时宽度范围:</b>0-10000
	 * @param heightValue 高度值,非等比缩放时高度.<b>范围:</b>0-10000
	 */
	public void setZooming(int zoomType, int widthValue, int heightValue) {
		String size = null;
		switch (zoomType) {
		case ImageConstant.TRANSFORM_ZOOMING_TYPE_HEIGHT: 
			checkInt(widthValue, "zooming height", 0, 10000);
			size = "b0_" + widthValue;
			break;
		case ImageConstant.TRANSFORM_ZOOMING_TYPE_WIDTH:
			checkInt(widthValue, "zooming width", 0, 10000);
			size = "b" + widthValue + "_0";
			break;
		case ImageConstant.TRANSFORM_ZOOMING_TYPE_PIXELS:
			checkInt(widthValue, "zooming pixels", 0, 100000000);
			size = "p" + widthValue;
			break;
		case ImageConstant.TRANSFORM_ZOOMING_TYPE_UNRATIO:
			checkInt(widthValue, "zooming unratio" , 0, 10000);
			checkInt(heightValue, "zooming height(unratio)" , 0, 10000);
			size = "u" + widthValue + "_" + heightValue;
			break;
		default:
			throw new BaeException(BaeException.PARAM_ERROR,
					"zoomType param error");
		}
		this.data.put("size", size);
		
	}
	
	/**
	 * 设置缩放图片
	 * @param zoomType 可按宽度、高度、像素缩放,取值参考ImageConstant
	 * @param value 缩放值,根据缩放类型的不同,取值范围不同.<br/>
	 * <b>宽度范围:</b>0-10000 <br/> 
	 * <b>高度范围:</b>0-10000 <br/>
	 * <b>像素范围:</b>0-100000000 <br/>
	 * 
	 */
	public void setZooming(int zoomType, int value){
		setZooming(zoomType, value, 0);
	}

	/**
	 * 设置裁剪相关信息
	 * @param x  裁剪起始x坐标,<b>范围:</b>1-100000
	 * @param y  裁剪起始y坐标,<b>范围:</b>1-100000
	 * @param width	  裁剪目标宽度,<b>范围:</b>1-100000
	 * @param height 裁剪目标高度,<b>范围:</b>1-100000
	 */
	public void setCropping(int x, int y, int width, int height) {
		
		checkInt(x, "cut_x", 0, 10000);
		checkInt(y, "cut_y", 0, 10000);
		checkInt(height, "cut_h", 0, 10000);
		checkInt(width, "cut_w", 0, 10000);
		this.data.put("cut_x", x);
		this.data.put("cut_y", y);
		this.data.put("cut_h", height);
		this.data.put("cut_w", width);
		
	}

	/**
	 * 设置旋转角度 
	 * @param degree  旋转的角度数，<b>范围:</b>0-360
	 */
	public void setRotation(int degree) {
		checkInt(degree, "degree", 0, 360);
		this.data.put("rotate", degree);
	}

	/**
	 * 设置灰度
	 * @param hue  灰度调整，<b>范围:</b>1-100
	 */
	public void setHue(int hue) {
		checkInt(hue, "hue", 1, 100);
		this.data.put("hue", hue);
		
	}

	/**
	 * 设置亮度
	 * @param lightness 亮度调整，<b>范围:</b>1以上
	 */
	public void setLightness(int lightness) {
		checkInt(lightness, "lightness", 1, Integer.MAX_VALUE);
		this.data.put("lightness", lightness);
		
	}

	/**
	 *  设置对比度
	 * @param contrast 对比度调整，<b>范围:</b>0或1(0为降低对比度，1为增强对比度)
	 */
	public void setContrast(int contrast) {
		checkInt(contrast, "contrast", 0, 1);
		this.data.put("contrast", contrast);
		
	}

	/**
	 * 设置图像模糊/锐化处理
	 * @param sharpness 图像模糊/锐化处理，<b>范围:</b>1-200
	 */
	public void setSharpness(int sharpness) {
		checkInt(sharpness,"sharpen", 1, 200);
		this.data.put("sharpen", sharpness);
	}

	/**
	 * 设置色彩饱和度
	 * @param saturation  色彩饱和度，<b>范围:</b>1-100
	 */
	public void setSaturation(int saturation) {
		checkInt(saturation, "saturation", 1, 100);
		this.data.put("saturation", saturation);
		
	}
	
	/**
	 * 设置图片格式
	 * @param imageType  图片类型，取值参考ImageConstant,不支持BMP类型
	 * <br>图片质量默认为80
	 */
	public void setTranscoding(int imageType) {
		setTranscoding(imageType,80);
	}

	/**
	 * 设置图片格式
	 * @param imageType  图片类型，取值参考ImageConstant
	 * @param quality    图片压缩质量，<b>范围:</b>0-100
	 */
	public void setTranscoding(int imageType, int quality) {
		checkInt(quality, "quality", 0, 100);
		switch(imageType){
		case ImageConstant.IMG_JPG:
			this.data.put("imgtype", 1);
			this.data.put("quality", quality);
			break;
		case ImageConstant.IMG_GIF:
			this.data.put("imgtype", 2);
			this.data.put("quality", quality);
			break;
		case ImageConstant.IMG_PNG:
			this.data.put("imgtype", 3);
			break;
		case ImageConstant.IMG_WEBP:
			this.data.put("imgtype", 4);
			break;
		default:
			throw new BaeException(BaeException.PARAM_ERROR,
					"transcoding param error");
		}
	}

	/**
	 * 设置图片压缩质量
	 * @param quality  图片压缩质量，目标图片格式为jpg时使用，<b>范围:</b>0-100
	 */
	public void setQuality(int quality) {
		checkInt(quality, "quality", 0, 100);
		this.data.put("quality", quality);
	}

	/**
	 * 设置获取gif第一帧
	 */
	public void setGetGifFirstFrame() {
		this.data.put("tieba", 1);
	}

	/**
	 * 设置自动校准
	 */
	public void setAutorotate() {
		this.data.put("autorotate", 1);
	}

	/**
	 * 水平翻转
	 */
	public void horizontalFlip() {
		this.data.put("flop", 1);
	}

	/**
	 * 垂直翻转
	 */
	public void verticalFlip() {
		this.data.put("flip", 1);
		
	}

	/**
	 * 清除所有操作
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
	
	
	

}
