package name.vampidroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CardDetails extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Put your code here
		
		Intent i = getIntent();
		
		String path = i.getData().getPath();
		
		setContentView(R.layout.cryptcarddetails);
		
		
	}
}
