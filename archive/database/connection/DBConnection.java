package archive.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import archive.database.error.DBErrorHandler;

public class DBConnection 
{
	
	String url = String.format("jdbc:postgresql://%s/%s", "10.20.60.74", "testdb");
	Connection dbCon;
	
	//logIn and password values provided by user input
	public DBConnection(String logIn, String password) 
	{
		loadDriver();
		
		try 
		{
			//this connection object is the basis for all interaction with the PostgreSQL database
			dbCon = DriverManager.getConnection(url, 
					logIn, password);
		}
		catch(SQLException ex)
		{
			DBErrorHandler.handle("Connection");
		}
	}
	/*
	 * This driver must be successfully loaded before anything else can be done
	 */
	private void loadDriver() 
	{
		try 
		{
			 Class.forName("org.postgresql.Driver");
		} 
		catch(ClassNotFoundException cnfe) 
		{
			DBErrorHandler.handle("DriverLoad");
		}
	}
	
	public Connection getDBConnection() 
	{
		return dbCon;
	}
	
	public void closeConnection()
	{
		try 
		{
			dbCon.close();
			System.out.println("Database connection closed");
		} 
		catch (SQLException e) 
		{
			DBErrorHandler.handle("ConnectionClose");
		}
	}

}
