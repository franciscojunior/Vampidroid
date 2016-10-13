package name.vampidroid.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;

import name.vampidroid.CryptCardsListViewAdapter;
import name.vampidroid.CursorRecyclerAdapter;
import name.vampidroid.DatabaseHelper;
import name.vampidroid.FilterModel;
import name.vampidroid.LibraryCardsListViewAdapter;
import name.vampidroid.R;

/**
 * Created by fxjr on 17/03/16.
 */
public class CardsListFragment extends Fragment {

    private final static String TAG = "CardsListFragment";


    private CharSequence filter;
    private int cardType;
    private String query;


    public CursorRecyclerAdapter getCardsAdapter() {
        return cardsAdapter;
    }

    private CursorRecyclerAdapter cardsAdapter;


    private SQLiteDatabase db;

    public CardsListFragment() {
        Log.d(TAG, "CardsListFragment constructor " + this);

        //db = DatabaseHelper.getDatabase();
        //Thread.dumpStack();

    }

    public static CardsListFragment newInstance(int cardType, String listQuery) {
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("CardType", cardType);
        args.putString("ListQuery", listQuery);

        CardsListFragment f = new CardsListFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        cardType = getArguments().getInt("CardType");
        query = getArguments().getString("ListQuery");

        if (cardType == 0)
            cardsAdapter = new CryptCardsListViewAdapter(getContext(), null);
        else
            cardsAdapter = new LibraryCardsListViewAdapter(getContext(), null);


        cardsAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            private static final String TAG = "CardsListFragment";

            @Override
            public Cursor runQuery(CharSequence constraint) {
                Log.d(TAG, "runQuery: Thread Id: " + Thread.currentThread().getId());
                filter = constraint;
                return DatabaseHelper.getDatabase().rawQuery(query + constraint, null);

            }

        });

        if (savedInstanceState != null) {
            filter = savedInstanceState.getCharSequence("filter");
        } else {
            filter = "";
        }

//        new QueryDatabaseOperation().execute(query + filter);

        // Initialize the adapter with the filter constraint.
        // This will be run in a different thread so it won't impact the UIThread

        cardsAdapter.getFilter().filter(filter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence("filter", filter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView... ");
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_cards_list, container, false);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        Log.d(TAG, "CardsListFragment: " + this);
        // specify an adapter (see also next example)

        recyclerView.setAdapter(cardsAdapter);

        return recyclerView;
    }

    public int getCardType() {
        return cardType;
    }

    public void filterCards(FilterModel filterModel) {

        switch (cardType) {

            case 0:
                cardsAdapter.getFilter().filter(filterModel.getNameFilterQuery() + filterModel.getCryptFilterQuery() + filterModel.getOrderBy());
                break;
            case 1:
                cardsAdapter.getFilter().filter(filterModel.getNameFilterQuery() + filterModel.getLibraryFilterQuery() + filterModel.getOrderBy());
                break;


        }
    }


    private class QueryDatabaseOperation extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... params) {


            Cursor c = db.rawQuery(params[0], null);


            return c;
        }

        @Override
        protected void onPostExecute(Cursor result) {

            Log.d(TAG, "onPostExecute... ");

            if (result != null) {

                cardsAdapter.changeCursor(result);
            }


        }
    }
}

