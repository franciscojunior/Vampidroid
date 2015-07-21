package name.vampidroid;

import name.vampidroid.fragments.LibraryDetailsFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;


public class LibraryDetails extends ActionBarActivity {

		
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
            LibraryDetailsFragment details = new LibraryDetailsFragment();
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