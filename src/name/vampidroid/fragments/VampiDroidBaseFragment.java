// References:
//http://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard
// http://stackoverflow.com/questions/6035711/android-fragment-with-compatibility-package-on-2-3-3-creates-specified-child-a

package name.vampidroid.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import name.vampidroid.FilterModel;
import name.vampidroid.FilterModel.CommaEAmpTokenizer;
import name.vampidroid.R;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import com.jakewharton.android.viewpagerindicator.TitlePageIndicator;
import com.jakewharton.android.viewpagerindicator.TitleProvider;

public abstract class VampiDroidBaseFragment extends Fragment {

	private Handler mHandler = new Handler();

	public static String CARD_ID = "card_id";
	public static String DECK_NAME = "deck_name";

	protected ViewPager mViewPager;

	protected TextIndicatorAdapter mTitleFlowIndicatorAdapter;

	private boolean mDualPane = false;

	protected EditText mCardNameFilterText;

	protected MultiAutoCompleteTextView mCryptFilterText;

	protected MultiAutoCompleteTextView mLibraryFilterText;

	protected boolean mCryptFilterTextChanged;

	protected boolean mLibraryFilterTextChanged;

	private boolean mCanApplyFilter = false; // This flag prevents the
												// background thread from being
												// run.

	protected FilterModel mFilterModel = new FilterModel();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("vampidroidbasefragment", "onCreate");

