/*
 * The browsing history of a user is a record of all the files that he/she has retrieved and viewed.
 * The name of the file and the time when it was accessed are logged into this record.
 * 
 */

package archive.database.records;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import archive.database.error.DBErrorHandler;

public class BrowsingHistoryLogger 
{

	//inserts a new entry into the browsing history of the specified user
	public static void log(Connection connection, String username, int fileId, String timeAndDate) 
	{
		
		String tableName = username + "_history";
		
		try 
		{
			PreparedStatement ps;
			ps = connection.prepareStatement("INSERT INTO ? VALUES(?, ?)");
			ps.setString(1, tableName);
			ps.setInt(2, fileId);
			ps.setString(3, timeAndDate);
		
			ps.executeUpdate();
			ps.close();
			
		}
		catch (SQLException ex) 
		{
			DBErrorHandler.handle("HistoryLog");
		}
	}
}
