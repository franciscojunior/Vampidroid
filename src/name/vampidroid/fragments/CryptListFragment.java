package name.vampidroid.fragments;

import name.vampidroid.DatabaseHelper;
import name.vampidroid.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CryptListFragment extends ListFragment {

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	String mQuery;

	OnCryptCardSelectedListener mListener;
	
	boolean mHighlight;
	
	int mCurrentPosition = 0;
	
	// interface to communicate click events with containers.
	
	public interface OnCryptCardSelectedListener {
		
		public void onCryptCardSelected(long cardID);
		
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setFastScrollEnabled(true);

		if (mHighlight)
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		else
			getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
	}

	public void filterData(String filter) {
		
		
		
		SQLiteDatabase db = DatabaseHelper.getDatabase(getActivity()
				.getApplicationContext());
		
		Cursor c = db.rawQuery( mQuery + filter, null);
	
		mAdapter.changeCursor(c);
		
		
	}

	@Override
	public void onAttach(FragmentActivity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		SQLiteDatabase db = DatabaseHelper.getDatabase(getActivity()
				.getApplicationContext());
		
		mQuery = getArguments().getString("CryptQuery");
		
		
		Cursor c = db.rawQuery(mQuery, null);

		mAdapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(), R.layout.cryptlistitem, c,
				DatabaseHelper.STRING_ARRAY_NAME_DISCIPLINES_CAPACITY_INITIALCARDTEXT,
				new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
						R.id.txtCardCost, R.id.txtCardInitialText });

		setListAdapter(mAdapter);
		
		try {
            mListener = (OnCryptCardSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnCryptCardSelectedListener");
        }

        
	}
	
	
	private void refreshList() {

		// Clear any selection.
		
		if (mHighlight) {
			
			getListView().setItemChecked(0, true);

			
		}
			

		
		SQLiteDatabase db = DatabaseHelper.getDatabase(getActivity()
				.getApplicationContext());
		
		Cursor c = db.rawQuery( mQuery, null);
	
		mAdapter.changeCursor(c);
	
		
	}
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.
		// MenuItem item = menu.add("Search");
		// item.setIcon(android.R.drawable.ic_menu_search);
		// item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// SearchView sv = new SearchView(getActivity());
		// sv.setOnQueryTextListener(this);
		// item.setActionView(sv);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
			
		mListener.onCryptCardSelected(id);
		
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAdapter.getCursor().close();
		mAdapter = null;
		setListAdapter(null);
	}

	public void setQuery(String cryptQuery) {
		
		mQuery = cryptQuery;
		
		refreshList();
		
		
	}

	public void setHighlightChoice(boolean highlight) {
		// TODO Auto-generated method stub
		
		mHighlight = highlight;
		
	}
	

}