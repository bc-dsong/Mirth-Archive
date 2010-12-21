/*
 * Each new user entry should have the following information: 
 *
 * 1. type
 * 2. password
 * 3. personal preferences/settings
 * 4. search history/viewed history (not yet implemented)
 * 5. status
 * 6. username 
 * 
 * Unlike with the FileRetriever, there is no intermediate layer between the user interface
 * and the database in retrieving user records.
 * 
 */

package archive.database.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import archive.database.error.DBErrorHandler;

public class UserManager 
{
	private static final int MINIMUM_NAME_LENGTH = 4;
	private static final int MINIMUM_PASS_LENGTH = 6;
	
	Connection connection;
	
	public UserManager(Connection connection)
	{
		this.connection = connection;
	}

	public void retrieveUserInformation(String username)
	{
		String password;
		String type;
		String status;
		
		try {
				if(userExist(username) == false) 
				{
					DBErrorHandler.handle("NonexistiantUser");
				}
				else
				{
					PreparedStatement ps;
					String user = null;
			
					//this SQL query with the assumption that username will
					//be the unique key
					ps = connection.prepareStatement("SELECT password, type, status FROM users WHERE username = ?");
					ps.setString(1, username);
					ResultSet rs = ps.executeQuery();
				
					while(rs.next())
					{
						password = rs.getString(1);
						type = rs.getString(2);
						status = rs.getString(3);
					}
	
					//admin.displayUserInfo(username, password, type, status);
				
					ps.close();
				}
		}
		catch (SQLException ex) 
		{
			DBErrorHandler.handle("RetrieveUser");
		}
	}
	
	//adds a brand new user to the system, will return if either the username or password are not the
	//appropriate length.
	public void addUser(String username, String password, String type, String status)
	{
		if(userExist(username) == true) 
		{
			DBErrorHandler.handle("DuplicateUser");
		}
		else
		{
			if(username.length() >= MINIMUM_NAME_LENGTH)
			{
				if(password.length() >= MINIMUM_PASS_LENGTH)
				{
					try 
					{
						PreparedStatement ps;
						ps = connection.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?)");
						ps.setString(1, username);
						ps.setString(2, password);
						ps.setString(3, type);
						ps.setString(4, status);
							
						ps.executeUpdate();
						ps.close();
						
						//once the user is successfully created, their default user settings are created
						DefaultUserSettingsGenerator.generate(connection, username);
						BrowsingHistoryTableGenerator.generate(connection, username);
						
						System.out.println("New user added");
					}
				
					catch (SQLException ex) 
					{
						DBErrorHandler.handle("AddUser");
					}
				}
				else 
				{
					DBErrorHandler.handle("BadPassword");
				}
			}
			else 
			{
				DBErrorHandler.handle("BadUserName");
			}
		}
	}
	
	//updates general information for a particular user
	public void editUser(String username, String password, String type, String status) 
	{
		if(userExist(username) == false) 
		{
			DBErrorHandler.handle("NonexistiantUser");
		}
		else
		{
			try 
			{
			
				PreparedStatement ps;
				ps = connection.prepareStatement("UPDATE users SET password = ?, type = ?, status = ? WHERE username = ?");
				ps.setString(1, password);
				ps.setString(2, type);
				ps.setString(3, status);
				ps.setString(4, username);
			
				ps.executeUpdate();
				ps.close();
				System.out.println("User information updated");
			}
			catch (SQLException ex) 
			{
				DBErrorHandler.handle("EditUser");
			}
		}
	}
	
	//updates the personal settings of all users to the specified values
	public void updateGlobalSettings(int resultCount, int timeOut, String displayMethod) 
	{
		try 
		{
			PreparedStatement ps;
			ps = connection.prepareStatement("UPDATE user_settings SET result_count = ?, time_out = ?, display = ?");
			ps.setInt(1, resultCount);
			ps.setInt(2, timeOut);
			ps.setString(3, displayMethod);
		
			ps.executeUpdate();
			ps.close();
			System.out.println("Global settings updated");
		}
		catch (SQLException ex) 
		{
			DBErrorHandler.handle("GlobalSettings");
		}
	}
	
	//checks if a particular user exists in the database
	public boolean userExist(String username) 
	{
		
		boolean exist = false;
		
		try 
		{
			
			PreparedStatement ps;
			String user = null;
		
			ps = connection.prepareStatement("SELECT users FROM users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
		
			while(rs.next()) 
			{
				user = rs.getString(1);
			}
			
			if(user != null) 
			{
				exist = true;
			}
			
			ps.close();
		}
		catch (SQLException ex) 
		{
			DBErrorHandler.handle("UserExist");
		}
		
		return exist;
	}
}
