package top.ibase4j.core.util;

import java.util.Date;

public class DateConvert {

	/**
	 * 将传入的日期转为日期字符串，格式：yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String formatToDateString(Date date) {
		return DateConstant.FORMAT_DATE.format(date);
	}
	
	/**
	 * 得到当前时间的日期字符串，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatToCurDateString() {
		return formatToDateString(new Date());
	}
	
	/**
	 * 将传入的日期转为时间串，精确到毫秒，格式：yyyyMMddHHmmssSSS
	 * @param date
	 * @return
	 */
	public static String formatToTimeString(Date date) {
		return DateConstant.FORMAT_TIME.format(date);
	}
	
	/**
	 * 得到当前的时间串，精确到毫秒，格式：yyyyMMddHHmmssSSS
	 * @return
	 */
	public static String formatToCurTimeString() {
		return formatToTimeString(new Date());
	}
	
	/**
	 * 获取当前日期的年月，格式：yyyyMM
	 * @return
	 */
	public static String formatToCurYearMonth() {
		return DateConstant.FORMAT_YEAR_MONTH.format(new Date());
	}
}
