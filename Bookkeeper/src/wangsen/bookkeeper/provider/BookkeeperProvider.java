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
	
	private static final int MATCH_BILLS = 1;
	private static final int MATCH_BILLS_ID = 2;
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sUriMatcher.addURI(BookkeeperContract.AUTHORITY, BillTable.PATH, MATCH_BILLS);
		sUriMatcher.addURI(BookkeeperContract.AUTHORITY, BillTable.PATH  + "/#", MATCH_BILLS_ID);
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
		
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case MATCH_BILLS:
			break;
		case MATCH_BILLS_ID:
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
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case MATCH_BILLS:
			return BillTable.CONTENT_TYPE;
		case MATCH_BILLS_ID:
			return BillTable.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final int match = sUriMatcher.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long id = 0;
		switch (match) {
		case MATCH_BILLS:
			id = db.insert(BillTable.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BillTable.PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final int match = sUriMatcher.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (match) {
		case MATCH_BILLS:
			rowsDeleted = db.delete(BillTable.TABLE_NAME, selection, selectionArgs);
			break;
		case MATCH_BILLS_ID:
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
		final int match = sUriMatcher.match(uri);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (match) {
		case MATCH_BILLS:
			rowsUpdated = db.update(BillTable.TABLE_NAME, values, selection, selectionArgs);
			break;
		case MATCH_BILLS_ID:
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
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
