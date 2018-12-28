package top.ibase4j.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期操作辅助类
 *
 * @author ShenHuaJie
 * @version $Id: DateUtil.java, v 0.1 2014年3月28日 上午8:58:11 ShenHuaJie Exp $
 */
public final class DateUtil {
	private DateUtil() {
	}

	/** 日期格式 **/
	public interface DATE_PATTERN {
		String HHMMSS = "HHmmss";
		String HH_MM_SS = "HH:mm:ss";
		String YYYYMMDD = "yyyyMMdd";
		String YYYY_MM_DD = "yyyy-MM-dd";
		String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
		String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
		String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
		String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	}

	/**
	 * 格式化日期
	 *
	 * @param date
	 * @return
	 */
	public static final String format(Object date) {
		return format(date, DATE_PATTERN.YYYY_MM_DD);
	}

	/**
	 * 格式化日期
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static final String format(Object date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern == null) {
			return format(date);
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 获取日期
	 *
	 * @return
	 */
	public static final String getDate() {
		return format(new Date());
	}

	/**
	 * 获取日期时间
	 *
	 * @return
	 */
	public static final String getDateTime() {
		return format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 获取日期
	 *
	 * @param pattern
	 * @return
	 */
	public static final String getDateTime(String pattern) {
		return format(new Date(), pattern);
	}

	/**
	 * 日期计算
	 *
	 * @param date
	 * @param field
	 * @param amount
	 * @return
	 */
	public static final Date addDate(Date date, int field, int amount) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 字符串转换为日期:不支持yyM[M]d[d]格式
	 *
	 * @param date
	 * @return
	 */
	public static final Date stringToDate(String date) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		String separator = String.valueOf(date.charAt(4));
		String pattern = "yyyyMMdd";
		if (!separator.matches("\\d*")) {
			pattern = "yyyy" + separator + "MM" + separator + "dd";
			if (date.length() < 10) {
				pattern = "yyyy" + separator + "M" + separator + "d";
			}
			pattern += " HH:mm:ss.SSS";
		} else if (date.length() < 8) {
			pattern = "yyyyMd";
		} else {
			pattern += "HHmmss.SSS";
		}
		pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 间隔秒
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static final Integer getBetween(Date startDate, Date endDate) {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		long n = end.getTimeInMillis() - start.getTimeInMillis();
		return (int) (n / 1000l);
	}

	/**
	 * 间隔天数
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static final Integer getDayBetween(Date startDate, Date endDate) {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MILLISECOND, 0);

		long n = end.getTimeInMillis() - start.getTimeInMillis();
		return (int) (n / (60 * 60 * 24 * 1000l));
	}

	/**
	 * 间隔月
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static final Integer getMonthBetween(Date startDate, Date endDate) {
		if (startDate == null || endDate == null || !startDate.before(endDate)) {
			return null;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int year1 = start.get(Calendar.YEAR);
		int year2 = end.get(Calendar.YEAR);
		int month1 = start.get(Calendar.MONTH);
		int month2 = end.get(Calendar.MONTH);
		int n = (year2 - year1) * 12;
		n = n + month2 - month1;
		return n;
	}

	/**
	 * 间隔月，多一天就多算一个月
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static final Integer getMonthBetweenWithDay(Date startDate, Date endDate) {
		if (startDate == null || endDate == null || !startDate.before(endDate)) {
			return null;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int year1 = start.get(Calendar.YEAR);
		int year2 = end.get(Calendar.YEAR);
		int month1 = start.get(Calendar.MONTH);
		int month2 = end.get(Calendar.MONTH);
		int n = (year2 - year1) * 12;
		n = n + month2 - month1;
		int day1 = start.get(Calendar.DAY_OF_MONTH);
		int day2 = end.get(Calendar.DAY_OF_MONTH);
		if (day1 <= day2) {
			n++;
		}
		return n;
	}

	/**
	 * 日期转换 String 类型
	 * 
	 * @param date
	 * @return
	 */
	public static String fmtDT(Date date) {
		String strDate = null;
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strDate = sdf.format(date);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strDate = sdf.format(new Date());
		}
		return strDate;
	}
	/**
	 * 数据转换
	 * 
	 * @param date
	 * @return
	 */
	public static Integer IntegerOf(String num) {
		Integer intv = null;
		if (StringUtils.isNotEmpty(num)) {
			intv=Integer.valueOf(num).intValue();
		} 
		return intv;
	}
	
