package com.baidu.bae.api.image;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.bae.api.factory.BaeFactory;

public class BaeImageSceneTest {
	private static String AK = "bGUoCievDf4XIjoIYqk7xao2";
	private static String SK = "pNceSAlS4HB8fToDilmXQvwSc6nHInHW";
	private static String HOST = "imageui.newoffline.bae.baidu.com";
	private static String secret = null;
	BaeImageService service = BaeFactory.getBaeImageService(AK, SK, HOST);
	String url = "http://hiphotos.baidu.com/baidu/pic/item/81b7ac86c57a211b66096e75.jpg";

	
	@Test
	public void testTransform() {
		Image image = new Image();
		image.setURL(url);
		
		Transform transform = new Transform();
		transform.setRotation(145);
		byte[] bs = service.applyTransform(image, transform);

		File f = new File("D:/test.jpg");
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bs);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testAnnotate() {
		Image image = new Image(url);
		Annotate annotate = new Annotate("百度云");
		//annotate.setText("百度云");
		annotate.setFont(1,30,"000000");
		annotate.setOpacity(0.99f);
		annotate.setQuality(20);
		byte[] bs = service.applyAnnotate(image, annotate);

		File f = new File("D:/test.jpg");
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bs);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testQRCode() {
		QRCode qrcode = new QRCode("百度云");
		//qrcode.setText("百度云");
		//qrcode.setLevel(4);
		//qrcode.setSize(90);
		//qrcode.setBackground("AABBCC");
		//qrcode.setVersion(10);
		byte[] bs = service.applyQRCode(qrcode);

		File f = new File("D:/test.jpg");
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bs);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testComposite() {
		Image image1 = new Image();
		image1.setURL(url);
		Image image2 = new Image(url);
		Composite com1 = new Composite(image1);
		Composite com2 = new Composite();
		com1.setAnchor(ImageConstant.POS_TOP_LEFT);
		com1.setOpacity(0.9f);
		com2.setImage(image2);
		com2.setAnchor(ImageConstant.POS_CENTER_CENTER);
		com2.setPos(-100, -200);
		ArrayList<Composite> composites = new ArrayList<Composite>();
		composites.add(com1);
		composites.add(com2);
		byte[] bs = service.applyComposite(composites);

		File f = new File("D:/test.jpg");
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bs);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGengerateVCode() {
		VCode vc = new VCode();
		//vc.setLen(5);
		Map<String,String> data = service.generateVCode(vc);
		System.out.println("imgurl:" + data.get("imgurl"));
		System.out.println("secret:" + data.get("secret"));
		this.secret = data.get("secret");
		System.out.println(this.secret.length());
		
	}
	
	@Test
	public void testVerifyVCode() {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		VCode vc = new VCode();
		vc.setSecret(this.secret);
		vc.setInput(input);
		Map<String,String> data = service.verifyVCode(vc);
		System.out.println("status:" + data.get("status"));
		System.out.println("reason:" + data.get("reason"));
		Assert.assertEquals(0, Integer.valueOf(data.get("status")).intValue());
	}
}
