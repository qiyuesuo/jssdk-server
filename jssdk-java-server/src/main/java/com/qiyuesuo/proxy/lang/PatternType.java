package com.qiyuesuo.proxy.lang;

/**
 * 
 * 时间格式
 *
 */
public enum PatternType {
	
	DEFAULT_PATTERN("yyyy-MM-dd"),
	STANDARD_PATTERN("yyyy-MM-dd HH:mm:ss");
	
	private String pattern;
	
	PatternType(String pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public String toString() {
		return this.pattern;
	}
}