	/**
	 * 日期转换 String 类型
	 * 
	 * @param date
	 * @return
	 */
	public static String fmtD(Date date) {
		String strDate = null;
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			strDate = sdf.format(date);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			strDate = sdf.format(new Date());
		}
		return strDate;
	}
	/**
	 * 日期转换 String 类型
	 * 
	 * @param date
	 * @return
	 */
	public static String fmtYMD(Date date) {
		String strDate = "";
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
			strDate = sdf.format(date);
		}
		return strDate;
	}
	/**
	 * 根据刑期格式获取年月日期
	 * 
	 * @param yfmt
	 *            10_10_10
	 * @param type
	 *            1年2月3日
	 * @return
	 */
	public static int getnyr(String yfmt, int type) {
		int nyr = 0;
		if (StringUtils.isNotEmpty(yfmt)) {
			if (!yfmt.equals("终身") && !(yfmt.indexOf("无期") > -1) && !(yfmt.indexOf("死") > -1)
					&& !(yfmt.equals("00_00_00"))) {
				String fmt[] = yfmt.split("_");
				if (type == 1) {// 获取年
					String year = fmt[0];
					nyr = Integer.valueOf(year);
				} else if (type == 2) {// 获取年
					String month = fmt[1];
					nyr = Integer.valueOf(month);
				} else if (type == 3) {// 获取年
					String day = fmt[2];
					nyr = Integer.valueOf(day);
				}
			}
		}
		return nyr;
	}

	/**
	 * 根据开始时间和结束时间获取年月日
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束数据
	 * @param type
	 *            1年2月3日
	 * @return
	 */
	public static int getDateBetweenNYR(Date startTime, Date endTime, int type) {
		int nyr = 0;
		if (startTime != null && endTime != null) {
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(startTime);
			c2.setTime(endTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(c2.getTimeInMillis() - c1.getTimeInMillis());
			int n = (calendar.get(Calendar.YEAR) - 1970);
			int m = calendar.get(Calendar.MONTH);
			int t = (calendar.get(Calendar.DATE) - 1);
			int year = n;
			int month = m;
			int day = t;
			if (day >= 15) {
				month = month + 1;
			}
			if (month >= 12) {
				year = year + 1;
				month = month - 12;
			}
			if (type == 1) {
				nyr = year;
			} else if (type == 2) {
				nyr = month;
			} else if (type == 3) {
				nyr = day;
			}
		}

		return nyr;
	}

	/**
	 * 根据开始日期和增加幅度计算日期
	 * 
	 * @param date
	 *            结束日期
	 * @param xqfmt
	 *            减去的幅度
	 * @return
	 */
	public static String dateJuge(Date date, String xqfmt) {
		Date td = null;
		if (date != null && StringUtils.isNotEmpty(xqfmt) && xqfmt.indexOf("_") > -1) {
			String xq[] = StringUtils.split(xqfmt, "_");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, -Integer.valueOf(xq[0]));
			cal.add(Calendar.MONTH, -Integer.valueOf(xq[1]));
			cal.add(Calendar.DATE, -Integer.valueOf(xq[2]));
			td = cal.getTime();
		}
		return fmtD(td);
	}
	/**
	 * 字符串转换为日期:不支持yyM[M]d[d]格式
	 *
	 * @param date
	 * @return
	 */
	public static final Date fmtStringToDate(String date) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		String separator = String.valueOf(date.charAt(4));
		String pattern = "yyyyMMdd";
		if (!separator.matches("\\d*")) {
			pattern = "yyyy" + separator + "MM" + separator + "dd";
			if (date.length() < 10) {
				pattern = "yyyy" + separator + "M" + separator + "d";
			}
			pattern += " HH:mm:ss.SSS";
		} else if (date.length() < 8) {
			pattern = "yyyyMd";
		} else {
			pattern += "HHmmss.SSS";
		}
		pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * @param xq 例如：10_00_00格式后10年
	 * @return
	 */
 public static String xqFmt(String xq){
	if(StringUtils.isNotEmpty(xq)&&!xq.equals("00_00_00")){
		if(!"无期徒刑".equals(xq) &&!"死刑,缓期二年执行".equals(xq)&&!"死刑".equals(xq)&&!"减余刑".equals(xq)&&!"终身".equals(xq)){
			String [] strs=xq.split("_");
			int n=Integer.parseInt(strs[0], 10);
			int y=Integer.parseInt(strs[1], 10);
			int r=Integer.parseInt(strs[2], 10);
			String yxq="";
			if(n>0){
				yxq+=n+"年";
			}
			if(y>0){
				yxq+=y+"个月";
			}
			if(r>0){
				yxq+=r+"日";
			}   
		   return yxq;
		}else{
			return xq;	
		}
	}else{
		return "";
	}
  }
 
}


