/*
 *created by: Duncan Tsai/Danielle Song
 *
 */

package archive.solr.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SolrDataViewer 
{

	public static void printResults(ArrayList<Map<String, String>> resultList)
	{
		if(resultList.isEmpty())
		{
			System.out.println("No matches");
		}
		else
		{
			for (Iterator<Map<String, String>> i = resultList.iterator(); i.hasNext();)
			{
				Map<String, String> current = (Map<String, String>) i.next(); 
				
				Set<String> currentKeyset = current.keySet();
				
				for (Iterator<String> keyIterator = currentKeyset.iterator(); keyIterator.hasNext();)
				{
					String currentKey = keyIterator.next(); 
					String currentValue = current.get(currentKey); 
					
					System.out.println(currentKey + ": " + currentValue); 
				}
				
				System.out.println("");
			}
		}
	}
}
