package name.vampidroid;

import name.vampidroid.fragments.CryptDetailsFragment;
import name.vampidroid.fragments.CryptListFragment.OnCryptCardSelectedListener;
import name.vampidroid.fragments.LibraryDetailsFragment;
import name.vampidroid.fragments.LibraryListFragment.OnLibraryCardSelectedListener;
import name.vampidroid.fragments.VampiDroidFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;


public abstract class VampiDroidBase extends FragmentActivity implements OnCryptCardSelectedListener, OnLibraryCardSelectedListener {

	
	public static String CARD_ID = "card_id";
	public static String DECK_NAME = "deck_name";
	
	protected EditText searchTextEdit = null;
	
	protected boolean mHasDetailsFrame = false;
	protected VampiDroidFragment mVampidroidFragment;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.d("vampidroidbase", "onCreate");
		
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.main);
	    setContentView(R.layout.main);
	    
	    
	    // Check to see if we have a frame in which to embed the details
		// fragment directly in the containing UI.
		View detailsFrame = findViewById(R.id.frame_carddetails);
		mHasDetailsFrame = (detailsFrame != null) && (detailsFrame.getVisibility() == View.VISIBLE);
	    
	    
	    	    
	    
	    mVampidroidFragment = (VampiDroidFragment) getSupportFragmentManager().findFragmentByTag("vampidroid_container");
	    
	    
	    if (mVampidroidFragment == null) {
	    	
	    	Log.d("vampidroid", "fragment null");
	    	
	    	mVampidroidFragment = VampiDroidFragment.newInstance(getCryptQuery(), getLibraryQuery(), hasDetailsFrame());
	    	
	    	getSupportFragmentManager().beginTransaction()
		    .replace(R.id.fragment_vampidroid_container, mVampidroidFragment, "vampidroid_container")
			.commit();
	    	
	    }
	    else {
	    	
	    	Log.d("vampidroid", "fragment not null");
	    	
	    	mVampidroidFragment.setDualPane(hasDetailsFrame());
	    	
	    }
	    
	    	    
	    
	    
    	
	}
	



	@Override
	public void onCryptCardSelected(long cardID) {
		// TODO Auto-generated method stub
		
		
		showCryptCardDetails(this, cardID);
		
	}
	
	@Override
	public void onLibraryCardSelected(long cardID) {
		// TODO Auto-generated method stub
		
		showLibraryCardDetails(this, cardID);
	}

	protected void setupSearchTextBox() {
		EditText searchTextEdit = new EditText(this);
	    
	    searchTextEdit.setFocusableInTouchMode(false);
	    searchTextEdit.setHint("Search Cards...");
	    searchTextEdit.setInputType(InputType.TYPE_NULL);
	    
	    getSupportActionBar().setCustomView(searchTextEdit);
	    getSupportActionBar().setDisplayShowCustomEnabled(true);
	    
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
	}

	protected abstract String getCryptQuery();
	

	protected abstract String getLibraryQuery();



	protected String formatQueryString(String stringExtra) {
		// TODO Auto-generated method stub
		return stringExtra.trim().replace("'", "''");
	}

	

	public void showCryptCardDetails(Context context, long id) {
	
		if (hasDetailsFrame()) {
			// If we are not currently showing a fragment for the new
			// position, we need to create and install a new one.
			CryptDetailsFragment cdf = CryptDetailsFragment.newInstance(id);
			
			
			// Execute a transaction, replacing any existing fragment
			// with this one inside the frame.
			
			
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.frame_carddetails, cdf)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
	
		}
	
		else {
	
			Intent cryptCardIntent = new Intent(context,
					CryptDetails.class);
	
			cryptCardIntent.putExtra(CARD_ID, id);
	
			cryptCardIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	
			startActivity(cryptCardIntent);
		}
	
	}
	
	public void showLibraryCardDetails(Context context, long id) {
		
		if (hasDetailsFrame()) {
			// If we are not currently showing a fragment for the new
			// position, we need to create and install a new one.
			LibraryDetailsFragment ldf = LibraryDetailsFragment.newInstance(id);
	
			// Execute a transaction, replacing any existing fragment
			// with this one inside the frame.
			
			
			
			
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.frame_carddetails, ldf)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
	
		}
	
		else {
	
			Intent libraryCardIntent = new Intent(context,
					LibraryDetails.class);
	
			libraryCardIntent.putExtra(CARD_ID, id);
	
			libraryCardIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	
			startActivity(libraryCardIntent);
		}
	
	}
	

	public boolean hasDetailsFrame() {
		
			return mHasDetailsFrame;
			
		}

		

	/** Updates queries used by the lists contained in the Crypt and Library lists */
	public void updateQueries() {

		//VampiDroidFragment fragment = (VampiDroidFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_vampidroid_container);
		
		mVampidroidFragment.updateQueries(getCryptQuery(), getLibraryQuery());
		
		
	}
		

}
