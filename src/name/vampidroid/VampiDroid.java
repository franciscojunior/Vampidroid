package name.vampidroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

public class VampiDroid extends VampiDroidBase {

	protected String getLibraryQuery() {
		return ALL_FROM_LIBRARY_QUERY;
	}

	protected String getCryptQuery() {
		return ALL_FROM_CRYPT_QUERY;
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

		// remove this hack later.
		// make VampiDroidSearch a subclass of another base class instead of
		// VampiDroid activity.

		DATABASE.close();
		DATABASE = null;

		searchTextEdit.removeTextChangedListener(filterTextWatcher);

	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			VampiDroid.this.filterCryptText(s);
		}

	};

	public void filterCryptText(CharSequence s) {

		((SimpleCursorAdapter) listCrypt.getAdapter()).getFilter().filter(s);

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


	FilterQueryProvider filterCrypt = new FilterQueryProvider() {

		public Cursor runQuery(CharSequence constraint) {

			// Log.d(LOG_TAG, "runQuery constraint:"+constraint);
			// uri, projection, and sortOrder might be the same as previous
			// but you might want a new selection, based on your filter content
			// (constraint)
			// Cursor cur = managedQuery(uri, projection, selection,
			// selectionArgs, sortOrder);

			// Cursor c =
			// getDatabase(getApplicationContext()).rawQuery("select _id, Name, Disciplines, Capacity, substr(CardText, 1, 40) as InitialCardText from crypt where Name like '%"
			// + constraint + "%'", null);

			// Cursor c =
			// getDatabase(getApplicationContext()).rawQuery(VampiDroid.ALL_FROM_CRYPT_QUERY
			// + " where Name like '%" + constraint + "%'", null);

			/*
			 * String query = SQLiteQueryBuilder.buildQueryString(false,
			 * "crypt", VampiDroid.ALL_FROM_CRYPT_QUERY_AS_COLUMNS ,
			 * "Name like '%?%'", new String[] {constraint.toString()}, null,
			 * null, null);
			 */

			Cursor c = getDatabase(getApplicationContext()).query("crypt",
					VampiDroid.ALL_FROM_CRYPT_QUERY_AS_COLUMNS, "Name like ?",
					new String[] { "%" + constraint.toString() + "%" }, null,
					null, null);

			return c; // now your adapter will have the new filtered content

			/*
			 * Cursor c = getDatabase(getApplicationContext()).query("crypt",
			 * VampiDroid.ALL_FROM_CRYPT_QUERY_AS_COLUMNS , "Name like '%?%'",
			 * new String[] {constraint.toString()}, null, null, null);
			 */

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.i("vampidroid", "vampidroid.oncreate");
		
		checkAndShowChangeLog();        
        

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, "Search")
						.setIcon(android.R.drawable.ic_search_category_default);
		
		menu.add(Menu.NONE, Menu.FIRST+2, Menu.NONE, "Clear Recent Searches")
						.setIcon(android.R.drawable.ic_menu_recent_history);
		
		menu.add(Menu.NONE, Menu.FIRST+3, Menu.NONE, "Preferences")
						.setIcon(android.R.drawable.ic_menu_preferences);
		
		menu.add(Menu.NONE, Menu.FIRST+99, Menu.NONE, "About")
						.setIcon(android.R.drawable.ic_menu_info_details);
	
		return(super.onCreateOptionsMenu(menu));
	}

	void clearRecentSearches() {
		// TODO Auto-generated method stub
		
		// Ask user if he really want to clear recent searches. If so, clear them.
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	VampidroidSuggestionProvider.getBridge(VampiDroid.this).clearHistory();
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case Menu.FIRST+1:
				onSearchRequested(); 
				return true;
			
			case Menu.FIRST+2:
				clearRecentSearches();
				return true;
	
			case Menu.FIRST+3:
				startActivity(new Intent(this, EditPreferences.class)); 
				return true;
			
			case Menu.FIRST+99:
				
				showAbout();
				return true;
				
		}
	
		return(super.onOptionsItemSelected(item));
	}

}

/*
 * References:
 * http://stackoverflow.com/questions/1737009/how-to-make-a-nice-looking
 * -listview-filter-on-android
 */