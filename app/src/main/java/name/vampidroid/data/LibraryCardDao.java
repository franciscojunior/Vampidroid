package name.vampidroid.data;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by francisco on 11/09/17.
 */

@Dao
public abstract class LibraryCardDao {

    @Query("select * from LibraryCard where uid = :id")
    public abstract Flowable<LibraryCard> getById(long id);

    @RawQuery(observedEntities = LibraryCard.class)
    public abstract Flowable<List<LibraryCard>> getCardsByQuery(SupportSQLiteQuery query);

}
