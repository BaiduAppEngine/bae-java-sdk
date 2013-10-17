package com.baidu.bae.api.image;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.bae.api.exception.BaeException;
import com.baidu.bae.api.factory.BaeFactory;


public class BaeImageTransformTest {

	private static String AK = "bGUoCievDf4XIjoIYqk7xao2";
	private static String SK = "pNceSAlS4HB8fToDilmXQvwSc6nHInHW";
	private static String HOST = "yunservicebus.newoffline.bae.baidu.com";

	
		BaeImageService service = BaeFactory.getBaeImageService(AK, SK, HOST);
		String url = "http://hiphotos.baidu.com/baidu/pic/item/81b7ac86c57a211b66096e75.jpg";
		Transform transform = new Transform();
		static String text = "tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
				"tttttttttttttttttttttttttttttttttttttttttttttttttttttttt";
		


	@Test
	public void testSetZooming() {
		try{
			transform.setZooming(ImageConstant.TRANSFORM_ZOOMING_TYPE_WIDTH, 100001, 0);
		}catch(BaeException e){
				Assert.assertEquals("[zooming width] must between 0 and 10000", e.getErrmsg());
			
		}
		
		try{
			transform.setZooming(ImageConstant.TRANSFORM_ZOOMING_TYPE_HEIGHT, 100001, 0);
		}catch(BaeException e){
				Assert.assertEquals("[zooming height] must between 0 and 10000", e.getErrmsg());
			
		}
		
		try{
			transform.setZooming(ImageConstant.TRANSFORM_ZOOMING_TYPE_PIXELS, 100000001, 0);
		}catch(BaeException e){
				Assert.assertEquals("[zooming pixels] must between 0 and 100000000", e.getErrmsg());
			
		}
		
		try{
			transform.setZooming(ImageConstant.TRANSFORM_ZOOMING_TYPE_UNRATIO, 10001, 0);
		}catch(BaeException e){
				Assert.assertEquals("[zooming unratio] must between 0 and 10000", e.getErrmsg());
			
		}
		
		try{
			transform.setZooming(ImageConstant.TRANSFORM_ZOOMING_TYPE_UNRATIO, 10000, 10001);
		}catch(BaeException e){
				Assert.assertEquals("[zooming height(unratio)] must between 0 and 10000", e.getErrmsg());
			
		}
		
		try{
			transform.setZooming(5, 10000, 10001);
		}catch(BaeException e){
				Assert.assertEquals("zoomType param error", e.getErrmsg());
			
		}
		
		
	}
	
	@Test
	public void testSetCropping() {
		try{
			transform.setCropping(1000001, 0, 0, 0);
		}catch(BaeException e){
			Assert.assertEquals("[cut_x] must between 0 and 10000", e.getErrmsg());
			
		}
		
		try{
			transform.setCropping(0,1000001,  0, 0);
		}catch(BaeException e){
			Assert.assertEquals("[cut_y] must between 0 and 10000", e.getErrmsg());
			
		}
		try{
			transform.setCropping(0,  0,1000001, 0);
		}catch(BaeException e){
			Assert.assertEquals("[cut_w] must between 0 and 10000", e.getErrmsg());
			
		}
		
		try{
			transform.setCropping(0,  0, 0,1000001);
		}catch(BaeException e){
			Assert.assertEquals("[cut_h] must between 0 and 10000", e.getErrmsg());
			
		}
		
		try{
			transform.setCropping(0,  0, 0,-1);
		}catch(BaeException e){
			Assert.assertEquals("[cut_h] must between 0 and 10000", e.getErrmsg());
			
		}
		
		
	}
	
	@Test
	public void testSetRotation() {
		try{
			transform.setRotation(-1);
		}catch(BaeException e){
			Assert.assertEquals("[degree] must between 0 and 360", e.getErrmsg());
			
		}
		
		try{
			transform.setRotation(361);
		}catch(BaeException e){
			Assert.assertEquals("[degree] must between 0 and 360", e.getErrmsg());
			
		}
		
	}
	
	@Test
	public void testSetHue() {
		try{
			transform.setHue(0);
		}catch(BaeException e){
			Assert.assertEquals("[hue] must between 1 and 100", e.getErrmsg());
			
		}
		
		try{
			transform.setHue(101);
		}catch(BaeException e){
			Assert.assertEquals("[hue] must between 1 and 100", e.getErrmsg());
			
		}

	}
	
	@Test
	public void testSetLightness() {
		try{
			transform.setLightness(0);
		}catch(BaeException e){
			Assert.assertEquals("[lightness] must between 1 and 2147483647", e.getErrmsg());
			
		}

	}
	
	@Test
	public void testSetContrast() {
		try{
			transform.setContrast(-1);
		}catch(BaeException e){
			Assert.assertEquals("[contrast] must between 0 and 1", e.getErrmsg());
			
		}
		
		try{
			transform.setContrast(2);
		}catch(BaeException e){
			Assert.assertEquals("[contrast] must between 0 and 1", e.getErrmsg());
			
		}

	}
	
	@Test
	public void testSetSharpness() {
		try{
			transform.setSharpness(0);
		}catch(BaeException e){
			Assert.assertEquals("[sharpen] must between 1 and 200", e.getErrmsg());
			
		}
		
		try{
			transform.setSharpness(201);
		}catch(BaeException e){
			Assert.assertEquals("[sharpen] must between 1 and 200", e.getErrmsg());
			
		}
		
	}
	
	@Test
	public void testSetSaturation() {
		try{
			transform.setSaturation(0);
		}catch(BaeException e){
			Assert.assertEquals("[saturation] must between 1 and 100", e.getErrmsg());
			
		}
		
		try{
			transform.setSaturation(101);
		}catch(BaeException e){
			Assert.assertEquals("[saturation] must between 1 and 100", e.getErrmsg());
			
		}
	}
	
	@Test
	public void testSetTranscoding() {
		try{
			transform.setTranscoding(0, 101);
		}catch(BaeException e){
			Assert.assertEquals("[quality] must between 0 and 100", e.getErrmsg());
			
		}
		
		try{
			transform.setTranscoding(1, 10);
		}catch(BaeException e){
			Assert.assertEquals("transcoding param error", e.getErrmsg());
			
		}
		
		try{
			transform.setTranscoding(5, 10);
		}catch(BaeException e){
			Assert.assertEquals("transcoding param error", e.getErrmsg());
			
		}
		
	}
	
	@Test
	public void testSetQuality() {
		try{
			transform.setQuality(-1);
		}catch(BaeException e){
			Assert.assertEquals("[quality] must between 0 and 100", e.getErrmsg());
			
		}
	
		try{
			transform.setQuality(101);
		}catch(BaeException e){
			Assert.assertEquals("[quality] must between 0 and 100", e.getErrmsg());
			
		}
	}
		
}
