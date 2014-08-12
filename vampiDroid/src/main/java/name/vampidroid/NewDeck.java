package name.vampidroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

public class NewDeck extends FragmentActivity {

	
	public static String DECK_NAME = "DECK_NAME";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_deck);
		
	}
	
	
	
	public void OnButtonOkClick(View v) {
		
		String deckName = ((EditText)findViewById(R.id.editDeckName)).getText().toString().trim();
		
		Intent result = new Intent();
		result.putExtra(DECK_NAME, deckName);
		
		setResult(RESULT_OK, result);
		
		finish();
		
	}
	
	public void OnButtonCancelClick(View v) {
		
		setResult(RESULT_CANCELED);
		finish();
	}
	
	

}
