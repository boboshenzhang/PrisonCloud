package top.ibase4j.core.support.file.fastdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luhuiguo.fastdfs.conn.ConnectionManager;
import com.luhuiguo.fastdfs.conn.ConnectionPoolConfig;
import com.luhuiguo.fastdfs.conn.FdfsConnectionPool;
import com.luhuiguo.fastdfs.conn.PooledConnectionFactory;
import com.luhuiguo.fastdfs.conn.TrackerConnectionManager;
import com.luhuiguo.fastdfs.service.DefaultFastFileStorageClient;
import com.luhuiguo.fastdfs.service.DefaultTrackerClient;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.luhuiguo.fastdfs.service.TrackerClient;

import top.ibase4j.core.util.DataUtil;
import top.ibase4j.core.util.InstanceUtil;
import top.ibase4j.core.util.PropertiesUtil;

/**
 * @author ShenHuaJie
 * @version 2016年6月27日 上午9:51:06
 */
@SuppressWarnings("serial")
public class FileManager implements Serializable {
	private static Logger logger = LogManager.getLogger();
	private static FileManager fileManager;
	private FastFileStorageClient fastFileStorageClient;

	public static FileManager getInstance() {
		if (fileManager == null) {
			synchronized (FileManager.class) {
				fileManager = new FileManager();
			}
		}
		return fileManager;
	}

	private FileManager() {
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setSoTimeout(PropertiesUtil.getInt("fdfs.soTimeout", 1000));
		pooledConnectionFactory.setConnectTimeout(PropertiesUtil.getInt("fdfs.connectTimeout", 1000));
		ConnectionPoolConfig connectionPoolConfig = new ConnectionPoolConfig();
		FdfsConnectionPool pool = new FdfsConnectionPool(pooledConnectionFactory, connectionPoolConfig);
		TrackerConnectionManager trackerConnectionManager = new TrackerConnectionManager(pool,
				InstanceUtil.newArrayList(PropertiesUtil.getString("fdfs.trackerList").split(",")));
		TrackerClient trackerClient = new DefaultTrackerClient(trackerConnectionManager);
		ConnectionManager connectionManager = new ConnectionManager(pool);
		fastFileStorageClient = new DefaultFastFileStorageClient(trackerClient, connectionManager);
	}

	public void upload(final FileModel file) {
		//modifid by zhangbo  isEmpty modified as isNotEmpty
		//group name exist erro
		if (DataUtil.isNotEmpty(file.getGroupName())) {
			String path = fastFileStorageClient.uploadFile(file.getContent(), file.getExt()).getFullPath();
			logger.info("Upload to fastdfs success =>" + path);
			file.setRemotePath(PropertiesUtil.getString("fdfs.fileHost") + path);
		} else {
			String path = fastFileStorageClient.uploadFile(file.getGroupName(), file.getContent(), file.getExt())
					.getFullPath();
			logger.info("Upload to fastdfs success =>" + path);
			file.setRemotePath(PropertiesUtil.getString("fdfs.fileHost") + path);
		}
	}

	public FileModel getFile(String groupName, String path) {
		FileModel file = new FileModel();
		file.setContent(fastFileStorageClient.downloadFile(groupName, path));
		return file;
	}

	public void deleteFile(String groupName, String path) throws Exception {
		fastFileStorageClient.deleteFile(groupName, path);
	}
	
	public byte[] download_file(String groupname, String filename) {          
		try {
			return fastFileStorageClient.downloadFile(groupname, filename);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
    }
	
	public InputStream download_file(String nginxUrl) throws IOException {          
		URL url = new URL(nginxUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		return inputStream;
    }
	
	public static void main(String[]args){
		String fileUrl = "http://192.168.0.22:8888/group1/M00/00/02/wKgAFluBIZSAdMnPAAAS9FCpIFs322.zip";
		
		int length = fileUrl.indexOf("group1");
		
		System.out.println(length + "-------------length-----------------" + fileUrl.substring(length+7,fileUrl.length()));
	}
	

}
