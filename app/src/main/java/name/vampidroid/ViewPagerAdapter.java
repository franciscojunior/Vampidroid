package name.vampidroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import name.vampidroid.fragments.CryptCardsFragment;
import name.vampidroid.fragments.LibraryCardsFragment;

/**
 * Created by fxjr on 27/02/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "ViewPagerAdapter";

    private final String[] recyclerViewTitles = new String[] {"Crypt", "Library"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return recyclerViewTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new CryptCardsFragment();
            case 1: return new LibraryCardsFragment();
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return recyclerViewTitles[position];
    }


//    /**
//     * This method is used to reload the data inside the pages (in this case, recyclerviews) when
//     * the images need to be reloaded. This is done by forcing the recyclerview to repopulate the data from the adapter.
//     */
//    public void refreshCardImages() {
//
//
//
////        LinearLayoutManager cryptLayoutManager = (LinearLayoutManager) recyclerViews[0].getLayoutManager();
////        LinearLayoutManager libraryLayoutManager = (LinearLayoutManager) recyclerViews[1].getLayoutManager();
////
////
////        for (int i = cryptLayoutManager.findFirstVisibleItemPosition(); i < cryptLayoutManager.findLastVisibleItemPosition(); i++) {
////            recyclerViewsAdapters[0].notifyItemChanged(i);
////        }
////
////        for (int i = libraryLayoutManager.findFirstVisibleItemPosition(); i < libraryLayoutManager.findLastVisibleItemPosition(); i++) {
////            recyclerViewsAdapters[1].notifyItemChanged(i);
////        }
//
//        // TODO: 24/10/17 Check if it is worthwhile to implement it differently.
//        // As the number of items in the list is not big, it is simpler to rebind all data
//        // in order to reload the card image. Also, the code readability is better.
//
//        cryptCardsListViewAdapter.notifyDataSetChanged();
//        libraryCardsListViewAdapter.notifyDataSetChanged();
//
//    }
}