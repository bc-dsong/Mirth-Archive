package archive.main;

import archive.solr.connection.SolrConnection;
import archive.solr.data.SolrDataAdder;
import archive.solr.data.SolrDataRetriever;

public class Main 
{
	public static void main(String args[]) 
	{
		SolrConnection connection = new SolrConnection();
		SolrDataRetriever retriever = new SolrDataRetriever(connection.getSolrConnection());
		//SolrDataAdder adder = new SolrDataAdder(connection.getSolrConnection());
		//adder.processXML("SampleCCDDocument.xml");
		//retriever.processAdvancedQuery("medication:prosac", "Sanchez", "birthdate: 77777666, allergy:hay, condition:scoliosis");
		retriever.processNormalQuery("allergy:fluffy cats");
		
	}
}
