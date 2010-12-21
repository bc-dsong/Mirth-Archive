package archive.database.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import archive.database.error.DBErrorHandler;

public class LogInAuthenticator 
{
	String serverLogIn = "studentteam";
	String serverPassword = "Sk94pPR1Dw";
	Connection dbConnection;
	
	public LogInAuthenticator()
	{
		DBConnection connection = new DBConnection(serverLogIn, serverPassword);
		dbConnection = connection.getDBConnection();
	}
	
	public void authenticate(String username, String password)
	{
		String user = null;
		PreparedStatement ps;
		
		int result_count;
		int time_out;
		String display;
		
		try
		{
			ps = dbConnection.prepareStatement("SELECT users FROM users WHERE username = ? AND password = ?");
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet userResult = ps.executeQuery();
			
			while(userResult.next()) 
			{
				user = userResult.getString(1);
			}
			
			if(user != null) //such a user exists
			{
				//get the user status, either admin or not
				
				//if non admin, load personal settings and move to search page
				
				ps = dbConnection.prepareStatement("SELECT result_count, time_out, display FROM user_settings WHERE username = ?");
				ResultSet settingResult = ps.executeQuery();
				
				while(settingResult.next())
				{
					result_count = settingResult.getInt(1);
					time_out = settingResult.getInt(2);
				    display = settingResult.getString(3);
				}
			    
			    //User user = new User();
				//user.setUserPageSettings(result_count, time_out, display);
			    //moveToSearchPage(user);
				
				//if admin, load specialized admin pages
			}
			else
			{
				DBErrorHandler.handle("incorrect login");
				//also clear text fields
			}
			
			ps.close();
		}
		catch(SQLException e)
		{
			
		}
	}
	
	
	
}
