/*
 * class used to sumbit and receive files from the database
 * 
 */

package archive.database.records;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import archive.database.error.DBErrorHandler;

public class RecordHandler 
{
	Connection connection;
	String userName;
	
	public RecordHandler(Connection connection, String userName) 
	{
		this.connection = connection;
		this.userName = userName;
	}

	public void submitFile(int Id, String fileName)
	{
		try
		{
			File file = new File(fileName);

			FileInputStream fis = new FileInputStream(file);
			PreparedStatement ps = connection.prepareStatement("INSERT INTO records VALUES (?, ?, ?)");
			ps.setBinaryStream(1, fis, (int)file.length());
			ps.setInt(2, Id);
			ps.setInt(3, (int)file.length());
			ps.executeUpdate();
			ps.close();
			fis.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	//Retrieves files from the database as binary data.
	//
	//*note that this method will only work if files were placed into the database as bytea or blob
	public void retrieveFile(int recordId) 
	{
		PreparedStatement ps;
		byte[] fileBytes = null;
		OutputStream out; 
		
		try 
		{
			ps = connection.prepareStatement("SELECT file FROM records WHERE id = ?");
			ps.setInt(1, recordId);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) 
			{
				fileBytes = rs.getBytes(1);
			}
			
			try 
			{
				File f;
			    f = new File("outputfile.xml");
			    f.createNewFile();
				out = new FileOutputStream("outputfile.xml");
				out.write(fileBytes);
				ps.close();
				out.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			BrowsingHistoryLogger.log(connection, userName, recordId, DateUtil.getCurrentTimeAndDate());		
		}
		catch(SQLException ex) 
		{
			DBErrorHandler.handle("FileRetrieve");
		}
	}	
}
