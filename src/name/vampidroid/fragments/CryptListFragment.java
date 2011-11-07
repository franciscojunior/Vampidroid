package name.vampidroid.fragments;

import name.vampidroid.DatabaseHelper;
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

	String mQuery;

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


//		registerForContextMenu(getListView());

		// getListView().setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		//
		//
		// if (event.getAction() == MotionEvent.ACTION_DOWN ) {
		// InputMethodManager imm = (InputMethodManager)
		// getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(
		// getListView().getWindowToken(), 0);
		//
		// System.out.println("touch");
		// }
		//
		// return false;
		// }
		// });

	}

	//
	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onCreate(savedInstanceState);
	//
	// if (savedInstanceState != null)
	// mQuery = savedInstanceState.getString("query");
	// else
	// mQuery = getArguments().getString("CryptQuery");
	//
	//
	//
	//
	// SQLiteDatabase db = DatabaseHelper.getDatabase();
	//
	// //mQuery = getArguments().getString("CryptQuery");
	//
	//
	// Cursor c = db.rawQuery(mQuery, null);
	//
	// // mAdapter = new SimpleCursorAdapter(
	// // getActivity().getApplicationContext(), R.layout.cryptlistitem, c,
	// // DatabaseHelper.STRING_ARRAY_NAME_DISCIPLINES_CAPACITY_INITIALCARDTEXT,
	// // new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
	// // R.id.txtCardCost, R.id.txtCardInitialText }) {
	// //
	// // @Override
	// // public View getView(int position, View convertView,
	// // ViewGroup parent) {
	// // // TODO Auto-generated method stub
	// //
	// // View reusableView = super.getView(position, convertView, parent);
	// //
	// // reusableView.setBackgroundResource(mAlternateListDrawables[position %
	// 2]);
	// //
	// // /*if (position % 2 == 0){
	// //
	// reusableView.setBackgroundResource(R.drawable.list_background_colorselector1);
	// // } else {
	// //
	// reusableView.setBackgroundResource(R.drawable.list_background_colorselector2);
	// // }*/
	// //
	// // return reusableView;
	// // }
	// //
	// //
	// //
	// // };
	//
	// // mAdapter = new SimpleCursorAdapter(
	// // getActivity().getApplicationContext(), R.layout.cryptlistitem, c,
	// // DatabaseHelper.STRING_ARRAY_NAME_DISCIPLINES_CAPACITY_INITIALCARDTEXT,
	// // new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
	// // R.id.txtCardCost, R.id.txtCardInitialText }) ;
	//
	//
	//
	//
	//
	//
	// // mAdapter = new SimpleCursorAdapter(
	// // getActivity().getApplicationContext(), R.layout.cryptlistitem, c,
	// // DatabaseHelper.STRING_ARRAY_CRYPT_LIST_COLUMNS,
	// // new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
	// // R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtCardGroup }) ;
	//
	//
	// mAdapter = new CardListCursorAdapter(CardType.CRYPT,
	// getActivity(), R.layout.cryptlistitem, c,
	// DatabaseHelper.STRING_ARRAY_CRYPT_LIST_COLUMNS,
	// new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
	// R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtCardGroup }) ;
	//
	// setListAdapter(mAdapter);
	//
	// try {
	// mListener = (OnCryptCardSelectedListener) getActivity();
	// } catch (ClassCastException e) {
	// throw new ClassCastException(getActivity().toString() +
	// " must implement OnCryptCardSelectedListener");
	// }
	//
	//
	//
	// }

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
			throw new ClassCastException(getActivity().toString()
					+ " must implement OnCryptCardSelectedListener");
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

	public void filterData(String filter) {

		if (filter.trim().length() > 0) {
			
			SQLiteDatabase db = DatabaseHelper.getDatabase();

			Cursor c = db.rawQuery(mQuery + filter, null);

			mAdapter.changeCursor(c);
		}

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

		Cursor c = db.rawQuery(mQuery, null);

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
	// menu.setHeaderTitle("Crypt card options");
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
	// DatabaseHelper.addCryptFavoriteCard(getActivity()
	// .getApplicationContext(), id);
	//
	// return true;
	//
	// case MENU_REMOVE_FAVORITES_ID:
	// id = getListAdapter().getItemId(info.position);
	//
	// DatabaseHelper.removeCryptFavoriteCard(getActivity()
	// .getApplicationContext(), id);
	//
	// return true;
	//
	// }
	//
	// return super.onContextItemSelected(item);
	// }

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

		refreshList();

	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// Favorite cards may have been changed. Update views to reflect changed
		// favorite status.
		getListView().invalidateViews();
	}

	public void setHighlightChoice(boolean highlight) {
		// TODO Auto-generated method stub

		mHighlight = highlight;

	}

	// public void setSelectableMode(boolean selectable) {
	//
	// if (selectable)
	// mAdapter = new CardListCursorAdapter(
	// getActivity().getApplicationContext(), R.layout.cryptlistitem,
	// mAdapter.getCursor(),
	// DatabaseHelper.STRING_ARRAY_CRYPT_LIST_COLUMNS,
	// new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
	// R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtCardGroup }) ;
	//
	//
	//
	// else
	// mAdapter = new SimpleCursorAdapter(
	// getActivity().getApplicationContext(), R.layout.cryptlistitem,
	// mAdapter.getCursor(),
	// DatabaseHelper.STRING_ARRAY_CRYPT_LIST_COLUMNS,
	// new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
	// R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtCardGroup }) ;
	//
	// setListAdapter(mAdapter);
	//
	//
	// }

}