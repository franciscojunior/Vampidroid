package name.vampidroid;

import android.os.Bundle;
import android.util.Log;


public class FavoriteCards extends VampiDroidBase {

	
	
	
	protected String getLibraryQuery() {
		return DatabaseHelper.ALL_FROM_LIBRARY_FAVORITES_QUERY;
	}

	protected String getCryptQuery() {
		return DatabaseHelper.ALL_FROM_CRYPT_FAVORITES_QUERY;
	}

	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.i("vampidroid", "vampidroid.oncreate");
		
		
	
	}
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main_menu, menu);
//		
//		return super.onCreateOptionsMenu(menu);
//        
//	}

	
	
	

}

