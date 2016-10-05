package name.vampidroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.List;

import name.vampidroid.fragments.CardsListFragment;
import name.vampidroid.fragments.SettingsFragment;
import name.vampidroid.ui.widget.CardFilters;

import static name.vampidroid.Utils.playDrawerToggleAnim;


public class VampiDroid extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private final static String TAG = "MainActivity";

	private ViewPager viewPager;
	private TabLayout tabLayout;
	private CryptCardsListViewAdapter cryptCardsListViewAdapter;
	private LibraryCardsListViewAdapter libraryCardsListViewAdapter;

	private List<CardsListFragment> fragmentsToFilter2 = new ArrayList<>();

	private FrameLayout search_container;
	private Toolbar toolbar;
	private DrawerLayout drawerLayout;
	private DrawerArrowDrawable drawerArrowDrawable;

	private boolean searchShown = false;
	private TextView search_bar_text_view;

	String filter = "";

	FilterModel filterModel = new FilterModel();

	boolean restoring = false;
	private BadgeView searchFiltersBadge;
	private CardFilters cardFilters;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate... ");


		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		viewPager = (ViewPager) findViewById(R.id.viewpager);

		tabLayout = (TabLayout) findViewById(R.id.tablayout);



		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);



		final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
				appbar.setExpanded(true);
				search_bar_text_view.requestFocus();

				// Reference: http://stackoverflow.com/questions/2403632/android-show-soft-keyboard-automatically-when-focus-is-on-an-edittext
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(search_bar_text_view, InputMethodManager.SHOW_IMPLICIT);


			}
		});

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



		drawerArrowDrawable = new DrawerArrowDrawable(this);

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);


		search_container = (FrameLayout) getLayoutInflater().inflate(R.layout.persistent_search_bar, null);


		setupSearchContainter(search_container);

		toolbar.addView(search_container);

		// TODO: 11/06/16 Check how to make those initializations off the main thread.
		setupSearchFilterNavigation();







	}

	private void setupSearchFilterNavigation() {

		cardFilters = (CardFilters) findViewById(R.id.cardFilters);

//		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//			@Override
//			public void onTabSelected(TabLayout.Tab tab) {
//				if (tab.getPosition() == 0) {
//					cardFilters.showCryptFilters();
//				} else {
//					cardFilters.showLibraryFilters();
//				}
//			}
//
//			@Override
//			public void onTabUnselected(TabLayout.Tab tab) {
//
//			}
//
//			@Override
//			public void onTabReselected(TabLayout.Tab tab) {
//
//			}
//		});



		cardFilters.setOnCardFiltersChangeListener(new CardFilters.OnCardFiltersChangeListener() {
			@Override
			public void onGroupsChanged(int group, boolean isChecked) {
				filterModel.setGroup(group, isChecked);
				updateFiltersBadgeText();
				filterCards();
			}

			@Override
			public void onCapacitiesChanged(int minCapacity, int maxCapacity) {

				filterModel.setCapacityMin(minCapacity);
				filterModel.setCapacityMax(maxCapacity);
				updateFiltersBadgeText();
				filterCards();
			}

			@Override
			public void onCryptDisciplineChanged(String discipline, boolean isBasic, boolean isChecked) {

				filterModel.setDiscipline(discipline, isBasic, isChecked);
				updateFiltersBadgeText();
				filterCards();

			}


		});

	}

	private void updateFiltersBadgeText() {
		int numberOfFiltersApplied = cardFilters.getNumberOfFiltersApplied();
		if (numberOfFiltersApplied > 0) {
			searchFiltersBadge.setText(String.valueOf(numberOfFiltersApplied));
			searchFiltersBadge.show();
		} else {
			searchFiltersBadge.hide();
		}

	}


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		Log.d(TAG, "onRestoreInstanceState() called with: " + "savedInstanceState =");

		restoring = true;

        super.onRestoreInstanceState(savedInstanceState);

		filterModel = savedInstanceState.getParcelable("filtermodel");

		restoring = false;

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState() called with: " + "outState = ");

		super.onSaveInstanceState(outState);



		outState.putParcelable("filtermodel", filterModel);



	}

	@Override
	protected void onResume() {
		super.onResume();

        Log.d(TAG, "onResume: ");

        // Update possible changes to preferences.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean prefSearchCardText = sharedPref.getBoolean(SettingsFragment.KEY_PREF_SEARCH_CARD_TEXT, false);

        if (prefSearchCardText != filterModel.searchInsideCardText) {
            filterModel.setSearchInsideCardText(prefSearchCardText);
            filterCards();
        }

        // Sync navigation drawer selected item.
        // Reference: http://stackoverflow.com/questions/34502848/how-to-change-selected-item-in-the-navigation-drawer-depending-on-the-activity-v?rq=1

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_camera);


		updateFiltersBadgeText();

	}

	private void setupSearchContainter(FrameLayout search_container) {

		final ImageView imageViewLeftAction = (ImageView) search_container.findViewById(R.id.left_action);
		search_bar_text_view = (TextView) search_container.findViewById(R.id.search_bar_text);
		final ImageView imageViewCloseButton = (ImageView) search_container.findViewById(R.id.clear_btn);
		final ImageView imageViewSearchSettingsButton = (ImageView) search_container.findViewById(R.id.search_settings);


		imageViewLeftAction.setImageDrawable(drawerArrowDrawable);
		imageViewLeftAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (search_bar_text_view.getText().length() > 0) {
					search_bar_text_view.setText("");
				} else if (drawerArrowDrawable.getProgress() != 0){
					playDrawerToggleAnim(drawerArrowDrawable);
					// Reference: http://stackoverflow.com/questions/5056734/android-force-edittext-to-remove-focus/16477251#16477251
					search_bar_text_view.clearFocus();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);
				} else {
					drawerLayout.openDrawer(GravityCompat.START);
				}





			}
		});




		search_bar_text_view.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

                filterModel.setName(s);

                filterCards();

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.length() > 0) {
					imageViewCloseButton.setVisibility(View.VISIBLE);
				} else {
					imageViewCloseButton.setVisibility(View.GONE);

				}



			}
		});


		imageViewCloseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search_bar_text_view.setText("");
			}
		});


		imageViewSearchSettingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


