package name.vampidroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.support.design.widget.RxTabLayout;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import name.vampidroid.ui.widget.CardFilters;
import name.vampidroid.ui.widget.PersistentSearchBar;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class VampiDroid extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MainActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;

    PersistentSearchBar persistentSearchBar;
    private Toolbar toolbar;
    DrawerLayout drawerLayout;

    FilterState filterState = new FilterState();

    boolean restoring = false;
    CardFilters cardFilters;
    private ViewPagerAdapter viewPagerAdapter;

    private CardsViewModel cardsViewModel;

    boolean refreshDataNeeded = false;
    private boolean prefSearchCardTextWhenPaused;


    private CompositeSubscription subscriptions = new CompositeSubscription();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate... ");


        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        cardsViewModel = ((VampiDroidApplication)getApplication()).getCardsViewModel();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
                appbar.setExpanded(true);

                persistentSearchBar.editSearchBarText();

            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        persistentSearchBar = new PersistentSearchBar(this);

        setupPersistentSearchBar();


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setCustomView(persistentSearchBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            toolbar.setContentInsetsAbsolute(10, 10);
//        }


        // Reference: http://stackoverflow.com/questions/26433409/android-lollipop-appcompat-actionbar-custom-view-doesnt-take-up-whole-screen-w
        ViewGroup.LayoutParams lp = persistentSearchBar.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        persistentSearchBar.setLayoutParams(lp);


        // TODO: 11/06/16 Check how to make those initializations off the main thread.
        setupSearchFilterNavigation();

        if (savedInstanceState != null) {
            filterState = savedInstanceState.getParcelable("filtermodel");
        } else {
            filterState = new FilterState();
//            filterState.searchInsideCardText = cardsViewModel.getSearchTextCardPreference().get();
        }

        bind();

    }

    private void bind() {

        subscriptions.add(cardsViewModel.getSearchTextCardObservable()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean searchTextCard) {
                        Log.d(TAG, "bind: searchTextcard");
                        refreshDataNeeded = true;
                        persistentSearchBar.setSearchBarTextHint(searchTextCard ? R.string.search_bar_filter_card_name_and_card_text : R.string.search_bar_filter_card_name);
//                        filterState.searchInsideCardText = searchTextCard;
                    }
                }));

        subscriptions.add(cardsViewModel.getCardsImagesFolderObservable()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String ignored) {
                        Log.d(TAG, "bind: cardsImagesFolder");
                        refreshDataNeeded = true;
                    }
                }));



        Observable<Cursor> cryptCardsObservable = cardsViewModel.getCryptCards();
        Observable<Cursor> libraryCardsObservable = cardsViewModel.getLibraryCards();

        Observable<String> cryptTabTitle = cardsViewModel.getCryptTabTitle();
        Observable<String> libraryTabTitle = cardsViewModel.getLibraryTabTitle();




        subscriptions.add(cryptCardsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        viewPagerAdapter.setData(0, cursor);
                    }
                }));

        subscriptions.add(libraryCardsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        viewPagerAdapter.setData(1, cursor);
                    }
                }));


        subscriptions.add(cryptTabTitle
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        // Only go forward if the title is different.
                        // We avoid bothering Android main thread if it is not.
                        return !tabLayout.getTabAt(0).getText().equals(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "call: cryptTabTitle updated: " + s);
                        tabLayout.getTabAt(0).setText(s);
                    }
                })

        );

        subscriptions.add(libraryTabTitle
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !tabLayout.getTabAt(1).getText().equals(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "call: libraryTabTitle updated: " + s);
                        tabLayout.getTabAt(1).setText(s);

                    }
                })

        );


    }
