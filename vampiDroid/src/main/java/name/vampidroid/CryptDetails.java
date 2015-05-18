package name.vampidroid;

import name.vampidroid.fragments.CryptDetailsFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;


public class CryptDetails extends ActionBarActivity {
	
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Put your code here
		
		Intent i = getIntent();
		
		String deck = i.getStringExtra(VampiDroidBase.DECK_NAME);
		long card_id = i.getLongExtra(VampiDroidBase.CARD_ID, 0);
		
		String query;
		
		query = QUERY_CRYPT;
		
		
		setContentView(R.layout.cryptcarddetails);
		
		SQLiteDatabase db = VampiDroidBase.getDatabase(getApplicationContext());
		Cursor c = db.rawQuery(query + String.valueOf(card_id), null );
		c.moveToFirst();
		
		String cardName = c.getString(0);
		String cardType = c.getString(1);
		String cardClan = c.getString(2);
		String cardDisciplines = c.getString(3);
		String cardText = c.getString(4);
		String cardCapacity = c.getString(5);
		String cardArtist = c.getString(6);
		String cardSetRarity = c.getString(7);
		String cardGroup = c.getString(8);
		
		c.close();
		
		
		
		TextView txt = (TextView) findViewById(R.id.txtCardName);
		txt.setText(cardName);
		
		txt = (TextView) findViewById(R.id.txtCardCapacity);
		txt.setText(cardCapacity);

		txt = (TextView) findViewById(R.id.txtCardType);
		txt.setText(cardType);
		

		txt = (TextView) findViewById(R.id.txtCardClan);
		txt.setText(cardClan);
		

		txt = (TextView) findViewById(R.id.txtCardDisciplines);
		txt.setText(cardDisciplines);
		

		txt = (TextView) findViewById(R.id.txtCardArtist);
		txt.setText(cardArtist);

		txt = (TextView) findViewById(R.id.txtCardText);
		txt.setText(cardText);
		
		txt = (TextView) findViewById(R.id.txtSetRarity);
		txt.setText(cardSetRarity);
		
		txt = (TextView) findViewById(R.id.txtCardGroup);
		txt.setText(cardGroup);
	}*/
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
        	getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_HIGH) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            CryptDetailsFragment details = new CryptDetailsFragment();
            details.setArguments(getIntent().getExtras());
            
            getSupportFragmentManager()
            	.beginTransaction()
            	.add(android.R.id.content, details)
            	.commit();
        }
        
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, VampiDroid.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
}
