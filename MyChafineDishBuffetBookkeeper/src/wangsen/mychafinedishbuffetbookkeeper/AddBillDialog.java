package wangsen.mychafinedishbuffetbookkeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;

public class AddBillDialog extends DialogFragment {
	
	private NumberPicker mNumPickerAdult;
	private NumberPicker mNumPickerChild;
	private Switch mSwitchBillPaid;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.title_add_check)
			.setNegativeButton(android.R.string.cancel, null)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					saveCheck();
				}
			});
		
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View dialogView = inflater.inflate(R.layout.dialog_add_bill, null);
		mNumPickerAdult = (NumberPicker) dialogView.findViewById(R.id.num_picker_adult);
		mNumPickerAdult.setMinValue(0);
		mNumPickerAdult.setMaxValue(50);
		mNumPickerChild = (NumberPicker) dialogView.findViewById(R.id.num_picker_child);
		mNumPickerChild.setMinValue(0);
		mNumPickerChild.setMaxValue(50);
		mSwitchBillPaid = (Switch) dialogView.findViewById(R.id.switch_bill_paid);
		builder.setView(dialogView);
		
		return builder.create();
	}
	
	private void saveCheck() {
		BookkeeperDbHelper dbHelper = new BookkeeperDbHelper(getActivity());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		int adultsCount = mNumPickerAdult.getValue();
		int childrenCount = mNumPickerChild.getValue();
		int payment = adultsCount * BookkeeperContract.Bill.ADULT_COST +
				childrenCount * BookkeeperContract.Bill.CHILD_COST;
		long time = System.currentTimeMillis();
		int billPaid = mSwitchBillPaid.isChecked() ? 1 : 0;
		
		ContentValues values = new ContentValues();
		values.put(BookkeeperContract.Bill.COLUMN_NAME_ADULTS_COUNT, adultsCount);
		values.put(BookkeeperContract.Bill.COLUMN_NAME_CHILDREN_COUNT, childrenCount);
		values.put(BookkeeperContract.Bill.COLUMN_NAME_PAYMENKT, payment);
		values.put(BookkeeperContract.Bill.COLUMN_NAME_TIME, time);
		values.put(BookkeeperContract.Bill.COLUMN_NAME_PAYMENT_CHECK, billPaid);
		
		long newRowId = db.insert(BookkeeperContract.Bill.TABLE_NAME, null, values);
		Log.d(Util.TAG_INSERT, "new row id = " + String.valueOf(newRowId));
	}
}
