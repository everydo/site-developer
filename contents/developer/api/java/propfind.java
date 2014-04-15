import java.io.IOException;
import java.net.URLDecoder;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.xml.Namespace;


public class demo_propfind {
	public static void main(String[] args) throws IOException, DavException {
		HttpClient client = new HttpClient();
		Credentials creds = new UsernamePasswordCredentials("admin", "admin");
		client.getState().setCredentials(AuthScope.ANY, creds);
		
		//得到属性
		String dirUrl = "http://localhost:8089/default/files/";
		DavMethod find = new PropFindMethod(dirUrl, DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_1);
	    client.executeMethod(find);
	    
		if (find.getStatusCode() != 207){
			System.out.println("方法执行失败，错误代码是：" + find.getStatusCode());
			return;
		}
	    
	    MultiStatus multiStatus = find.getResponseBodyAsMultiStatus();
	    MultiStatusResponse[] responses = multiStatus.getResponses();
	    
		Namespace edoNamespace = Namespace.getNamespace("http://ns.everydo.com/basic");
		
	    // 打印部分属性信息( 所有的属性都得到了 )
		for (int i=0; i<responses.length; i++) {
			 MultiStatusResponse content = responses[i];
			 DavPropertySet properys = content.getProperties(200);
			 
			 // 打印文件或者文件夹的url
		     String docHref = content.getHref();
		     System.out.println("source url: " + URLDecoder.decode(docHref, "utf-8"));
		     
		     //是否为目录
		     Boolean isFile = properys.get("iscollection").getValue().equals("0");
		     System.out.println("IsFile: " + isFile.toString());
		     
		     // 打印文件或者文件夹的名称
		     String sourceName = properys.get("displayname").getValue().toString();
             System.out.println("display name: " + sourceName);
             
		     if (isFile){
	             // 打印文件的标签， subjects 不属于webdav标准属性，是我们自定义的属性，所以要从我们的命名空间去寻找
	             DavProperty subjects = properys.get("subjects", edoNamespace);
	             // 只有打上标签的文件才能获得标签
	             if (subjects == null || subjects.getValue() == null){
	            	 System.out.println("subjects: null");
	             }else{
	            	 System.out.println("subjects: " + subjects.getValue().toString());
	             }
		     }else{
		    	 System.out.println("subjects: null");
		     }
	    }
	}
}
