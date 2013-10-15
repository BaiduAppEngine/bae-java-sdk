package com.baidu.bae.api.image;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.bae.api.exception.BaeException;
import com.baidu.bae.api.factory.BaeFactory;
//@RunWith(Parameterized.class)
public class BaeImageAnnotateTest {
	private static String AK = "bGUoCievDf4XIjoIYqk7xao2";
	private static String SK = "pNceSAlS4HB8fToDilmXQvwSc6nHInHW";
	private static String HOST = "imageui.newoffline.bae.baidu.com";
	private String funName;
	private String errMsg;
	private String input;
	
		BaeImageService service = BaeFactory.getBaeImageService(AK, SK, HOST);
		String url = "http://hiphotos.baidu.com/baidu/pic/item/81b7ac86c57a211b66096e75.jpg";
		Annotate annotate = new Annotate();
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
		
	
	/*@Parameters
	public static Collection dataProvider(){
		Object[][] data = {
				{"setText", text, "[text length] must between 1 and 500"},
				{"setText", "", "[text length] must between 1 and 500"},
				{"setOpacity", -1, "[opacity] must between 0.0 and 1.0"},
							};
		return Arrays.asList(data);
	}
	
	public BaeImageAnnotateTest(String funName, String input, String errMsg){
		this.errMsg = errMsg;
		this.funName = funName;
		this.input = input;
	}*/

	@Test
	public void testSetText() {
		try{
			annotate.setText(text);
		}catch(BaeException e){
				Assert.assertEquals("[text length] must between 1 and 500", e.getErrmsg());
			
		}
	}
	
	@Test
	public void testSetOpacity(){
		try{
			annotate.setOpacity(1.2f);
		}catch(BaeException e){
			Assert.assertEquals("[opacity] must between 0.0 and 1.0", e.getErrmsg());
		}
		
		try{
			annotate.setOpacity(-1);
		}catch(BaeException e){
			Assert.assertEquals("[opacity] must between 0.0 and 1.0", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetFont(){
		try{
			annotate.setFont(-1, 10, "112233");
		}catch(BaeException e){
			Assert.assertEquals("[font name] must between 0 and 4", e.getErrmsg());
		}
		
		try{
			annotate.setFont(0, -1, "112233");
		}catch(BaeException e){
			Assert.assertEquals("[font size] must between 0 and 1000", e.getErrmsg());
		}
		
		try{
			annotate.setFont(0, 11, "11223");
		}catch(BaeException e){
			Assert.assertEquals("[RGB] must between 6 and 6", e.getErrmsg());
		}
		
		try{
			annotate.setFont(0, 11, "xxffdd");
		}catch(BaeException e){
			Assert.assertEquals("invalid RGB color[xxffdd]", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetQuality(){
		try{
			annotate.setQuality(-1);
		}catch(BaeException e){
			Assert.assertEquals("[quality] must between 0 and 100", e.getErrmsg());
		}
		
		try{
			annotate.setQuality(101);
		}catch(BaeException e){
			Assert.assertEquals("[quality] must between 0 and 100", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetOutputType(){
		try{
			annotate.setOutputType(-1);
		}catch(BaeException e){
			Assert.assertEquals("[output type] must between 0 and 4", e.getErrmsg());
		}
		
		try{
			annotate.setQuality(5);
		}catch(BaeException e){
			Assert.assertEquals("[output type] must between 0 and 4", e.getErrmsg());
		}
	}
	
	@Test
	public void testSetPos(){
		try{
			annotate.setPos(-1, 0);
		}catch(BaeException e){
			Assert.assertEquals("[x_offset] must between 0 and 2147483647", e.getErrmsg());
		}
		
		try{
			annotate.setPos(0, -1);
		}catch(BaeException e){
			Assert.assertEquals("[y_offset] must between 0 and 2147483647", e.getErrmsg());
		}
		
	}
}