		super.onCreate(savedInstanceState);

	}

	@Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	//
	// Log.d("vampidroidbasefragment", "onCreateView");
	//
	// View v = null;
	//
	// v = inflater.inflate(R.layout.main_viewpager, container, false);
	//
	// // Reference for the false value:
	// //
	// http://stackoverflow.com/questions/6035711/android-fragment-with-compatibility-package-on-2-3-3-creates-specified-child-a
	//
	// mViewPager = (ViewPager) v.findViewById(R.id.pager);
	//
	// TitlePageIndicator tpi = (TitlePageIndicator) v
	// .findViewById(R.id.titlepageindicator);
	//
	// mTitleFlowIndicatorAdapter = new TextIndicatorAdapter(getActivity());
	//
	// mViewPager.setAdapter(mTitleFlowIndicatorAdapter);
	//
	// tpi.setViewPager(mViewPager);
	//
	// // If this is the first time, create new fragments for the lists.
	// if (savedInstanceState == null) {
	// Bundle cryptBundle = new Bundle();
	// cryptBundle.putString("CryptQuery",
	// getArguments().getString("CryptQuery"));
	//
	// mTitleFlowIndicatorAdapter.addTab("Crypt", CryptListFragment.class,
	// cryptBundle);
	//
	// Bundle libraryBundle = new Bundle();
	// libraryBundle.putString("LibraryQuery",
	// getArguments().getString("LibraryQuery"));
	//
	// mTitleFlowIndicatorAdapter.addTab("Library",
	// LibraryListFragment.class, libraryBundle);
	//
	// } else {
	//
	// // // Not the first time, we are returning from a previous state.
	// // Maybe a screen orientation change.
	//
	// mTitleFlowIndicatorAdapter.addTab(
	// "Crypt",
	// getActivity().getSupportFragmentManager().getFragment(
	// savedInstanceState, "tab0"));
	// mTitleFlowIndicatorAdapter.addTab(
	// "Library",
	// getActivity().getSupportFragmentManager().getFragment(
	// savedInstanceState, "tab1"));
	//
	// }
	//
	// // Set choice behaviour.
	//
	// CryptListFragment crypt = (CryptListFragment) mTitleFlowIndicatorAdapter
	// .getItem(0);
	//
	// crypt.setHighlightChoice(isDualPane());
	//
	// LibraryListFragment library = (LibraryListFragment)
	// mTitleFlowIndicatorAdapter
	// .getItem(1);
	//
	// library.setHighlightChoice(isDualPane());
	//
	// setupFilters(v);
	//
	// // setupMotionEventActionDown(v);
	//
	// return v;
	//
	// }
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		Log.d("vampidroidbasefragment", "onCreateView");

		View v = null;

		v = inflater.inflate(R.layout.main_viewpager, container, false);

		// Reference for the false value:
		// http://stackoverflow.com/questions/6035711/android-fragment-with-compatibility-package-on-2-3-3-creates-specified-child-a

		mViewPager = (ViewPager) v.findViewById(R.id.pager);

		TitlePageIndicator tpi = (TitlePageIndicator) v.findViewById(R.id.titlepageindicator);

		mTitleFlowIndicatorAdapter = new TextIndicatorAdapter(getActivity());

		mViewPager.setAdapter(mTitleFlowIndicatorAdapter);

		tpi.setViewPager(mViewPager);

		// If this is the first time, create new fragments for the lists.
		if (savedInstanceState == null) {
			mTitleFlowIndicatorAdapter.addTab("Crypt", CryptListFragment.class, null);

			mTitleFlowIndicatorAdapter.addTab("Library", LibraryListFragment.class, null);

		} else {

			// // Not the first time, we are returning from a previous state.
			// Maybe a screen orientation change.

			mTitleFlowIndicatorAdapter.addTab("Crypt",
					getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "tab0"));
			mTitleFlowIndicatorAdapter.addTab("Library",
					getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "tab1"));

		}

		// Set choice behaviour.

		CryptListFragment crypt = (CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0);

		crypt.setHighlightChoice(isDualPane());

		LibraryListFragment library = (LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1);

		library.setHighlightChoice(isDualPane());

		setupFilters(v);

		// setupMotionEventActionDown(v);

		return v;

	}

	private void setupMotionEventActionDown(final View v) {
		// TODO Auto-generated method stub

		v.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub


				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				}

				return false;
			}
		});

	}

	protected void setupFilterTextBox(View v) {

		mCardNameFilterText = (EditText) v.findViewById(R.id.edit_card_filter);

		mCardNameFilterText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				deferFiltersUpdate();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

		ArrayAdapter<String> adapterCrypt = new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_dropdown_item_1line, mFilterModel.getCryptFilterStrings());

		mCryptFilterText = (MultiAutoCompleteTextView) v.findViewById(R.id.crypt_filters);
		mCryptFilterText.setAdapter(adapterCrypt);
		mCryptFilterText.setTokenizer(tokenizer);

		ArrayAdapter<String> adapterLibrary = new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_dropdown_item_1line, mFilterModel.getLibraryFilterStrings());

		mLibraryFilterText = (MultiAutoCompleteTextView) v.findViewById(R.id.library_filters);
		mLibraryFilterText.setAdapter(adapterLibrary);
		mLibraryFilterText.setTokenizer(tokenizer);

		mCryptFilterText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				mCryptFilterTextChanged = true;

				deferFiltersUpdate();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mLibraryFilterText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				mLibraryFilterTextChanged = true;

				deferFiltersUpdate();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	protected void deferFiltersUpdate() {

		// This may have been called by TextChangedListeners before the fragment
		// is ready. On a rotation for example or when restarting fragment.
		// Android calls setText on the EditText when restarting which triggers
		// those listeners ahead of time.
		if (mCanApplyFilter) {

			mHandler.removeCallbacks(mUpdateFilters);
			mHandler.postDelayed(mUpdateFilters, 500);
		}

	}

	private Runnable mUpdateFilters = new Runnable() {

		public void run() {

			mFilterModel.setNameFilter(mCardNameFilterText.getText().toString().trim());

			// ((CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0))
			// .filterData(VampiDroid.mFilterModel.getCryptFilter()
			// + " and lower(Name) like '%" + filter + "%'");
			// ((LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1))
			// .filterData(VampiDroid.mFilterModel.getLibraryFilter()
			// + " and lower(Name) like '%" + filter + "%'");

			// Just update model if there was any change....

			// if (mCryptFilterTextChanged) {
			//
			// mFilterModel.clearCryptFilters();
			// mFilterModel.buildCryptFiltersFromString(mCryptFilterText.getText().toString().trim());
			// mCryptFilterTextChanged = false;
			// }
			//
			// if (mLibraryFilterTextChanged) {
			// mFilterModel.clearLibraryFilters();
			//
			// mFilterModel.buildLibraryFiltersFromString(mLibraryFilterText.getText().toString().trim());
			//
			// mLibraryFilterTextChanged = false;
			// }
			//
			// getCryptListFragment().filterData(
			// mFilterModel.getCryptFilterQuery() +
			// mFilterModel.getNameFilterQuery()
			// + mFilterModel.getOrderByFilterQuery());
			// getLibraryListFragment().filterData(
			// mFilterModel.getLibraryFilterQuery() +
			// mFilterModel.getNameFilterQuery()
			// + mFilterModel.getOrderByFilterQuery());

			mFilterModel.setCryptFilter(mCryptFilterText.getText().toString().trim());

			mFilterModel.setLibraryFilter(mLibraryFilterText.getText().toString().trim());
			
			
			if (mFilterModel.isCryptFilterChanged()) {
				getCryptListFragment().setFilter(
						mFilterModel.getCryptFilterQuery() + mFilterModel.getNameFilterQuery());
				getCryptListFragment().refreshList();
				
			}

			if (mFilterModel.isLibraryFilterChanged()) {
				getLibraryListFragment().setFilter(
						mFilterModel.getLibraryFilterQuery() + mFilterModel.getNameFilterQuery());
				getLibraryListFragment().refreshList();
				
			}

		}
	};

	// private void setupFilterLayout(View v) {
	//
	// // Get filters layout and expandable image references.
	// final View filtersLayout = v.findViewById(R.id.filters_layout);
	// final ImageView expandableImageView = (ImageView)
	// v.findViewById(R.id.expandableImage);
	//
	//
	//
	// View filtersLayoutHeader = v.findViewById(R.id.filters_layout_header);
	//
	// filtersLayoutHeader.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	// if (filtersLayout.getVisibility() == View.VISIBLE) {
	// filtersLayout.setVisibility(View.GONE);
	// expandableImageView
	// .setImageResource(R.drawable.expander_ic_minimized);
	//
	// // Hide keyboard
	// // As per
	// //
	// http://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard
	// InputMethodManager imm = (InputMethodManager)
	// getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(
	// filtersLayout.getWindowToken(), 0);
	//
	// } else {
	// filtersLayout.setVisibility(View.VISIBLE);
	// expandableImageView
	// .setImageResource(R.drawable.expander_ic_maximized);
	// }
	// }
	// });
	//
	// }

	@Override
	public void onResume() {

		Log.d("vampidroid", "vampidroidbasefragment.onresume");

		super.onResume();

		mCanApplyFilter = true;

		// Apply filters on resume, if any...
		mUpdateFilters.run();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// Remove any callback handlers so they aren't triggered on the wrong
		// fragment (the old one) in cases of orientation change or
		// activity restart.
		mHandler.removeCallbacks(mUpdateFilters);

	}

	private void setupFilterLayout(View v) {

		// Get filters layout and expandable image references.
		final View cryptFilter = v.findViewById(R.id.crypt_filters);
		final View libraryFilter = v.findViewById(R.id.library_filters);

		final ImageView expandableImageView = (ImageView) v.findViewById(R.id.expandFiltersImageView);

		View filtersLayoutHeader = v.findViewById(R.id.filters_layout_header);

		filtersLayoutHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (cryptFilter.getVisibility() == View.VISIBLE) {
					cryptFilter.setVisibility(View.GONE);
					libraryFilter.setVisibility(View.GONE);
					expandableImageView.setImageResource(R.drawable.expander_ic_minimized);

					// Hide keyboard
					// As per
					// http://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(cryptFilter.getWindowToken(), 0);

				} else {

					cryptFilter.setVisibility(View.VISIBLE);
					libraryFilter.setVisibility(View.VISIBLE);
					expandableImageView.setImageResource(R.drawable.expander_ic_maximized);
				}
			}
		});

	}

	// private void setupFilterLayout(View v) {
	//
	// // Get filters layout and expandable image references.
	//
	//
	// final ImageView expandableImageView = (ImageView)
	// v.findViewById(R.id.expandFiltersImageView);
	//
	//
	// final View filtersLayoutHeader =
	// v.findViewById(R.id.filters_layout_header);
	//
	// expandableImageView.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	//
	// if (filtersLayoutHeader.getVisibility() == View.VISIBLE) {
	// filtersLayoutHeader.setVisibility(View.GONE);
	// expandableImageView.setImageResource(R.drawable.expander_ic_minimized);
	//
	// // Hide keyboard
	// // As per
	// //
	// http://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard
	// InputMethodManager imm = (InputMethodManager)
	// getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(
	// filtersLayoutHeader.getWindowToken(), 0);
	//
	// }
	// else {
	// filtersLayoutHeader.setVisibility(View.VISIBLE);
	// expandableImageView.setImageResource(R.drawable.expander_ic_maximized);
	//
	//
	//
	// }
	// }
	// });
	//
	// }

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

	protected abstract String getCryptQuery();

	protected abstract String getLibraryQuery();

	public void setDualPane(boolean mDualPane) {
		this.mDualPane = mDualPane;
	}

	public boolean isDualPane() {
		return mDualPane;
	}

	public CryptListFragment getCryptListFragment() {
		return ((CryptListFragment) mTitleFlowIndicatorAdapter.getItem(0));
	}

	public LibraryListFragment getLibraryListFragment() {
		return ((LibraryListFragment) mTitleFlowIndicatorAdapter.getItem(1));
	}

	public static class TextIndicatorAdapter extends FragmentPagerAdapter implements TitleProvider {

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

			TabInfo info = new TabInfo(tag, clss, args, Fragment.instantiate(mContext, clss.getName(), args));
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
