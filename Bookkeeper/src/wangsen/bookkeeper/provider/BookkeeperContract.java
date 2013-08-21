package wangsen.bookkeeper.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 13-8-6.
 */
public final class BookkeeperContract {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Bookkeeper.db";
	
	public static final String AUTHORITY = 
			"wangsen.bookkeeper.provider";
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

	private static final String INT_TYPE = " INTEGER";
	private static final String COMMA_SEP = ", ";

	public static abstract class BillTable implements BaseColumns {
		
		private static final String MIME_TYPE_SUFFIX = "/vnd.wangsen.bookkeeper.provider.bill";

		public static final String TABLE_NAME = "bill_table";
		public static final String COLUMN_NAME_ADULTS_COUNT = "adults_count";
		public static final String COLUMN_NAME_CHILDREN_COUNT = "children_count";
		public static final String COLUMN_NAME_TIME = "time";
		public static final String COLUMN_NAME_PAYMENT = "payment";
		public static final String COLUMN_NAME_BILL_PAID = "bill_paid";
		public static final String COLUMN_NAME_INCOME_SUM = 
				"SUM("+ BillTable.COLUMN_NAME_PAYMENT + ")";

		public static final int ADULT_COST = 30;
		public static final int CHILD_COST = 15;

		static final String SQL_CREATE_CHECKS = 
			"CREATE TABLE " + TABLE_NAME + " (" 
			+ _ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME_ADULTS_COUNT + INT_TYPE + COMMA_SEP
			+ COLUMN_NAME_CHILDREN_COUNT + INT_TYPE + COMMA_SEP 
			+ COLUMN_NAME_TIME + INT_TYPE + COMMA_SEP 
			+ COLUMN_NAME_PAYMENT + INT_TYPE + COMMA_SEP 
			+ COLUMN_NAME_BILL_PAID + INT_TYPE + " );";

		static final String SQL_DELETE_CHECKS = 
				"DROP TABLE IF EXISTS " + TABLE_NAME;
		
		public static final String PATH = "bills";
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
		
		public static final String CONTENT_TYPE = 
				ContentResolver.CURSOR_DIR_BASE_TYPE + MIME_TYPE_SUFFIX;
		
		public static final String CONTENT_ITEM_TYPE = 
				ContentResolver.CURSOR_ITEM_BASE_TYPE + MIME_TYPE_SUFFIX;
	}

}
