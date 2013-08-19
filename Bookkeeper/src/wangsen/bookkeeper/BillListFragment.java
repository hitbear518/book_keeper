package wangsen.bookkeeper;

import wangsen.bookkeeper.provider.BookkeeperContract.BillTable;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Administrator on 13-8-6.
 */
public class BillListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	// Views
	private ImageButton mAddBtn;

	private BillCursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getLoaderManager().initLoader(0, null, this);
		mAdapter = new BillCursorAdapter(getActivity(), null, 0);
		setListAdapter(mAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_list, container, false);

		mAddBtn = (ImageButton) root.findViewById(R.id.add_btn);
		mAddBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AddBillDialog().show(getFragmentManager(), "dialog");
			}
		});

		return root;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { BillTable._ID,
				BillTable.COLUMN_NAME_ADULTS_COUNT,
				BillTable.COLUMN_NAME_CHILDREN_COUNT,
				BillTable.COLUMN_NAME_PAYMENKT, BillTable.COLUMN_NAME_TIME,
				BillTable.COLUMN_NAME_PAYMENT_CHECK };

		String sortOrder = BillTable.COLUMN_NAME_TIME + " DESC";
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				BillTable.CONTENT_URI, projection, null, null, sortOrder);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
}
