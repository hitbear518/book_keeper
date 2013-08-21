package wangsen.bookkeeper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Util {

	public static SimpleDateFormat sdf ;
	public static final String TAG_INSERT = "insert";
	public static final DateFormat DATE_TIME_FORMAT = DateFormat.getDateTimeInstance();
	
	public static long dayStartMillis() {
		Calendar dayStart = Calendar.getInstance();
		dayStart.set(Calendar.HOUR_OF_DAY, 0);
		dayStart.set(Calendar.MINUTE, 0);
		dayStart.set(Calendar.SECOND, 0);
		dayStart.set(Calendar.MILLISECOND, 0);
		return dayStart.getTimeInMillis();
	}
	
	public static long weekStartMillis() {
		Calendar weekStart = Calendar.getInstance(Locale.CHINA);
		weekStart.setFirstDayOfWeek(Calendar.MONDAY);
		weekStart.set(Calendar.DAY_OF_WEEK, weekStart.getFirstDayOfWeek());
		weekStart.set(Calendar.HOUR_OF_DAY, 0);
		weekStart.set(Calendar.MINUTE, 0);
		weekStart.set(Calendar.SECOND, 0);
		weekStart.set(Calendar.MILLISECOND, 0);
		return weekStart.getTimeInMillis();
	}
	
	public static long monthStartMillis() {
		Calendar monthStart = Calendar.getInstance();
		monthStart.set(Calendar.DAY_OF_MONTH, 1);
		monthStart.set(Calendar.HOUR_OF_DAY, 0);
		monthStart.set(Calendar.MINUTE, 0);
		monthStart.set(Calendar.SECOND, 0);
		monthStart.set(Calendar.MILLISECOND, 0);
		return monthStart.getTimeInMillis();
	}
}
