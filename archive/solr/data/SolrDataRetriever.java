/*
 * created by: Duncan Tsai/Danielle Song
 * 
 * This class uses the supplied server object to process solr queries with query strings
 * provided by the user.
 * 
 */

package archive.solr.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import archive.solr.error.SolrErrorHandler;

public class SolrDataRetriever 
{
	
	CommonsHttpSolrServer server;
	QueryResponse rsp;

	public SolrDataRetriever(CommonsHttpSolrServer server)
	{
		this.server = server;
	}
	
	//queries the solr server with a given string, doing a "Any" search where any and all instances of
	//words included in the string are provided in the response 
	public void processNormalQuery(String queryString)
	{
		try
		{
			SolrQuery normalQuery = new SolrQuery();
			normalQuery.setQuery(queryString);
			rsp = server.query(normalQuery);
			
			SolrDocumentList docs = rsp.getResults();
			processSingleResponse(docs);
		}
		catch(SolrServerException e)
		{
			//send alert to UI, handle gently
		}
	}
	
	//queries the solr server with any possible combination of "normal", "exact", or "filter" type searches
	public void processAdvancedQuery(String normalQueryInput, String exactQueryInput, String filterQueryInput)
	{
		try
		{
			if(normalQueryInput == null)//must be an exact phrase search, either with or without filtering
			{
				String quotedQueryString = setExactQuery(exactQueryInput);
				
				if(filterQueryInput == null)//only an exact phrase search
				{
					processNormalQuery(quotedQueryString);
				}
				else//its an exact phrase search with added filters
				{
					ArrayList<String> filterList = createFilterStringList(filterQueryInput);
					SolrQuery tempQuery = new SolrQuery();
					tempQuery.setQuery(quotedQueryString);
					tempQuery = setFilterQueries(tempQuery, filterList);
					
					rsp = server.query(tempQuery);
					SolrDocumentList docs = rsp.getResults();
					processSingleResponse(docs);
					
				}
			}
			else if(exactQueryInput == null)//must be a general type search, either with or without filtering
			{
				if(filterQueryInput == null)//only a general type search
				{
					processNormalQuery(normalQueryInput);
				}
				else//its a general type search with added filters
				{
					ArrayList<String> filterList = createFilterStringList(filterQueryInput);
					SolrQuery tempQuery = new SolrQuery();
					tempQuery.setQuery(normalQueryInput);
					tempQuery = setFilterQueries(tempQuery, filterList);
					
					rsp = server.query(tempQuery);
					SolrDocumentList docs = rsp.getResults();
					processSingleResponse(docs);
				}
			}
			else if(filterQueryInput == null)//a combination of a general search with an exact phrase search
			{
				SolrQuery tempQuery1 = new SolrQuery();
				tempQuery1.setQuery(normalQueryInput);
				rsp = server.query(tempQuery1);
				SolrDocumentList docList1 = rsp.getResults();
				
				String quotedQueryString = setExactQuery(exactQueryInput);
				SolrQuery tempQuery2 = new SolrQuery();
				tempQuery2.setQuery(quotedQueryString);
				rsp = server.query(tempQuery2);
				SolrDocumentList docList2 = rsp.getResults();
				
				processMultipleDocLists(docList1, docList2);
				
			}
			else //all three fields contain data
			{
				
				ArrayList<String> filterList = createFilterStringList(filterQueryInput);
				
				//filtered normal search
				SolrQuery tempQuery1 = new SolrQuery();
				tempQuery1.setQuery(normalQueryInput);
				tempQuery1 = setFilterQueries(tempQuery1, filterList);
				
				rsp = server.query(tempQuery1);
				SolrDocumentList docList1 = rsp.getResults();
				
				//filtered exact phrase search
				String quotedQueryString = setExactQuery(exactQueryInput);
				SolrQuery tempQuery2 = new SolrQuery();
				tempQuery2.setQuery(quotedQueryString);
				tempQuery2 = setFilterQueries(tempQuery2, filterList);
				
				rsp = server.query(tempQuery2);
				SolrDocumentList docList2 = rsp.getResults();
				
				processMultipleDocLists(docList1, docList2);
			}
			
		}
		catch(SolrServerException e)
		{
			//send alert to UI, handle gently
		}
	}
	
	private HashMap<String, String> processSolrDoc(SolrDocument doc)
	{
		HashMap<String, String>result = new HashMap<String, String>();
		
		result.put("id", doc.getFieldValue("id").toString());
		result.put("name", doc.getFieldValue("name").toString());
		result.put("given", doc.getFieldValue("given").toString()); 
		result.put("family", doc.getFieldValue("family").toString());
		result.put("birthdate", doc.getFieldValue("birthdate").toString());
		result.put("gender", doc.getFieldValue("gender").toString());
		result.put("condition", doc.getFieldValue("condition").toString());
		result.put("medication", doc.getFieldValue("medication").toString());
		result.put("allergy", doc.getFieldValue("allergy").toString());
		
		return result;
	}
	
	private void processSingleResponse(SolrDocumentList docs)
	{
		ArrayList<Map<String, String>> resultSet = new ArrayList<Map<String, String>>();
		
		Iterator<SolrDocument> dociterator = docs.iterator();
			
		while(dociterator.hasNext())
	    {
			SolrDocument doc = dociterator.next();
			resultSet.add(processSolrDoc(doc));
	     }
		//pass the Arraylist of Maps to a separate class to display the results
		SolrDataViewer.printResults(resultSet); 
	}
	
	private void processMultipleDocLists(SolrDocumentList docs1, SolrDocumentList docs2)
	{
		ArrayList<Map<String, String>> finalResultSet = new ArrayList<Map<String, String>>();
		
		for (Iterator<SolrDocument> i = docs1.iterator(); i.hasNext();)
		{
			SolrDocument current1 = (SolrDocument)i.next();
			
			for (Iterator<SolrDocument> j = docs2.iterator(); j.hasNext();)
			{
				SolrDocument current2 = (SolrDocument)j.next();
				
				if(current1.toString().equals(current2.toString()))
				{
					if (!finalResultSet.contains(current1))
					{
						finalResultSet.add(processSolrDoc(current1)); 
					}
				}
			}
		}
		
		SolrDataViewer.printResults(finalResultSet);
	}
	
	//appends double quotes to a search string so it queried as a whole phrase
	private String setExactQuery(String queryString)
	{
		return '"' + queryString + '"';
	}
	
	//takes in a SolrQuery and adds any number of strings that are to be excluded during final search
	private SolrQuery setFilterQueries(SolrQuery initialQuery, ArrayList<String> includeList)
	{
		SolrQuery tempQuery = new SolrQuery();
		tempQuery = initialQuery;
		
		for(int i = 0; i < includeList.size(); i++)
		{
			tempQuery.addFilterQuery(includeList.get(i));
		}
		
		return tempQuery;
	}
	
	//breaks down a string into a number of tokens using commas as delimiters, used for extracting individual 
	//strings to be put into setFilterQuery calls
	private ArrayList<String> createFilterStringList(String filterValues)
	{
		ArrayList<String> includeList = new ArrayList<String>(); 
		
		StringTokenizer st = new StringTokenizer(filterValues, ","); 
		while (st.hasMoreTokens())
		{
			includeList.add(st.nextToken());
		}
		
		return includeList; 
	}
	
}
