package name.vampidroid;

import name.vampidroid.DatabaseHelper.CardType;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This class is responsible to enable deck card edition. User will add/remove
 * cards to/from a deck through this activity.
 * 
 */
public class AddRemoveDeckCard extends FragmentActivity {

	private SimpleCursorAdapter mAdapter;
	private String mCardName;
	private long mCardId;
	private CardType mCardType;

	private Spinner mDecks;

	private EditText mCardCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_remove_deck_card);

		Intent intent = getIntent();

		mCardId = intent.getLongExtra("CardId", 0);
		mCardType = (CardType) intent.getSerializableExtra("CardType");

		mCardName = DatabaseHelper.getCardName(mCardId, mCardType);
		//mCardName = "DummyCardName";
		

		mDecks = (Spinner) findViewById(R.id.spinnerDeckName);

		TextView cardName = (TextView) findViewById(R.id.textCardName);

		mCardCount = (EditText) findViewById(R.id.editCardNumber);

		Cursor c = DatabaseHelper.getDecks();

		mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, c,
				new String[] { "Name" }, new int[] { android.R.id.text1 });

		mDecks.setAdapter(mAdapter);

		setLastDeckSpinnerPosition();

		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		cardName.setText(mCardName);

		mDecks.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				int cardsInDeck = DatabaseHelper.getCountCardsInDeck(id,
						mCardId, mCardType);

				mCardCount.setText(String.valueOf(cardsInDeck));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		mCardCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCardCount.selectAll();

			}
		});

	}

	private void setLastDeckSpinnerPosition() {
		// TODO Auto-generated method stub

		for (int i = 0; i < mDecks.getCount(); i++) {
			long itemIdAtPosition2 = mDecks.getItemIdAtPosition(i);
			if (itemIdAtPosition2 == DatabaseHelper.LAST_SELECTED_DECK) {
				mDecks.setSelection(i);
				break;
			}
		}
	}

	public void OnButtonOkClick(View v) {

		
		long deckId = mDecks.getSelectedItemId();

		String cardCount = mCardCount.getText().toString();
		
		if (cardCount.length() > 0) {
			DatabaseHelper.addDeckCard(deckId, mCardId, mCardType,
					Integer.parseInt(cardCount));
			
			DatabaseHelper.LAST_SELECTED_DECK = deckId;
		}

		finish();

	}

	public void OnButtonCancelClick(View v) {

		finish();
	}

}
