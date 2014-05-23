import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.Status;
import org.apache.jackrabbit.webdav.client.methods.PropPatchMethod;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.property.DefaultDavProperty;
import org.apache.jackrabbit.webdav.xml.Namespace;


public class demo_proppatch {
	public static void main(String[] args) throws IOException, DavException {
		HttpClient client = new HttpClient();
		Credentials creds = new UsernamePasswordCredentials("admin", "admin");
		client.getState().setCredentials(AuthScope.ANY, creds);
                client.getParams().setAuthenticationPreemptive(true);
		
		String dirUrl = "http://localhost:8089/default/files/";
		DavPropertyNameSet removeProperties = new DavPropertyNameSet(); 
		
		/*
		 * 修改webdav属性
		 * displayname 是文件（夹）的Title, 是可修改的。
		 * iscollection 是文件（夹）的特定属性，文件固定返回0， 文件夹固定返回1，只读属性。
		 * 这个方法因尝试修改iscollection, 导致整个修改失败。
		 */
		System.out.println(">>将 文本.txt 文件改名为： 本地上传.txt, 并且尝试修改iscollection属性");
		String fileName = "文本.txt";
		String serverFileUrl = dirUrl + URLEncoder.encode(fileName,"utf-8");
		DefaultDavProperty newName = new DefaultDavProperty(DavPropertyName.create("displayname"), "本地上传.txt");
		DefaultDavProperty newIsCollection = new DefaultDavProperty(DavPropertyName.create("iscollection"), 0);
		DavPropertySet newNameSet = new DavPropertySet(); 
		newNameSet.add(newName); 
		// 注释下面一行， 可以令这个修改成功执行
		newNameSet.add(newIsCollection);
		PropPatchMethod newNameProPatch = new PropPatchMethod(serverFileUrl, newNameSet, removeProperties);
		client.executeMethod(newNameProPatch); 
		// 解析返回的response
		show_response(newNameProPatch);
		
	    /*
	     * 修改自定义属性
	     * subjects 是文件的特有的属性，是文件的标签设置。以‘，’号分割。
	     * 更多的易度自定义属性介绍，请参考文档《 文档管理集成规范.pptx》。
	     */
		serverFileUrl = dirUrl + URLEncoder.encode("本地上传.txt","utf-8");
		DavPropertySet newSubjectSet = new DavPropertySet(); 
		DefaultDavProperty newSubject = new DefaultDavProperty("subjects", "开发部,合同,期限", Namespace.getNamespace("http://ns.everydo.com/basic")); 
		newSubjectSet.add(newSubject);
		PropPatchMethod newSubjectProPatch = new PropPatchMethod(serverFileUrl, newSubjectSet, removeProperties); 
		client.executeMethod(newSubjectProPatch); 
		System.out.println("***********************************");
		System.out.println(">>给 本地上传.txt 文件添加 ‘开发部’， ‘合同’ 两个标签");
		show_response(newSubjectProPatch);
	}
	
	/*
	 * 这里会打印一个文件，所有要修改的属性，一个或者多个修改结果。
	 * 如果某个某个属性执行结果是424，那是因为文档系统的数据库是有事务的。
	 * 当另一个属性因为别的原因执行失败，会导致其他的属性也失败，返回424。
	 */
	public static void show_response(PropPatchMethod methods)throws IOException, DavException {
		System.out.println("返回结果：");
		
		if (methods.getStatusCode() != 207){
			System.out.println("方法执行失败，错误代码是：" + methods.getStatusCode());
			return;
		}
		
	    MultiStatus multiStatus = methods.getResponseBodyAsMultiStatus();
	    for (MultiStatusResponse content : multiStatus.getResponses()) {
	    	for (Status status : content.getStatus()) {
	    		int statusCode = status.getStatusCode();
	    		DavPropertyNameSet nameSet = content.getPropertyNames(statusCode);
	    		if (statusCode >= 200 & statusCode < 300){
	    			System.out.println("成功设置了: ");
	    		}else{
	    			System.out.println("以下属性设置失败，错误代码是： " + statusCode);
	    		}
    			for (DavPropertyName name : nameSet){
    				System.out.println(name);
    			}
	    	}
	    }
	}
	
	
}
