package name.vampidroid.fragments;

import java.util.ArrayList;

import name.vampidroid.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.android.viewpagerindicator.TitlePageIndicator;
import com.jakewharton.android.viewpagerindicator.TitleProvider;

public abstract class VampiDroidBaseFragment extends Fragment {

	public static String CARD_ID = "card_id";
	public static String DECK_NAME = "deck_name";

	protected ViewPager mViewPager;

	protected TextIndicatorAdapter mTitleFlowIndicatorAdapter;
	
	private boolean mDualPane = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("vampidroidbasefragment", "onCreate");

		super.onCreate(savedInstanceState);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.d("vampidroidbasefragment", "onCreateView");

		
		View v = null;
		
		v = inflater.inflate(R.layout.main_viewpager, container,
				false);
		
		// Reference for the false value:
		// http://stackoverflow.com/questions/6035711/android-fragment-with-compatibility-package-on-2-3-3-creates-specified-child-a

		
		mViewPager = (ViewPager) v.findViewById(R.id.pager);


		TitlePageIndicator tpi = (TitlePageIndicator) v
				.findViewById(R.id.titlepageindicator);

		
		mTitleFlowIndicatorAdapter = new TextIndicatorAdapter(getActivity());
		
		
		mViewPager.setAdapter(mTitleFlowIndicatorAdapter);
				
		tpi.setViewPager(mViewPager);


		// If this is the first time, create new fragments for the lists.
		if (savedInstanceState == null) {
			Bundle cryptBundle = new Bundle();
			cryptBundle.putString("CryptQuery",
					getArguments().getString("CryptQuery"));
			
			
			mTitleFlowIndicatorAdapter.addTab("Crypt", CryptListFragment.class,
					cryptBundle);
	
			Bundle libraryBundle = new Bundle();
			libraryBundle.putString("LibraryQuery",
					getArguments().getString("LibraryQuery"));

			
			mTitleFlowIndicatorAdapter.addTab("Library", LibraryListFragment.class,
					libraryBundle);

		}
		else {
			
//			// Not the first time, we are returning from a previous state. Maybe a screen orientation change.
			
			mTitleFlowIndicatorAdapter.addTab("Crypt", getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "tab0"));
			mTitleFlowIndicatorAdapter.addTab("Library", getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "tab1"));
			
		}
		
		
		// Set choice behaviour.
		

		CryptListFragment crypt = (CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0);
		
		crypt.setHighlightChoice(isDualPane());
		
		LibraryListFragment library = (LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1);
		
		library.setHighlightChoice(isDualPane());
		
		setupFilterTextBox(v);
		
		
		return v;

	}



	protected void setupFilterTextBox(View v) {
		
		EditText et = (EditText) v.findViewById(R.id.edit_card_filter);
	    
	    
	    et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				
				
				((CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0)).filterData(" and Name like '%" + s + "%'");
				((LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1)).filterData(" and Name like '%" + s + "%'");
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
		});
	}
	
	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		
		
		for (int i = 0; i < mTitleFlowIndicatorAdapter.getCount(); i++) {
			
			Fragment fragment = mTitleFlowIndicatorAdapter.getItem(i);
			getActivity().getSupportFragmentManager().putFragment(outState, "tab" + String.valueOf(i), fragment);
			
		}
		
		
		
	}



	public ListFragment getChildFragment(int position) {
		return (ListFragment) mTitleFlowIndicatorAdapter.getItem(position);
	}
	
	
	public void updateQueries( String cryptQuery, String libraryQuery) {
		
		((CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0)).setQuery(cryptQuery);
		((LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1)).setQuery(libraryQuery);
	}

	protected abstract String getCryptQuery();

	protected abstract String getLibraryQuery();


	protected String formatQueryString(String stringExtra) {
		
		return stringExtra.trim().replace("'", "''");
	}

	public void setDualPane(boolean mDualPane) {
		this.mDualPane = mDualPane;
	}


	public boolean isDualPane() {
		return mDualPane;
	}

	public static class TextIndicatorAdapter extends FragmentPagerAdapter
			implements TitleProvider {

		private FragmentActivity mContext;


		public TextIndicatorAdapter(FragmentActivity activity) {
			super(activity.getSupportFragmentManager());

			mContext = activity;
			
			
		}

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;
			private final Fragment fragment;

			TabInfo(String _tag, Class<?> _class, Bundle _args, Fragment _fragment) {
				tag = _tag;
				clss = _class;
				args = _args;
				fragment = _fragment;
				
			}
		}

		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		
		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return info.fragment;
		}

		@Override
		public int getCount() {
			
			return mTabs.size();
		}

		@Override
		public String getTitle(int position) {
			
			TabInfo info = mTabs.get(position);
			return info.tag;
		}

		public void addTab(String tag, Class<?> clss, Bundle args) {

			TabInfo info = new TabInfo(tag, clss, args,  Fragment.instantiate(mContext, clss.getName(),
					args));
			mTabs.add(info);
			notifyDataSetChanged();
		}
		
		public void addTab(String tag, Fragment fragment) {

			TabInfo info = new TabInfo(tag, null, null, fragment);
			mTabs.add(info);
			
			notifyDataSetChanged();
		}

		
		

	}

}
