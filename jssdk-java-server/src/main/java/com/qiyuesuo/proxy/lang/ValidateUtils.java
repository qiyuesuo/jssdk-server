package com.qiyuesuo.proxy.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qiyuesuo.proxy.lang.StringUtils;

/**
 * 校验工具类
 */
public class ValidateUtils {
	
	private static char[] baseCodes = "0123456789ABCDEFGHJKLMNPQRTUWXY".toCharArray();
	private static int[] wi = { 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28 };

	/**
	 * 判断邮箱格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(final String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		Pattern pattern = Pattern.compile(
				"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断手机号码格式是否正确
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(final String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^(\\+86|86)?1[0-9]{10}$");
		Matcher matcher = pattern.matcher(mobile);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	

	/**
	 * 校验密码格式最少8位，最多20位，至少含有数字，字母，特殊字符中的两种
	 * @param password
	 * @return
	 */
	public static boolean validPW(final String password) {
		String pw = StringUtils.trim(password);
		if (StringUtils.isBlank(pw)) {
			return false;
		}
		if (pw.matches("((?=.*\\d)(?=.*\\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{8,20}$")) {
			return true;
		}
		return false;
	} 
	
	/**
	 * 校验签署密码，只能是6位数字
	 * @param password
	 * @return
	 */
	public static boolean validSignPW(final String password){
		String pw = StringUtils.trim(password);
		if (StringUtils.isBlank(pw)) {
			return false;
		}
		if (pw.matches("^[0-9]{6}$")) {
			return true;
		}
		return false;
	}
	

	/**
	 * 统一社会信用代码校验
	 * @param uscc
	 * @return true: 格式正确; false: 格式错误
	 */
	public static boolean validateUscc(String uscc) {
		if (uscc == null || uscc.length() != 18) {
			return false;
		}
		char[] usccCodes = uscc.toCharArray();
		Map<Character, Integer> codes = new HashMap<Character, Integer>();
		for (int i = 0; i < baseCodes.length; i++) {
			codes.put(baseCodes[i], i);
		}
		char check = usccCodes[17];
		if (codes.get(check) == null) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < 17; i++) {
			char key = usccCodes[i];
			if (codes.get(key) == null) {
				return false;
			}
			sum += ((int) codes.get(key) * wi[i]);
		}
		int value = 31 - sum % 31;
		value = value == 31 ? 0 : value;
		return value == (int) codes.get(check);
	}
	
	/**
	 * 工商注册号校验
	 * @param registerNo
	 * @return true: 格式正确; false: 格式错误
	 */
	public static boolean validateRegister(String registerNo) {
		try {
			if (registerNo == null || registerNo.length() != 15) {
				return false;
			}
			int m = 10;
			int[] s = new int[15];
			int[] p = new int[15];
			int[] a = new int[15];
			p[0] = m;
			for (int i = 0; i < 15; i++) {
				a[i] = Integer.valueOf(registerNo.substring(i, i + 1));
				s[i] = (p[i] % (m + 1)) + a[i];
				if (i >= 14) {
					break;
				}
				if (0 == s[i] % m) {
					p[i + 1] = m * 2;
				} else {
					p[i + 1] = (s[i] % m) * 2;
				}
			}
			if (1 == (s[14] % m)) {
				return true;
			}
		} catch (Exception e) {
		}
		
		return false;
	}
	
	/**
	 * 校验整数
	 * @param num
	 * @return
	 */
	public static boolean validNum(final String num){
		if (StringUtils.isEmpty(num)) {
			return false;
		}
		Pattern pattern = Pattern.compile("-?[1-9]+[0-9]*|0");
		Matcher matcher = pattern.matcher(num);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

}
