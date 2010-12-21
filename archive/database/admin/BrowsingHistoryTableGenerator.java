package archive.database.admin;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import archive.database.error.DBErrorHandler;

public class BrowsingHistoryTableGenerator 
{
	//automatically generates a new empty table for holding the browsing history
	//of the newly created user
	public static void generate(Connection connection, String username) 
	{
		
		try 
		{
			Statement stmt;
			String tableName = username + "_history";
			String createTable = "CREATE TABLE " + tableName + " (filename varchar(255), timestamp varchar(255))";
			System.out.println(createTable);
			
			stmt = connection.createStatement();
	   		stmt.executeUpdate(createTable);
			stmt.close();

		}
		catch (SQLException ex) 
		{
			DBErrorHandler.handle("HistoryTable");
		}
	}
}
