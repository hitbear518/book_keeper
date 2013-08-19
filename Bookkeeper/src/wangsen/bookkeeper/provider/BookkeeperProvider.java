package wangsen.bookkeeper.provider;

import wangsen.bookkeeper.provider.BookkeeperContract.BillTable;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BookkeeperProvider extends ContentProvider {
	
	private BookkeeperDbHelper mDbHelper;
	
	private static final int CODE_BILLS = 1;
	private static final int CODE_BILL_ID = 2;
	
	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		URI_MATCHER.addURI(BookkeeperContract.AUTHORITY, BillTable.BILL_PATH, CODE_BILLS);
		URI_MATCHER.addURI(BookkeeperContract.AUTHORITY, BillTable.BILL_PATH  + "/#", CODE_BILL_ID);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new BookkeeperDbHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(BillTable.TABLE_NAME);
		
		int uriType = URI_MATCHER.match(uri);
		switch (uriType) {
		case CODE_BILLS:
			break;
		case CODE_BILL_ID:
			queryBuilder.appendWhere(BillTable._ID + " = " + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor c = queryBuilder.query(
				db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriCode = URI_MATCHER.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long id = 0;
		switch (uriCode) {
		case CODE_BILLS:
			id = db.insert(BillTable.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BillTable.BILL_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriCode = URI_MATCHER.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriCode) {
		case CODE_BILLS:
			rowsDeleted = db.delete(BillTable.TABLE_NAME, selection, selectionArgs);
			break;
		case CODE_BILL_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(BillTable.TABLE_NAME, 
						BillTable._ID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(BillTable.TABLE_NAME, 
						BillTable._ID + "=" + id + " and " + selection, 
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknow URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriCode = URI_MATCHER.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriCode) {
		case CODE_BILLS:
			rowsUpdated = db.update(BillTable.TABLE_NAME, values, selection, selectionArgs);
			break;
		case CODE_BILL_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(BillTable.TABLE_NAME, values, 
						BillTable._ID + "=" + id, null);
			} else {
				rowsUpdated = db.update(BillTable.TABLE_NAME, values, 
						BillTable._ID + "=" + id + " and " + selection, 
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		return rowsUpdated;
	}

}
