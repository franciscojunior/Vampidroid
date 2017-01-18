package name.vampidroid;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fxjr on 27/02/16.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    private final Context context;

    private final String[] recyclerViewTitles = new String[] {"Crypt", "Library"};

    private final CursorRecyclerAdapter[] recyclerViewsAdapters = new CursorRecyclerAdapter[2];


    public ViewPagerAdapter(Context context) {
        this.context = context;

        recyclerViewsAdapters[0] = new CryptCardsListViewAdapter(context, null);
        recyclerViewsAdapters[1] = new LibraryCardsListViewAdapter(context, null);
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

        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.fragment_cards_list, container, false);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // specify an adapter (see also next example)

        recyclerView.setAdapter(recyclerViewsAdapters[position]);

        container.addView(recyclerView);

        return recyclerView;

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

    public void setData(int position, Cursor data) {
        recyclerViewsAdapters[position].changeCursor(data);
    }


}