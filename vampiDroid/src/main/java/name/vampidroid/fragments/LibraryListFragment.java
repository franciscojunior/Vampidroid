package name.vampidroid.fragments;

import name.vampidroid.DatabaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class LibraryListFragment extends SherlockListFragment {

	private static String TAG = "LibraryListFragment";

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	String mQuery = "";
	
	String mFilter = "";

	String mOrderBy = "";

	OnLibraryCardSelectedListener mListener;

	private boolean mHighlight;

	private static final int MENU_ADD_FAVORITES_ID = Menu.FIRST + 3;
	private static final int MENU_REMOVE_FAVORITES_ID = Menu.FIRST + 4;

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

		getListView().setBackgroundResource(android.R.color.black);

		registerForContextMenu(getListView());

	}

	// interface to communicate click events with containers.

	public interface OnLibraryCardSelectedListener {

		public void onLibraryCardSelected(long cardID);

	}

	public void setFilter(String filter) {
		
		

//		SQLiteDatabase db = DatabaseHelper.getDatabase();
//
//		Cursor c = db.rawQuery(mQuery + filter + mOrderBy, null);
//
//		mAdapter.changeCursor(c);
		
		mFilter = " " + filter + " ";

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

		if (mAdapter != null)
			mAdapter.getCursor().close();
		mAdapter = null;
		setListAdapter(null);

	}

	public void setQuery(String libraryQuery) {

		mQuery = libraryQuery;
		// refreshList();

	}

	public void setOrderBy(String orderBy) {

		mOrderBy = " " + orderBy;
	}

	public void refreshList() {

		SQLiteDatabase db = DatabaseHelper.getDatabase();

		Cursor c = db.rawQuery(mQuery + mFilter + mOrderBy, null);

		mAdapter.changeCursor(c);

	}

	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v,
	// ContextMenu.ContextMenuInfo menuInfo) {
	//
	// //MenuInflater inflater = getActivity().getMenuInflater();
	// //inflater.inflate(R.menu.crypt_list_context_menu, menu);
	//
	//
	// AdapterView.AdapterContextMenuInfo info =
	// (AdapterView.AdapterContextMenuInfo) menuInfo;
	//
	// long id = getListAdapter().getItemId(info.position);
	//
	// if
	// (DatabaseHelper.containsLibraryFavorite(getActivity().getApplicationContext(),
	// id))
	// menu.add(Menu.NONE, MENU_REMOVE_FAVORITES_ID, Menu.NONE,
	// R.string.remove_favorite_card);
	// else
	// menu.add(Menu.NONE, MENU_ADD_FAVORITES_ID, Menu.NONE,
	// R.string.add_favorite_card);
	//
	//
	//
	//
	// menu.setHeaderTitle("Library card options");
	//
	// }
	//
	//
	// @Override
	// public boolean onContextItemSelected(MenuItem item) {
	// AdapterView.AdapterContextMenuInfo info;
	//
	// info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	//
	// long id;
	// switch (item.getItemId()) {
	//
	// case MENU_ADD_FAVORITES_ID:
	//
	// id = getListAdapter().getItemId(info.position);
	//
	// DatabaseHelper.addLibraryFavoriteCard(getActivity()
	// .getApplicationContext(), id);
	//
	// return true;
	//
	// case MENU_REMOVE_FAVORITES_ID:
	// id = getListAdapter().getItemId(info.position);
	//
	// DatabaseHelper.removeLibraryFavoriteCard(getActivity()
	// .getApplicationContext(), id);
	//
	// return true;
	//
	// }
	//
	// return super.onContextItemSelected(item);
	// }

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
	public void setListAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setListAdapter(adapter);
		mAdapter = (SimpleCursorAdapter) adapter;

	}

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onCreate(savedInstanceState);
	//
	// if (savedInstanceState != null)
	// mQuery = savedInstanceState.getString("query");
	// else
	// mQuery = getArguments().getString("LibraryQuery");
	//
	// SQLiteDatabase db = DatabaseHelper.getDatabase();
	//
	// Cursor c = db.rawQuery(mQuery, null);
	//
	// // mAdapter = new SimpleCursorAdapter(getActivity()
	// // .getApplicationContext(), R.layout.librarylistitem, c,
	// // new String[] { "Name", "Type", "Clan", "Discipline" },
	// // new int[] { R.id.txtCardName, R.id.txtCardType,
	// // R.id.txtCardClan, R.id.txtCardDiscipline });
	//
	// mAdapter = new CardListCursorAdapter(CardType.LIBRARY, getActivity(),
	// R.layout.librarylistitem, c, new String[] { "Name", "Type",
	// "Clan", "Discipline" }, new int[] { R.id.txtCardName,
	// R.id.txtCardType, R.id.txtCardClan,
	// R.id.txtCardDiscipline });
	//
	// setListAdapter(mAdapter);
	//
	// try {
	// mListener = (OnLibraryCardSelectedListener) getActivity();
	// } catch (ClassCastException e) {
	// throw new ClassCastException(getActivity().toString()
	// + " must implement OnLibraryCardSelectedListener");
	// }
	//
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
			mQuery = savedInstanceState.getString("query");

		try {
			mListener = (OnLibraryCardSelectedListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().toString() + " must implement OnLibraryCardSelectedListener");
		}

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

}