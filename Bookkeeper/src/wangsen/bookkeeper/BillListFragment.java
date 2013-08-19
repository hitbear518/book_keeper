package wangsen.bookkeeper;

import wangsen.bookkeeper.provider.BookkeeperContract.BillTable;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;

/**
 * Created by Administrator on 13-8-6.
 */
public class BillListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {
	
	private ImageButton mAddBtn;
	private BillCursorAdapter mAdapter;
	
	private static final int LOADER_ID = 0; 
	private static final int DELETE_ID = Menu.FIRST + 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getLoaderManager().initLoader(LOADER_ID, null, this);
		mAdapter = new BillCursorAdapter(getActivity(), null, 0);
		setListAdapter(mAdapter);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			Uri  uri = Uri.parse(BillTable.CONTENT_URI + "/" + info.id);
			getActivity().getContentResolver().delete(uri, null, null);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {
		case LOADER_ID:
			String[] projection = { BillTable._ID,
					BillTable.COLUMN_NAME_ADULTS_COUNT,
					BillTable.COLUMN_NAME_CHILDREN_COUNT,
					BillTable.COLUMN_NAME_PAYMENKT, BillTable.COLUMN_NAME_TIME,
					BillTable.COLUMN_NAME_PAYMENT_CHECK };
			String sortOrder = BillTable.COLUMN_NAME_TIME + " DESC";
			CursorLoader cursorLoader = new CursorLoader(getActivity(),
					BillTable.CONTENT_URI, projection, null, null, sortOrder);
			return cursorLoader;
		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}
}
