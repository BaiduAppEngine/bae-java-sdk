package com.baidu.bae.api.zcache;

import java.io.Serializable;

public class ObjectTest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1718215339194931294L;
	private String str;
	private byte b;
	private short s;
	private int i;
	private long l;
	private float f;
	private double d;
	private char c;
	private boolean bool;

	public static class innerClass {
		public static String in_str;
		public static int in_int;
	}
	public String getInnerStr() {
		return innerClass.in_str;
	}
	public int getInnerInt() {
		return innerClass.in_int;
	}
	
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public byte getB() {
		return b;
	}
	public void setB(byte b) {
		this.b = b;
	}
	public short getS() {
		return s;
	}
	public void setS(short s) {
		this.s = s;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public long getL() {
		return l;
	}
	public void setL(long l) {
		this.l = l;
	}
	public float getF() {
		return f;
	}
	public void setF(float f) {
		this.f = f;
	}
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	public char getC() {
		return c;
	}
	public void setC(char c) {
		this.c = c;
	}
	public boolean isBool() {
		return bool;
	}
	public void setBool(boolean bool) {
		this.bool = bool;
	}
	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof ObjectTest) 
				&& (this.bool == ((ObjectTest)obj).bool) && (this.b == ((ObjectTest)obj).b) && (this.c == ((ObjectTest)obj).c) 
				&& (this.d == ((ObjectTest)obj).d) && (this.f == ((ObjectTest)obj).f) && (this.i == ((ObjectTest)obj).i) 
				&& (this.l == ((ObjectTest)obj).l) && (this.s == ((ObjectTest)obj).s) && (this.str == ((ObjectTest)obj).str) 
				&& (this.getInnerInt() == ((ObjectTest)obj).getInnerInt()) 
				&& (this.getInnerStr() == ((ObjectTest)obj).getInnerStr()) ) {
			return true;
		}
		return false;
	}
}
