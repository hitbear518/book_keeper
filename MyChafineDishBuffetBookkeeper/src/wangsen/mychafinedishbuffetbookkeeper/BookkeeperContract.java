package wangsen.mychafinedishbuffetbookkeeper;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 13-8-6.
 */
public final class BookkeeperContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Bookkeeper.db";

//    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";

    public BookkeeperContract() {
    }

    public static abstract class Bill implements BaseColumns {

        static final String SQL_CREATE_CHECKS =
            "CREATE TABLE " + BookkeeperContract.Bill.TABLE_NAME + " ("
                + BookkeeperContract.Bill._ID + " INTEGER PRIMARY KEY, "
                + BookkeeperContract.Bill.COLUMN_NAME_ADULTS_COUNT + INT_TYPE + COMMA_SEP
                + BookkeeperContract.Bill.COLUMN_NAME_CHILDREN_COUNT + INT_TYPE + COMMA_SEP
                + BookkeeperContract.Bill.COLUMN_NAME_TIME + INT_TYPE + COMMA_SEP
                + BookkeeperContract.Bill.COLUMN_NAME_PAYMENKT + INT_TYPE + COMMA_SEP
                + BookkeeperContract.Bill.COLUMN_NAME_PAYMENT_CHECK + INT_TYPE + " );";
        static final String SQL_DELETE_CHECKS =
            "DROP TABLE IF EXISTS " + BookkeeperContract.Bill.TABLE_NAME;

        public static final String TABLE_NAME = "bill_table";
        public static final String COLUMN_NAME_ADULTS_COUNT = "adults_count";
        public static final String COLUMN_NAME_CHILDREN_COUNT = "children_count";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_PAYMENKT = "payment";
        public static final String COLUMN_NAME_PAYMENT_CHECK = "payment_check";
        
        public static final int ADULT_COST = 30;
        public static final int CHILD_COST = 15;
    }
    
}
