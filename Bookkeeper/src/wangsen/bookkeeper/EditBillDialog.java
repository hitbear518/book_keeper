package wangsen.bookkeeper;

import wangsen.bookkeeper.provider.BookkeeperContract;
import wangsen.bookkeeper.provider.BookkeeperContract.BillTable;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

public class EditBillDialog extends DialogFragment {
	
	public static final String FRAG_TAG = "dialog";
	
	public static final String ARG_ADULTS_COUNT = "adults count";
	public static final String ARG_CHILDREN_COUNT = "children count";
	public static final String ARG_BILL_PAID = "bill paid";
	public static final String ARG_ROW_ID = "row id";
	
	private NumberPicker mNumPickerAdult;
	private NumberPicker mNumPickerChild;
	private Switch mSwitchBillPaid;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if (getArguments() != null) {
			builder.setTitle(R.string.title_edit_bill);
		} else {
			builder.setTitle(R.string.title_new_bill);
		}
		builder.setNegativeButton(android.R.string.cancel, null)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Bundle args = getArguments();
					if (args != null) {
						updateBill();
					} else {
						insertBill();
					}
				}
			});
		
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View dialogView = inflater.inflate(R.layout.dialog_edit_bill, null);
		mNumPickerAdult = (NumberPicker) dialogView.findViewById(R.id.num_picker_adult);
		mNumPickerAdult.setMinValue(0);
		mNumPickerAdult.setMaxValue(50);
		mNumPickerChild = (NumberPicker) dialogView.findViewById(R.id.num_picker_child);
		mNumPickerChild.setMinValue(0);
		mNumPickerChild.setMaxValue(50);
		mSwitchBillPaid = (Switch) dialogView.findViewById(R.id.switch_bill_paid);
		
		Bundle args = getArguments();
		if (args != null) {
			int adultsCount = args.getInt(ARG_ADULTS_COUNT);
			int childrenCount = args.getInt(ARG_CHILDREN_COUNT);
			boolean billPaid = args.getBoolean(ARG_BILL_PAID);
			
			mNumPickerAdult.setValue(adultsCount);
			mNumPickerChild.setValue(childrenCount);
			mSwitchBillPaid.setChecked(billPaid);
		}
		
		builder.setView(dialogView);
		
		return builder.create();
	}
	
	private void insertBill() {
		int adultsCount = mNumPickerAdult.getValue();
		int childrenCount = mNumPickerChild.getValue();
		int payment = adultsCount * BookkeeperContract.BillTable.ADULT_COST +
				childrenCount * BookkeeperContract.BillTable.CHILD_COST;
		long time = System.currentTimeMillis();
		int billPaid = mSwitchBillPaid.isChecked() ? 1 : 0;
		
		ContentValues values = new ContentValues();
		values.put(BillTable.COLUMN_NAME_ADULTS_COUNT, adultsCount);
		values.put(BillTable.COLUMN_NAME_CHILDREN_COUNT, childrenCount);
		values.put(BillTable.COLUMN_NAME_PAYMENT, payment);
		values.put(BillTable.COLUMN_NAME_TIME, time);
		values.put(BillTable.COLUMN_NAME_BILL_PAID, billPaid);
		
		getActivity().getContentResolver().insert(BillTable.CONTENT_URI, values);
	}
	
	private void updateBill() {
		int adultsCount = mNumPickerAdult.getValue();
		int childrenCount = mNumPickerChild.getValue();
		int payment = adultsCount * BookkeeperContract.BillTable.ADULT_COST +
				childrenCount * BookkeeperContract.BillTable.CHILD_COST;
		int billPaid = mSwitchBillPaid.isChecked() ? 1 : 0;
		
		ContentValues values = new ContentValues();
		values.put(BillTable.COLUMN_NAME_ADULTS_COUNT, adultsCount);
		values.put(BillTable.COLUMN_NAME_CHILDREN_COUNT, childrenCount);
		values.put(BillTable.COLUMN_NAME_PAYMENT, payment);
		values.put(BillTable.COLUMN_NAME_BILL_PAID, billPaid);
		
		long rowId = getArguments().getLong(ARG_ROW_ID);
		Uri uri = Uri.withAppendedPath(BillTable.CONTENT_URI, String.valueOf(rowId));
		
		getActivity().getContentResolver().update(uri, values, null, null);
	}
}
