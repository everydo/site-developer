

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.client.methods.SearchMethod;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameIterator;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.xml.Namespace;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatusResponse;

public class demo_search {
	
	public static void main(String[] args) {

		 HostConfiguration hostConfig = new HostConfiguration();           
	     HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	     HttpConnectionManagerParams params = new HttpConnectionManagerParams();
	     int maxHostConnections = 20;
	     params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
	     connectionManager.setParams(params);    
	     HttpClient client = new HttpClient(connectionManager);
	     Credentials creds = new UsernamePasswordCredentials("admin", "admin");
	     client.getState().setCredentials(AuthScope.ANY, creds);
	     client.setHostConfiguration(hostConfig);
	     
	     // select, 返回的对象可以得到display(名称), subjects(标签), iscollection(是否目录)三个属性
	     String select = 
    		"<D:select>" + 
				"<D:prop  xmlns:ns1=\"http://ns.everydo.com/basic\" xmlns:ns0=\"DAV:\">"+
					"<ns0:displayname />"+
					"<ns1:subjects />"+
					"<ns0:iscollection />"+
					"<ns0:getlastmodified />"+
				"</D:prop>"+ 
			"</D:select>";
	     
	     // from, 确定搜索的范围，infinity表示搜索文件夹下所有的文件（夹）, 0 表示只搜索单个文件夹下的文件（夹）
	     String from = 
		     "<D:from>"+
				"<D:scope>"+
					"<D:href>/</D:href>"+
					"<D:depth>infinity</D:depth>"+
				"</D:scope>"+
			"</D:from>";

	     // where, 限制 getlastmodified, 大于今天凌晨，小于今天24点的所有'新创建的'，'修改的' 文件合集
	     String lessDay = get_custom_time_string(0, 0, 0);
	     String greaterDay = get_custom_time_string(23, 59, 59);
	     String where = 
			"<D:where>"+
				"<D:lte caseless=\"no\">"+
					"<D:prop xmlns=\"DAV:\">"+
						"<getlastmodified />"+
					"</D:prop>"+
					"<D:literal>" + greaterDay + "</D:literal>"+
				"</D:lte>"+
				"<D:gte caseless=\"no\">"+
				"<D:prop xmlns=\"DAV:\">"+
					"<getlastmodified />"+
				"</D:prop>"+
				"<D:literal>" + lessDay + "</D:literal>"+
				"</D:gte>"+
			"</D:where>";
	     
	    // orderby, 按照标题排序
	    String orderby = 
	    	 "<D:orderby>"+
				"<D:order>"+ 
					"<D:prop xmlns:ns0=\"DAV:\">"+
						"<ns0:displayname />"+ 
					"</D:prop>"+ 
					"<D:descending />"+ 
				"</D:order>"+
			"</D:orderby>";
	     
        // 只返回20个结果
        String limit = 
            "<D:limit>"+
                "<D:nresults>20</D:nresults>"+
            "</D:limit>";

	    String query = select + from + where + orderby + limit;

	    SearchMethod searchMethod=null;
		try {
             // 只返回第20个到最后多个数据集
			 searchMethod = new SearchMethod("http://localhost:8089/files?start=20",query,"D:basicsearch");
			 
			 client.executeMethod(searchMethod);
			 Namespace edoNamespace = Namespace.getNamespace("http://ns.everydo.com/basic");
			 
		     if (searchMethod.getStatusCode() != 207){
				 System.out.println("方法执行失败，错误代码是：" + searchMethod.getStatusCode());
				 return;
 			 }

			 MultiStatusResponse[] responses = null;
			 try{
				 MultiStatus resp = searchMethod.getResponseBodyAsMultiStatus();
				 responses = resp.getResponses();
			 } catch (DavException e){
				 System.out.println(e.getMessage());
			 }
				 
			 for (int i=0; i<responses.length; i++) {
				 MultiStatusResponse content = responses[i];
				 
				 String docHref = content.getHref();
				 System.out.println("source url: " + URLDecoder.decode(docHref, "utf-8"));
				 
				 /*
				  *  打印所有的属性和值
				  */
				 // 得到所有属性的名字空间
//				 DavPropertySet properys = content.getProperties(200);
//				 DavPropertyNameSet nameSet = content.getPropertyNames(200);
//				 DavPropertyNameIterator nameSetIter = nameSet.iterator();
//				 
//				 // 逐个打印属性和值
//				 while (nameSetIter.hasNext()){
//					 DavProperty propery = properys.get(nameSetIter.next());
//					 System.out.print(propery.getName());
//					 if (propery.getValue() == null){
//						 System.out.println(": null");
//						 continue;
//					 }
//					 System.out.println(": " + propery.getValue().toString());
//				 }
				 
				 /*
				  * 分别打印特定的属性和值
				  */			     
				 DavPropertySet properys = content.getProperties(200);

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
			     }
			 } 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 得到定制的MGT时间
	 */
	@SuppressWarnings("static-access")
	private static String get_custom_time_string(int hour, int minute, int second) {
		final SimpleDateFormat sdf =
	        new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(calendar.YEAR), 
				calendar.get(calendar.MONTH),
				calendar.get(calendar.DAY_OF_MONTH), 
				hour, 
				minute,
				second);
		return sdf.format(calendar.getTime());
	}
}
