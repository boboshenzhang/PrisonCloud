package top.ibase4j.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP工具类
 * 
 * @author zhangbo
 */
public class FtpUtils {
	private FTPClient ftpClient = null;
	private String server;
	private int port;
	private String userName;
	private String userPassword;

	public FtpUtils(String server, int port, String userName, String userPassword) {
		this.server = server;
		this.port = port;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	/**
	 * 改名FTP上的文件
	 */

	public boolean renameFile(String srcFname, String targetFname) {
		boolean flag = false;
		if (ftpClient != null) {
			try {
				flag = ftpClient.rename(srcFname, targetFname);
				System.out.println("重命名文件[" + srcFname + "]为[" + targetFname + "]成功");
			} catch (IOException e) {
				e.printStackTrace();
				this.close();
			}
		}

		return flag;
	}

	/**
	 * 连接服务器
	 * 
	 * @return 连接成功与否 true:成功， false:失败
	 */
	public boolean open() {
		if (ftpClient != null && ftpClient.isConnected()) {
			return true;
		}
		try {
			ftpClient = new FTPClient();
			// 连接
			try {
				ftpClient.connect(this.server, this.port);
			} catch (Exception e) {
				System.out.println("FTP server [" + server + ":" + port + "]登陆失败!");
				return false;
			}
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				try {
					ftpClient.login(this.userName, this.userPassword);
				} catch (Exception e) {
					System.out.println("FTP用户[" + userName + "]登陆失败!");
					return false;
				}
			}

			setFtpClient(ftpClient);
			System.out.println("FTP param:" + this.server + ";port:" + this.port + ";name:" + this.userName + ";pwd:"
					+ this.userPassword);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			return true;
		} catch (Exception ex) {
			this.close();
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 切换到父目录
	 * 
	 * @return 切换结果 true：成功， false：失败
	 */
	private boolean changeToParentDir() {
		try {
			return ftpClient.changeToParentDirectory();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 改变当前目录到指定目录
	 * 
	 * @param dir
	 *            目的目录
	 * @return 切换结果 true：成功，false：失败
	 */
	private boolean cd(String dir) {
		try {
			return ftpClient.changeWorkingDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取目录下所有的文件名称
	 * 
	 * @param filePath
	 *            指定的目录
	 * @return 文件列表,或者null
	 */
	private FTPFile[] getFileList(String filePath) {
		try {
			return ftpClient.listFiles(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 层层切换工作目录
	 * 
	 * @param ftpPath
	 *            目的目录
	 * @return 切换结果
	 */
	public boolean changeDir(String ftpPath) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
			// 将路径中的斜杠统一
			char[] chars = ftpPath.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {
				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpPath = sbStr.toString();
			if (ftpPath.indexOf('/') == -1) {
				// 只有一层目录
				ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
			} else {
				// 多层目录循环创建
				String[] paths = ftpPath.split("/");
				for (int i = 0; i < paths.length; i++) {
					ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
	 * 
	 * @param ftpPath
	 *            需要创建的目录
	 * @return
	 */
	public boolean mkDir(String ftpPath) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
			// 将路径中的斜杠统一
			char[] chars = ftpPath.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {
				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpPath = sbStr.toString();
			System.out.println("ftpPath:" + ftpPath);
			if (ftpPath.indexOf('/') == -1) {
				// 只有一层目录
				ftpClient.makeDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
				ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
			} else {
				// 多层目录循环创建
				String[] paths = ftpPath.split("/");
				for (int i = 0; i < paths.length; i++) {
					ftpClient.makeDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
					ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "iso-8859-1"));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 上传文件到FTP服务器
	 * 
	 * @param localDirectoryAndFileName
	 *            本地文件目录和文件名
	 * @param ftpFileName
	 *            上传到服务器的文件名
	 * @param ftpDirectory
	 *            FTP目录如:/path1/pathb2/,如果目录不存在会自动创建目录
	 * @return
	 */
	public boolean upload(byte[] byteFile, String ftpFileName, String ftpDirectory) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		boolean flag = false;
		if (ftpClient != null) {
			InputStream input = new ByteArrayInputStream(byteFile);
			try {
				// 创建目录
				this.mkDir(ftpDirectory);
				ftpClient.setBufferSize(100000);
				ftpClient.setControlEncoding("UTF-8");
				// 设置文件类型（二进制）
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 上传
				flag = ftpClient.storeFile(new String(ftpFileName.getBytes(), "iso-8859-1"), input);
			} catch (Exception e) {
				this.close();
				e.printStackTrace();
				return false;
			} finally {
				try {
					if (input != null) {
						input.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("上传文件成功，本地文件名： " + ftpFileName + "，上传到目录：" + ftpDirectory + "/" + ftpFileName);
		return flag;
	}

	public boolean upload(InputStream input, String ftpFileName, String ftpDirectory) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		boolean flag = false;
		if (ftpClient != null) {
			try {
				// 创建目录
				this.mkDir(ftpDirectory);
				ftpClient.setBufferSize(100000);
				ftpClient.setControlEncoding("UTF-8");
				// 设置文件类型（二进制）
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 上传
				flag = ftpClient.storeFile(new String(ftpFileName.getBytes(), "iso-8859-1"), input);
			} catch (Exception e) {
				this.close();
				e.printStackTrace();
				return false;
			} finally {
				try {
					if (input != null) {
						input.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("上传文件成功，本地文件名： " + ftpFileName + "，上传到目录：" + ftpDirectory + "/" + ftpFileName);
		return flag;
	}

	/**
	 * 从FTP服务器上下载文件
	 * 
	 * @param ftpDirectoryAndFileName
	 *            ftp服务器文件路径，以/dir形式开始
	 * @param localDirectoryAndFileName
	 *            保存到本地的目录
	 * @return
	 */
	public boolean get(String ftpDirectoryAndFileName, String localDirectoryAndFileName) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		ftpClient.enterLocalPassiveMode(); // Use passive mode as default
		try {
			// 将路径中的斜杠统一
			char[] chars = ftpDirectoryAndFileName.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for (int i = 0; i < chars.length; i++) {
				if ('\\' == chars[i]) {
					sbStr.append('/');
				} else {
					sbStr.append(chars[i]);
				}
			}
			ftpDirectoryAndFileName = sbStr.toString();
			String filePath = ftpDirectoryAndFileName.substring(0, ftpDirectoryAndFileName.lastIndexOf("/"));
			String fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf("/") + 1);
			this.changeDir(filePath);
			FileOutputStream fos = new FileOutputStream(localDirectoryAndFileName);
			ftpClient.retrieveFile(new String(fileName.getBytes(), "iso-8859-1"), fos); // download
			fos.flush();
			fos.close();
			// file
			System.out.println(ftpClient.getReplyString()); // check result
			System.out.println("从ftp服务器上下载文件：" + ftpDirectoryAndFileName + "， 保存到：" + localDirectoryAndFileName);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			this.close();
		}
	}

	/**
	 * 返回FTP目录下的文件列表
	 * 
	 * @param pathName
	 * @return
	 */
	public String[] getFileNameList(String pathName) {
		try {
			return ftpClient.listNames(pathName);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除FTP上的文件
	 * 
	 * @param ftpDirAndFileName
	 *            路径开头不能加/，比如应该是test/filename1
	 * @return
	 */
	public boolean deleteFile(String ftpDirAndFileName) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
			return ftpClient.deleteFile(ftpDirAndFileName);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除FTP目录
	 * 
	 * @param ftpDirectory
	 * @return
	 */
	public boolean deleteDirectory(String ftpDirectory) {
		if (!ftpClient.isConnected()) {
			return false;
		}
		try {
			return ftpClient.removeDirectory(ftpDirectory);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 关闭链接
	 */
	public void close() {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
			System.out.println("成功关闭连接，服务器ip:" + this.server + ", 端口:" + this.port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public static void main(String[] args) {
		FtpUtils ftp = new FtpUtils("10.0.0.73", 21, "test", "123456");
		ftp.open();
		ftp.get("prison_family/test.txt", "D:/1.txt");
	}

}