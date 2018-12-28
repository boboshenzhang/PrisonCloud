package top.ibase4j.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * wkhtmltopdf工具类
 *
 * 约定： 1. 插件安装位置，在Windows系统中将插件安装在D盘根目录下（D:/）, 在Linux系统中将插件安装在opt目录下（/opt）
 *
 * 注意： 1.
 * wkhtmltopdf的Linux版本中，解压后，默认的文件名为"wkhtmltox"，为了统一起见，一律将解压后的文件名，重命名为"wkhtmltopdf"（命令：mv
 * wkhtmltox wkhtmltopdf）
 *
 * Created by kagome on 2016/7/26.
 */
public class CustomWKHtmlToPdfUtil {
	// 临时目录的路径
	public static final String TEMP_DIR_PATH = CustomWKHtmlToPdfUtil.class.getResource("/").getPath().substring(1)
			+ "temp/";

	static {
		// 生成临时目录
		new File(TEMP_DIR_PATH).mkdirs();
	}

	public static String input2String(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String str = sb.toString();
		return str;
	}

	public static void main(String[] args) throws Exception {
		savePdf("http://10.0.0.90/prison-punishment/jianxingzuifanController.do?printShb&id=63481004-9418-48a5-b57b-cfd9384ebba9",
				"D:/hhhh.pdf");
	}

	public static void savePdf(String strUrl, String pdfFilePath) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3 * 1000);
			// 得到输入流
			InputStream inputStream = conn.getInputStream();
			String htmlStr = input2String(inputStream);
			htmlToPdf(strToHtmlFile(htmlStr), pdfFilePath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将HTML文件内容输出为PDF文件
	 *
	 * @param htmlFilePath
	 *            HTML文件路径
	 * @param pdfFilePath
	 *            PDF文件路径
	 */
	public static void htmlToPdf(String htmlFilePath, String pdfFilePath) {
		try {
			Process process = Runtime.getRuntime().exec(getCommand(htmlFilePath, pdfFilePath));
			new Thread(new ClearBufferThread(process.getInputStream())).start();
			new Thread(new ClearBufferThread(process.getErrorStream())).start();
			process.waitFor();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将HTML字符串转换为HTML文件
	 *
	 * @param htmlStr
	 *            HTML字符串
	 * @return HTML文件的绝对路径
	 */
	public static String strToHtmlFile(String htmlStr) {
		OutputStream outputStream = null;
		try {
			String htmlFilePath = TEMP_DIR_PATH + UUID.randomUUID().toString() + ".html";
			outputStream = new FileOutputStream(htmlFilePath);
			outputStream.write(htmlStr.getBytes("UTF-8"));
			return htmlFilePath;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 获得HTML转PDF的命令语句
	 *
	 * @param htmlFilePath
	 *            HTML文件路径
	 * @param pdfFilePath
	 *            PDF文件路径
	 * @return HTML转PDF的命令语句
	 */
	private static String getCommand(String htmlFilePath, String pdfFilePath) {
		String pdfPath = pdfFilePath.substring(0, pdfFilePath.lastIndexOf("/"));
		File pdfPathFile = new File(pdfPath);
		if (!pdfPathFile.exists())
			pdfPathFile.mkdirs();
		String osName = System.getProperty("os.name");
		// Windows
		if (osName.startsWith("Windows")) {
			return String.format("C:/Program Files (x86)/wkhtmltopdf/bin/wkhtmltopdf.exe %s %s", htmlFilePath,
					pdfFilePath);
		} else {// Linux
			return String.format("/opt/wkhtmltopdf/bin/wkhtmltopdf %s %s", htmlFilePath, pdfFilePath);
		}
	}

	/**
	 * 清理输入流缓存的线程 Created by kagome on 2016/8/9.
	 */
	public static class ClearBufferThread implements Runnable {
		private InputStream inputStream;

		public ClearBufferThread(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				while (br.readLine() != null)
					;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}