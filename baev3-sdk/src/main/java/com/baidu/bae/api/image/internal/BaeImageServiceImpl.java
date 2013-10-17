package com.baidu.bae.api.image.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.baidu.bae.api.base.BaseApi;
import com.baidu.bae.api.exception.BaeException;
import com.baidu.bae.api.image.Annotate;
import com.baidu.bae.api.image.Composite;
import com.baidu.bae.api.image.Image;
import com.baidu.bae.api.image.BaeImageService;
import com.baidu.bae.api.image.QRCode;
import com.baidu.bae.api.image.Transform;
import com.baidu.bae.api.image.VCode;
import com.baidu.bae.api.util.Utils;

/**
 * 服务调用接口<br/>
 * 
 * 获取相关功能的实例，并且通过调用applyMethod执行相应的服务
 * 
 */
public class BaeImageServiceImpl extends BaseApi implements BaeImageService {

	private final static int QRCODE = 0;
	private final static int ANNOTATE = 1;
	private final static int COMPOSITE = 2;
	
	
	public BaeImageServiceImpl(String clientId, String clientSecret, String host) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		if (!host.endsWith("/")) {
			host += "/";
		}
		this.host = "http://" + host + "rest/2.0/imageui/resource";
	}
	
	/**
	 * 图片变换操作
	 * @param image 图片信息
	 * @param transform 变换操作对象 
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyTransform(Image image, Transform transform) {
		Map<String,Object> data = transform.getOperations();
		if(image.isURL()){
			data.put("src", image.getURL());
		}else{
			throw new BaeException(BaeException.PARAM_ERROR,"image source should be url!");
		}

		Map<String, Object> paramData = new HashMap<String, Object>();
		for (String key : data.keySet()) {
			paramData.put(key, data.get(key));
		}
		Map<String, Object> postData = super.getParams("process", this.host,
				paramData);
		JSONObject json = super.postData(postData, this.host);
		String result = json.getJSONObject("response_params").getString(
				"image_data");
		return Utils.base64Decode(result);
	}

	/**
	 * 文字水印操作
	 * @param image 图片信息
	 * @param annotate 文字水印操作对象
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyAnnotate(Image image,Annotate annotate) {
		Map<String,Object> data = annotate.getOperations();
		String URL = "";
		if(image.isURL()){
			URL = image.getURL();
		}else{
			//将来支持本地上传
			//URL = "";
			throw new BaeException(BaeException.PARAM_ERROR,"image source should be url!");
		}
		if(null == data.get("text")){
			throw new BaeException(BaeException.PARAM_ERROR,"no text script");
		}
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		datas.add(data);
		String jsonParams = formJSONParams(new String[]{URL}, datas, ANNOTATE, new boolean[]{true});
		Map<String, Object> paramData = new HashMap<String, Object>();
		paramData.put("strudata", jsonParams);
		Map<String, Object> postData = super.getParams("processExt", this.host,paramData);
		JSONObject json = super.postData(postData, this.host);
		String result = json.getJSONObject("response_params").getString("image_data");
		return Utils.base64Decode(result);
	}

	/**
	 * 二维码操作
	 * @param qrCode 二维码操作对象
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyQRCode(QRCode qrCode) {
		Map<String,Object> data = qrCode.getOperations();
		if(null == data.get("text")){
			throw new BaeException(BaeException.PARAM_ERROR,"no text script");
		}
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		datas.add(data);
		String jsonParams = formJSONParams(null, datas, QRCODE, new boolean[]{true});
		Map<String, Object> paramData = new HashMap<String, Object>();
		paramData.put("strudata", jsonParams);
		Map<String, Object> postData = super.getParams("processExt", this.host,paramData);
		JSONObject json = super.postData(postData, this.host);
		String result = json.getJSONObject("response_params").getString("image_data");
		return Utils.base64Decode(result);
	}

	/**
	 * 图片合成操作,暂时只支持两张图片合成
	 * @param composites 图片集合
	 * @param canvas_width 画布宽度,<b>范围:</b>0-10000
	 * @param canvas_height 画布高度,<b>范围:</b>0-10000
	 * @param outputType 图片输出格式,支持JPG、GIF、BMP、PNG.取值请参考BaeImageConstant.
	 * @param quality 图片压缩质量,<b>范围:</b>0-100
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyComposite(Collection<Composite> composites,
			int canvas_width, int canvas_height, int outputType,int quality) {
		Map<String,Object> data = null;
		Composite composite = null;
		String URL = "";
		Image image = null;
		String result = null;
		
		checkInt(canvas_width, "canvas width", 0, 10000);
		checkInt(canvas_height, "canvas height", 0, 10000);
		checkInt(outputType, "output type", 0, 3);
		checkInt(quality, "quality", 0, 100);
		Map<String,Object> commonParams = new HashMap<String,Object>();
		commonParams.put("canvas_width", canvas_width);
		commonParams.put("canvas_height", canvas_height);
		commonParams.put("desttype", outputType);
		commonParams.put("quality", quality);
		
		int len = composites.size();
		if(len < 2){
			throw new BaeException(BaeException.PARAM_ERROR,"short of images, at least two elements");
		}
		if(len > 2){
			throw new BaeException(BaeException.PARAM_ERROR,"too many images, at most two elements");
		}
		
		Iterator<Composite> compoIterator = composites.iterator();
		composite = compoIterator.next();
		image = composite.getImage();
		if(null == image){
			throw new BaeException(BaeException.PARAM_ERROR, "no bae image source!");
		}
		data = composite.getOperations();
		data.putAll(commonParams);
		if(image.isURL()){
			URL = image.getURL();
		}else{
			//将来支持本地上传
			//URL = "";
			throw new BaeException(BaeException.PARAM_ERROR,"image source should be url!");
		}
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		datas.add(data);
		List<String> urls = new ArrayList<String>();
		urls.add(URL);
		
		//urls and datas add null;
		datas.add(null);
		urls.add(null);
		
		while(compoIterator.hasNext()){
			composite = compoIterator.next();
			image = composite.getImage();
			if(null == image){
				throw new BaeException(BaeException.PARAM_ERROR, "no bae image source!");
			}
			data = composite.getOperations();
			data.putAll(commonParams);
			if(image.isURL()){
				URL = image.getURL();
			}else{
				//将来支持本地上传
				//URL = "";
				throw new BaeException(BaeException.PARAM_ERROR,"image source should be url!");
			}
			urls.set(1, URL);
			datas.set(1, data);
			String jsonParams = formJSONParams(urls.toArray(), datas, COMPOSITE, new boolean[]{true,true});
			//System.out.println(jsonParams);
			Map<String, Object> paramData = new HashMap<String, Object>();
			paramData.put("strudata", jsonParams);
			Map<String, Object> postData = super.getParams("processExt", this.host,paramData);
			JSONObject json = super.postData(postData, this.host);
			result = json.getJSONObject("response_params").getString("image_data");
			// a new image after composition
			/*retImageStr = Utils.base64Decode(result);

			retURL = uploadToBCS(retImageStr);
			urls.set(0, retURL);
			datas.set(0,new HashMap<String,Object>());// set default value
			
			*/  // local file upload	
		}
		return Utils.base64Decode(result);
		
		
	}
	
	/**
	 * 图片合成操作,暂时只支持两张图片合成
	 * @param composites 图片集合
	 * 画布宽度,默认为1000 
	 * 画布高度,默认为1000
	 * 图片输出格式,默认为JPG
	 * 图片压缩质量,默认为80
	 * @return 请求成功返回图片相关数据
	 */
	public byte[] applyComposite(Collection<Composite> composites){
		return applyComposite(composites, 1000, 1000, 0, 80);
	}

	/**
	 * 校验验证码操作
	 * @param VCode 验证码对象,需要设置输入以及密文
	 * @return mixed 请求成功,返回验证码校验结果
	 */
	public Map<String,String> verifyVCode(VCode VCode) {
		Map<String,Object> data = VCode.getOperations();
		if(null == data.get("input")){
			throw new BaeException(BaeException.PARAM_ERROR, "input field is empty");
		}
		if(null == data.get("vcode")){
			throw new BaeException(BaeException.PARAM_ERROR, "secret field is empty");
		}
		data.put("vcservice", 1);
		
		Map<String, Object> paramData = new HashMap<String, Object>();
		for (String key : data.keySet()) {
			paramData.put(key, data.get(key));
		}
		Map<String, Object> postData = super.getParams("process", this.host,
				paramData);
		JSONObject json = super.postData(postData, this.host);
		JSONObject resParams = json.getJSONObject("response_params");
		String status = resParams.getString("status");
		String reason = resParams.getString("str_reason");
		Map<String,String> retMap = new HashMap<String,String>();
		retMap.put("status", status);
		retMap.put("reason", reason);
		return retMap;
	}

	/**
	 * 生成验证码操作
	 * @param vCode 验证码对象,可设置验证码的位数和干扰程度
	 * @return mixed 请求成功,返回验证码图片的URL,以及该验证码的密文
	 */
	public Map<String,String> generateVCode(VCode vCode) {
		Map<String,Object> data = vCode.getOperations();
		data.put("vcservice", 0);

		Map<String, Object> paramData = new HashMap<String, Object>();
		for (String key : data.keySet()) {
			paramData.put(key, data.get(key));
		}
		Map<String, Object> postData = super.getParams("process", this.host,
				paramData);
		JSONObject json = super.postData(postData, this.host);
		JSONObject resParams = json.getJSONObject("response_params");
		String status = resParams.getString("status");
		if(Integer.parseInt(status) != 0){
			throw new BaeException(BaeException.PARAM_ERROR, "failed to get verification code");
		}
		String imgurl = resParams.getString("imgurl");
		String secret = resParams.getString("vcode_str");
		Map<String,String> retMap = new HashMap<String,String>();
		retMap.put("imgurl", imgurl);
		retMap.put("secret", secret);
		return retMap;
	}
	
	
	private String formJSONParams(Object[] imageSrc, List<Map<String, Object>> datas,int procType, boolean[] isURL) {
		JSONObject jsonParams = new JSONObject();
		JSONObject sourceData = new JSONObject();
		JSONArray reqDataSource = new JSONArray();
		JSONObject data0Describe = new JSONObject();
		JSONObject data1Describe = new JSONObject();
		JSONObject httpReqpack = new JSONObject();//reserve
		JSONObject operations = new JSONObject();
		String text = null;
		String strOpacity = "";
		float opacity;
		Map<String, Object> data = null;
		switch (procType) {
			/****************************QR Code*******************************/
			case 0:
				data = datas.get(0);
				// top process_type
				jsonParams.put("process_type", procType + "");// value = '0'
				
				// top req_data_num
				jsonParams.put("req_data_num", "1"); // fixed value;
				
				// top source_data
				text = (String)data.get("text");  //get text in qr code
				sourceData.put("data1", base64Encode(text.getBytes()));
				jsonParams.put("source_data", sourceData);
				
				// top req_data_source
				data0Describe.put("sourcemethod", "BODY");
				data0Describe.put("source_data_type", 0); //0-text script, 1-image
				data0Describe.put("http_reqpack", httpReqpack);
				// form operations
				if(null != data.get("size")){
					operations.put("size", data.get("size"));
				}else{
					operations.put("size", 3);
				}	
				if(null != data.get("version")){
					operations.put("version", data.get("version"));
				}else{
					operations.put("version", 0);
				}

				if(null != data.get("margin")){
					operations.put("margin", data.get("margin"));
				}else{
					operations.put("margin", 4);
				}
				if(null != data.get("level")){
					operations.put("level", data.get("level"));
				}else{
					operations.put("level", 2);
				}
				
				if(null != data.get("foreground")){
					operations.put("foreground", data.get("foreground"));
				}else{
					operations.put("foreground", "000000");
				}
				if(null != data.get("background")){
					operations.put("background", data.get("background"));
				}else{
					operations.put("background", "FFFFFF");
				}
				data0Describe.put("operations", operations);
				reqDataSource.add(0, data0Describe);
				jsonParams.put("req_data_source", reqDataSource);
			break;
			/****************Annotate*********************/
			case 1:
				data = datas.get(0);
				// top process_type
				jsonParams.put("process_type", procType + "");// value = '1'
				
				// top req_data_num
				jsonParams.put("req_data_num", "2");// fixed value;
			
				text = (String)data.get("text");//get text in annotate
				if(isURL[0]){
					sourceData.put("data1", base64Encode(text.getBytes()));
					data0Describe.put("sourcemethod", "GET");
					data0Describe.put("source_url", (String)imageSrc[0]);;
				}else{
					data0Describe.put("sourcemethod", "BODY");
					sourceData.put("data1", base64Encode(((String)imageSrc[0]).getBytes()));
					sourceData.put("data2", base64Encode(text.getBytes()));
				}
				// top source_data
				jsonParams.put("source_data", sourceData);
				
				data0Describe.put("source_data_type", 1);
				data0Describe.put("http_reqpack", httpReqpack);
				
				data1Describe.put("sourcemethod", "BODY");
				data1Describe.put("source_data_type", 0);
				data1Describe.put("http_reqpack", httpReqpack);
				data1Describe.put("operations", operations); // empty operations

				// form operations
				if(null != data.get("x_offset")){
					operations.put("x_offset", data.get("x_offset"));
				}else{
					operations.put("x_offset",0);
				}
					
				if(null != data.get("y_offset")){
					operations.put("y_offset", data.get("y_offset"));
				}else{
					operations.put("y_offset",0);
				}
				
				if(null != data.get("opacity")){
					opacity = (Float)data.get("opacity");
					strOpacity = Integer.toHexString((int)Math.ceil(255-opacity*255));
					if(strOpacity.length() == 1){
						strOpacity = "0" + strOpacity.toUpperCase();
					}else{
						strOpacity = strOpacity.toUpperCase();
					}
				}else{
					strOpacity = "FF";
				}
				
				if(null != data.get("font_name")){
					operations.put("font_name",data.get("font_name"));
				}else{
					operations.put("font_name", 0);
				}
				if(null != data.get("font_color")){
					operations.put("font_color", "#" + data.get("font_color") + strOpacity);
				}else{
					operations.put("font_color","#000000" + strOpacity);
				}
				
				if(null != data.get("font_size")){
					operations.put("font_size",data.get("font_size"));
				}else{
					operations.put("font_size",25);
				}
				if(null != data.get("desttype")){
					operations.put("desttype",data.get("desttype"));
				}else{
					operations.put("desttype",0);
				}
				if(null != data.get("quality")){
					operations.put("quality",data.get("quality"));
				}else{
					operations.put("quality",80);
				}
				data0Describe.put("operations", operations);
				reqDataSource.add(0, data0Describe);
				reqDataSource.add(1, data1Describe);
				// top req_data_source
				jsonParams.put("req_data_source", reqDataSource);

			break;
			/********************Composite**********************/
			case 2:
				// top process_type
				jsonParams.put("process_type", procType + "");// value = '1'
				// top req_data_num
				jsonParams.put("req_data_num", "2");// fixed value;

				int count = 0;
				boolean isEmpty = true;
				while(count < 2){
					//get operations
					data = datas.get(count);
					
					if(isURL[count] == true){
						data0Describe.put("sourcemethod", "GET");
						data0Describe.put("source_url", imageSrc[count]);;
					}else{
						data0Describe.put("sourcemethod", "BODY");
						if(isEmpty == true){
							sourceData.put("data1", base64Encode(((String)imageSrc[count]).getBytes()));
							isEmpty = false;
						}else{
							sourceData.put("data2", base64Encode(((String)imageSrc[count]).getBytes()));
						}
						
					}
					data0Describe.put("http_reqpack", httpReqpack);
					data0Describe.put("source_data_type", 1);

					if(null != data.get("x_offset")){
						operations.put("x_offset", data.get("x_offset"));
					}else{
						operations.put("x_offset", 0);
					}
						
					if(null != data.get("y_offset")){
						operations.put("y_offset", data.get("y_offset"));
					}else{
						operations.put("y_offset", 0);
					}

					if(null != data.get("opacity")){
						operations.put("opacity", data.get("opacity"));
					}else{
						operations.put("opacity", 0.0);
					}

					if(null != data.get("anchor_point")){
						operations.put("anchor_point", data.get("anchor_point"));
					}else{
						operations.put("anchor_point", 0);
					}
					/*********************Common params********************************/
					if(null != data.get("canvas_width")){
						operations.put("canvas_width", data.get("canvas_width"));
					}else{
						operations.put("canvas_width", 0);
					}
					if(null != data.get("canvas_height")){
						operations.put("canvas_height", data.get("canvas_height"));
					}else{
						operations.put("canvas_height", 0);
					}
					if(null != data.get("desttype")){
						operations.put("desttype", data.get("desttype"));
					}else{
						operations.put("desttype", 0);
					}
					if(null != data.get("quality")){
						operations.put("quality", data.get("quality"));
					}else{
						operations.put("quality", 80);
					}
					data0Describe.put("operations", operations);
					reqDataSource.add(count, data0Describe);
					data0Describe.clear();
					count++;
					
				}
				
				// top source_data
				jsonParams.put("source_data", sourceData);
				jsonParams.put("req_data_source", reqDataSource);
			break;
		}
		return jsonParams.toString();
		
	}
	
	@SuppressWarnings("restriction")
	public String base64Encode(byte[] input) {
		String retVal = null;
		try {
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			retVal = encoder.encode(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	private void checkInt(int dest, String prompt, int min, int max)
	{
		if(dest < min || dest > max) {
			throw new BaeException(BaeException.PARAM_ERROR, "["
					+ prompt + "]" + " must between " + min + " and " + max);
		}
		
	}

}
