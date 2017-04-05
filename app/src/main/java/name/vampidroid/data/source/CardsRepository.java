package name.vampidroid.data.source;

import android.database.Cursor;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import name.vampidroid.DatabaseHelper;
import name.vampidroid.data.CryptCard;

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

    public Observable<CryptCard> getCryptCard(final long cardId) {

        return Observable
                .fromCallable(new Callable<CryptCard>() {
                    @Override
                    public CryptCard call() throws Exception {
                        String query;

                        query = "select Name, Type, Clan, Disciplines, CardText, Capacity, Artist, _Set, _Group, Adv from crypt where _id = ?";;

                        Cursor c = DatabaseHelper.getDatabase().rawQuery(query, new String[]{String.valueOf(cardId)});
                        c.moveToFirst();

                        CryptCard cryptCard = new CryptCard(
                                c.getString(0),
                                c.getString(1),
                                c.getString(2),
                                c.getString(3),
                                c.getString(4),
                                c.getString(5),
                                c.getString(6),
                                c.getString(7),
                                c.getString(8),
                                c.getString(9)
                        );

                        c.close();
                        return cryptCard;

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