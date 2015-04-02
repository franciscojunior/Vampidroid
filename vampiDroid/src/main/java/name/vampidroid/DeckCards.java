package name.vampidroid;

import name.vampidroid.DatabaseHelper.CardType;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class DeckCards extends VampiDroidBase {

	private long mDeckId;
	private String mDeckName;
	private CardListCursorAdapter mCryptAdapter;
	private CardListCursorAdapter mLibraryAdapter;

	protected String getLibraryQuery() {
		return DatabaseHelper.ALL_FROM_LIBRARY_DECK_QUERY.replace("?", String.valueOf(mDeckId));
	}

	protected String getCryptQuery() {
		return DatabaseHelper.ALL_FROM_CRYPT_DECK_QUERY.replace("?", String.valueOf(mDeckId));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		Intent i = getIntent();

		mDeckId = i.getLongExtra("DeckId", 0);
		
		mDeckName = DatabaseHelper.getDeckName(mDeckId); 

		super.onCreate(savedInstanceState);

		Log.d("vampidroid", "deckcards.oncreate");
		
		setTitle("Deck: " + mDeckName);
		
		DatabaseHelper.LAST_SELECTED_DECK = mDeckId;
		
		mCryptAdapter = new CardListCursorAdapter(CardType.CRYPT, this, R.layout.deckcryptlistitem,
				null, DatabaseHelper.FROM_STRING_ARRAY_DECK_CRYPT_LIST_COLUMNS, new int[] { R.id.txtCardName,
						R.id.txtCardExtraInformation, R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtCardGroup,
						R.id.txtCardCount });
		
		mLibraryAdapter = new CardListCursorAdapter(CardType.LIBRARY, this, R.layout.decklibrarylistitem, null,
				DatabaseHelper.FROM_STRING_ARRAY_DECK_LIBRARY_LIST_COLUMNS, new int[] { R.id.txtCardName,
						R.id.txtCardType, R.id.txtCardClan, R.id.txtCardDiscipline, R.id.txtCardCount });



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getSupportMenuInflater().inflate(R.menu.deck_cards_menu, menu);

		return super.onCreateOptionsMenu(menu);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_share:
        case R.id.menu_share_action:
			shareDeckCards();
			break;

		}

		return super.onOptionsItemSelected(item);
	}


	private void shareDeckCards() {

		
		StringBuilder sb = new StringBuilder();
		
		SQLiteDatabase db = DatabaseHelper.getDatabase();
		
		
		// Deck Name
		
		sb.append(mDeckName).append("\n\n");
		
		
		// Crypt 
		Cursor c = db.rawQuery(DatabaseHelper.SELECT_DECK_CRYPT_FOR_SHARING, new String[] { String.valueOf(mDeckId) });
		
		
		if (c.moveToFirst()) {
			
			do {
				
				sb.append(c.getString(0)).append(" x ").append(c.getString(1)).append("\n");
				
			} while (c.moveToNext());
			
			
		}
		
		c.close();
		
		sb.append("\n");
		
		
		// Library
		c = db.rawQuery(DatabaseHelper.SELECT_DECK_LIBRARY_FOR_SHARING, new String[] { String.valueOf(mDeckId) });
		
		
		if (c.moveToFirst()) {
			
			do {
				
				sb.append(c.getString(0)).append(" x ").append(c.getString(1)).append("\n");
				
			} while (c.moveToNext());
			
			
		}
		
		c.close();
		
		
		
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mDeckName);
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());

		startActivity(Intent.createChooser(shareIntent, getResources()
				.getString(R.string.share_deck_cards_text)));
		
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.d("vampidroid", "deckcards.onPostCreate");
		
		super.onPostCreate(savedInstanceState);
		
		mVampidroidFragment.getCryptListFragment().setListAdapter(mCryptAdapter);
		mVampidroidFragment.getCryptListFragment().setQuery(getCryptQuery());
		mVampidroidFragment.getCryptListFragment().setOrderBy(DatabaseHelper.ORDER_BY_NAME);
		
		
		
		mVampidroidFragment.getLibraryListFragment().setListAdapter(mLibraryAdapter);
		mVampidroidFragment.getLibraryListFragment().setQuery(getLibraryQuery());
		mVampidroidFragment.getLibraryListFragment().setOrderBy(DatabaseHelper.ORDER_BY_NAME);

		
	}


}
