/***************************************************************************
 * 
 * Copyright (c) 2010 Baidu.com, Inc. All Rights Reserved
 * 
 **************************************************************************/
 
 
 
/**
 * @file ZcacheSocket.java
 * @author mengxiansen(mengxiansen@baidu.com)
 * @date 2012-02-29
 * @brief Connect Zcache server
 *  
 **/
package com.baidu.bae.api.memcache;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baidu.bae.api.memcache.internal.NsHead;
/**
 * @author mengxiansen
 * This class is to connect Zcache server , input bytes(_request), and output bytes(_response)
 * 
 */
public class ZcacheSocket {
	private static Logger logger = Logger.getLogger(ZcacheSocket.class.getName());
	private String _host;
	private int _port;
	private int timeout;
	/**
	 * Constructor
	 * 
	 * @param host     server host
	 * @param port     server port
	 */
	protected ZcacheSocket(String host, int port, int timeout) {
		this._host = host;
		this._port = port;
		this.timeout = timeout;
	}	
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	protected byte[] getResponseFromSever(byte[] request) {
		logger.log(Level.FINE, "connect " + _host + ":" + _port);
		InetAddress address = null;
		//InetSocketAddress saddress = null;
        Socket socket = new Socket();
        BufferedOutputStream out = null;
        long b1 = 0;
        try {
            address = InetAddress.getByName(_host);
            b1 = System.currentTimeMillis();
            SocketAddress sa = new InetSocketAddress(address, _port);
            socket.connect(sa, timeout*1000);            
            out = new BufferedOutputStream(socket.getOutputStream()); 
            out.write(request);
            out.flush();
            
            InputStream ins = socket.getInputStream();
            NsHead nsr = new NsHead(ins);
            byte[] response = new byte[nsr.getBodylen()];
			NsHead.read(ins, response);
			return response;
        } catch (Exception ex) {
        	long b2 = System.currentTimeMillis();
            System.out.println("timeout is " + (b2-b1) + "ms");
            StackTraceElement[] ste = ex.getStackTrace();
			String ex_str = "" + ex + "\n";
			for (int i=0;i<ste.length;i++) {
				ex_str +=  "		" + ste[i].toString() + "\n";
			}
			logger.log(Level.WARNING, "connect sever : " + _host + ":" + _port + " error!!!\n" + "StackTrace : {[(" + ex_str + ")]}\n");
			return null;
        } finally {  
            try { 
                out.close();  
                socket.close();  
            } catch (Exception ex) {  
            	logger.log(Level.WARNING, "close sever : " + _host + ":" + _port + " error!!!\n" + "StackTrace : {[(" + ex + ")]}\n");
               	return null; 
            }  
        } 
	}
}
