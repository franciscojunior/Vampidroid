package name.vampidroid.fragments;

import name.vampidroid.DatabaseHelper;
import name.vampidroid.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.Menu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LibraryListFragment extends ListFragment {

	private static String TAG = "LibraryListFragment";

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	String mQuery;

	OnLibraryCardSelectedListener mListener;

	private boolean mHighlight;
	
	private static final int MENU_ADD_FAVORITES_ID = Menu.FIRST+3;
	private static final int MENU_REMOVE_FAVORITES_ID = Menu.FIRST+4;
	
	

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
		
		//getListView().setBackgroundResource(R.color.Black);
		
		registerForContextMenu(getListView());
		
	}

	// interface to communicate click events with containers.

	public interface OnLibraryCardSelectedListener {

		public void onLibraryCardSelected(long cardID);

	}

	public void filterData(String filter) {

		SQLiteDatabase db = DatabaseHelper.getDatabase(getActivity()
				.getApplicationContext());

		Cursor c = db.rawQuery(mQuery + filter, null);

		mAdapter.changeCursor(c);

	}

	@Override
	public void onAttach(FragmentActivity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		Log.d(TAG, "onAttach");
		
		

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
		// Insert desired behavior here.

		mListener.onLibraryCardSelected(id);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAdapter.getCursor().close();
		mAdapter = null;
		setListAdapter(null);

	}

	public void setQuery(String libraryQuery) {

		mQuery = libraryQuery;
		refreshList();
		
	}

	private void refreshList() {
	
		SQLiteDatabase db = DatabaseHelper.getDatabase(getActivity()
				.getApplicationContext());
		
		Cursor c = db.rawQuery( mQuery, null);
	
		mAdapter.changeCursor(c);
	
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		
		//MenuInflater inflater = getActivity().getMenuInflater();
		//inflater.inflate(R.menu.crypt_list_context_menu, menu);
		
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			
		long id = getListAdapter().getItemId(info.position);
		
		if (DatabaseHelper.containsLibraryFavorite(getActivity().getApplicationContext(), id))
			menu.add(Menu.NONE, MENU_REMOVE_FAVORITES_ID, Menu.NONE, R.string.remove_favorite_card);
		else
			menu.add(Menu.NONE, MENU_ADD_FAVORITES_ID, Menu.NONE, R.string.add_favorite_card);
		
		
		
		
		menu.setHeaderTitle("Crypt card options");
		
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		long id;
		switch (item.getItemId()) {
		
			case MENU_ADD_FAVORITES_ID:
				
				id = getListAdapter().getItemId(info.position);
				
				DatabaseHelper.addLibraryFavoriteCard(getActivity()
						.getApplicationContext(), id);

				return true;
				
			case MENU_REMOVE_FAVORITES_ID:
				id = getListAdapter().getItemId(info.position);
				
				DatabaseHelper.removeLibraryFavoriteCard(getActivity()
						.getApplicationContext(), id);

				return true;

		}

		return super.onContextItemSelected(item);
	}

	

	public void setHighlightChoice(boolean highlight) {
		// TODO Auto-generated method stub
		
		mHighlight = highlight;
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putString("query", mQuery);
	}

	@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			if (savedInstanceState != null)
				mQuery = savedInstanceState.getString("query");
			else
				mQuery = getArguments().getString("LibraryQuery");
			

			SQLiteDatabase db = DatabaseHelper.getDatabase(getActivity()
					.getApplicationContext());

			Cursor c = db.rawQuery(mQuery, null);

			mAdapter = new SimpleCursorAdapter(getActivity()
					.getApplicationContext(), R.layout.librarylistitem, c,
					new String[] { "Name", "Type", "Clan", "Discipline" },
					new int[] { R.id.txtCardName, R.id.txtCardType,
							R.id.txtCardClan, R.id.txtCardDiscipline });

			setListAdapter(mAdapter);
			
			
			
			
			try {
				mListener = (OnLibraryCardSelectedListener) getActivity();
			} catch (ClassCastException e) {
				throw new ClassCastException(getActivity().toString()
						+ " must implement OnLibraryCardSelectedListener");
			}
	
			
			
		}
	

}