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
import name.vampidroid.CursorRecyclerViewAdapter;
import name.vampidroid.DatabaseHelper;
import name.vampidroid.FilterModel;
import name.vampidroid.LibraryCardsListViewAdapter;
import name.vampidroid.R;
import name.vampidroid.VampiDroid;

/**
 * Created by fxjr on 17/03/16.
 */
public class CardsListFragment extends Fragment{

    private final static String TAG = "CardsListFragment";


    private String filter;
    private int cardType;
    private String query;


    public CursorRecyclerAdapter getCardsAdapter() {
        return cardsAdapter;
    }

    private CursorRecyclerAdapter cardsAdapter;


    private SQLiteDatabase db;

    private FilterModel filterModel = new FilterModel();

    public CardsListFragment() {
        Log.d(TAG, "CardsListFragment constructor " + this);

        db = DatabaseHelper.getDatabase();
        //Thread.dumpStack();

    }

    public static CardsListFragment newInstance(int cardType, String listQuery) {
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("CardType", cardType );
        args.putString("ListQuery", listQuery);

        CardsListFragment f = new CardsListFragment();
        f.setArguments(args);

        return f;
    }


    public void setFilter(String filter, String[] parameters) {
        this.filter = filter;

        Log.d(TAG, "CardsListFragment: " + this);

        Log.d(TAG, "setFilter... query:" +  query);

        Log.d(TAG, "setFilter... cardsAdapter:" +  cardsAdapter);


        Cursor c = db.rawQuery(query + filter, parameters);

        cardsAdapter.changeCursor(c);






    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardType = getArguments().getInt("CardType");
        query = getArguments().getString("ListQuery");

//        SQLiteDatabase db = DatabaseHelper.getDatabase();
//
//        Cursor c = db.rawQuery(query, null);

        if (cardType == 0)
            cardsAdapter = new CryptCardsListViewAdapter(null);
        else
            cardsAdapter = new LibraryCardsListViewAdapter(null);

        cardsAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            private static final String TAG = "CardsListFragment";

            @Override
            public Cursor runQuery(CharSequence constraint) {

                Log.d(TAG, "onQueryTextChange: Thread Id: " + Thread.currentThread().getId());
                Log.d(TAG, "onQueryTextChange: constraint: " + constraint);

                if (cardType == 0) {
                    filterModel.setCryptFilter(constraint.toString());
                    Log.d(TAG, "onQueryTextChange: cryptquery: " + filterModel.getCryptFilterQuery());

                }
                else {
                    filterModel.setLibraryFilter(constraint.toString());
                    Log.d(TAG, "onQueryTextChange: libraryquery: " + filterModel.getLibraryFilterQuery());
                }

                Log.d(TAG, "runQuery: test");

                return db.rawQuery(query + " and lower(name) like ?", new String[] {"%" + constraint.toString().toLowerCase() + "%"});
                //return db.rawQuery(query + filterModel.getCryptFilterQuery());

            }

        });

        new QueryDatabaseOperation().execute(query);

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
            else {

            }


        }
    }
}
