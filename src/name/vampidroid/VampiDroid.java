package name.vampidroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


public class VampiDroid extends VampiDroidBase {

	public static final String KEY_CHANGELOG_VERSION_VIEWED = "change_log_viewed";
	public static final String KEY_TUTORIAL_BUTTONS_VIEWED = "tutorial_buttons_viewed";
	
	private boolean mIsShowingTutorial = false;
	
	
	protected String getLibraryQuery() {
		return DatabaseHelper.ALL_FROM_LIBRARY_QUERY;
	}

	protected String getCryptQuery() {
		return DatabaseHelper.ALL_FROM_CRYPT_QUERY;
	}

	
	

	void checkAndShowChangeLog() {
		// TODO Auto-generated method stub

		try {
			// current version
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;

			// version where changelog has been viewed
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(this);
			int viewedChangelogVersion = settings.getInt(
					KEY_CHANGELOG_VERSION_VIEWED, 0);

			if (viewedChangelogVersion < versionCode) {
				Editor editor = settings.edit();
				editor.putInt(KEY_CHANGELOG_VERSION_VIEWED, versionCode);
				editor.commit();

				showChangeLog();
			}
		} catch (NameNotFoundException e) {
			Log.w("Unable to get version code. Will not show changelog", e);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.i("vampidroid", "vampidroid.ondestroy");

		

		DatabaseHelper.closeDatabase();
		
	}

	public void showTutorialButtons(View v) {
		
		
		changeTutorialButtonsVisibility(View.VISIBLE);
		mIsShowingTutorial = true;
	
	}
	
	public void hideTutorialButtons(View v) {
		
		if (mIsShowingTutorial) {
			changeTutorialButtonsVisibility(View.GONE);
			mIsShowingTutorial = false;
		}
	}
	
	private void changeTutorialButtonsVisibility(int visibility) {
		
		View v = findViewById(R.id.tutorial_layout);
		
		if (v != null)
			v.setVisibility(visibility);
	}
	
	

	protected void showChangeLog() {
		// Displays changelog view...
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.changelog, null);

		new AlertDialog.Builder(this)
				.setTitle("Changelog")
				.setIcon(android.R.drawable.ic_menu_info_details)
				.setView(view)
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//
							}
						}).show();
	}

	private void showAbout() {
		// TODO Auto-generated method stub

		// Displays about view...
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.about, null);

		new AlertDialog.Builder(this)
				.setTitle("About")
				.setIcon(android.R.drawable.ic_menu_info_details)
				.setView(view)
				.setPositiveButton("Show Changelog",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								showChangeLog();
							}
						})
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//
							}
						}).show();

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.i("vampidroid", "vampidroid.oncreate");
		
		checkAndShowChangeLog();   
	
		checkAndShowTutorialButtons();
		
		
	}
	
	
	private void checkAndShowTutorialButtons() {
		// TODO Auto-generated method stub
		
		// version where changelog has been viewed
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean tutorialButtonsViewed = settings.getBoolean(
				KEY_TUTORIAL_BUTTONS_VIEWED, false);

		if (!tutorialButtonsViewed) {
			
			showTutorialButtons(null);
			Editor editor = settings.edit();
			editor.putBoolean(KEY_TUTORIAL_BUTTONS_VIEWED, true);
			editor.commit();
			
			
		}
		
		
			
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
        
	}

	void clearRecentSearches() {
		// TODO Auto-generated method stub
		
		// Ask user if he really want to clear recent searches. If so, clear them.
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	VampidroidSuggestionProvider.getBridge(VampiDroid.this.getApplicationContext()).clearHistory();
		            break;
	
		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};
	
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to clear your recent searches?")
			.setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		
		
	}
	
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		hideTutorialButtons(null);
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		hideTutorialButtons(null);
		
		switch (item.getItemId()) {
		
			case R.id.menu_search:
				onSearchRequested(); 
				return true;
			
			case R.id.menu_clear_recent_searches:
				clearRecentSearches();
				return true;
	
			case R.id.menu_preferences:
				startActivity(new Intent(this, EditPreferences.class)); 
				return true;
			
			case R.id.menu_about:
				showAbout();
				return true;
				
		}
	
		return(super.onOptionsItemSelected(item));
	}
	

}

