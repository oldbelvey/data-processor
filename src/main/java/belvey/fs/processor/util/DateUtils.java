package belvey.fs.processor.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static SimpleDateFormat UTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	
	public static Date getdate(String str) throws ParseException{
		return  UTC.parse(str);
	}
	
	public static Long getTimeStamp(String str) throws ParseException{
		return  UTC.parse(str).getTime();
	}
	
	public static void main(String[] args) throws ParseException {
		
		Date d = getdate("2017-03-10 00:06:46,681");
		System.out.println(d.toString());
	}
	
}
