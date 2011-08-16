package name.vampidroid.fragments;

import name.vampidroid.DatabaseHelper;
import android.os.Bundle;
import android.util.Log;

public class VampiDroidFragment extends VampiDroidBaseFragment {

	@Override
	public void onDestroy() {
		
		Log.d("vampidroidfragment", "ondestroy");
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	protected String getLibraryQuery() {
		return DatabaseHelper.ALL_FROM_LIBRARY_QUERY;
	}

	protected String getCryptQuery() {
		return DatabaseHelper.ALL_FROM_CRYPT_QUERY;
	}

		
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	 }
	 
	 
	 public static VampiDroidFragment newInstance(String cryptQuery, String libraryQuery, boolean hasDetailsFrame) {
			
			VampiDroidFragment fragment = new VampiDroidFragment();
			
			Bundle b = new Bundle();
			b.putString("CryptQuery", cryptQuery);
			b.putString("LibraryQuery", libraryQuery);
			
			
			fragment.setArguments(b);
			fragment.setDualPane(hasDetailsFrame);
			
			return fragment;
			
		}
		
	

}

/*
 * References:
 * http://stackoverflow.com/questions/1737009/how-to-make-a-nice-looking
 * -listview-filter-on-android
 */