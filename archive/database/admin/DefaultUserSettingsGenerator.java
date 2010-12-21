/*
 * A set of user settings will be automatically generated along with the creation of that user 
 * The settings include:
 * 
 * 1. number of results displayed per page
 * 2. time (in minutes) before user timeout
 * 3. default method of opening search results (new tab, new window)
 * 
 */

package archive.database.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import archive.database.error.DBErrorHandler;

public class DefaultUserSettingsGenerator 
{
	public static void generate(Connection connection, String username) 
	{
		try 
		{
			PreparedStatement ps;
			ps = connection.prepareStatement("INSERT INTO user_settings VALUES(?, 10, 10, 'newtab')");
			ps.setString(1, username);
		
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException ex) 
		{
			DBErrorHandler.handle("UserSettings");
		}
	}
}
