package name.vampidroid;

import java.util.Arrays;

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
				
				// Initialize the database...
				DatabaseHelper.getDatabase();
				
				
				startActivity(new Intent(SplashScreen.this, VampiDroid.class));
				
				SplashScreen.this.finish();
				
			}
			
			
		}.start();
		
		
		
		
	}

	
	
	
}
