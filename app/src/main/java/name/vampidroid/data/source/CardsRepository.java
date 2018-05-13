package name.vampidroid.data.source;

import android.arch.persistence.db.SimpleSQLiteQuery;

import java.util.List;

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

        return DatabaseHelper.getRoomDatabase().cryptCardDao().getCardsByQuery(new SimpleSQLiteQuery(DatabaseHelper.MAIN_LIST_CRYPT_QUERY + filter));

    }

    public Flowable<List<LibraryCard>> getLibraryCards(final String filter) {

        return DatabaseHelper.getRoomDatabase().libraryCardDao().getCardsByQuery(new SimpleSQLiteQuery(DatabaseHelper.MAIN_LIST_LIBRARY_QUERY + filter));

    }

    public Flowable<LibraryCard> getLibraryCard(final long cardId) {

        return DatabaseHelper.getRoomDatabase().libraryCardDao().getById(cardId);
    }

    public Flowable<CryptCard> getCryptCard(long cardId) {

        return DatabaseHelper.getRoomDatabase().cryptCardDao().getById(cardId);

    }
}