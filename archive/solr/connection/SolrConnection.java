
package archive.solr.connection;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

import archive.solr.error.SolrErrorHandler;

public class SolrConnection {
	
	 //String url = "http://10.20.60.74:8080/solr";
	 
	
	String url = "http://localhost:8983/solr";
	
	 CommonsHttpSolrServer server;
	 
	 public SolrConnection()
	 {	 
		 try 
		 {
			 //this server is the basis for all interaction with the Solr server. In addition,
			 //only one instance of the server can exist at one time, otherwise there is the
			 //risk of memory leaks.
			 server = new CommonsHttpSolrServer( url );
			 server.setSoTimeout(1000);  
			 server.setConnectionTimeout(1000);
			 server.setDefaultMaxConnectionsPerHost(100);
			 server.setMaxTotalConnections(100);
			 server.setFollowRedirects(false); 
			 server.setAllowCompression(true);
			 server.setMaxRetries(1); 
			 server.setParser(new XMLResponseParser()); 
			 
		 }	
		 catch (MalformedURLException e) 
		 {
			SolrErrorHandler.handle("SolrConnection");
		 }
	 }
	 
	 public CommonsHttpSolrServer getSolrConnection()
	 {
		 return server;
	 }
}
