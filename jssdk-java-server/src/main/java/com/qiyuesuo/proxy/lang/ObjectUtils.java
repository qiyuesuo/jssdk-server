package com.qiyuesuo.proxy.lang;

public class ObjectUtils {
	
	public static boolean isAnyNull(Object... objects) {
		if (objects == null) {
			return true;
		}
		for (Object object : objects) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}
}
