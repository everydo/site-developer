import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;


public class demo_create_folder {
	public static void main(String[] args) throws IOException, DavException {

		HttpClient client = new HttpClient();
		Credentials creds = new UsernamePasswordCredentials("admin", "admin");
		client.getState().setCredentials(AuthScope.ANY, creds);
                client.getParams().setAuthenticationPreemptive(true);
	
		 // 新建文件夹
		String dirUrl = "http://localhost:8089/default/files/" + URLEncoder.encode("新建的文件夹", "utf-8");
		MkColMethod mkDir=new MkColMethod(dirUrl);
		client.executeMethod(mkDir);
		
		// 查看返回结果
		System.out.println("create the folder: " + dirUrl);
		System.out.println("response: " + mkDir.getStatusCode() + " " + mkDir.getStatusText());
	}
}
