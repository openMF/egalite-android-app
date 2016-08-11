package com.bfsi.egalite.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for date / time operations or formatting
 * 
 * @author vijay
 * 
 */
public class DateUtil {

	/**
	 * Calculates total milliseconds for given number of days
	 * 
	 * @param day
	 * @return
	 */
	public static long toMilliSeconds(double day) {
		return (long) (day * 24 * 60 * 60 * 1000);
	}

	/**
	 * Gives long milliseconds using system time
	 * 
	 * @return
	 */
	
	public static long getCurrentDataTime() {
		long datetime = 0;
		datetime = System.currentTimeMillis();
		if(!(CommonContexts.APP_TODAY.equals("")))
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			String hours = dateFormat.format(new Date(datetime));
			String finalValue = CommonContexts.APP_TODAY+" "+hours;
			datetime = stringToMilliseconds(finalValue);
		}
		return datetime;
	}

	/**
	 * Converts Date to long milliseconds
	 * 
	 * @param date
	 * @return
	 */
	public static long getConvertedDatetime(Date date) {
		long datetime = 0;
		datetime = date.getTime();
		return datetime;
	}

	/**
	 * Gives current Date in stringformat <dd-MMM-yyyy>
	 * 
	 * @return
	 */
	public static String currentDate() {
		String today = null;
		Date now = new Date();
		today = CommonContexts.endateFormat.format(now);
		return today;
	}
	public static long stringToMilliseconds(String date)
	{
		Date dates = null;
		try{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss"); 
	    dates = (Date) formatter.parse(date);
	    System.out.println(CommonContexts.dateFormat.format(new Date(dates.getTime())));
	    
	    return dates.getTime();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
		
	}
	public static long stringToMillisecondss(String date)
	{
		Date dates = null;
		try{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	    dates = (Date) formatter.parse(date);
	    return dates.getTime();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
		
	}
	public static long stringToMillisecondsss(String date)
	{
		Date dates = null;
		try{
	    dates = (Date) CommonContexts.dateMonthYear.parse(date);
	    return dates.getTime();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
		
	}

}
