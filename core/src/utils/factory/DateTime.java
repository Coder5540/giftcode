package utils.factory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

	public static String FORMAT = "yyyy-MM-dd";

	public static Date getDate(long time) {
		return new Date(time);
	}

	public static String getStringDate(long time, String format) {
		return new SimpleDateFormat(format).format(new Date(time));
	}

	public static boolean isDateValid(String date) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getCurrentDate(String format) {
		return new SimpleDateFormat(format).format(Calendar.getInstance()
				.getTime());
	}

	public static String getDateBefor(String format, int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -day);
		return new SimpleDateFormat(format).format(cal.getTime());
	}

	public static String getDateBefor(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -day);
		return new SimpleDateFormat(FORMAT).format(cal.getTime());
	}

	public static String getDateAfter(String format, int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, day);
		return new SimpleDateFormat(format).format(cal.getTime());
	}

}
