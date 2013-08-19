package wangsen.mychafinedishbuffetbookkeeper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Administrator on 13-8-6.
 */
public class BillListFragment extends ListFragment {

    // Views
    private ImageButton mAddBtn;

    private BillCursorAdapter mAdapter;
    private BookkeeperDbHelper mDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mDbHelper = new BookkeeperDbHelper(getActivity());
       
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
        	BookkeeperContract.Bill._ID,
        	BookkeeperContract.Bill.COLUMN_NAME_ADULTS_COUNT,
        	BookkeeperContract.Bill.COLUMN_NAME_CHILDREN_COUNT,
        	BookkeeperContract.Bill.COLUMN_NAME_PAYMENKT,
        	BookkeeperContract.Bill.COLUMN_NAME_TIME,
        	BookkeeperContract.Bill.COLUMN_NAME_PAYMENT_CHECK
        };
        String sortOrder = BookkeeperContract.Bill.COLUMN_NAME_TIME + " DESC";
        Cursor c = db.query(BookkeeperContract.Bill.TABLE_NAME, projection, null, null, null, null, sortOrder);
        
//        String[] fromColumns = {
//        	BookkeeperContract.Bill.COLUMN_NAME_ADULTS_COUNT,
//        	BookkeeperContract.Bill.COLUMN_NAME_CHILDREN_COUNT,
//        	BookkeeperContract.Bill.COLUMN_NAME_PAYMENKT,
//        	BookkeeperContract.Bill.COLUMN_NAME_TIME,
//        	BookkeeperContract.Bill.COLUMN_NAME_PAYMENT_CHECK
//        };
//        int[] toFields = {
//        	R.id.label_adults_count,
//        	R.id.label_children_count,
//        	R.id.label_payment,
//        	R.id.label_time,
//        	R.id.img_payment_check
//        };
        mAdapter = new BillCursorAdapter(getActivity(), c, 0);
        setListAdapter(mAdapter);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
}
