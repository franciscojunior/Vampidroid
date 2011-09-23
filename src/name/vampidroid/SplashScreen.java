package name.vampidroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SplashScreen extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.splashscreen);
		
		
		new Thread() {


			@Override
			public void run() {
				
				Thread.yield();
				
				// Initialize the database...
				DatabaseHelper.getDatabase(getApplicationContext());
				
				startActivity(new Intent(SplashScreen.this, VampiDroid.class));
				SplashScreen.this.finish();
			}
			
			
		}.start();
		
		
		
		
	}

	
	
	
}
