package top.ibase4j.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * xml解析工具类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年9月27日 下午2:04:19
 * 
 */

public class XmlUtils {

	protected final static Logger logger = Logger.getLogger(XmlUtils.class);
	private static String PREFIX_CDATA = "<![CDATA[";
	private static String SUFFIX_CDATA = "]]>";

	/**
	 * 初始化XStream可支持某一字段可以加入CDATA标签,如果需要某一字段使用原文,就需要在String类型的text的头加上
	 * "<![CDATA["和结尾处加上"]]>"标签， 以供XStream输出时进行识别
	 * 
	 * @param isAddCDATA
	 *            是否支持CDATA标签
	 */

	public static XStream initXStream(boolean isAddCDATA) {
		XStream xstream = null; 
		if (isAddCDATA) {
			xstream = new XStream(new XppDriver() {

				public HierarchicalStreamWriter createWriter(Writer out) {
					return new PrettyPrintWriter(out) {
						protected void writeText(QuickWriter writer, String text) {
							if (text.startsWith(PREFIX_CDATA) && text.endsWith(SUFFIX_CDATA)) {
								writer.write(text);
							} else {
								super.writeText(writer, text);
							}
						}
					};
				}
			});
		} else {
			xstream = new XStream();
		}
		return xstream;
	}

	/**
	 * 将一个xml流转换为bean实体类
	 * 
	 * @param type
	 * @param is
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(Class<T> type, InputStream is) {
		XStream xmStream = new XStream(new DomDriver("utf-8"));
		// 设置可忽略为在javabean类中定义的界面属性
		xmStream.ignoreUnknownElements();
		xmStream.registerConverter(new MyIntCoverter());
		xmStream.registerConverter(new MyLongCoverter());
		xmStream.registerConverter(new MyFloatCoverter());
		xmStream.registerConverter(new MyDoubleCoverter());
		xmStream.registerConverter(new MyDateCoverter());
		xmStream.autodetectAnnotations(true);
		xmStream.processAnnotations(type);
		T obj = null;
		try {
			obj = (T) xmStream.fromXML(is);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);

		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return obj;
	}

	public static <T> T toBean(Class<T> type, byte[] bytes) {
		if (bytes == null)
			return null;
		return toBean(type, new ByteArrayInputStream(bytes));
	}

	private static class MyIntCoverter extends IntConverter {

		@Override
		public Object fromString(String str) {
			int value;
			try {
				value = (Integer) super.fromString(str);
			} catch (Exception e) {
				value = 0;
			}
			return value;
		}

		@Override
		public String toString(Object obj) {
			return super.toString(obj);
		}
	}

	private static class MyLongCoverter extends LongConverter {
		@Override
		public Object fromString(String str) {
			long value;
			try {
				value = (Long) super.fromString(str);
			} catch (Exception e) {
				value = 0;
			}
			return value;
		}

		@Override
		public String toString(Object obj) {
			return super.toString(obj);
		}
	}

	private static class MyFloatCoverter extends FloatConverter {
		@Override
		public Object fromString(String str) {
			float value;
			try {
				value = (Float) super.fromString(str);
			} catch (Exception e) {
				value = 0;
			}
			return value;
		}

		@Override
		public String toString(Object obj) {
			return super.toString(obj);
		}
	}

	private static class MyDoubleCoverter extends DoubleConverter {
		@Override
		public Object fromString(String str) {
			double value;
			try {
				value = (Double) super.fromString(str);
			} catch (Exception e) {
				value = 0;
			}
			return value;
		}

		@Override
		public String toString(Object obj) {
			return super.toString(obj);
		}
	}

	private static class MyDateCoverter implements Converter {

		@Override
		public boolean canConvert(Class type) {
			return Date.class == type;
		}

		@Override
		public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {

		}

		@Override
		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			GregorianCalendar calendar = new GregorianCalendar();
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (StringUtils.isNotBlank(reader.getValue()))
					calendar.setTime(dateFm.parse(reader.getValue()));
				else
					return null;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return calendar.getTime();
		}

	}

	public static String surrounded(String str) {
		return PREFIX_CDATA + str + SUFFIX_CDATA;
	}

}
