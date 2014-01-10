package com.baidu.bae.api.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.thrift.TException;

import com.baidu.bae.api.log.client.LogClient;
import com.baidu.bae.api.log.schema.BaeLogLevel;
import com.baidu.bae.api.log.schema.SecretEntry;

public class BaeLogAppender extends AppenderSkeleton{
	public BaeLogAppender(){
		_secret = new SecretEntry();
	}
	
	@Override
	public void finalize(){
		close();
	}
	
	@Override
	public void close() {
		_client.close();
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	protected void append(LoggingEvent event) {
		if (null == _client){
			if (!_secret.isSetUser() || !_secret.isSetPasswd()){
				LogLog.warn("AK or SK not set to appender");
				return;
			}
			_client = new LogClient(_secret, _bufcount);
		}
		BaeLogLevel level = _getLogLevel(event.getLevel());
		String      tag   = event.getLoggerName();
		String      msg;
		if (this.getLayout() != null){
			msg   = this.getLayout().format(event);
		} else {
			msg   = (String)event.getMessage();
		}
		try {
			_client.log(msg, level, tag, event.timeStamp);
		} catch (TException e) {
			LogLog.warn("print log error : " + e.getMessage());
		}
	}
	
	private BaeLogLevel _getLogLevel(Level level) {
		if (level == Level.FATAL) {
			return BaeLogLevel.FATAL;
		}
		if (level == Level.WARN){
			return BaeLogLevel.WARNING;
		}
		if (level == level.INFO) {
			return BaeLogLevel.NOTICE;
		}
		if (level == level.ERROR) {
			return BaeLogLevel.FATAL;
		}
		if (level == level.TRACE) {
			return BaeLogLevel.TRACE;
		}
		if (level == level.DEBUG) {
			return BaeLogLevel.DEBUG;
		}
		else {
			return BaeLogLevel.NOTICE;
		}
	}
	
	public void setAk(String ak){
		_secret.user = ak;
		_secret.setUserIsSet(true);
	}
	public void setSk(String sk){
		_secret.passwd = sk;
		_secret.setPasswdIsSet(true);
	}
	
	public void setBufcount(int bufcount) {
		this._bufcount = bufcount;
	}
	
	private SecretEntry _secret;	
	private int         _bufcount = 0;
	private LogClient _client = null;
}
