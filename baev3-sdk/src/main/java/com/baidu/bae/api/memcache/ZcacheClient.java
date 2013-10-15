package com.baidu.bae.api.memcache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.baidu.gson.JsonObject;
import com.baidu.mcpack.McpackException;

public class ZcacheClient {
	private ZcacheSocket[] sockets;
	//private SockIOPool pool;
	private McpackAndNShead man;
	private int N;
	
	private String[] hosts;
	private int timeout;
	
	protected ZcacheClient(String[] hosts, int timeout) {
		this.hosts = hosts;
		this.timeout = timeout;
		this.N = hosts.length;
		init();
		
	}
	private void init() {
		/*this.pool = SockIOPool.getInstance();
		this.pool.setServers(ZcacheConf.HOSTS);
		Integer[] weights = {3};
        pool.setWeights( weights );

        // 设置初始连接数、最小和最大连接数以及最大处理时间
        pool.setInitConn( 5 );
        pool.setMinConn( 5 );
        pool.setMaxConn( 250 );
        pool.setMaxIdle( 1000 * 60 * 60 * 6 );

        // 设置主线程的睡眠时间
        pool.setMaintSleep( 30 );

        // 设置TCP的参数，连接超时等
        pool.setNagle( false );
        pool.setSocketTO( 3000 );
        pool.setSocketConnectTO( 0 );

        // 初始化连接池
        pool.initialize();*/
		
		sockets = new ZcacheSocket[N];
		for (int i=0;i<N;i++) {
			String[] adds = this.hosts[i].trim().split(":");
			String ip = adds[0].trim();
			int port = Integer.parseInt(adds[1].trim());
			sockets[i] = new ZcacheSocket(ip, port, this.timeout);
		}
		man = new McpackAndNShead();
	}
	
	
	
	public String[] getHosts() {
		return hosts;
	}
	public void setHosts(String[] hosts) {
		this.hosts = hosts;
		N = this.hosts.length;
		init();
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
		init();
	}
	private byte[] _talkWithServer(byte[] request) {
		int n = N;
		while (n-- >= 0) {
			int r =  new Random().nextInt(N);
			byte[] b = null;
			//SockIOPool.SockIO sock = this.pool.getSock(ZcacheConf.HOSTS[r]);
			ZcacheSocket sock = sockets[r];
			if (sock != null) {
				//b = sockets[r].getResponseFromSever(request);
				b = sock.getResponseFromSever(request);
			}
			if (b==null) {
				continue;
			}
			return b;
		}
		return null;
	}
	
	private JsonObject talkWithServerManager(String pname, String token,String appid, long login, Map<String,Long> userinfo, String cmd) throws McpackException {
		byte[] request = man.makePackManager(pname, token, login, cmd, userinfo);
		byte[] response = _talkWithServer(request);
		//System.out.println("response json is " + man.byteToJson(response));
		return man.byteToJson(response);
	}
	
	private JsonObject talkWithServer(String pname, String token,String appid, long login, String cmd, Map<String,Object> query_map, long expiry) throws McpackException, IOException {
		byte[] request  = man.makePack(pname, token,appid, login, cmd, expiry, query_map);
		byte[] response = _talkWithServer(request);
		if (response == null) {
			return null;
		}
		//System.out.println("response json is " + man.byteToJson(response));
		return man.byteToJson(response);		
	}	
	
	private JsonObject addSetReplace(String pname, String token,String appid, long login, String cmd, String key, Object value, long expiry) throws McpackException, IOException {
		Map<String,Object> query_map = new HashMap<String,Object>();
		query_map.put(key, value);
		return talkWithServer(pname, token,appid, login, cmd, query_map, expiry);
	}
	
	private JsonObject incrdecr(String pname, String token,String appid, long login, String cmd, String key, Long inc, long expiry) throws McpackException, IOException {
		Map<String,Object> query_map = new HashMap<String,Object>();
		query_map.put(key, inc);
		return talkWithServer(pname, token,appid, login, cmd, query_map, expiry);
	}
	
