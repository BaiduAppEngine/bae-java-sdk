package com.baidu.bae.api.memcache.local;

public class McpackAndNShead {

	protected String byteToString(byte[] buff) {
		StringBuffer sb = new StringBuffer();
		sb.append(buff[0]);
		for (int i=1;i<buff.length;i++) {
			sb.append("," + buff[i]);
		}
		return sb.toString();
	}
	protected byte[] stringToByte(String str) {
		String[] strs = str.split(",");
		byte[] buff = new byte[strs.length];
		for (int i=0;i<strs.length;i++) {
			buff[i] = Byte.parseByte(strs[i]);
		}
		return buff;
	}
	protected boolean isBaseType(Object value) {
		return (
				value instanceof Byte            ||
				value instanceof Integer         ||
				value instanceof Long            ||
				value instanceof Short
				)
			? true
			: false;
	}
	public static void main(String[] agrs) {
		McpackAndNShead mn = new McpackAndNShead();
		String str = "-84,-19,0,5,116,0,8,116,101,115,116,95,115,114,116";
		System.out.println(str);
		byte[] b = mn.stringToByte(str);
		System.out.print(b[0]);
		for (int i=1;i<b.length;i++) {
			System.out.print("," + b[i]);
		}
	}
}
