package com.qiyuesuo.proxy.lang;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * MD5加密工具类
 * </p>
 */
public class MD5 {
	private MessageDigest md5;
	private boolean init = false;

	private void init() {
		if(!init) {
			try {
				this.md5 = MessageDigest.getInstance("MD5");
				init = true;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public MD5() {
		init();
	}

	public MD5(String inStr) {
		init();
		md5.update(inStr.getBytes());
	}

	public MD5(byte[] input) {
		init();
		md5.update(input);
	}
	
	public void update(byte[] input) {
		md5.update(input);
	}

	public String compute() {
		byte[] md5Bytes = this.md5.digest();

		StringBuffer hexValue = new StringBuffer();

		for (byte md5Byte : md5Bytes) {
			int val = md5Byte & 0xFF;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}


	/**
	 * @Description 字符串加密为MD5 中文加密一致通用,必须转码处理： plainText.getBytes("UTF-8")
	 * @param plainText 需要加密的字符串
	 * @return
	 * @throws Exception
	 */
	public static String toMD5(String plainText) {
		if (plainText == null) {
			return null;
		}

		try {
			byte[] data = plainText.getBytes("UTF-8");
			return md5String(data);
		} catch (UnsupportedEncodingException e) {
			// SKIP
		}
		return null;
	}

	public static String md5String(byte[] data) {
		String md5Str = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance(Algorithm.MD5.getKey());
			byte[] buf = md5.digest(data);
			for (byte element : buf) {
				md5Str += CryptUtils.byte2Hex(element);
			}
		} catch (NoSuchAlgorithmException e) {
			// SKIP
		}
		return md5Str;
	}

}
