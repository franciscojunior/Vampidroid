package name.vampidroid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

public abstract class VampiDroidBase extends TabActivity {

	protected static final String VAMPIDROID_DB = "VampiDroid.db";
	protected static final String KEY_CHANGELOG_VERSION_VIEWED = "change_log_viewed";
	protected static final String KEY_DATABASE_VERSION = "database_version";
	protected static final int DATABASE_VERSION = 2;
	static final String[] STRING_ARRAY_NAME_DISCIPLINES_CAPACITY_INITIALCARDTEXT = new String[] { "Name", "Disciplines", "Capacity", "InitialCardText" };
	static final String[] STRING_ARRAY_NAME_DISCIPLINES_CAPACITY_INITIALCARDTEXT_ADV = new String[] { "Name", "Disciplines", "Capacity", "InitialCardText","Adv" };
	
	public static final String ALL_FROM_CRYPT_QUERY = "select _id, Name, Disciplines, Capacity, substr(CardText, 1, 40) as InitialCardText, Adv from crypt";
	
	
	public static final String[] ALL_FROM_CRYPT_QUERY_AS_COLUMNS = new String[] {
				"_id", "Name", "Disciplines", "Capacity",
				"substr(CardText, 1, 40) as InitialCardText" };
	public static final String ALL_FROM_LIBRARY_QUERY = "select _id, Name from library";
	private static final String[] STRING_ARRAY_NAME_DISCIPLINES_CAPACITY = new String[] {
				"Name", "Disciplines", "Capacity" };
	
	public static SQLiteDatabase DATABASE = null;
	public static String CARD_ID = "card_id";
	public static String DECK_NAME = "deck_name";
	
	
	protected static SQLiteDatabase getDatabase(Context context) {
		/*File databaseFile = context.getFileStreamPath(VAMPIDROID_DB);
		return SQLiteDatabase.openDatabase(databaseFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);*/
		
		if (DATABASE == null) {
			checkAndCreateDatabaseFile(context);
			DATABASE = SQLiteDatabase.openDatabase(context.getFileStreamPath(VAMPIDROID_DB).getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE); 
			
		}
			
		return DATABASE;
	}

