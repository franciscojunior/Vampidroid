package name.vampidroid.data.source;

import android.database.Cursor;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import name.vampidroid.DatabaseHelper;

/**
 * Created by fxjr on 03/01/17.
 */

public class CardsRepository {

    private static final String TAG = "CardsRepository";


    public Observable<Cursor> getCryptCards(final String filter) {

        return Observable.fromCallable(new Callable<Cursor>() {
            @Override
            public Cursor call() throws Exception {
//                Log.d(TAG, "call: Thread Id: " + Thread.currentThread().getId());
//                Log.d(TAG, "call: Thread Name: " + Thread.currentThread().getName());
                return DatabaseHelper.getDatabase().rawQuery(DatabaseHelper.ALL_FROM_CRYPT_QUERY + filter, null);
            }
        })
                .subscribeOn(Schedulers.io()); // We want this call to go through the io.
    }




    public Observable<Cursor> getLibraryCards(final String filter) {

        return Observable
                .fromCallable(new Callable<Cursor>() {
                    @Override
                    public Cursor call() throws Exception {
//                        Log.d(TAG, "call: Thread Id: " + Thread.currentThread().getId());
//                        Log.d(TAG, "call: Thread Name: " + Thread.currentThread().getName());
                        return DatabaseHelper.getDatabase().rawQuery(DatabaseHelper.ALL_FROM_LIBRARY_QUERY + filter, null);

                    }
                })
                .subscribeOn(Schedulers.io()); // We want this call to go through the io.
    }

    public Observable<Cursor> getCryptCard(final long cardId) {

        return Observable
                .fromCallable(new Callable<Cursor>() {
                    @Override
                    public Cursor call() throws Exception {
                        String query;

                        query = "select Name, Type, Clan, Disciplines, CardText, Capacity, Artist, _Set, _Group, Adv from crypt where _id = ?";;

                        Cursor c = DatabaseHelper.getDatabase().rawQuery(query, new String[]{String.valueOf(cardId)});
                        c.moveToFirst();
                        return c;

                    }
                })
                .subscribeOn(Schedulers.io()); // We want this call to go through the io.


    }

    public Observable<Cursor> getLibraryCard(final long cardId) {
        return Observable
                .fromCallable(new Callable<Cursor>() {
                    @Override
                    public Cursor call() throws Exception {
                        String query;

                        query = "select Name, Type, Clan, Discipline, CardText, PoolCost, BloodCost, Artist, _Set from library where _id = ?";

                        Cursor c = DatabaseHelper.getDatabase().rawQuery(query, new String[]{String.valueOf(cardId)});
                        c.moveToFirst();
                        return c;

                    }
                })
                .subscribeOn(Schedulers.io()); // We want this call to go through the io.

    }
}