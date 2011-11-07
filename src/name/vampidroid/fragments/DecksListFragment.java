package name.vampidroid.fragments;

import name.vampidroid.DatabaseHelper;
import name.vampidroid.DeckCards;
import name.vampidroid.NewDeck;
import name.vampidroid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DecksListFragment extends ListFragment {

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	private static int CREATE_DECK_REQUEST_CODE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Cursor c = DatabaseHelper.getDecksWithCardsCount();

		mAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.deckslistitem, c,
				new String[] { "Name", "CryptCardsCount", "LibraryCardsCount", "CryptCapacityAvg", "CryptCapacityMin", "CryptCapacityMax"  }, new int[] { R.id.txtCardName,
						R.id.txtCryptCardsCount, R.id.txtLibraryCardsCount, R.id.txtCryptCapacityAvg, R.id.txtCryptCapacityMin, R.id.txtCryptCapacityMax  });

		setListAdapter(mAdapter);

		setHasOptionsMenu(true);
		

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		getListView().setFastScrollEnabled(true);

		setEmptyText("No decks found. Add one from menu");

		getListView().setBackgroundResource(R.color.Black);
		
		registerForContextMenu(getListView());


	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.decks_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.menu_add_new_deck:
			// showAddNewDeck();
			startActivityForResult(new Intent(this.getActivity(), NewDeck.class), CREATE_DECK_REQUEST_CODE);
			return true;

		}

		return (super.onOptionsItemSelected(item));

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == CREATE_DECK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

			DatabaseHelper.createDeck(data.getStringExtra(NewDeck.DECK_NAME));

			updateDecksList();

		}

	}

	private void updateDecksList() {
		mAdapter.changeCursor(DatabaseHelper.getDecksWithCardsCount());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// Data may have been modified. So, refresh it.

		updateDecksList();

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Intent i = new Intent();

		i.putExtra("DeckId", id);

		i.setClass(getActivity(), DeckCards.class);

		startActivity(i);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.decks_list_context_menu, menu);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		long id = getListAdapter().getItemId(info.position);

		menu.setHeaderTitle("Deck options");

	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info;

		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		
		switch (item.getItemId()) {

		case R.id.context_menu_remove_deck:
			

			// ClickListener to check if User really wants to delete deck.
			
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            //Yes button clicked
			        	long deckId = getListAdapter().getItemId(info.position);
			        	DatabaseHelper.removeDeck(deckId);
			        	updateDecksList();
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			            break;
			        }
			    }
			};

			
			AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
			builder.setMessage("Are you sure you want to delete this deck?")
				.setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();

			return true;
		}

		return super.onContextItemSelected(item);
	}
	
	
	
	
	

}
