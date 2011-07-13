package name.vampidroid;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class VampiDroidSearch extends VampiDroidBase {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Cache list objects here as we are reusing them inside this activity.
		
		listCrypt = (ListView) findViewById(R.id.ListViewCrypt);
		listLibrary = (ListView) findViewById(R.id.ListViewLibrary);
		
		
	}

	
	
	
	
	@Override
	protected String getLibraryQuery() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
		String query = formatQueryString(getIntent().getStringExtra(SearchManager.QUERY));
			
		VampidroidSuggestionProvider.getBridge(this).saveRecentQuery(query, null);
		
		
		String queryLibraryDatabase;
		
		if (prefs.getBoolean("searchCardText", false))
			queryLibraryDatabase = "select _id, Name from library where Name like '%" + query + "%' or CardText like '%" + query + "%'";
		else
			queryLibraryDatabase = "select _id, Name from library where Name like '%" + query + "%'";
		
		
        
		// TODO Auto-generated method stub
		return queryLibraryDatabase;
		
	}

	@Override
	protected String getCryptQuery() {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
		String query = formatQueryString(getIntent().getStringExtra(SearchManager.QUERY));
			
		VampidroidSuggestionProvider.getBridge(this).saveRecentQuery(query, null);
		
		String queryCryptDatabase;
		
		if (prefs.getBoolean("searchCardText", false))
			queryCryptDatabase = "select _id, Name, Disciplines, Capacity, substr(CardText, 1, 40) as InitialCardText, Adv from crypt where Name like '%" + query + "%' or CardText like '%" + query + "%'";
		else
			queryCryptDatabase = "select _id, Name, Disciplines, Capacity, substr(CardText, 1, 40) as InitialCardText, Adv from crypt where Name like '%" + query + "%'";
		
		
        
		// TODO Auto-generated method stub
		return queryCryptDatabase;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		Log.i("vampidroid", "VampiDroidSearch.onNewintent");
		super.onNewIntent(intent);
		
		//userSearchTerm = intent.getStringExtra(SearchManager.QUERY);
		setIntent(intent);
		
		/*fillListFromDatabaseQuery(getCryptQuery(), listCrypt);
		fillListFromDatabaseQuery(getLibraryQuery(), listLibrary);*/
		
		closeAdapterCursors();
		
		fillListsFromDatabaseQuery(getCryptQuery(), getLibraryQuery(), listCrypt, listLibrary);
		
	}





	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("vampidroid", "VampiDroidSearch.onStop");
	}





	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("vampidroid", "VampiDroidSearch.onDestroy");
		
		closeAdapterCursors();
	}





	protected void closeAdapterCursors() {
		
		SimpleCursorAdapter adapter = (SimpleCursorAdapter) listCrypt.getAdapter();
		
		adapter.getCursor().close();
		
		adapter = (SimpleCursorAdapter) listLibrary.getAdapter();
		
		adapter.getCursor().close();
	}
	
	
}