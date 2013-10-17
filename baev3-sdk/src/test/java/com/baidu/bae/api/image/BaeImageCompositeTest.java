package com.baidu.bae.api.image;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.bae.api.exception.BaeException;
import com.baidu.bae.api.factory.BaeFactory;
import com.baidu.bae.api.image.internal.BaeImageServiceImpl;

public class BaeImageCompositeTest {
	private static String AK = "bGUoCievDf4XIjoIYqk7xao2";
	private static String SK = "pNceSAlS4HB8fToDilmXQvwSc6nHInHW";
	private static String HOST = "yunservicebus.newoffline.bae.baidu.com";

	
		BaeImageService service = BaeFactory.getBaeImageService(AK, SK, HOST);
		String url = "http://hiphotos.baidu.com/baidu/pic/item/81b7ac86c57a211b66096e75.jpg";
		Composite com = new Composite();
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
	public void testSetPos() {
		try{
			com.setPos(-1,0);
		}catch(BaeException e){
				Assert.assertEquals("[zooming width] must between 0 and 10000", e.getErrmsg());
			
		}
	}
	
	@Test
	public void testSetOpacity() {
		try{
			com.setOpacity(1.1f);
		}catch(BaeException e){
				Assert.assertEquals("[opacity] must between 0.0 and 1.0", e.getErrmsg());
			
		}
		
		try{
			com.setOpacity(-1f);
		}catch(BaeException e){
				Assert.assertEquals("[opacity] must between 0.0 and 1.0", e.getErrmsg());
			
		}
	}
	
	@Test
	public void testSetAnchor() {
		try{
			com.setAnchor(-1);
		}catch(BaeException e){
				Assert.assertEquals("[anchor point] must between 0 and 8", e.getErrmsg());
			
		}
		
		try{
			com.setAnchor(9);
		}catch(BaeException e){
				Assert.assertEquals("[anchor point] must between 0 and 8", e.getErrmsg());
			
		}
	}
}
