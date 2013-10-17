package com.baidu.bae.api.image;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.bae.api.exception.BaeException;
import com.baidu.bae.api.factory.BaeFactory;
import com.baidu.bae.api.image.internal.BaeImageServiceImpl;

public class BaeImageQRCodeTest {
	private static String AK = "bGUoCievDf4XIjoIYqk7xao2";
	private static String SK = "pNceSAlS4HB8fToDilmXQvwSc6nHInHW";
	private static String HOST = "yunservicebus.newoffline.bae.baidu.com";
	private String funName;
	private String errMsg;
	private String input;
	
		BaeImageService service = BaeFactory.getBaeImageService(AK, SK, HOST);
		String url = "http://hiphotos.baidu.com/baidu/pic/item/81b7ac86c57a211b66096e75.jpg";
		QRCode qrcode = new QRCode();
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
	public void testSetText() {
		try{
			qrcode.setText(text);
		}catch(BaeException e){
				Assert.assertEquals("[text length] must between 1 and 500", e.getErrmsg());
			
		}
	}
	
	@Test
	public void testSetSize(){
		try{
			qrcode.setSize(0);
		}catch(BaeException e){
			Assert.assertEquals("[size] must between 1 and 100", e.getErrmsg());
		}
		
		try{
			qrcode.setSize(101);
		}catch(BaeException e){
			Assert.assertEquals("[size] must between 1 and 100", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetMargin(){
		try{
			qrcode.setMargin(0);
		}catch(BaeException e){
			Assert.assertEquals("[margin] must between 1 and 100", e.getErrmsg());
		}
		
		try{
			qrcode.setMargin(101);
		}catch(BaeException e){
			Assert.assertEquals("[margin] must between 1 and 100", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetLevel(){
		try{
			qrcode.setLevel(0);
		}catch(BaeException e){
			Assert.assertEquals("[level] must between 1 and 4", e.getErrmsg());
		}
		
		try{
			qrcode.setLevel(5);
		}catch(BaeException e){
			Assert.assertEquals("[level] must between 1 and 4", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetForeground(){
		try{
			qrcode.setForeground("ddddd");
		}catch(BaeException e){
			Assert.assertEquals("[RGB] must between 6 and 6", e.getErrmsg());
		}
		
		try{
			qrcode.setForeground("XXDDXX");
		}catch(BaeException e){
			Assert.assertEquals("invalid RGB color[XXDDXX]", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetBackground(){
		try{
			qrcode.setBackground("ddddd");
		}catch(BaeException e){
			Assert.assertEquals("[RGB] must between 6 and 6", e.getErrmsg());
		}
		
		try{
			qrcode.setBackground("XXDDXX");
		}catch(BaeException e){
			Assert.assertEquals("invalid RGB color[XXDDXX]", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetVersion(){
		try{
			qrcode.setVersion(-1);
		}catch(BaeException e){
			Assert.assertEquals("[version] must between 0 and 30", e.getErrmsg());
		}
		
		try{
			qrcode.setVersion(31);
		}catch(BaeException e){
			Assert.assertEquals("[version] must between 0 and 30", e.getErrmsg());
		}
	}
}