//
//    private void updatePersistentSearchBarHint() {
//        persistentSearchBar.setSearchBarTextHint(cardsViewModel.getSearchTextCardPreference().get() ? R.string.search_bar_filter_card_name_and_card_text : R.string.search_bar_filter_card_name);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbind();


    }

    private void unbind() {
        subscriptions.unsubscribe();

    }

    private void setupSearchFilterNavigation() {

        cardFilters = (CardFilters) findViewById(R.id.cardFilters);

        subscriptions.add(RxTabLayout.selections(tabLayout)
                .skip(1) // Skip first emission on subscribe
                .subscribe(new Action1<TabLayout.Tab>() {
                    @Override
                    public void call(TabLayout.Tab tab) {
                        if (tab.getPosition() == 0) {
                            cardFilters.showCryptFilters();
                        } else {
                            cardFilters.showLibraryFilters();
                        }

                        updateSearchSettingsButtonState();
                    }
                }));


        cardFilters.setOnCardFiltersChangeListener(new CardFilters.OnCardFiltersChangeListener() {
            @Override
            public void onGroupsChanged(int group, boolean isChecked) {
                if (!restoring) {
                    filterState.setGroup(group, isChecked);
                    updateSearchSettingsButtonState();
                    filterCryptCards();
                }
            }

            @Override
            public void onCryptDisciplineChanged(String discipline, boolean isBasic, boolean isChecked) {
                if (!restoring) {
                    filterState.setDiscipline(discipline, isBasic, isChecked);
                    updateSearchSettingsButtonState();
                    filterCryptCards();
                }

            }

            @Override
            public void onClansChanged(String clan, boolean isChecked) {
                if (!restoring) {
                    filterState.setClan(clan, isChecked);
                    updateSearchSettingsButtonState();
                    filterCryptCards();
                }
            }

            @Override
            public void onCardTypeChanged(String cardType, boolean isChecked) {
                if (!restoring) {
                    filterState.setCardType(cardType, isChecked);
                    updateSearchSettingsButtonState();
                    filterLibraryCards();
                }
            }

            @Override
            public void onLibraryDisciplineChanged(String discipline, boolean isChecked) {
                if (!restoring) {
                    filterState.setLibraryDiscipline(discipline, isChecked);
                    updateSearchSettingsButtonState();
                    filterLibraryCards();
                }
            }

            @Override
            public void onCapacitiesChanged(int minCapacity, int maxCapacity) {
                if (!restoring) {
                    filterState.setCapacityMin(minCapacity);
                    filterState.setCapacityMax(maxCapacity);
                    updateSearchSettingsButtonState();
                    filterCryptCards();
                }
            }

            @Override
            public void onReset() {
                filterState.reset();
                updateSearchSettingsButtonState();
                filterCryptCards();
                filterLibraryCards();
            }


        });

    }

    void updateSearchSettingsButtonState() {

        boolean haveChanges = false;

        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                haveChanges = cardFilters.getNumberOfCryptFiltersApplied() > 0;
                break;
            case 1:
                haveChanges = cardFilters.getNumberOfLibraryFiltersApplied() > 0;
                break;
        }
        persistentSearchBar.updateChangesIndicator(haveChanges, ContextCompat.getColor(VampiDroid.this, R.color.colorAccent));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.d(TAG, "onRestoreInstanceState() called with: " + "savedInstanceState =");

        // To avoid refreshing data because of the searchbar text change listener.
        restoring = true;
        super.onRestoreInstanceState(savedInstanceState);
        restoring = false;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() called with: " + "outState = ");

        super.onSaveInstanceState(outState);

        outState.putParcelable("filtermodel", filterState);


    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");

        if (refreshDataNeeded) {
            Log.d(TAG, "onResume: refreshDataNeeded");
            filterCryptCards();
            filterLibraryCards();
            refreshDataNeeded = false;
        }

        // Sync navigation drawer selected item.
        // Reference: http://stackoverflow.com/questions/34502848/how-to-change-selected-item-in-the-navigation-drawer-depending-on-the-activity-v?rq=1

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_cards);

        updateSearchSettingsButtonState();

    }

    private void setupPersistentSearchBar() {

        subscriptions.add(RxTextView.textChanges(persistentSearchBar.getSearchBarTextView())
                .observeOn(Schedulers.computation())
                .skip(1) // Skip first emission when subscribing...
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        return !restoring;
                    }
                })
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        filterState.setName(charSequence);
                        filterCryptCards();
                        filterLibraryCards();

                    }
                })


        );



        persistentSearchBar.setSearchSettingsClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                SearchSettingsFragment searchSettingsFragment = SearchSettingsFragment.newInstance();
//                searchSettingsFragment.show(getSupportFragmentManager(), "search_settings_fragment");

                // Open right navigation view.
                drawerLayout.openDrawer(GravityCompat.END);


            }
        });

        persistentSearchBar.setHamburgerClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }


    void filterCryptCards() {

        Log.d(TAG, "filterCryptCards() called");
        cardsViewModel.filterCryptCards(filterState);
    }

    void filterLibraryCards() {

        Log.d(TAG, "filterLibraryCards() called");
        cardsViewModel.filterLibraryCards(filterState);

    }


    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (persistentSearchBar.isSearchBarTextFocused()) {
            persistentSearchBar.clearSearchBarTextFocus();
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

        if (id == R.id.nav_cards) {
            // Handle the camera action
        } else if (id == R.id.nav_settings) {
            Intent launch = new Intent(this, SettingsActivity.class);
            startActivity(launch);
            // Don't close the drawer when selecting settings.
            // It was causing stuttering when starting the Settings activity.
            closeDrawer = false;
        } else if (id == R.id.nav_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View dialogView = getLayoutInflater().inflate(R.layout.about, null);

            builder.setTitle("About")
                    .setView(dialogView)
                    .setPositiveButton("Close", null);

            builder.show();

        }


        if (closeDrawer) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }


    public void clearFilters(View v) {

        cardFilters.clearFilters();

    }

}



