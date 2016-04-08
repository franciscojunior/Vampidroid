package name.vampidroid;

import java.util.Arrays;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class SplashScreen extends FragmentActivity {

    private static final String TAG = "SplashScreen";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.splashscreen);

		new UpdateDatabaseOperation().execute();

		
//
//		new Thread() {
//
//
//			@Override
//			public void run() {
//
//				// Initialize the database...
//				DatabaseHelper.getDatabase();
//
//
//				startActivity(new Intent(SplashScreen.this, VampiDroid.class));
//
//				SplashScreen.this.finish();
//
//			}
//
//
//		}.start();
//
//
		
		
	}


	private class UpdateDatabaseOperation extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				DatabaseHelper.getDatabase();
			} catch (Exception e) {
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			Log.d(TAG, "onPostExecute... ");

			if (result) {



				startActivity(new Intent(SplashScreen.this, VampiDroid.class));

				SplashScreen.this.finish();
			}
			else {

			}


		}
	}
	
}
