package wangsen.bookkeeper;

import wangsen.bookkeeper.provider.BookkeeperContract.BillTable;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatisticsFragment extends Fragment
	implements LoaderCallbacks<Cursor>{
	
	private static final int LOADER_ID_DAY = 0;
	private static final int LOADER_ID_WEEK = 1;
	private static final int LOADER_ID_MONTH = 2;
	
	private TextView mLabelIncomeDay;
	private TextView mLabelIncomeWeek;
	private TextView mLabelIncomeMonth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getLoaderManager().initLoader(LOADER_ID_DAY, null, this);
		getLoaderManager().initLoader(LOADER_ID_WEEK, null, this);
		getLoaderManager().initLoader(LOADER_ID_MONTH, null, this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_statistics, container, false);
		mLabelIncomeDay = (TextView) view.findViewById(R.id.label_income_day);
		mLabelIncomeWeek = (TextView) view.findViewById(R.id.label_income_week);
		mLabelIncomeMonth = (TextView) view.findViewById(R.id.label_income_month);
		return view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {
				BillTable.COLUMN_NAME_INCOME_SUM
		};
		
		String selection = BillTable.COLUMN_NAME_TIME + " > ?";
		String[] selectionArgs = new String[1];
		switch (id) {
		case LOADER_ID_DAY:
			selectionArgs[0] = String.valueOf(Util.dayStartMillis());	
		break;
		case LOADER_ID_WEEK:
			selectionArgs[0] = String.valueOf(Util.weekStartMillis());	
		break;
		case LOADER_ID_MONTH:
			selectionArgs[0] = String.valueOf(Util.monthStartMillis());	
		break;	
		default:
			throw new IllegalArgumentException("Unknown loader id: " + id);
		}
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				BillTable.CONTENT_URI, projection, selection, selectionArgs, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		data.moveToFirst();
		int incomeSum = data.getInt(
				data.getColumnIndexOrThrow(BillTable.COLUMN_NAME_INCOME_SUM));
		switch (loader.getId()) {
		case LOADER_ID_DAY:
			mLabelIncomeDay.setText(getString(R.string.income_day) +
					incomeSum + getString(R.string.yuan));
		break;
		case LOADER_ID_WEEK:
			mLabelIncomeWeek.setText(getString(R.string.income_week) +
					incomeSum + getString(R.string.yuan));
		break;
		case LOADER_ID_MONTH:
			mLabelIncomeMonth.setText(getString(R.string.income_month) +
					incomeSum + getString(R.string.yuan));
		break;	
		default:
			throw new IllegalArgumentException("Unknown loader id: " + loader.getId());
		}
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mLabelIncomeDay.setText(null);
		mLabelIncomeWeek.setText(null);
		mLabelIncomeMonth.setText(null);
	}
}
