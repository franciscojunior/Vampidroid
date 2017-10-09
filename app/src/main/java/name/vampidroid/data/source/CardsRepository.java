package name.vampidroid.data.source;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import name.vampidroid.DatabaseHelper;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.CryptCardDao;
import name.vampidroid.data.LibraryCard;
import name.vampidroid.data.LibraryCardDao;

/**
 * Created by fxjr on 03/01/17.
 */

public class CardsRepository {

    private static final String TAG = "CardsRepository";


    public Flowable<List<CryptCard>> getCryptCards(final String filter) {

        return Flowable.fromCallable(new Callable<List<CryptCard>>() {
            @Override
            public List<CryptCard> call() throws Exception {
//                Log.d(TAG, "call: Thread Id: " + Thread.currentThread().getId());
//                Log.d(TAG, "call: Thread Name: " + Thread.currentThread().getName());
//                return DatabaseHelper.getDatabase().rawQuery(DatabaseHelper.MAIN_LIST_CRYPT_QUERY + filter, null);

                return CryptCardDao.convertCursorToCryptCardList(DatabaseHelper.getRoomDatabase().query(DatabaseHelper.MAIN_LIST_CRYPT_QUERY + filter, null));
            }
        });
    }


    public Flowable<List<LibraryCard>> getLibraryCards(final String filter) {

        return Flowable.fromCallable(new Callable<List<LibraryCard>>() {
            @Override
            public List<LibraryCard> call() throws Exception {
//                Log.d(TAG, "call: Thread Id: " + Thread.currentThread().getId());
//                Log.d(TAG, "call: Thread Name: " + Thread.currentThread().getName());
//                return DatabaseHelper.getDatabase().rawQuery(DatabaseHelper.MAIN_LIST_CRYPT_QUERY + filter, null);

                return LibraryCardDao.convertCursorToLibraryCardList(DatabaseHelper.getRoomDatabase().query(DatabaseHelper.MAIN_LIST_LIBRARY_QUERY + filter, null));
            }
        });
    }

    public Flowable<LibraryCard> getLibraryCard(final long cardId) {

        return DatabaseHelper.getRoomDatabase().libraryCardDao().getById(cardId);
    }

    public Flowable<CryptCard> getCryptCard(long cardId) {
        return DatabaseHelper.getRoomDatabase().cryptCardDao().getById(cardId);
    }
}