package archive.database.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import archive.database.error.DBErrorHandler;

public class PersonalSettingEditor 
{
	private static final int MINIMUM_PASS_LENGTH = 6;
	
	Connection connection;
	String username = null;
	
	public PersonalSettingEditor(Connection connection, String username)
	{
		this.connection = connection;
		this.username = username;
	}
	
	public void editPassword(String password)
	{
		
		if(password.length() >= MINIMUM_PASS_LENGTH)
		{
			try 
			{
				PreparedStatement ps;
				ps = connection.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
				ps.setString(1, password);
				ps.setString(2, username);
		
				ps.executeUpdate();
				ps.close();
				System.out.println("Password changed");
			}
			catch (SQLException ex) 
			{
				//place holder code, should be replaced with alert sent to interface
				System.err.print("SQLException: ");
				System.err.println(ex.getMessage());
			}
		}
		else
		{
			DBErrorHandler.handle("BadPassword");
		}
	}
	
	public void editSettings(int resultCount, String displayMethod)
	{
		try 
		{
			PreparedStatement ps;
			ps = connection.prepareStatement("UPDATE user_settings SET result_count = ?, display = ? WHERE username = ?");
			ps.setInt(1, resultCount);
			ps.setString(2, displayMethod);
			ps.setString(3, username);
		
			ps.executeUpdate();
			ps.close();
			System.out.println("Global settings updated");
		}
		catch (SQLException ex) 
		{
			DBErrorHandler.handle("EditSettings");
		}
	}
	
}
