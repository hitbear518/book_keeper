package wangsen.bookkeeper;

import wangsen.bookkeeper.provider.BookkeeperContract.BillTable;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by Administrator on 13-8-6.
 */
public class BillListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {
	
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_delete:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			Uri  uri = Uri.parse(BillTable.CONTENT_URI + "/" + info.id);
			getActivity().getContentResolver().delete(uri, null, null);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
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
				new EditBillDialog().show(getFragmentManager(), "dialog");
			}
		});

		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			private boolean mMultipleItemCheckked;
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				if (mMultipleItemCheckked) {
					menu.findItem(R.id.action_edit).setEnabled(false);
				} else {
					menu.findItem(R.id.action_edit).setEnabled(true);
				}
				return false;
			}
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
			}
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.context_menu, menu);
				mMultipleItemCheckked = false;
				return true;
			}
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_delete:
					deleteSelectedItems();
					mode.finish();
					return true;
				case R.id.action_edit:
					editItem();
					mode.finish();
					return true;
				default:
					return false;
				}
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position,
					long id, boolean checked) {
				if (getListView().getCheckedItemCount() > 1 != mMultipleItemCheckked) {
					mMultipleItemCheckked = getListView().getCheckedItemCount() > 1;
					mode.invalidate();
				}
			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { 
				BillTable._ID,
				BillTable.COLUMN_NAME_ADULTS_COUNT,
				BillTable.COLUMN_NAME_CHILDREN_COUNT,
				BillTable.COLUMN_NAME_PAYMENKT, BillTable.COLUMN_NAME_TIME,
				BillTable.COLUMN_NAME_BILL_PAID };
		
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
	
	private void deleteSelectedItems() {
		long[] idsLong = getListView().getCheckedItemIds();
		StringBuilder questionMarksBuilder = new StringBuilder();
		String[] selectionArgs = new String[idsLong.length];
		for (int i = 0; i < idsLong.length; i++) {
			questionMarksBuilder.append("?, ");
			selectionArgs[i] = String.valueOf(idsLong[i]);
		}
		String questionMarks = questionMarksBuilder.substring(0, questionMarksBuilder.length() - 2);
		String selection = BillTable._ID + " IN (" + questionMarks + ")";
		getActivity().getContentResolver().delete(BillTable.CONTENT_URI, selection, selectionArgs);
	}
	
	private void editItem() {
		SparseBooleanArray positions = getListView().getCheckedItemPositions();
		int position = positions.keyAt(0);
		CursorWrapper c = (CursorWrapper) getListView().getItemAtPosition(position);
		
		int adultsCount = c.getInt(
				c.getColumnIndexOrThrow(BillTable.COLUMN_NAME_ADULTS_COUNT));
		int childrenCount = c.getInt(
				c.getColumnIndexOrThrow(BillTable.COLUMN_NAME_CHILDREN_COUNT));
		int billPaid = c.getInt(
				c.getColumnIndexOrThrow(BillTable.COLUMN_NAME_BILL_PAID));
		long rowId = c.getLong(
				c.getColumnIndexOrThrow(BillTable._ID));
		
		EditBillDialog dialog = new EditBillDialog();
		Bundle args = new Bundle();
		args.putInt(EditBillDialog.ARG_ADULTS_COUNT, adultsCount);
		args.putInt(EditBillDialog.ARG_CHILDREN_COUNT, childrenCount);
		args.putBoolean(EditBillDialog.ARG_BILL_PAID, billPaid > 0 ? true : false);
		args.putLong(EditBillDialog.ARG_ROW_ID, rowId);
		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "dialog");
	}
}
