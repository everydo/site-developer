import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;


public class demo_put {
	public static void main(String[] args) throws IOException, DavException {
		HttpClient client = new HttpClient();
		Credentials creds = new UsernamePasswordCredentials("admin", "admin");
		client.getState().setCredentials(AuthScope.ANY, creds);
		
		String fileName = "本地文件_上传.doc";
		String dirUrl = "http://localhost:8089/default/files/";
		String uplaodFileUrl = dirUrl + URLEncoder.encode(fileName,"utf-8");
		String filePath = "d:\\output\\" + fileName;
		
		PutMethod put = new PutMethod(uplaodFileUrl);
		RequestEntity requestEntity = new InputStreamRequestEntity(new FileInputStream(filePath));
		put.setRequestEntity(requestEntity);
		client.executeMethod(put);
		System.out.println("response: " + put.getStatusCode() + " " + put.getStatusText());

	}
}
