package name.vampidroid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

public class VampiDroid extends TabActivity {
	
	
	private static final String VAMPIDROID_DB = "VampiDroid.db";
	private static final String KEY_CHANGELOG_VERSION_VIEWED = "changelog_version_viewed";
	public static String DECK_NAME = "deck_name";
	public static String CARD_ID = "card_id";

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, "Search")
						.setIcon(android.R.drawable.ic_search_category_default);
		
		menu.add(Menu.NONE, Menu.FIRST+2, Menu.NONE, "Clear Recent Searches");
		
		menu.add(Menu.NONE, Menu.FIRST+3, Menu.NONE, "Preferences")
						.setIcon(android.R.drawable.ic_menu_preferences);

		menu.add(Menu.NONE, Menu.FIRST+99, Menu.NONE, "About")
						.setIcon(android.R.drawable.ic_menu_info_details);

		return(super.onCreateOptionsMenu(menu));
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
				
		}

		return(super.onOptionsItemSelected(item));
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
        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
              //
          }
        }).show();
		
	}


	private void clearRecentSearches() {
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


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        createDatabaseFile();
        
        
        Intent intent = getIntent();
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        showChangeLog();        
        
        String queryCryptDatabase;
        
        
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
			String query = formatQueryString(intent.getStringExtra(SearchManager.QUERY));
			
			VampidroidSuggestionProvider.getBridge(this).saveRecentQuery(query, null);
			
			if (prefs.getBoolean("searchCardText", false))
				queryCryptDatabase = "select _id, Name from crypt where Name like '%" + query + "%' or CardText like '%" + query + "%'";
			else
				queryCryptDatabase = "select _id, Name from crypt where Name like '%" + query + "%'";
		}
        else
        	queryCryptDatabase = "select _id, Name from crypt";
        
        
        // Fill crypt list.
        
        ListView listCrypt = (ListView) findViewById(R.id.ListViewCrypt);
        
        SQLiteDatabase db = getDatabase(this);
        Cursor c = db.rawQuery(queryCryptDatabase, null);
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listitem2, 
        		c, new String[] { "Name" }, new int[] {android.R.id.text1});
        
        
        listCrypt.setAdapter(adapter);
        
        
        
        listCrypt.setOnItemClickListener(new OnItemClickListener() {

        	public void onItemClick(AdapterView<?> parent, 
		            View v, int position, long id) {
				
				// TODO Auto-generated method stub
        		/*SQLiteDatabase db = getDatabase();
				Cursor c = db.rawQuery("select CardText from crypt where _id = " + String.valueOf(id), null );
				c.moveToFirst();
				String cardText = c.getString(0);
				c.close();
				db.close();
				*/
								
				/*AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				
				builder.setMessage(cardText);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(  DialogInterface dialog, int id) {
			                dialog.dismiss();
			           }
			       });
        
        
				AlertDialog alertDialog = builder.create(); 
				alertDialog.show();
				
				*/
        		
        		
				
				Intent cryptCardIntent = new Intent(v.getContext(), CryptDetails.class );
				
				cryptCardIntent.putExtra(DECK_NAME, "crypt");
				cryptCardIntent.putExtra(CARD_ID, id);
				
				startActivity(cryptCardIntent);
				
				
				
			}
        	
        	
		});
			
			
        
        
        //listCrypt.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, COUNTRIES));
        
        
        // Fill crypt list.
        
        String queryLibraryDatabase;
        
        
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
			String query = formatQueryString(intent.getStringExtra(SearchManager.QUERY));
			
			if (prefs.getBoolean("searchCardText", false))
				queryLibraryDatabase = "select _id, Name from library where Name like '%" + query + "%' or CardText like '%" + query + "%'";
			else
				queryLibraryDatabase = "select _id, Name from library where Name like '%" + query + "%'";
		}
        else
        	queryLibraryDatabase = "select _id, Name from library";

        
        ListView listLibrary = (ListView) findViewById(R.id.ListViewLibrary);
        
        
        c = db.rawQuery(queryLibraryDatabase, null);
        
        adapter = new SimpleCursorAdapter(this, R.layout.listitem2, 
        		c, new String[] { "Name" }, new int[] {android.R.id.text1});
        
        
        listLibrary.setAdapter(adapter);
        
        db.close();
        
        
        listLibrary.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, 
		            View v, int position, long id) {
				
				// TODO Auto-generated method stub
				/*SQLiteDatabase db = getDatabase();
				Cursor c = db.rawQuery("select CardText from library where _id = " + String.valueOf(id), null );
				c.moveToFirst();
				String cardText = c.getString(0);
				c.close();
				db.close();
				*/
				
				/*AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				
				builder.setMessage(cardText);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(  DialogInterface dialog, int id) {
			                dialog.dismiss();
			           }
			       });
        
        
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
				
				 */
				
				Intent libraryCardIntent = new Intent(v.getContext(), LibraryDetails.class );
				
				libraryCardIntent.putExtra(DECK_NAME, "library");
				libraryCardIntent.putExtra(CARD_ID, id);
				
				startActivity(libraryCardIntent);
								
			}
        	
        	
		});
        
        TabHost mTabHost = getTabHost();
        
        Resources res = getResources();
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_crypt").setIndicator("Crypt", res.getDrawable(R.drawable.ic_tab_crypt)).setContent(R.id.ListViewCrypt));
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_library").setIndicator("Library", res.getDrawable(R.drawable.ic_tab_library)).setContent(R.id.ListViewLibrary));
        
        //Show only the tab which has values or the first tab if there is no data at all.
        
        // Default tab is the first one.
        mTabHost.setCurrentTab(0);
        
        if (listCrypt.getAdapter().isEmpty() && !listLibrary.getAdapter().isEmpty() )
        	mTabHost.setCurrentTab(1);
        	
    }


	private void showChangeLog() {
		// TODO Auto-generated method stub
		
		try {
	        //current version
	        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
	        int versionCode = packageInfo.versionCode; 

	        //version where changelog has been viewed
	        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	        int viewedChangelogVersion = settings.getInt(KEY_CHANGELOG_VERSION_VIEWED, 0);

	        if(viewedChangelogVersion<versionCode) {
	            Editor editor=settings.edit();
	            editor.putInt(KEY_CHANGELOG_VERSION_VIEWED, versionCode);
	            editor.commit();
	            
	            // Displays changelog view...
	            LayoutInflater li = LayoutInflater.from(this);
	            View view = li.inflate(R.layout.changelog, null);

	            new AlertDialog.Builder(this)
	            .setTitle("Changelog")
	            .setIcon(android.R.drawable.ic_menu_info_details)
	            .setView(view)
	            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int whichButton) {
	                  //
	              }
	            }).show();
	        }
	    } catch (NameNotFoundException e) {
	        Log.w("Unable to get version code. Will not show changelog", e);
	    }
		
	}


	private void createDatabaseFile() {
		
		
		// The file will be stored in the private data area of the application.
		// Reference: http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
		File databasefile = getFileStreamPath(VAMPIDROID_DB);
		
		if (databasefile.exists())
		{
			databasefile = null;
			return;
		}
		
		AssetManager am = getAssets();		
		
		try {
			
			// Asset Manager doesn't work with files bigger than 1Mb at a time.
			// Check here for explanation: http://stackoverflow.com/questions/2860157/load-files-bigger-than-1m-from-assets-folder
			// Had to change the file suffix to .mp3 so it isn't compressed and can be opened directly.
						
			InputStream in = am.open("VampiDroid.mp3");
			
			OutputStream out = openFileOutput(VAMPIDROID_DB, Context.MODE_PRIVATE);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}

			in.close();
						
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
			
				
		
	}


	private String formatQueryString(String stringExtra) {
		// TODO Auto-generated method stub
		return stringExtra.trim().replace("'", "''");
	}


	static SQLiteDatabase getDatabase(Context context) {
		File databaseFile = context.getFileStreamPath(VAMPIDROID_DB);
		return SQLiteDatabase.openDatabase(databaseFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
}
