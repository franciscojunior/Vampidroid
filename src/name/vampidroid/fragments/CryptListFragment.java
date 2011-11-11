package name.vampidroid.fragments;

import name.vampidroid.DatabaseHelper;
import name.vampidroid.FilterModel;
import name.vampidroid.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.Menu;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CryptListFragment extends ListFragment {

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	String mQuery = "";

	String mFilter = "";

	String mOrderBy = "";

	OnCryptCardSelectedListener mListener;

	boolean mHighlight;

	int mCurrentPosition = 0;

	// final int mAlternateListDrawables[] =
	// {R.drawable.list_background_colorselector1,
	// R.drawable.list_background_colorselector2};

	private static final int MENU_ADD_FAVORITES_ID = Menu.FIRST + 1;
	private static final int MENU_REMOVE_FAVORITES_ID = Menu.FIRST + 2;

	// interface to communicate click events with containers.

	public interface OnCryptCardSelectedListener {

		public void onCryptCardSelected(long cardID);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		getListView().setFastScrollEnabled(true);

		setEmptyText("No card match specified filter");

		if (mHighlight)
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		else
			getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);

		getListView().setBackgroundResource(R.color.Black);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.d("vampidroid", "cryptlistfragment.oncreate");

		if (savedInstanceState != null)
			mQuery = savedInstanceState.getString("query");

		try {
			mListener = (OnCryptCardSelectedListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().toString() + " must implement OnCryptCardSelectedListener");
		}

	}

	@Override
	public void setListAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setListAdapter(adapter);
		mAdapter = (SimpleCursorAdapter) adapter;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		outState.putString("query", mQuery);
	}

	public void setFilter(String filter) {

		// SQLiteDatabase db = DatabaseHelper.getDatabase();
		//
		// Cursor c = db.rawQuery(mQuery + filter + mOrderBy, null);
		//
		// mAdapter.changeCursor(c);

		mFilter = " " + filter + " " ;

	}

	@Override
	public void onAttach(FragmentActivity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	public void refreshList() {

		// Clear any selection.

		if (mHighlight) {

			getListView().setItemChecked(0, true);

		}

		SQLiteDatabase db = DatabaseHelper.getDatabase();
		
		System.out.println(mQuery + mFilter + mOrderBy);

		Cursor c = db.rawQuery(mQuery + mFilter + mOrderBy, null);

		mAdapter.changeCursor(c);

	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		mListener.onCryptCardSelected(id);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mAdapter != null)
			mAdapter.getCursor().close();
		mAdapter = null;
		setListAdapter(null);
	}

	public void setQuery(String cryptQuery) {

		mQuery = cryptQuery;

		// refreshList();

	}

	public void setOrderBy(String orderBy) {

		mOrderBy = " " + orderBy;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// Favorite cards may have been changed. Update views to reflect changed
		// favorite status.
		// getListView().invalidateViews();

		refreshList();
	}

	public void setHighlightChoice(boolean highlight) {
		// TODO Auto-generated method stub

		mHighlight = highlight;

	}

}