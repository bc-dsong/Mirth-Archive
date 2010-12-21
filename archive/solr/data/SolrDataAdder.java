/*
 *created by: Duncan Tsai/Danielle Song
 *
 *This class processes an XML file and pulls out specified information to be placed into a SolrDocument,
 *which is then submitted to the server.
 *
 *This class is technically unrelated to the overall function of the system. It exists only to submit documents and
 *has no connection with other components.
 *
 *is currently nonfunctional due to bad request errors.
 *
 */

package archive.solr.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import archive.solr.error.SolrErrorHandler;

public class SolrDataAdder 
{
	CommonsHttpSolrServer server;
	
	public SolrDataAdder(CommonsHttpSolrServer server)
	{
		this.server = server;
	}
	
	//processes an XML file into a solrDocument given a string file name
	public void processXML(String fileName)
	{
		try 
		{
			File file = new File(fileName);
		
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("patient");
			
			SolrInputDocument solrDoc = new SolrInputDocument();
			
			 for (int i = 0; i < list.getLength(); i++) 
			 {
				 Node node = list.item(i);
				 
				 if (node.getNodeType() == Node.ELEMENT_NODE)
				 {
					 Element firstElement = (Element) node;
					 
				     NodeList givenElmntLst = firstElement.getElementsByTagName("given");
				     Element givenNmElmnt = (Element) givenElmntLst.item(0);
				     NodeList fstNm = givenNmElmnt.getChildNodes();
				     String given = ((Node) fstNm.item(0)).getNodeValue();
				     
				     solrDoc.addField("given", given, 1.0f);
				     
				     System.out.println("Given : "  + given);
				     
				     NodeList familyElmntLst = firstElement.getElementsByTagName("family");
				     Element familyNmElmnt = (Element) familyElmntLst.item(0);
				     NodeList lastNm = familyNmElmnt.getChildNodes();
				     String family = ((Node) lastNm.item(0)).getNodeValue();
				     
				     solrDoc.addField("family", family, 1.0f);
				     
				     System.out.println("Family : "  + family);	
				     
				     NodeList birthDateList = firstElement.getElementsByTagName("birthTime");
				     Element birthDateElement = (Element) birthDateList.item(0);
				     String birthdate = birthDateElement.getAttribute("value");
				     
				     solrDoc.addField("birthdate", birthdate, 1.0f);
				     
				     System.out.println("Birth date: " + birthdate); 
				  
				     NodeList genderList = firstElement.getElementsByTagName("administrativeGenderCode");
				     Element genderElement = (Element) genderList.item(0);
				     String gender = genderElement.getAttribute("code");
				     
				     solrDoc.addField("gender", gender, 1.0f);
				     
				     System.out.println("Gender: " + gender); 
				 }
			 }
			 System.out.println(solrDoc.toString());
			 //add returns a bad request. commit does not.
			 server.add(solrDoc);
			 server.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//SolrErrorHandler.handle("XMLParse");
		}
	}
}
