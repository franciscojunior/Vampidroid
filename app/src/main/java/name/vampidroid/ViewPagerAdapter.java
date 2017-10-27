package name.vampidroid;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import name.vampidroid.data.Card;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.LibraryCard;

/**
 * Created by fxjr on 27/02/16.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    private final Context context;

    private final String[] recyclerViewTitles = new String[] {"Crypt", "Library"};

    private final CardsListViewAdapter[] recyclerViewsAdapters = new CardsListViewAdapter[2];

    public ViewPagerAdapter(Context context) {
        this.context = context;

        recyclerViewsAdapters[0] = new CryptCardsListViewAdapter(null);
        recyclerViewsAdapters[1] = new LibraryCardsListViewAdapter(null);

    }

    @Override
    public int getCount() {
        return recyclerViewTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View v = LayoutInflater.from(context).inflate(R.layout.fragment_cards_list, container, false);


        RecyclerView recyclerView = v.findViewById(R.id.my_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // specify an adapter (see also next example)

        recyclerView.setAdapter(recyclerViewsAdapters[position]);

        container.addView(v);

        return v;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroyItem() called with: container = [" + container + "], position = [" + position + "], object = [" + object + "]");
        container.removeViewAt(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return recyclerViewTitles[position];
    }

    public void setCryptData(Pair<List<CryptCard>, DiffUtil.DiffResult> dataPair) {
        recyclerViewsAdapters[0].setCardList(dataPair.first);
        dataPair.second.dispatchUpdatesTo(recyclerViewsAdapters[0]);
    }

    public void setLibraryData(Pair<List<LibraryCard>, DiffUtil.DiffResult> dataPair) {
        recyclerViewsAdapters[1].setCardList(dataPair.first);
        dataPair.second.dispatchUpdatesTo(recyclerViewsAdapters[1]);
    }

    /**
     * This method is used to reload the data inside the pages (in this case, recyclerviews) when
     * the images need to be reloaded. This is done by forcing the recyclerview to repopulate the data from the adapter.
     */
    public void refreshCardImages() {



//        LinearLayoutManager cryptLayoutManager = (LinearLayoutManager) recyclerViews[0].getLayoutManager();
//        LinearLayoutManager libraryLayoutManager = (LinearLayoutManager) recyclerViews[1].getLayoutManager();
//
//
//        for (int i = cryptLayoutManager.findFirstVisibleItemPosition(); i < cryptLayoutManager.findLastVisibleItemPosition(); i++) {
//            recyclerViewsAdapters[0].notifyItemChanged(i);
//        }
//
//        for (int i = libraryLayoutManager.findFirstVisibleItemPosition(); i < libraryLayoutManager.findLastVisibleItemPosition(); i++) {
//            recyclerViewsAdapters[1].notifyItemChanged(i);
//        }

        // TODO: 24/10/17 Check if it is worthwhile to implement it differently.
        // As the number of items in the list is not big, it is simpler to rebind all data
        // in order to reload the card image. Also, the code readability is better.

        recyclerViewsAdapters[0].notifyDataSetChanged();
        recyclerViewsAdapters[1].notifyDataSetChanged();

    }

}