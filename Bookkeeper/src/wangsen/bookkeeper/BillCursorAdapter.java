package wangsen.bookkeeper;

import java.util.Date;

import wangsen.bookkeeper.provider.BookkeeperContract;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BillCursorAdapter extends CursorAdapter {
	
	private LayoutInflater mInflater;

	public BillCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.bill_list_item, parent, false);
		bindView(view, context, cursor);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView labelAdultsCount = (TextView) view.findViewById(R.id.label_adults_count);
		labelAdultsCount.setText(cursor.getString(
				cursor.getColumnIndex(BookkeeperContract.BillTable.COLUMN_NAME_ADULTS_COUNT))
				+ context.getString(R.string.adult));
		TextView labelChildrenCount = (TextView) view.findViewById(R.id.label_children_count);
		labelChildrenCount.setText(cursor.getString(
				cursor.getColumnIndex(BookkeeperContract.BillTable.COLUMN_NAME_CHILDREN_COUNT))
				+ context.getString(R.string.child));
		TextView labelPayment = (TextView) view.findViewById(R.id.label_payment);
		labelPayment.setText(cursor.getString(
				cursor.getColumnIndex(BookkeeperContract.BillTable.COLUMN_NAME_PAYMENKT))
				+ context.getString(R.string.yuan));
		TextView labelTime = (TextView) view.findViewById(R.id.label_date_time);
		Date dateTime = new Date(cursor.getLong(
				cursor.getColumnIndex(BookkeeperContract.BillTable.COLUMN_NAME_TIME)));
		String dateTimeStr = Util.DATE_TIME_FORMAT.format(dateTime);
		labelTime.setText(dateTimeStr);
		ImageView imgBillPaid = (ImageView) view.findViewById(R.id.img_payment_check);
		int billPaid = cursor.getInt(
				cursor.getColumnIndex(BookkeeperContract.BillTable.COLUMN_NAME_PAYMENT_CHECK));
		imgBillPaid.setImageResource(billPaid == 1 ? R.drawable.ic_bill_paid : R.drawable.ic_bill_not_paid );
	}
}
