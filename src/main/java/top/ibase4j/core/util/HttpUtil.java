package top.ibase4j.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity; 
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity; 
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class HttpUtil {
    private static final Logger logger = LogManager.getLogger();

    private static final MediaType CONTENT_TYPE_FORM = MediaType
            .parse("application/x-www-form-urlencoded;charset=UTF-8");
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

    private HttpUtil() {
    }

	public static void doFile(String fileName){
		try{
			RandomAccessFile raf = new RandomAccessFile(fileName,"rw");
			byte b[] = new byte[50000];  
			raf.read(b);
			for(int i=0;i<b.length;i++){
				System.out.print(b[i]+",");
			}
			//b[12] = 48;//commend by danielinbiti，这个是修改成10g：V10.02.01。如果在修改前自己可以截出头部的几个字节确认一下，是不是就是修改第12,18字节
			//b[18] = 49;
			//raf.seek(0);
			//raf.write(b);
			
			FileUtils.writeByteArrayToFile(new File("D:/1.txt"), b);
		}catch(Exception ex){
			
		}
	}
    public static void main(String[] args) {
    	doFile("D:/document/180929.DMP");
    }

    public static final String get(String url) {
        String result = "";
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        method.addRequestHeader("User-Agent", DEFAULT_USER_AGENT);
        try {
            client.executeMethod(method);
            result = method.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTraceAsString(e));
        } finally {
            method.releaseConnection();
        }
        return result;
    }

    public static final String post(String url, ArrayList<NameValuePair> list) {
        String result = "";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        try {
            NameValuePair[] params = new NameValuePair[list.size()];
            for (int i = 0; i < list.size(); i++) {
                params[i] = list.get(i);
            }
            method.addParameters(params);
            client.executeMethod(method);
            result = method.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTraceAsString(e));
        } finally {
            method.releaseConnection();
        }
        return result;
    }

    public static String post(String url, String params) {
        RequestBody body = RequestBody.create(CONTENT_TYPE_FORM, params);
        Request request = new Request.Builder().url(url).post(body).build();
        return exec(request);
    }

    private static String exec(okhttp3.Request request) {
        try {
            okhttp3.Response response = new OkHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String postSSL(String url, String data, String certPath, String certPass) {
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            // 读取本机存放的PKCS12证书文件
            FileInputStream instream = new FileInputStream(certPath);
            try {
                // 指定PKCS12的密码(商户ID)
                clientStore.load(instream, certPass.toCharArray());
            } finally {
                instream.close();
            }
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(clientStore, certPass.toCharArray()).build();
            // 指定TLS版本
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            // 设置httpclient的SSLSocketFactory
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            try {
                HttpPost httpost = new HttpPost(url); // 设置响应头信息
                httpost.addHeader("Connection", "keep-alive");
                httpost.addHeader("Accept", "*/*");
                httpost.addHeader("Content-Type", CONTENT_TYPE_FORM.toString());
                httpost.addHeader("X-Requested-With", "XMLHttpRequest");
                httpost.addHeader("Cache-Control", "max-age=0");
                httpost.addHeader("User-Agent", DEFAULT_USER_AGENT);
                httpost.setEntity(new StringEntity(data, "UTF-8"));
                CloseableHttpResponse response = httpclient.execute(httpost);
                try {
                    HttpEntity entity = response.getEntity();
                    String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                    EntityUtils.consume(entity);
                    return jsonStr;
                } finally {
                    response.close();
                }
            } finally {
                httpclient.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