//                SearchSettingsFragment searchSettingsFragment = SearchSettingsFragment.newInstance();
//                searchSettingsFragment.show(getSupportFragmentManager(), "search_settings_fragment");

				// If the keyboard is present, hide it.
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);

				// Open right navigation view.
				drawerLayout.openDrawer(GravityCompat.END);


			}
		});

		search_bar_text_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					// Only animate if we are showing the burger icon.
					if (drawerArrowDrawable.getProgress() == 0.0)
						playDrawerToggleAnim(drawerArrowDrawable);
				}

			}
		});


		searchFiltersBadge = new BadgeView(this, imageViewSearchSettingsButton);
		searchFiltersBadge.setBadgeBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));


	}

	private void filterCards() {

        // If we are restoring, there is no need to filter now. The data will already be filtered out when the state was saved.
        if (restoring) {
            Log.d(TAG, "filterCards: not filtering because we are restoring");
            return;
        }

        for (CardsListFragment fragment :
                fragmentsToFilter2) {

            fragment.filterCards(filterModel);
        }

	}


	//    Reference: http://stackoverflow.com/questions/11358121/how-to-handle-the-checkbox-ischecked-and-unchecked-event-in-android
	//	http://stackoverflow.com/questions/10137692/how-to-get-resource-name-from-resource-id
	public void disciplinesClickHandler(View v) {

//		CheckBox checkbox = (CheckBox) v;
//
//		String discipline = getResources().getResourceEntryName(checkbox.getId());
//
//		// Check if the discipline is basic or advanced.
//
//		boolean isBasic = discipline.contains("Basic");
//
//		discipline = discipline.substring(discipline.length() - 3);
//
//		filterModel.setDiscipline(discipline, isBasic, checkbox.isChecked());
//
//
//		filterCards();

	}


	public void cardTypesViewGroupClickHandler(View view) {

		ViewGroup viewGroup = (ViewGroup) view;
		TextView label = (TextView) viewGroup.getChildAt(0);
		CheckBox checkBox = (CheckBox) viewGroup.getChildAt(1);

		checkBox.toggle();

		filterModel.setCardType(label.getText(), checkBox.isChecked());

		filterCards();

	}

	public void clansViewGroupClickHandler(View view) {

		ViewGroup viewGroup = (ViewGroup) view;
		TextView label = (TextView) viewGroup.getChildAt(0);
		CheckBox checkBox = (CheckBox) viewGroup.getChildAt(1);

		checkBox.toggle();

		filterModel.setClan(label.getText(), checkBox.isChecked());

		filterCards();

	}


	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof CardsListFragment)
			fragmentsToFilter2.add((CardsListFragment) fragment);
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(viewPagerAdapter);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else if (drawerArrowDrawable.getProgress() != 0){
			playDrawerToggleAnim(drawerArrowDrawable);
			// Reference: http://stackoverflow.com/questions/5056734/android-force-edittext-to-remove-focus/16477251#16477251
			search_bar_text_view.clearFocus();
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(search_bar_text_view.getWindowToken(), 0);

		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		Log.d(TAG, "onOptionsItemSelected: ");

		if (id == android.R.id.home) {

			drawerLayout.openDrawer(GravityCompat.START);

			return true;

		} else if (id == R.id.action_settings) {  //noinspection SimplifiableIfStatement
			return true;

		} else if (id == R.id.action_search) {

		}



		return super.onOptionsItemSelected(item);
	}

	// Reference: http://stackoverflow.com/questions/26835209/appcompat-v7-toolbar-up-back-arrow-not-working



	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {

		boolean closeDrawer = true;

		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

        /*} else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {*/

		} else if (id == R.id.nav_settings) {
            Intent launch = new Intent(this, SettingsActivity.class);
            startActivity(launch);
			// Don't close the drawer when selecting settings.
			// It was causing stuttering when starting the Settings activity.
			closeDrawer = false;
        }


		if (closeDrawer) {
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
			drawer.closeDrawer(GravityCompat.START);
		}

		return true;
	}



}


