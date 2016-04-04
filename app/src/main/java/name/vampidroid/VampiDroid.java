package name.vampidroid;

import android.animation.Animator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;

import java.util.ArrayList;
import java.util.List;

import name.vampidroid.fragments.CardsListFragment;


public class VampiDroid extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private final static String TAG = "MainActivity";

	private ViewPager viewPager;
	private TabLayout tabLayout;
	private CryptCardsListViewAdapter cryptCardsListViewAdapter;
	private LibraryCardsListViewAdapter libraryCardsListViewAdapter;

	private List<CardsListFragment> fragmentsToFilter = new ArrayList<>();
	private SearchView searchView;
    private MenuItem searchMenuItem;


    @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate... ");


		setContentView(R.layout.activity_main);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		viewPager = (ViewPager) findViewById(R.id.viewpager);

		tabLayout = (TabLayout) findViewById(R.id.tablayout);



		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);



		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();

				// Reference: ﻿https://www.raywenderlich.com/103367/material-design
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
					int cx = viewPager.getRight() - 30;
					int cy = viewPager.getBottom() - 60;
					int finalRadius = Math.max(viewPager.getWidth(), viewPager.getHeight());
					Animator anim = ViewAnimationUtils.createCircularReveal(viewPager, cx, cy, 0, finalRadius);
					//view.setVisibility(View.VISIBLE);
					anim.start();

				}


				// Reference: http://stackoverflow.com/questions/11710042/expand-and-give-focus-to-searchview-automatically
                // Had to add collapseActionView flag
                MenuItemCompat.expandActionView(searchMenuItem);


			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);





	}

	private void setupSearchView(final SearchView searchView) {


		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {


				Log.d(TAG, "onQueryTextChange... ");

				for (CardsListFragment fragment:
						fragmentsToFilter) {

					Log.d(TAG, "onQueryTextChange: Thread Id: " + Thread.currentThread().getId());
					fragment.getCardsAdapter().getFilter().filter(newText);

				}

				return true;
			}
		});


	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		if (fragment instanceof CardsListFragment)
			fragmentsToFilter.add((CardsListFragment) fragment);
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
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);


		// Sets searchable configuration defined in searchable.xml for this SearchView
		SearchManager searchManager =
				(SearchManager) getSystemService(Context.SEARCH_SERVICE);


        searchMenuItem = menu.findItem(R.id.action_search);

		searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


		setupSearchView(searchView);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_search) {

		}



		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

        /*} else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {*/

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Log.d(TAG, "onNewIntent... ");

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {


		}


	}


}

