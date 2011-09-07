package name.vampidroid;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.util.Log;

public class VampiDroidSearch extends VampiDroidBase {
	
	private String mSearchQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		if (savedInstanceState != null)
			mSearchQuery = savedInstanceState.getString("searchquery");
		else
			mSearchQuery = getSearchQuery();
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		
		setTitle("Search Results for: " + mSearchQuery);
		
	}

	private String getSearchQuery() {
		String query = formatQueryString(getIntent().getStringExtra(
				SearchManager.QUERY)).trim().toLowerCase();

		VampidroidSuggestionProvider.getBridge(this.getApplicationContext()).saveRecentQuery(query,
				null);
		
		return query;
	}
	
	
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putString("searchquery", mSearchQuery);
	}

	@Override
	protected String getLibraryQuery() {

//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(this.getApplicationContext());


//		VampidroidSuggestionProvider.getBridge(this.getApplicationContext()).saveRecentQuery(query,
//				null);


		/*
		if (prefs.getBoolean("searchCardText", false))
			queryLibraryDatabase = DatabaseHelper.ALL_FROM_LIBRARY_QUERY + " and (lower(Name) like '%?%' or lower(CardText) like '%?%') ";
		
		else
			queryLibraryDatabase = DatabaseHelper.ALL_FROM_LIBRARY_QUERY + " and (Name like '%?%') ";

		*/
		
		
		// TODO Auto-generated method stub
		return DatabaseHelper.ALL_FROM_LIBRARY_QUERY + " and (lower(CardText) like '%?%') ".replace("?", mSearchQuery);

	}

	@Override
	protected String getCryptQuery() {
		

//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(this.getApplicationContext());
//
//		String query = getSearchQuery();
//
//		VampidroidSuggestionProvider.getBridge(this.getApplicationContext()).saveRecentQuery(query,
//				null);
//
//		String queryCryptDatabase;
//		

		
//		if (prefs.getBoolean("searchCardText", false))
//			queryCryptDatabase = DatabaseHelper.ALL_FROM_CRYPT_QUERY + " and (lower(Name) like '%?%' or lower(CardText) like '%?%') ";
//		
//		else
//			queryCryptDatabase = DatabaseHelper.ALL_FROM_CRYPT_QUERY + " and (Name like '%?%') ";
		
		

		// TODO Auto-generated method stub
		return DatabaseHelper.ALL_FROM_CRYPT_QUERY + " and (lower(CardText) like '%?%') ".replace("?", mSearchQuery);
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub

		Log.i("vampidroid", "VampiDroidSearch.onNewintent");
		super.onNewIntent(intent);

		// userSearchTerm = intent.getStringExtra(SearchManager.QUERY);
		setIntent(intent);
		

		mSearchQuery = getSearchQuery();
		
		setTitle("Search Results for: " + mSearchQuery);
		
		
		updateQueries();
		
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
        
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.menu_search:
			onSearchRequested(); 
			return true;
		

		case android.R.id.home:
			// app icon in Action Bar clicked; go home
			Intent intent = new Intent(this, VampiDroid.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		}

		return (super.onOptionsItemSelected(item));
	}

}