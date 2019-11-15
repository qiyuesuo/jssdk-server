package com.qiyuesuo.proxy.lang;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	/**
	 * 修改日期,对指定日期的某一类型增加指定长度
	 * 
	 * @param date   日期
	 * @param field  需要修改的日期类型（例：日-Calendar.DAY_OF_MONTH）
	 * @param amount 修改长度（例：日-1）
	 */
	public static Date changeDate(Date date, int field, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(field, 1);
		return c.getTime();
	}
}
