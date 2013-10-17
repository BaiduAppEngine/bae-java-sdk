/***************************************************************************
 * 
 * Copyright (c) 2010 Baidu.com, Inc. All Rights Reserved
 * 
 **************************************************************************/
 
 
 
/**
 * @file NsHead.java
 * @date 2010-9-19
 * @brief NsHead.java requires Apache Commons IO library (http://commons.apache.org/io/)
 *  
 **/

package com.baidu.bae.api.memcache.internal;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
/**
 * Nshead
 *
 */
public class NsHead implements IByteArray {
	private short _id = 0;
	private short _version = 0;
	private int _logid = 0;
	private byte _provider[] = new byte[16];
	private int _magicnum = 0xfb709394;
	private int _reserved = 0;
	private int _bodylen = 0;
	
	public static final int MAGICNUM = 0xfb709394;

	public final static int size = 36;

	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	public NsHead() {
	}
	
	public NsHead(InputStream in) throws IOException {
		readNsHead(in);
	}

	public NsHead(InputStream in, boolean check_magic) throws IOException {
		readNsHead(in, check_magic);
	}

	public void readNsHead(InputStream in) throws IOException {
		_id = EndianUtils.readSwappedShort(in);
		_version = EndianUtils.readSwappedShort(in);
		_logid = EndianUtils.readSwappedInteger(in);
		read(in, _provider);
		_magicnum = EndianUtils.readSwappedInteger(in);
		if (MAGICNUM != _magicnum) {
			throw new IOException("wrong magic num("+_magicnum+"), expect "+MAGICNUM);
		}
		_reserved = EndianUtils.readSwappedInteger(in);
		_bodylen = EndianUtils.readSwappedInteger(in);
	}
	public void readNsHead(InputStream in, boolean check_magic) throws IOException {
		_id = EndianUtils.readSwappedShort(in);
		_version = EndianUtils.readSwappedShort(in);
		_logid = EndianUtils.readSwappedInteger(in);
		read(in, _provider);
		_magicnum = EndianUtils.readSwappedInteger(in);
		if (check_magic) {
			if (MAGICNUM != _magicnum) {
				throw new IOException("wrong magic num("+_magicnum+"), expect "+MAGICNUM);
			}
		}
		_reserved = EndianUtils.readSwappedInteger(in);
		_bodylen = EndianUtils.readSwappedInteger(in);
	}

	public void addContent(IByteArray content) {
		byte[] con = content.toByteArray();
		_bodylen += con.length;
		buffer.write(con, 0, con.length);
	}

	public void addContent(byte[] bs) {
		_bodylen += bs.length;
		buffer.write(bs, 0, bs.length);
	}

	public InputStream getBody() {
		byte[] body = buffer.toByteArray();
		return new ByteArrayInputStream(body);
	}

	public byte[] toByteArray() {
		byte[] result = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			EndianUtils.writeSwappedShort(output, _id);
			EndianUtils.writeSwappedShort(output, _version);
			EndianUtils.writeSwappedInteger(output, _logid);
			output.write(_provider);
			EndianUtils.writeSwappedInteger(output, _magicnum);
			EndianUtils.writeSwappedInteger(output, _reserved);

			byte[] content = buffer.toByteArray();
			assert (_bodylen == content.length);

			EndianUtils.writeSwappedInteger(output, _bodylen);
			output.write(content);

			result = output.toByteArray();
		} catch (IOException e) {
			result = null;
		}

		return result;
	}

	public short getId() {
		return _id;
	}

	public short getVersion() {
		return _version;
	}

	public int getLogid() {
		return _logid;
	}

	public String getProvider() {
		return new String(_provider);
	}

	public int getMagicnum() {
		return _magicnum;
	}

	public int getBodylen() {
		return _bodylen;
	}

	public int getReserved() {
		return _reserved;
	}

	public void setId(short id) {
		this._id = id;
	}

	public void setVersion(short version) {
		this._version = version;
	}

	public void setLogid(int logid) {
		this._logid = logid;
	}

	public void setProvider(String provider) {

		for (int i = 0; i < provider.length() && i < 16; i++) {
			this._provider[i] = (byte) provider.charAt(i);
		}
	}

	public void setReserved(int reserved) {
		this._reserved = reserved;
	}
	
	public static int read(InputStream in, byte[] b) throws IOException {
		int count = 0;
		while (count < b.length) {
			int cnt = in.read(b, count, (b.length - count));
			if (cnt == -1) {
				throw new IOException("read() return -1");
			}
			count += cnt;
		}
		return count;
	}

}

