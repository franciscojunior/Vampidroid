package name.vampidroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.LibraryCard;
import name.vampidroid.ui.widget.CardFilters;
import name.vampidroid.ui.widget.PersistentSearchBar;

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

    boolean refreshCardsListingNeeded = true;
    boolean refreshCardImagesNeeded = false;

    private CompositeDisposable subscriptions = new CompositeDisposable();


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
        }

        bind();

    }

    private void bind() {

        subscriptions.add(cardsViewModel.getSearchTextHintObservable()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer textHintResId) throws Exception {
                        persistentSearchBar.setSearchBarTextHint(textHintResId);
                    }
                }));

        subscriptions.add(cardsViewModel.getNeedRefreshCardsListing()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        refreshCardsListingNeeded = true;
                    }
                }));

        subscriptions.add(cardsViewModel.getNeedRefreshCardImages()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        refreshCardImagesNeeded = true;
                    }
                }));


        // Implement DiffUtil calculation off the main thread as per Erik Hellman's excellent article.
        // https://hellsoft.se/a-nice-combination-of-rxjava-and-diffutil-fe3807186012
        Pair<List<CryptCard>, DiffUtil.DiffResult> cryptCardsDiffResultInitialPair = Pair.create(null, null);

        subscriptions.add(cardsViewModel.getCryptCards()
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        viewPagerAdapter.setCryptData(null);
                    }
                })
                .observeOn(Schedulers.computation())
                .scan(cryptCardsDiffResultInitialPair, new BiFunction<Pair<List<CryptCard>, DiffUtil.DiffResult>, List<CryptCard>, Pair<List<CryptCard>, DiffUtil.DiffResult>>() {
                    @Override
                    public Pair<List<CryptCard>, DiffUtil.DiffResult> apply(Pair<List<CryptCard>, DiffUtil.DiffResult> listDiffResultPair, List<CryptCard> next) throws Exception {

                        CardsListViewAdapter.CardsListViewAdapterDiffCallback<CryptCard> diffCallback = new CardsListViewAdapter.CardsListViewAdapterDiffCallback<>(listDiffResultPair.first, next);
                        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback, false);

                        return new Pair<>(next, result);
                    }
                })
                .skip(1) // first emission of the scan will use the initial pair with null values. We don't need them.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Pair<List<CryptCard>, DiffUtil.DiffResult>>() {

                    @Override
                    public void accept(Pair<List<CryptCard>, DiffUtil.DiffResult> resultPair) throws Exception {
                        viewPagerAdapter.setCryptData(resultPair);

                    }
                }));

        Pair<List<LibraryCard>, DiffUtil.DiffResult> libraryCardsDiffResultInitialPair = Pair.create(null, null);

        subscriptions.add(cardsViewModel.getLibraryCards()
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        viewPagerAdapter.setLibraryData(null);
                    }
                })
                .observeOn(Schedulers.computation())
                .scan(libraryCardsDiffResultInitialPair, new BiFunction<Pair<List<LibraryCard>, DiffUtil.DiffResult>, List<LibraryCard>, Pair<List<LibraryCard>, DiffUtil.DiffResult>>() {
                    @Override
                    public Pair<List<LibraryCard>, DiffUtil.DiffResult> apply(Pair<List<LibraryCard>, DiffUtil.DiffResult> listDiffResultPair, List<LibraryCard> next) throws Exception {

                        CardsListViewAdapter.CardsListViewAdapterDiffCallback<LibraryCard> diffCallback = new CardsListViewAdapter.CardsListViewAdapterDiffCallback<>(listDiffResultPair.first, next);
                        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback, false);

                        return new Pair<>(next, result);
                    }
                })
                .skip(1) // first emission of the scan will use the initial pair with null values. We don't need them.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Pair<List<LibraryCard>, DiffUtil.DiffResult>>() {

                    @Override
                    public void accept(Pair<List<LibraryCard>, DiffUtil.DiffResult> resultPair) throws Exception {
                        viewPagerAdapter.setLibraryData(resultPair);

                    }
                }));


        subscriptions.add(cardsViewModel.getCryptTabTitle()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        // Only go forward if the title is different.
                        // We avoid bothering Android main thread if it is not.
                        return !tabLayout.getTabAt(0).getText().equals(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "call: cryptTabTitle updated: " + s);
                        tabLayout.getTabAt(0).setText(s);
                    }
                })

        );

        subscriptions.add(cardsViewModel.getLibraryTabTitle()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return !tabLayout.getTabAt(1).getText().equals(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        Log.d(TAG, "call: libraryTabTitle updated: " + s);
                        tabLayout.getTabAt(1).setText(s);

                    }
                })

        );


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbind();

    }

    private void unbind() {
        subscriptions.dispose();

    }

    private void setupSearchFilterNavigation() {

        cardFilters = (CardFilters) findViewById(R.id.cardFilters);

        subscriptions.add(RxTabLayout.selections(tabLayout)
                .skip(1) // Skip first emission on subscribe
                .subscribe(new Consumer<TabLayout.Tab>() {
                    @Override
                    public void accept(TabLayout.Tab tab) throws Exception {
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

        if (refreshCardsListingNeeded) {
            Log.d(TAG, "onResume: refreshCardsListingNeeded");
            filterCryptCards();
            filterLibraryCards();
            refreshCardsListingNeeded = false;
        }

        if (refreshCardImagesNeeded) {
            viewPagerAdapter.refreshCardImages();
            refreshCardImagesNeeded = false;
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
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        return !restoring;
                    }
                })
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) {
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