	protected JsonObject addOne(String pname, String token,String appid, long login, String key, Object value, long expiry) throws McpackException, IOException {
		return addSetReplace(pname, token,appid, login, "add", key, value, expiry);
	}
	
	protected JsonObject setOne(String pname, String token,String appid, long login, String key, Object value, long expiry) throws McpackException, IOException {
		return addSetReplace(pname, token,appid, login, "set", key, value, expiry);
	}
	
	protected JsonObject replaceOne(String pname, String token,String appid, long login, String key, Object value, long expiry) throws McpackException, IOException {
		return addSetReplace(pname, token,appid, login, "replace", key, value, expiry);
	}
	
	protected JsonObject deleteOne(String pname, String token,String appid, long login, String key, long expiry) throws McpackException, IOException {
		return addSetReplace(pname, token,appid, login, "delete", key, null, expiry);
	}
	
	protected JsonObject increment(String pname, String token,String appid, long login, String key, Long inc, long expiry) throws McpackException, IOException {
		return incrdecr(pname, token,appid, login, "increment", key, inc, expiry);
	}
	
	protected JsonObject decrement(String pname, String token,String appid, long login, String key, Long dec, long expiry) throws McpackException, IOException {
		return incrdecr(pname, token,appid, login, "decrement", key, dec, expiry);
	}
	
	protected JsonObject getOne(String pname, String token,String appid, long login, String key) throws McpackException, IOException {
		Map<String,Object> query_map = new HashMap<String,Object>();
		query_map.put(key, null);
		return talkWithServer(pname, token,appid, login, "get", query_map, -1);
	}
	
	protected JsonObject setMulti(String pname, String token,String appid, long login, Map<String,Object> query_map, long expiry) throws McpackException, IOException {
		return talkWithServer(pname, token,appid, login, "set", query_map, 0);
	}
	
	protected JsonObject getMulti(String pname, String token,String appid, long login, Map<String,Object> keys) throws McpackException, IOException {
		/*Map<String,Object> query_map = new HashMap<String,Object>();
		int size = keys.size();
		for (int i=0;i<size;i++) {
			query_map.put(keys.get(i), null);
		}*/
		return talkWithServer(pname, token,appid, login, "get", keys, -1);
	}
	
	
	
	protected JsonObject addUser(String pname, String token,String appid, long login, long block_num, long max_query_persec, long disk_size, long mem_size, long del_strategy) throws McpackException {
		Map<String,Long> userinfo_map = new HashMap<String,Long>();
		userinfo_map.put("block_num", block_num);
		userinfo_map.put("max_query_persec", max_query_persec);
		userinfo_map.put("disk_size", disk_size);
		userinfo_map.put("mem_size", mem_size);
		userinfo_map.put("del_strategy", del_strategy);
		return talkWithServerManager(pname, token,appid, login, userinfo_map, "addUser");
	}
	
	protected JsonObject modifyUser(String pname, String token,String appid, long login, long block_num, long max_query_persec, long disk_size, long mem_size, long del_strategy) throws McpackException {
		Map<String,Long> userinfo_map = new HashMap<String,Long>();
		userinfo_map.put("block_num", block_num);
		userinfo_map.put("max_query_persec", max_query_persec);
		userinfo_map.put("disk_size", disk_size);
		userinfo_map.put("mem_size", mem_size);
		userinfo_map.put("del_strategy", del_strategy);
		return talkWithServerManager(pname, token,appid, login, userinfo_map, "modifyUser");
	}
	
	protected JsonObject removeUser(String pname, String token,String appid, long login) throws McpackException {
		return talkWithServerManager(pname, token,appid, login, null, "removeUser");
	}
	
	protected JsonObject getUserConf(String pname, String token,String appid, long login) throws McpackException {
		return talkWithServerManager(pname, token,appid, login, null, "getUserConf");
	}
	//服务器端为实现的接口
	protected JsonObject getStat(String pname, String token,String appid, long login) throws McpackException {
		return talkWithServerManager(pname, token,appid, login, null, "getStat");
	}
}
