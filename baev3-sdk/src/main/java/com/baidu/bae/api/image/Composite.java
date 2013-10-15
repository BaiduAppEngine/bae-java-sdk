package com.baidu.bae.api.image;

import java.util.HashMap;
import java.util.Map;

import com.baidu.bae.api.exception.BaeException;
/**
 * 图片合成功能类<br/>
 * 
 * 可自定义图片的位置、透明度等信息
 * 
 */
public class Composite {

	private Map<String,Object> data = null;
	private Image image = null;
	
	/**
	 * 获取Composite实例
	 * 
	 */
	public Composite(){
		this.data = new HashMap<String,Object>();
	}
	/**
	 * 获取Composite实例
	 * @param image 图片数据源
	 */
	public Composite(Image image){
		this.data = new HashMap<String,Object>();
		setImage(image);
	}
 	
	/**
	 * 设置图片源
	 * @param image 图片信息源
	 */
	public void setImage(Image image) {
		this.image = image;

	}

	/**
	 * 获取图片信息源
	 * @return 返回图片信息源
	 */
	public Image getImage() {
		return this.image;
	}


	/**
	 * 获取水印操作参数
	 * @return 返回水印操作参数
	 */
	public Map<String,Object> getOperations() {
		return this.data;
	}

	/**
	 * 设置图片相对于锚点的位置
	 * @param x_offset 相对于锚点的水平位置,<b>可以为负</b>, 默认为0
	 * @param y_offset 相对于锚点的垂直位置,<b>可以为负</b>, 默认为0
	 */
	public void setPos(int x_offset, int y_offset) {
		checkInt(x_offset, "x offset", Integer.MIN_VALUE, Integer.MAX_VALUE);
		checkInt(y_offset, "y offset", Integer.MIN_VALUE, Integer.MAX_VALUE);
		data.put("x_offset", x_offset);
		data.put("y_offset", y_offset);
	}

	/**
	 * 设置图片透明度
	 * @param opacity 透明度大小,<b>范围:</b>0-1(0表示不透明,1表示完全透明)
	 */
	public void setOpacity(float opacity) {
		checkFloat(opacity, "opacity", 0.0f, 1.0f);
		data.put("opacity",opacity);

	}

	/**
	 * 设置图片的锚点位置
	 * @param anchor 锚点位置,<b>范围:</b>0-8,对应于"田"字的九个点, 默认为0.<br/>具体取值请参考ImageConstant
	 */
	public void setAnchor(int anchor) {
		checkInt(anchor, "anchor point", 0, 8);
		data.put("anchor_point", anchor);
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
	
	private void checkFloat(float dest, String prompt, float min, float max)
	{
		if(dest < min || dest > max) {
			throw new BaeException(BaeException.PARAM_ERROR, "["
					+ prompt + "]" + " must between " + min + " and " + max);
		}
		
	}

}
