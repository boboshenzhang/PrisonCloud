package top.ibase4j.core.util;

import java.text.SimpleDateFormat;

public class DateConstant {

	public static final String FORMAT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE_PATTERN_BEGIN_DATE = "yyyy-MM-dd 00:00:00";
	public static final String FORMAT_TIME_PATTERN = "yyyyMMddHHmmssSSS";
	public static final String FORMAT_DATE_PATTERN_END_DATE = "yyyy-MM-dd 23:59:59";
	public static final String FORMAT_DATE_PATTERN_DAY = "yyyy-MM-dd";
	public static final String FORMAT_YEAR_MONTH_PATTERN = "yyyyMM";
	
	/**
	 * 常规时间格式为：yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(FORMAT_DATE_PATTERN);
	/**
	 * 常规日期格式为：yyyy-MM-dd
	 */
	public static final SimpleDateFormat FORMAT_DAY = new SimpleDateFormat(FORMAT_DATE_PATTERN_DAY);
	/**
	 * 格式为：yyyyMMddHHmmssSSS
	 */
	public static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat(FORMAT_TIME_PATTERN);
	/**
	 * 格式为：yyyyMM
	 */
	public static final SimpleDateFormat FORMAT_YEAR_MONTH = new SimpleDateFormat(FORMAT_YEAR_MONTH_PATTERN);
}
