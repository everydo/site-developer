import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.jackrabbit.webdav.DavException;


public class demo_download {
	public static void main(String[] args) throws IOException, DavException {
		HttpClient client = new HttpClient();
		Credentials creds = new UsernamePasswordCredentials("admin", "admin");
		client.getState().setCredentials(AuthScope.ANY, creds);
		
		String fileName = "如何办护照.txt";
		
		// 下载文件
		String fileUrl = "http://localhost:8089/default/files/" + URLEncoder.encode(fileName, "utf-8");
		System.out.println(fileUrl);
		GetMethod method = new GetMethod(fileUrl);
		client.executeMethod(method);
		
		// 打印文件内容到控制台
		System.out.println(new String(method.getResponseBody(),"gbk")); // or utf-8 
		
		// 保存文件到磁盘
		String fileDir = "d:\\output";
		File fileDirObj = new File(fileDir);
		if (!fileDirObj.exists()) {
			fileDirObj.mkdirs();
		}
		
		File fileObj = new File(fileDirObj, fileName);
		FileOutputStream fops = new FileOutputStream(fileObj);
		fops.write(method.getResponseBody());
		fops.close();
		
	}

}
