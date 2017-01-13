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

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fxjr on 27/02/16.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    private final Context context;

    private final String[] noCountTitles = new String[] {"Crypt", "Library"};
    private String[] recyclerViewTitles = noCountTitles.clone();

    final CursorRecyclerAdapter[] recyclerViewsAdapters = new CursorRecyclerAdapter[2];
    private CompositeSubscription subscriptions;


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


    public void bind(final CardsViewModel cardsViewModel) {

        subscriptions = new CompositeSubscription();


        Observable<Cursor> cryptCardsObservable = cardsViewModel.getCryptCards();
        Observable<Cursor> libraryCardsObservable = cardsViewModel.getLibraryCards();

        Observable<String> cryptTabTitle = cardsViewModel.getCryptTabTitle();
        Observable<String> libraryTabTitle = cardsViewModel.getLibraryTabTitle();




        subscriptions.add(cryptCardsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        recyclerViewsAdapters[0].changeCursor(cursor);
//                        updateTabTitle(0, cardsViewModel.getShowCardsCount().get(), cursor.getCount());
                    }
                }));

        subscriptions.add(libraryCardsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        recyclerViewsAdapters[1].changeCursor(cursor);
//                        updateTabTitle(1, cardsViewModel.getShowCardsCount().get(), cursor.getCount());

                    }
                }));

//        subscriptions.add(cardsViewModel.getShowCardsCount().asObservable()
//                .skip(1) // skip first emission on subscribe, we don't have the cursors yet.
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Boolean>() {
//                    @Override
//                    public void call(Boolean showCount) {
//                        // TODO: 12/01/17 Is it better to keep the state in the observable itself, for example, calling the getShowCardsCount.get as above,
//                        // or should I keep this showCount state in a private field?
//                        Log.d(TAG, "bind: showCardsCount");
//                        updateTabTitle(0, showCount, recyclerViewsAdapters[0].getCursor().getCount());
//                        updateTabTitle(1, showCount, recyclerViewsAdapters[1].getCursor().getCount());
//                    }
//
//                }));


        subscriptions.add(cryptTabTitle
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "call: cryptTabTitle updated: " + s);
                        if (!recyclerViewTitles[0].equals(s)) {
                            recyclerViewTitles[0] = s;
                            notifyDataSetChanged();
                        }

                    }
                })

        );

        subscriptions.add(libraryTabTitle
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "call: libraryTabTitle updated: " + s);
                        if (!recyclerViewTitles[1].equals(s)) {
                            recyclerViewTitles[1] = s;
                            notifyDataSetChanged();
                        }

                    }
                })

        );


    }

    public void unbind() {
        subscriptions.unsubscribe();
    }
}