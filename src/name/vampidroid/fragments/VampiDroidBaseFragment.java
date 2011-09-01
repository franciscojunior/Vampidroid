package name.vampidroid.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import name.vampidroid.FilterModel;
import name.vampidroid.FilterModel.CommaEAmpTokenizer;
import name.vampidroid.R;
import android.content.Context;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import com.jakewharton.android.viewpagerindicator.TitlePageIndicator;
import com.jakewharton.android.viewpagerindicator.TitleProvider;

public abstract class VampiDroidBaseFragment extends Fragment {

	public static String CARD_ID = "card_id";
	public static String DECK_NAME = "deck_name";

	protected ViewPager mViewPager;

	protected TextIndicatorAdapter mTitleFlowIndicatorAdapter;

	private boolean mDualPane = false;
	
	
	public static FilterModel mFilterModel;

	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("vampidroidbasefragment", "onCreate");

		super.onCreate(savedInstanceState);
		
		mFilterModel = new FilterModel( Arrays.asList(getResources().getStringArray(R.array.clans)), Arrays.asList(getResources().getStringArray(R.array.types)), Arrays.asList(getResources().getStringArray(R.array.disciplineslibrary)), Arrays.asList(getResources().getStringArray(R.array.disciplinescrypt)));
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d("vampidroidbasefragment", "onCreateView");

		View v = null;

		v = inflater.inflate(R.layout.main_viewpager, container, false);

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

