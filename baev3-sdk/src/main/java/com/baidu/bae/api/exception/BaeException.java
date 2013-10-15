package com.baidu.bae.api.exception;

import java.util.HashMap;
import java.util.Map;

public class BaeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private int errno;
	
	private String errmsg;
	
	

	/**
	 * sdk内部错误
	 */
	public static final int SDK_ERROR = 1;
	
	/**
	 * 初始化异常
	 */
	public static final int INITIAL_ERROR = 2;
	
	/**
	 * 参数异常
	 */
	public static final int PARAM_ERROR = 3;
	/**
	 * 服务端正常响应但返回信息解析出错
	 */
	public static final int HTTP_ERROR_AND_JSON_ERROR = 4;
	/**
	 * 服务端返回错误且返回信息解析出错
	 */
	public static final int HTTP_OK_BUT_JSON__ERROR = 5;
	
	
	
	private static Map<Integer,String> errors = new HashMap<Integer,String>();
	static {
		errors.put(SDK_ERROR, "java sdk error");
		errors.put(PARAM_ERROR, "param error");
		errors.put(HTTP_ERROR_AND_JSON_ERROR, "http status is error, and the body returned is not a json string");
		errors.put(HTTP_OK_BUT_JSON__ERROR, "http status is ok, but the body returned is not a json string");
		errors.put(INITIAL_ERROR, "java sdk initial error");
	}
	
	/**
	 * 抛出异常
	 * @param errno  错误码
	 * @param cause  错误堆栈
	 */
	public BaeException(int errno, Throwable cause) {
    	super(errno + " : " + errors.get(errno), cause);
    	this.errno = errno;
    	this.errmsg = errors.get(errno);
    }
	
	/**
	 * 抛出异常
	 * @param errno   错误码
	 * @param message 错误信息
	 * @param cause   错误堆栈
	 */
    public BaeException(int errno, String message, Throwable cause) {
    	super(errno + " : " + message, cause);
    	this.errno = errno;
    	this.errmsg = message;
    }
    
    /**
     * 抛出异常
     * @param errno 错误码
     * @param message 错误信息
     */
    public BaeException(int errno, String message) {
    	super(errno + " : " + message);
    	this.errno = errno;
    	this.errmsg = message;
    }
    
    
    /**
     * 抛出异常
     * @param errno 错误码
     */
    public BaeException(int errno) {
    	super(errno + " : " + errors.get(errno));
    	this.errno = errno;
    	this.errmsg = errors.get(errno);
    }

	public int getErrno() {
		return errno;
	}


	public String getErrmsg() {
		return errmsg;
	}


    
	
}
