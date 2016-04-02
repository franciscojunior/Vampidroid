package name.vampidroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TextView copyright = new TextView(this);
		copyright.setText("Jyhad, Vampire: The Eternal Struggle and all game symbols are trademarks of Wizards of the Coast, Inc. and CCP hf. White Wolf, World of Darkness and all related terms are trademarks of CCP hf. " +
				"This product is not published nor endorsed by CCP hf.");
		
		copyright.setGravity(Gravity.CENTER);
		setContentView(copyright);
	}

}