	protected ListView listCrypt;
	protected ListView listLibrary;
	protected EditText searchTextEdit = null;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.main);
	    setContentView(R.layout.main);
	    
	 
	    searchTextEdit = (EditText) findViewById(R.id.search_box);
	    
	    
	    searchTextEdit.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View view, MotionEvent motionEvent) {
				// TODO Auto-generated method stub
				switch(motionEvent.getAction()){            
	            
					case MotionEvent.ACTION_CANCEL:             
		            case MotionEvent.ACTION_UP:
		            	onSearchRequested();
		                break;
				}
	    		
				return false;
			}
			
		});
	    
	    // Remove long click handling.
	    searchTextEdit.setOnLongClickListener(new OnLongClickListener() {
			
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	    
	    
	        
	    listCrypt = (ListView) findViewById(R.id.ListViewCrypt);
	    listCrypt.setTextFilterEnabled(false);
	
	    
	    ListView listLibrary = (ListView) findViewById(R.id.ListViewLibrary);
	    
	    
	    fillListsFromDatabaseQuery(getCryptQuery(), getLibraryQuery(), listCrypt, listLibrary);
	    
	    
	    listCrypt.setOnItemClickListener(new OnItemClickListener() {
	
	    	public void onItemClick(AdapterView<?> parent, 
		            View v, int position, long id) {
		
	    		Intent cryptCardIntent = new Intent(v.getContext(), CryptDetails.class );
				
				cryptCardIntent.putExtra(CARD_ID, id);
				
				cryptCardIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				
				startActivity(cryptCardIntent);
				
				
				
				
			}
	    	
	    	
		});
		
	    
	    listLibrary.setOnItemClickListener(new OnItemClickListener() {
	
			public void onItemClick(AdapterView<?> parent, 
		            View v, int position, long id) {
						
				Intent libraryCardIntent = new Intent(v.getContext(), LibraryDetails.class );
				
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

	protected abstract String getCryptQuery();
	

	protected abstract String getLibraryQuery();

	protected void fillListsFromDatabaseQuery(String databaseQueryCrypt, String databaseQueryLibrary,
			ListView listCrypt, ListView listLibrary) {
						
						
								
							    
			    SQLiteDatabase db = getDatabase(getApplicationContext());
			    
			    
			    // Fill crypt list
			    Cursor c = db.rawQuery(databaseQueryCrypt, null);
			    
			    /*SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listitem2, 
			    		c, new String[] { "Name" }, new int[] {android.R.id.text1});
			    */
			    
			    
			    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.cryptlistitem, 
			    		c, STRING_ARRAY_NAME_DISCIPLINES_CAPACITY_INITIALCARDTEXT_ADV, new int[] {R.id.txtCardName, R.id.txtCardExtraInformation, R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtAdvanced});
			    
			    //adapter.setFilterQueryProvider(filterCrypt);
			    listCrypt.setAdapter(adapter);
			    
			    
			    
			    
			    
			   /* MySectionedAdapter sectionedAdapter = new MySectionedAdapter(getApplicationContext());
			    
			    
			    c = db.query("crypt", new String[] {"_id", "Name", "Disciplines", "Capacity"}, 
			    		"Name like 'A%'", null, null, null, "Name");
			    
			    
			    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listitem3, 
			    		c, new String[] { "Name", "Disciplines", "Capacity" }, new int[] {R.id.txtCardName, R.id.txtCardExtraInformation, R.id.txtCardCost});
			    
			    sectionedAdapter.addSection("A", adapter);
			    
			    
			    c = db.query("crypt", new String[] {"_id", "Name", "Disciplines", "Capacity"}, 
			    		"Name like 'B%'", null, null, null, "Name");
			    
			    
			    adapter = new SimpleCursorAdapter(this, R.layout.listitem3, 
			    		c, new String[] { "Name", "Disciplines", "Capacity" }, new int[] {R.id.txtCardName, R.id.txtCardExtraInformation, R.id.txtCardCost});
			    
			    
			    sectionedAdapter.addSection("B", adapter);
			    
			    listCrypt.setAdapter(sectionedAdapter);
			    
			    */
			    
			    
			    // Fill library list
			    c = db.rawQuery(databaseQueryLibrary, null);
			    
			    adapter = new SimpleCursorAdapter(this, R.layout.listitem2, 
			    		c, new String[] { "Name" }, new int[] {android.R.id.text1});
			    
			    
			    listLibrary.setAdapter(adapter);
			    
			    
			    
			}

	protected String formatQueryString(String stringExtra) {
		// TODO Auto-generated method stub
		return stringExtra.trim().replace("'", "''");
	}

	

	private static void checkAndCreateDatabaseFile(Context context) {
		
		// The file will be stored in the private data area of the application.
		// Reference: http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
		File databaseFile = context.getFileStreamPath(VAMPIDROID_DB);
		
		//version where changelog has been viewed
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
	    int databaseVersion = settings.getInt(KEY_DATABASE_VERSION, 1);
	
	    if(databaseVersion<DATABASE_VERSION || !databaseFile.exists()) {
	    	createDatabaseFile(context);
	    	
	    	Editor editor=settings.edit();
	        editor.putInt(KEY_DATABASE_VERSION, DATABASE_VERSION);
	        editor.commit();
	        
	    }
	    
	    
	}

	 private static void createDatabaseFile(Context context) {
		
		
		
		AssetManager am = context.getAssets();		
		
		try {
			
			// Asset Manager doesn't work with files bigger than 1Mb at a time.
			// Check here for explanation: http://stackoverflow.com/questions/2860157/load-files-bigger-than-1m-from-assets-folder
			// Had to change the file suffix to .mp3 so it isn't compressed and can be opened directly.
						
			InputStream in = am.open("VampiDroid.mp3");
			
			OutputStream out = context.openFileOutput(VAMPIDROID_DB, Context.MODE_PRIVATE);
	
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

		

		

}
