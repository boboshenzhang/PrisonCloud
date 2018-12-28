package top.ibase4j.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.tidy.Tidy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;

public class PdfUtils {

	public static void HtmlToPdf(String url, String pdfPath) throws IOException {
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(url);
		ITextFontResolver fontResolver = renderer.getFontResolver();
		try {
			fontResolver.addFont("/usr/share/fonts/zh_CN/TrueType/simsunb.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字
			fontResolver.addFont("/usr/share/fonts/zh_CN/TrueType/arial.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字
			fontResolver.addFont("C:/WINDOWS/Fonts/SimSun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字
			fontResolver.addFont("C:/WINDOWS/Fonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			fontResolver.addFont("C:/WINDOWS/Fonts/simfang.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字
		} catch (com.itextpdf.text.DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 宋体字
		renderer.layout();
		OutputStream os = null;
		try {
			os = new FileOutputStream(pdfPath);
			renderer.layout();
			renderer.createPDF(os);
			os.close();
			os = null;
		} catch (com.itextpdf.text.DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

	}

	// 导出pdf add by huangt 2012.6.1
	public static File exportPdfFile(String urlStr, String outputFile) throws com.itextpdf.text.DocumentException {
		// String outputFile = this.fileRoot + "/" +
		// ServiceConstants.DIR_PUBINFO_EXPORT + "/" + getFileName() + ".pdf";

		OutputStream os;
		try {
			os = new FileOutputStream(outputFile);

			ITextRenderer renderer = new ITextRenderer();

			String str = getHtmlFile(urlStr);

			renderer.setDocumentFromString(str);
			ITextFontResolver fontResolver = renderer.getFontResolver();
			// fontResolver.addFont("/fonts/SIMSUN.TTC",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);// 宋体字
			// fontResolver.addFont("/fonts/ARIAL.TTF",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字
			fontResolver.addFont("C:/WINDOWS/Fonts/SimSun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字
			fontResolver.addFont("C:/WINDOWS/Fonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字
			renderer.layout();

			renderer.createPDF(os);

			os.flush();
			os.close();
			return new File(outputFile);
		} catch (FileNotFoundException e) {
			// logger.error("不存在文件！" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// logger.error("pdf出错了！" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// 读取页面内容 add by huangt 2012.6.1
	public static String getHtmlFile(String urlStr) {
		URL url;
		try {
			if (urlStr.indexOf("?") != -1) {
				urlStr = urlStr + "&locale=" + LocaleContextHolder.getLocale().toString();
			} else {
				urlStr = urlStr + "?locale=" + LocaleContextHolder.getLocale().toString();
			}
			url = new URL(urlStr);

			URLConnection uc = url.openConnection();
			InputStream is = uc.getInputStream();

			Tidy tidy = new Tidy();

			OutputStream os2 = new ByteArrayOutputStream();
			tidy.setXHTML(true); // 设定输出为xhtml(还可以输出为xml)
			// tidy.setCharEncoding(Configuration.UTF8); // 设定编码以正常转换中文
			tidy.setTidyMark(false); // 不设置它会在输出的文件中给加条meta信息
			tidy.setXmlPi(true); // 让它加上<?xml version="1.0"?>
			tidy.setIndentContent(true); // 缩进，可以省略，只是让格式看起来漂亮一些
			tidy.parse(is, os2);

			is.close();

			// 解决乱码 --将转换后的输出流重新读取改变编码
			String temp;
			StringBuffer sb = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(((ByteArrayOutputStream) os2).toByteArray()), "utf-8"));
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			System.out
					.println(StringEscapeUtils.unescapeXml(sb.toString()) + "=====================urlStrurlStrurlStr");
			return StringEscapeUtils.unescapeXml(sb.toString());
		} catch (IOException e) {
			// logger.error("读取客户端网页文本信息时出错了" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param path
	 *            保存路径
	 * @param url
	 *            生成pdf
	 * @param name
	 *            生成文书名称
	 * @throws IOException
	 */
	public static void savePdf(String path, String url, String name) throws IOException {
		File dir = new File(path + "/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File p = new File(path + "/" + name + ".pdf");
		if (!p.exists()) {
			p.createNewFile();
		}
		PdfUtils.HtmlToPdf(url, path + "/" + name + ".pdf");
	}

	/**
	 * 下载文书
	 * 
	 * @param urlString
	 * @param filename
	 * @param savePath
	 * @return
	 * @throws MalformedURLException
	 */
	public static boolean download(String urlString, String filename, String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(30000);
		con.setReadTimeout(30000);
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (is.available() == 0) {
			return false;
		}
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
