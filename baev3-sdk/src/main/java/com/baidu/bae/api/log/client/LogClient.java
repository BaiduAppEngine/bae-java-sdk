package com.baidu.bae.api.log.client;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.helpers.LogLog;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.baidu.bae.api.log.schema.BaeLog;
import com.baidu.bae.api.log.schema.BaeLogEntry;
import com.baidu.bae.api.log.schema.BaeLogLevel;
import com.baidu.bae.api.log.schema.BaeRet;
import com.baidu.bae.api.log.schema.SecretEntry;
import com.baidu.bae.api.log.schema.UserLogEntry;
import com.baidu.bae.api.log.schema.logchain;

public class LogClient {
	public LogClient(SecretEntry secret, int bufcount){
		if (bufcount == 0) {
			this._bufcount = DEFAULT_FLUSH_COUNT;
		}
		
		_secret = secret;
		_logs = new ArrayList<BaeLogEntry>();
		get_env();
		_socket    = new TSocket(_host, _port);
		_socket.setTimeout(DEFAULT_SOCKET_TIMEOUT);
		_transport = new TFramedTransport(_socket);
		_protocol  = new TBinaryProtocol(_transport);
		_client    = new logchain.Client(_protocol);
		_last_flush_time = System.currentTimeMillis();
	}
	
	private void get_env(){
		final Map<String, String> env = System.getenv();
		_appid = env.get("BAE_ENV_APPID");
		_host = env.get("BAE_ENV_LOG_HOST");
		try {
			_port = Integer.parseInt(env.get("BAE_ENV_LOG_PORT"));
		} catch (final NumberFormatException e) {
			LogLog.warn("log port non-exist or not an integer");
			_ok = false;
			return;
		}
		if (null == _appid || null == _host) {
			LogLog.warn("Please don't use baelog outside BAE V3 environment");
			_ok = false;
			return;
		}
		_ok = true;
	}
	
	public void open() throws TTransportException{
		if (_ok)
			_transport.open();
	}
	
	public boolean isOpen(){
		if (!_ok) {
			return false;	
		}
		return _transport.isOpen();
	}
	
	public void log(String log, BaeLogLevel level, long timestamp) throws TException {
		log(log, level, null, timestamp);
	}
	
	public void flush(){
		BaeRet ret = BaeRet.RETRY;
		int retry = 3;
		while (retry > 0) {
			try {
		        if (!isOpen()){
			        open();
		        }
				BaeLog log   = new BaeLog();
				log.secret   = _secret;
				log.messages = _logs;
				ret = _client.log(log);
			} catch (TException e) {
				LogLog.warn(e.getMessage());
				ret = BaeRet.RETRY;
			}
			if (ret == BaeRet.OK){
				_logs = new ArrayList();
				_last_flush_time = System.currentTimeMillis();
				break;
			}
			retry--;
		}
		if (retry == 0){
			LogLog.warn("Log to BAE error");
		}
	}
	
	public boolean shouldFlush(){
		long now = System.currentTimeMillis();
		
		if (now - _last_flush_time >= DEFAULT_FLUSH_INTERVAL 
				|| _logs.size() >= this._bufcount) {
			return true;
		}
		return false;
	}
	
	public void close() {
		if (_ok){
			flush();
			_transport.close();
		}
	}
	
	public void log(String log, BaeLogLevel level, String tag, long timestamp) throws TException{
		if (!_ok) {
			LogLog.warn("BaeLog env error");
			return;
		}
		
		UserLogEntry userlog = new UserLogEntry();
		userlog.appid = _appid;
		userlog.level = level;
		userlog.msg   = log;
		userlog.timestamp = timestamp;
		if (tag != null) {
			userlog.tag = tag;
		}
		byte[] buffer = new TSerializer().serialize(userlog);
		
		BaeLogEntry baelog = new BaeLogEntry();
		baelog.category = "user";
		baelog.content  = ByteBuffer.wrap(buffer);
		_logs.add(baelog);
		if (shouldFlush()){
			flush();
		}
	}

	List<BaeLogEntry>  _logs;
	SecretEntry        _secret;
	private boolean    _ok;
	private TSocket    _socket;
	private TTransport _transport;
	private TProtocol  _protocol;
	private logchain.Client _client;
	private String _appid = null, _host = null;
	private int _port;
	private long _last_flush_time;
	private int  _bufcount;

	private final static int DEFAULT_PORT = 7000;
	private final static int DEFAULT_FLUSH_INTERVAL = 3000; //3s
	private final static int DEFAULT_FLUSH_COUNT=200;
	private final static int DEFAULT_SOCKET_TIMEOUT = 5000; //5s
}
