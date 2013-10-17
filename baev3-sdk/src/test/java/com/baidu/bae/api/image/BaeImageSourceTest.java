package com.baidu.bae.api.image;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.bae.api.exception.BaeException;
import com.baidu.bae.api.factory.BaeFactory;

public class BaeImageSourceTest {
	private static String AK = "bGUoCievDf4XIjoIYqk7xao2";
	private static String SK = "pNceSAlS4HB8fToDilmXQvwSc6nHInHW";
	private static String HOST = "yunservicebus.newoffline.bae.baidu.com";
	
		BaeImageService service = BaeFactory.getBaeImageService(AK, SK, HOST);
		String url = "http://hiphotos.baidu.com/baidu/pic/item/81b7ac86c57a211b66096e75.jpg";
		Image image = new Image();
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
	public void testSetURL() {
		try{
			image.setURL("ABC");
		}catch(BaeException e){
				Assert.assertEquals("invalid image source url[ABC]", e.getErrmsg());
			
		}
	}
}
