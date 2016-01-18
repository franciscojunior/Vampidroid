package name.vampidroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;


public class DecksList extends AppCompatActivity {

	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.deckslist);
		

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
	
	
	

	
	
	private void showAddNewDeck() {
		// TODO Auto-generated method stub

		
		// Displays about view...
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.new_deck, null);

		new AlertDialog.Builder(this)
				// .setTitle("New Deck")
				// .setIcon(android.R.drawable.ic_menu_info_details)
				.setView(view)
				.setPositiveButton("Add deck",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//
							}
						}).show();

	}

}
