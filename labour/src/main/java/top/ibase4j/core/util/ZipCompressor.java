package top.ibase4j.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Hex;

public class ZipCompressor {
	static final int BUFFER = 8192;

	private File zipFile;

	public ZipCompressor(String pathName) {
		zipFile = new File(pathName);
	}

	public void compress(String... pathName) {
		ZipOutputStream out = null;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			out = new ZipOutputStream(cos);
			String basedir = "";
			for (int i = 0; i < pathName.length; i++) {
				System.out.println(pathName[i]);
				compress(new File(pathName[i]), out, basedir);
			}
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void compress(String srcPathName) {
		File file = new File(srcPathName);
		if (!file.exists())
			throw new RuntimeException(srcPathName + "不存在！");
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			System.out.println("=========压缩完成==========");
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			// System.out.println("压缩mu：" + basedir + file.getName());
			this.compressDirectory(file, out, basedir);
		} else {
			// System.out.println("压缩：" + basedir + file.getName());
			this.compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 */
	private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	private void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		/*String xmlPath0 = "E://xml";
		// 发送单位编号
		String code = "5212";
		// 接收单位编号
		String orgcode = "5201";
		// 业务实例编号
		String ywslbh = "65789";
		// 文件名称
		String zipname = "2013_0213K_" + code + "_" + orgcode + "_" + ywslbh;
		String xtxxpath = xmlPath0 + "/" + code + "/" + ywslbh;
		// 协同信息
		XtxxRoot xtxxroot = new XtxxRoot();
		String xt = "";
		Xtxx xtxx = putXtxx(xt);
		// 设置协同信息XML
		xtxxroot.setXtxx(xtxx);
		String xmlTop = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String xtxxxml = xmlTop + XstreamUtil.toXml(xtxxroot);
		xtxxxml = xmlTop + xtxxxml;
		String xmlFilePath = xtxxpath + "/xtxx.xml";
		try {
			// 生成协同信息XML
			FileUtils.writeByteArrayToFile(new File(xmlFilePath), xtxxxml.getBytes("UTF-8"), false);
			File txxxFile = new File(xtxxxml);
			if (!txxxFile.exists()) {
				txxxFile.mkdirs();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 业务信息
		JjYwxxRootK root = new JjYwxxRootK();
		// 协同案件编号
		XTajbh xtajbh = new XTajbh();
		JY jy = new JY();
		jy.setJyywbh("52014");
		xtajbh.setJy(jy);
		root.setXtajbh(xtajbh);
		Ajxx ajxx = putXjxx("2222222");
		root.setAjxx(ajxx);
		String xml = XstreamUtil.toXml(xtxxroot);
		xml = xmlTop + xml;
		String ywxxpath = xmlPath0 + "/" + code + "/" + ywslbh + "/ywxx";
		// 文书路径
		String wsfilepath = ywxxpath + "/WS";
		File wsFile = new File(wsfilepath);
		if (!wsFile.exists()) {
			wsFile.mkdirs();
		}
		String wsfilename = "ws1.text";
		File wsf = new File(wsfilepath, wsfilename);
		try {
			wsf.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 卷宗路径
		String jzfilepath = ywxxpath + "/JZ";
		File jzFile = new File(jzfilepath);
		if (!jzFile.exists()) {
			jzFile.mkdirs();
		}
		String jzfilename = "jz1.text";
		File jzf = new File(jzfilepath, jzfilename);
		try {
			jzf.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String ywxxxmlFilePath = ywxxpath + "/ywxx.xml";
		try {
			// 生成协同信息XML
			FileUtils.writeByteArrayToFile(new File(ywxxxmlFilePath), xml.getBytes("UTF-8"), false);
			File txxxFile = new File(ywxxxmlFilePath);
			if (!txxxFile.exists()) {
				txxxFile.mkdirs();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 压缩ywxx文件
		String zip = xtxxpath + "/ywxx.zip";
		ZipCompressor zc = new ZipCompressor(zip);
		zc.compress(ywxxpath + "/WS/", ywxxpath + "/JZ/", ywxxxmlFilePath);
		// 删除原打包文件
		File delFile = new File(ywxxpath);
		if (delFile.exists()) {
			FileUtil.deleteFile(delFile);
		}
		// 终极压缩包
		String codezip = xmlPath0 + "/" + zipname + "_MD5.zip";
		ZipCompressor codezc = new ZipCompressor(codezip);
		codezc.compress(zip, xmlFilePath);
		File newfile = new File(codezip);
		String fileMD5 = FileUtil.getMD5(newfile);
		String md5zip = codezip.replace("MD5", fileMD5.toUpperCase());
		newfile.renameTo(new File(md5zip));
		// 删除原文件夹
		File dezipFile = new File(xmlPath0 + "/" + code);
		if (dezipFile.exists()) {
			FileUtil.deleteFile(dezipFile);
		}*/
	}
}