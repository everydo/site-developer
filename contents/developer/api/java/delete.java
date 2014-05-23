import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;


public class demo_delete {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, DavException {

		HttpClient client = new HttpClient();
		Credentials creds = new UsernamePasswordCredentials("admin", "admin");
		client.getState().setCredentials(AuthScope.ANY, creds);
                client.getParams().setAuthenticationPreemptive(true);
		String fileName = "本地上传.txt";
		
		// 删除文件
		String fileUrl = "http://localhost:8089/files/" + URLEncoder.encode(fileName, "utf-8");
		DeleteMethod davDelete = new DeleteMethod(fileUrl);
		client.executeMethod(davDelete);
		System.out.println("response: " + davDelete.getStatusCode() + " " + davDelete.getStatusText());
	}

}
