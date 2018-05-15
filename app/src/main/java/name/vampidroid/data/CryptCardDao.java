package name.vampidroid.data;

import android.arch.paging.DataSource;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import io.reactivex.Flowable;

/**
 * Created by francisco on 31/08/17.
 */

@Dao
public abstract class CryptCardDao {

    @Query("select * from CryptCard where uid = :id")
    public abstract Flowable<CryptCard> getById(long id);

    @RawQuery(observedEntities = CryptCard.class)
    public abstract DataSource.Factory<Integer, CryptCard> getCardsByQuery(SupportSQLiteQuery query);

}
