package com.baidu.bae.api.image;

import java.util.Collection;
import java.util.Map;


/**
 * 服务调用接口<br/>
 * 
 * 通过调用applyMethod执行相应的服务
 * 
 */
public interface BaeImageService {
	
	
	/**
	 * 图片变换操作
	 * @param image 图片信息
	 * @param transform 变换操作对象 
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyTransform(Image image, 
			Transform transform);
	
	/**
	 * 文字水印操作
	 * @param image 图片信息
	 * @param annotate 文字水印操作对象
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyAnnotate(Image image, 
			Annotate annotate);
	
	/**
	 * 二维码操作
	 * @param qrCode 二维码操作对象
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyQRCode(QRCode qrCode);
	
	/**
	 * 图片合成操作,暂时只支持两张图片合成
	 * @param composites 图片集合
	 * @param canvas_width 画布宽度,<b>范围:</b>0-10000
	 * @param canvas_height 画布高度,<b>范围:</b>0-10000
	 * @param outputType 图片输出格式,支持JPG、GIF、BMP、PNG.取值请参考ImageConstant.
	 * @param quality 图片压缩质量,<b>范围:</b>0-100
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyComposite(Collection<Composite> composites,
			int canvas_width, int canvas_height, int outputType, int quality);
	/**
	 * 图片合成操作,暂时只支持两张图片合成
	 * @param composites 图片集合
	 * 画布宽度,默认为1000 
	 * 画布高度,默认为1000
	 * 图片输出格式,默认为JPG
	 * 图片压缩质量,默认为80
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyComposite(Collection<Composite> composites);
	/**
	 * 校验操作
	 * @param vCode 验证码对象,需要设置输入以及密文
	 * @return 请求成功,返回验证码校验结果
	 */
	public Map<String,String> verifyVCode(VCode vCode);
	
	/**
	 * 生成验证码操作
	 * @param vCode 验证码对象,可设置验证码的位数和干扰程度
	 * @return 请求成功,返回验证码图片的URL,以及该验证码的密文,在Map中对应的key为"imgurl"与"secret"
	 */
	public Map<String,String> generateVCode(VCode vCode);
	
	/**
	 * 获得上次请求的ID,若上次调用出错返回0
	 * @return 上次请求的ID
	 */
    public long getRequestId();
}
