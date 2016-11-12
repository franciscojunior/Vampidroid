package name.vampidroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import name.vampidroid.DatabaseHelper;
import name.vampidroid.fragments.CardsListFragment;

/**
 * Created by fxjr on 27/02/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "ViewPagerAdapter";

    private final Object[] fragments = new Object[2];
    private final String[] fragmentTitles = new String[]{"Crypt", "Library"};

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }


    // This is called when viewpager wants a new fragment and it isn't found in the fragment manager.
    // i.e. When the activity is restarted.
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: ...");
        if (position == 0)
            return CardsListFragment.newInstance(0, DatabaseHelper.ALL_FROM_CRYPT_QUERY);
        else
            return CardsListFragment.newInstance(1, DatabaseHelper.ALL_FROM_LIBRARY_QUERY);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        fragments[position] = super.instantiateItem(container, position);

        return fragments[position];
    }

    public Object getCachedItem(int position) {
        return fragments[position];
    }


    @Override
    public int getCount() {
        return fragmentTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles[position];
    }
}
