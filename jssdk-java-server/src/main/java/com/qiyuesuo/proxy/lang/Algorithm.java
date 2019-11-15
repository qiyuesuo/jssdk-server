package com.qiyuesuo.proxy.lang;

/**
 * <p>
 * 算法类型枚举类
 * </p>
 */
public enum Algorithm {
	MD5("MD5"), SHA1("SHA-1"),SHA256("SHA-256"), AES("AES"), RSA("RSA");

	/** 主键 */
	private final String key;

	Algorithm(final String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
