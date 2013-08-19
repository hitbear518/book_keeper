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

	private static final String INT_TYPE = " INTEGER";
	private static final String COMMA_SEP = ", ";
	
	public BookkeeperContract() {
	}

	public static abstract class BillTable implements BaseColumns {

		public static final String TABLE_NAME = "bill_table";
		public static final String COLUMN_NAME_ADULTS_COUNT = "adults_count";
		public static final String COLUMN_NAME_CHILDREN_COUNT = "children_count";
		public static final String COLUMN_NAME_TIME = "time";
		public static final String COLUMN_NAME_PAYMENKT = "payment";
		public static final String COLUMN_NAME_PAYMENT_CHECK = "payment_check";

		public static final int ADULT_COST = 30;
		public static final int CHILD_COST = 15;

		static final String SQL_CREATE_CHECKS = 
			"CREATE TABLE " + TABLE_NAME + " (" 
			+ _ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME_ADULTS_COUNT + INT_TYPE + COMMA_SEP
			+ COLUMN_NAME_CHILDREN_COUNT + INT_TYPE + COMMA_SEP 
			+ COLUMN_NAME_TIME + INT_TYPE + COMMA_SEP 
			+ COLUMN_NAME_PAYMENKT + INT_TYPE + COMMA_SEP 
			+ COLUMN_NAME_PAYMENT_CHECK + INT_TYPE + " );";

		static final String SQL_DELETE_CHECKS = 
				"DROP TABLE IF EXISTS " + TABLE_NAME;
		
		public static final String BILL_PATH = "bills";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BILL_PATH);
		
		public static final String CONTENT_TYPE = 
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/bills";
		
		public static final String CONTENT_ITEM_TYPE = 
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bill";
	}

}
