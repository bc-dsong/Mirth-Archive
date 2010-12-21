package archive.database.records;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateUtil 
{
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	public static String getCurrentTimeAndDate() 
	{
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());
	}

	 
}
