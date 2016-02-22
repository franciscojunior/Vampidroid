package name.vampidroid;

import name.vampidroid.DatabaseHelper.CardType;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class VampiDroid extends VampiDroidBase {

	public static final String KEY_CHANGELOG_VERSION_VIEWED = "change_log_viewed";
	public static final String KEY_TUTORIAL_BUTTONS_VIEWED = "tutorial_buttons_viewed_int";
	
	public static final int TUTORIAL_VERSION = 2;
	
	private boolean mIsShowingTutorial = false;
	
	private CardListCursorAdapter mCryptAdapter;
	private CardListCursorAdapter mLibraryAdapter;
	
	
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

		Log.d("vampidroid", "vampidroid.ondestroy");

		

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
				.setTitle("Changelog - v" + getVersionName())
				.setIcon(android.R.drawable.ic_menu_info_details)
				.setView(view)
				.setNegativeButton("Close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								
								checkAndShowTutorialButtons();
								
							}
						}).show();
	}

	private String getVersionName() {
		String versionName = "";
		
		
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			versionName = packageInfo.versionName;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			
		}
		return versionName;
	}

	private void showAbout() {
		// TODO Auto-generated method stub

		// Displays about view...
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.about, null);

		new AlertDialog.Builder(this)
				.setTitle("About VampiDroid - v" + getVersionName())
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
	
	private void showAdvancedSearchHelp() {
		// TODO Auto-generated method stub

		ScrollView sv = new ScrollView(this);
		TextView view = new TextView(this);
		sv.addView(view);
		view.setText("     Advanced filtering uses the filters specified in the adequate text box, separated with commas \",\". For instance, writing \"for, Brujah\" in the \"Crypt filters\" will return " +
				"only Brujah vampires with inferior Fortitude. The same applies to Library filtering.\n\n" +
				"     Disciplines: use the first three letters of the discipline (and \"thn\" for Thanatosis). Lower case means inferior level only (eg. \"aus\"), " +
                "Upper case means superior level only (eg. \"AUS\"). To filter vampires by a discipline regardless of its level, append \"+\" to the discipline (eg. \"aus+\" will return vampires with inferior or superior Auspex).\n\n" +
                "     Grouping: use the \"g\" filter. For instance, \"g23\" will return only vampires from groups 2 and 3.\n\n" +
                "     Capacity: use a condition such as \">5\" to return vampires with capacity 6 or more, \"<=10\" for capacity 10 or less, or \"=1\" for 1-capacity vampires.");

		new AlertDialog.Builder(this)
				.setTitle("Filters Help")
				.setIcon(android.R.drawable.ic_menu_info_details)
				.setView(sv)
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
		Log.d("vampidroid", "vampidroid.oncreate");
		
		super.onCreate(savedInstanceState);
		
		
		mCryptAdapter = new CardListCursorAdapter(CardType.CRYPT,
				this, R.layout.cryptlistitem, null,
				DatabaseHelper.STRING_ARRAY_CRYPT_LIST_COLUMNS,
				new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
						R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtCardGroup });

		
		mLibraryAdapter = new CardListCursorAdapter(CardType.LIBRARY, this,
				R.layout.librarylistitem, null, new String[] { "Name", "Type",
						"Clan", "Discipline" }, new int[] { R.id.txtCardName,
						R.id.txtCardType, R.id.txtCardClan,
						R.id.txtCardDiscipline });

		
		
		checkAndShowChangeLog();
		
		
	
	}
	

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.d("vampidroid", "vampidroid.onPostCreate");
		
		super.onPostCreate(savedInstanceState);
		
		mVampidroidFragment.getCryptListFragment().setListAdapter(mCryptAdapter);
		mVampidroidFragment.getCryptListFragment().setQuery(getCryptQuery());
		mVampidroidFragment.getCryptListFragment().setOrderBy(DatabaseHelper.ORDER_BY_NAME);
		
		
		mVampidroidFragment.getLibraryListFragment().setListAdapter(mLibraryAdapter);
		mVampidroidFragment.getLibraryListFragment().setQuery(getLibraryQuery());
		mVampidroidFragment.getLibraryListFragment().setOrderBy(DatabaseHelper.ORDER_BY_NAME);

		
	}

	private void checkAndShowTutorialButtons() {
		// TODO Auto-generated method stub
		

		// Check if the tutorial has been viewed.
		
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		int tutorialButtonsViewed = settings.getInt(KEY_TUTORIAL_BUTTONS_VIEWED, 0);

		if (tutorialButtonsViewed < TUTORIAL_VERSION) {
			
			startActivity(new Intent(this, Tutorial.class));
			
			Editor editor = settings.edit();
			editor.putInt(KEY_TUTORIAL_BUTTONS_VIEWED, TUTORIAL_VERSION);
			editor.commit();
			
			
		}
		
		
			
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);
		
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
	public boolean onOptionsItemSelected(MenuItem item) {
		
		hideTutorialButtons(null);
		
		switch (item.getItemId()) {
		
			case R.id.menu_search:
				onSearchRequested(); 
				return true;
			
			case R.id.menu_clear_recent_searches:
				clearRecentSearches();
				return true;
	
//			case R.id.menu_preferences:
//				startActivity(new Intent(this, EditPreferences.class)); 
//				return true;
//			
			case R.id.menu_help:
				showAdvancedSearchHelp();
				return true;
				
			case R.id.menu_about:
				showAbout();
				return true;
				
			case R.id.menu_show_favorite_cards:
				startActivity(new Intent(this, FavoriteCards.class));
				return true;
				
			case R.id.menu_show_decks:
				startActivity(new Intent(this, DecksList.class));
				return true;
				
		}
	
		return(super.onOptionsItemSelected(item));
	}
	

}

