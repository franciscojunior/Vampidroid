package name.vampidroid.data.source;

import android.database.Cursor;
import android.util.Log;

import java.util.concurrent.Callable;

import name.vampidroid.DatabaseHelper;
import rx.Observable;
import rx.schedulers.Schedulers;

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

}