			mTitleFlowIndicatorAdapter.addTab("Library",
					LibraryListFragment.class, libraryBundle);

		} else {

			// // Not the first time, we are returning from a previous state.
			// Maybe a screen orientation change.

			mTitleFlowIndicatorAdapter.addTab(
					"Crypt",
					getActivity().getSupportFragmentManager().getFragment(
							savedInstanceState, "tab0"));
			mTitleFlowIndicatorAdapter.addTab(
					"Library",
					getActivity().getSupportFragmentManager().getFragment(
							savedInstanceState, "tab1"));

		}

		// Set choice behaviour.

		CryptListFragment crypt = (CryptListFragment) mTitleFlowIndicatorAdapter
				.getItem(0);

		crypt.setHighlightChoice(isDualPane());

		LibraryListFragment library = (LibraryListFragment) mTitleFlowIndicatorAdapter
				.getItem(1);

		library.setHighlightChoice(isDualPane());

		setupFilters(v);

		return v;

	}

	protected void setupFilterTextBox(View v) {

		EditText et = (EditText) v.findViewById(R.id.edit_card_filter);

		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String filter = s.toString().toLowerCase();

				mFilterModel.setNameFilter(filter);

				// ((CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0))
				// .filterData(VampiDroid.mFilterModel.getCryptFilter()
				// + " and lower(Name) like '%" + filter + "%'");
				// ((LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1))
				// .filterData(VampiDroid.mFilterModel.getLibraryFilter()
				// + " and lower(Name) like '%" + filter + "%'");

				getCryptListFragment().filterData(
						mFilterModel.getCryptFilterQuery()
								+ mFilterModel.getNameFilterQuery());
				getLibraryListFragment().filterData(
						mFilterModel.getLibraryFilterQuery()
								+ mFilterModel.getNameFilterQuery());

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

	protected void setupFilters(View v) {

		setupFilterTextBox(v);

		setupFilterLayout(v);

		CommaEAmpTokenizer tokenizer = new CommaEAmpTokenizer();

		ArrayAdapter<String> adapterCrypt = new ArrayAdapter<String>(
				this.getActivity(),
				android.R.layout.simple_dropdown_item_1line,
				mFilterModel.getCryptFilterStrings());

		MultiAutoCompleteTextView cryptFilterText = (MultiAutoCompleteTextView) v
				.findViewById(R.id.crypt_filters);
		cryptFilterText.setAdapter(adapterCrypt);
		cryptFilterText.setTokenizer(tokenizer);

		ArrayAdapter<String> adapterLibrary = new ArrayAdapter<String>(
				this.getActivity(),
				android.R.layout.simple_dropdown_item_1line,
				mFilterModel.getLibraryFilterStrings());

		MultiAutoCompleteTextView libraryFilterText = (MultiAutoCompleteTextView) v
				.findViewById(R.id.library_filters);
		libraryFilterText.setAdapter(adapterLibrary);
		libraryFilterText.setTokenizer(tokenizer);

		// cryptFilterText.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		//
		//
		// String s = cryptFilterText.getText().toString().trim();
		//
		// if (s.endsWith(",")) {
		//
		// VampiDroid.mFilterModel.clearCryptFilters();
		//
		//
		// VampiDroid.mFilterModel.buildCryptFiltersFromString(s);
		//
		//
		// System.out.println(VampiDroid.mFilterModel.getCryptFilter());
		//
		// //((CryptListFragment)
		// mTitleFlowIndicatorAdapter.getItem(0)).filterData(VampiDroid.mFilterModel.getCryptFilter());
		//
		// //((LibraryListFragment)
		// mTitleFlowIndicatorAdapter.getItem(1)).filterData(VampiDroid.mFilterModel.getLibraryFilter());
		//
		//
		// ((CryptListFragment)
		// mTitleFlowIndicatorAdapter.getItem(0)).filterData(VampiDroid.mFilterModel.getCryptFilter());
		//
		// }
		// }
		// });

		//
		// libraryFilterText.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		//
		//
		// String s = libraryFilterText.getText().toString().trim();
		//
		// if (s.endsWith(",")) {
		//
		// VampiDroid.mFilterModel.clearLibraryFilters();
		//
		//
		// VampiDroid.mFilterModel.buildLibraryFiltersFromString(s);
		//
		//
		// System.out.println(VampiDroid.mFilterModel.getLibraryFilter());
		//
		// //((CryptListFragment)
		// mTitleFlowIndicatorAdapter.getItem(0)).filterData(VampiDroid.mFilterModel.getCryptFilter());
		//
		// //((LibraryListFragment)
		// mTitleFlowIndicatorAdapter.getItem(1)).filterData(VampiDroid.mFilterModel.getLibraryFilter());
		//
		//
		// ((LibraryListFragment)
		// mTitleFlowIndicatorAdapter.getItem(1)).filterData(VampiDroid.mFilterModel.getLibraryFilter());
		//
		// }
		// }
		// });

		cryptFilterText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String filter = s.toString().trim();

				if (filter.endsWith(",") || filter.length() == 0) {

					mFilterModel.clearCryptFilters();

					mFilterModel.buildCryptFiltersFromString(filter);

					getCryptListFragment().filterData(
							mFilterModel.getCryptFilterQuery()
									+ mFilterModel
											.getNameFilterQuery());

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		libraryFilterText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String filter = s.toString().trim();

				if (filter.endsWith(",") || filter.length() == 0) {

					mFilterModel.clearLibraryFilters();

					mFilterModel
							.buildLibraryFiltersFromString(filter);

					getLibraryListFragment().filterData(
							mFilterModel.getLibraryFilterQuery()
									+ mFilterModel
											.getNameFilterQuery());
				}
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

//	private void setupFilterLayout(View v) {
//
//		// Get filters layout and expandable image references.
//		final View filtersLayout = v.findViewById(R.id.filters_layout);
//		final ImageView expandableImageView = (ImageView) v.findViewById(R.id.expandableImage);
//		
//		
//		
//		View filtersLayoutHeader = v.findViewById(R.id.filters_layout_header);
//		
//		filtersLayoutHeader.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				if (filtersLayout.getVisibility() == View.VISIBLE) {
//					filtersLayout.setVisibility(View.GONE);
//					expandableImageView
//							.setImageResource(R.drawable.expander_ic_minimized);
//
//					// Hide keyboard
//					// As per
//					// http://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard
//					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(
//							filtersLayout.getWindowToken(), 0);
//
//				} else {
//					filtersLayout.setVisibility(View.VISIBLE);
//					expandableImageView
//							.setImageResource(R.drawable.expander_ic_maximized);
//				}
//			}
//		});
//		
//		
//		
//		
//		
//
//	}
	
	private void setupFilterLayout(View v) {

		// Get filters layout and expandable image references.
		final View cryptFilter = v.findViewById(R.id.crypt_filters);
		final View libraryFilter = v.findViewById(R.id.library_filters);

		
		final ImageView expandableImageView = (ImageView) v.findViewById(R.id.expandableImage);
		
		
		
		View filtersLayoutHeader = v.findViewById(R.id.filters_layout_header);
		
		filtersLayoutHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (cryptFilter.getVisibility() == View.VISIBLE) {
					cryptFilter.setVisibility(View.GONE);
					libraryFilter.setVisibility(View.GONE);
					expandableImageView
							.setImageResource(R.drawable.expander_ic_minimized);

					// Hide keyboard
					// As per
					// http://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							cryptFilter.getWindowToken(), 0);

				} else {
					
					cryptFilter.setVisibility(View.VISIBLE);
					libraryFilter.setVisibility(View.VISIBLE);
					expandableImageView
							.setImageResource(R.drawable.expander_ic_maximized);
				}
			}
		});
		
		
		
		
		

	}


	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		for (int i = 0; i < mTitleFlowIndicatorAdapter.getCount(); i++) {
	
			Fragment fragment = mTitleFlowIndicatorAdapter.getItem(i);
			
			getActivity().getSupportFragmentManager().putFragment(outState,
					"tab" + String.valueOf(i), fragment);
	
		}
	
		

	}

	public ListFragment getChildFragment(int position) {
		return (ListFragment) mTitleFlowIndicatorAdapter.getItem(position);
	}

	public void updateQueries(String cryptQuery, String libraryQuery) {

		((CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0))
				.setQuery(cryptQuery);
		((LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1))
				.setQuery(libraryQuery);
	}

	protected abstract String getCryptQuery();

	protected abstract String getLibraryQuery();

	public void setDualPane(boolean mDualPane) {
		this.mDualPane = mDualPane;
	}

	public boolean isDualPane() {
		return mDualPane;
	}

	protected CryptListFragment getCryptListFragment() {
		return ((CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0));
	}

	protected LibraryListFragment getLibraryListFragment() {
		return ((LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1));
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

			TabInfo(String _tag, Class<?> _class, Bundle _args,
					Fragment _fragment) {
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

			TabInfo info = new TabInfo(tag, clss, args, Fragment.instantiate(
					mContext, clss.getName(), args));
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
