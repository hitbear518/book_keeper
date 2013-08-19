package wangsen.bookkeeper.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 13-8-7.
 */
public class BookkeeperDbHelper extends SQLiteOpenHelper {

    public BookkeeperDbHelper(Context context) {
        super(context, BookkeeperContract.DATABASE_NAME, null, BookkeeperContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BookkeeperContract.BillTable.SQL_CREATE_CHECKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BookkeeperContract.BillTable.SQL_DELETE_CHECKS);
        onCreate(db);
    }
}
