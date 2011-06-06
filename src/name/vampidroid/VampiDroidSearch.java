package name.vampidroid;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;


public class VampiDroidSearch extends VampiDroid {


	String userSearchTerm;
	
	ListView listCrypt;
	ListView listLibrary;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		userSearchTerm = getIntent().getStringExtra(SearchManager.QUERY);
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Cache list objects here as we are reusing them inside this activity.
		
		listCrypt = (ListView) findViewById(R.id.ListViewCrypt);
		listLibrary = (ListView) findViewById(R.id.ListViewLibrary);
		
		
	}

	
	
	
	
	@Override
	protected String getLibraryQuery() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
		String query = formatQueryString(userSearchTerm);
			
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
        
		String query = formatQueryString(userSearchTerm);
			
		VampidroidSuggestionProvider.getBridge(this).saveRecentQuery(query, null);
		
		String queryCryptDatabase;
		
		if (prefs.getBoolean("searchCardText", false))
			queryCryptDatabase = "select _id, Name from crypt where Name like '%" + query + "%' or CardText like '%" + query + "%'";
		else
			queryCryptDatabase = "select _id, Name from crypt where Name like '%" + query + "%'";
		
		
        
		// TODO Auto-generated method stub
		return queryCryptDatabase;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		userSearchTerm = intent.getStringExtra(SearchManager.QUERY);
		
		listCrypt.clearChoices();
		listLibrary.clearChoices();
		
		/*fillListFromDatabaseQuery(getCryptQuery(), listCrypt);
		fillListFromDatabaseQuery(getLibraryQuery(), listLibrary);*/
		
		fillListsFromDatabaseQuery(getCryptQuery(), getLibraryQuery(), listCrypt, listLibrary);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
}
