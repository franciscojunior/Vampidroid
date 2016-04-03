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

import name.vampidroid.CryptCardsListViewAdapter;
import name.vampidroid.CursorRecyclerViewAdapter;
import name.vampidroid.DatabaseHelper;
import name.vampidroid.LibraryCardsListViewAdapter;
import name.vampidroid.R;
import name.vampidroid.VampiDroid;

/**
 * Created by fxjr on 17/03/16.
 */
public class CardsListFragment extends Fragment implements VampiDroid.FragmentFilterable{

    private final static String TAG = "CardsListFragment";


    private String filter;
    private int cardType;
    private String query;


    public CursorRecyclerViewAdapter getCardsAdapter() {
        return cardsAdapter;
    }

    private CursorRecyclerViewAdapter cardsAdapter;


    private SQLiteDatabase db;

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
            cardsAdapter = new CryptCardsListViewAdapter(getContext(), null);
        else
            cardsAdapter = new LibraryCardsListViewAdapter(getContext(), null);


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
