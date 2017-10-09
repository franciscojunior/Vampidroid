package name.vampidroid.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import name.vampidroid.DatabaseHelper;

/**
 * Created by francisco on 31/08/17.
 */

@Database(entities = {CryptCard.class, LibraryCard.class}, version = DatabaseHelper.DATABASE_VERSION)

public abstract class AppDatabase extends RoomDatabase {

    public abstract CryptCardDao cryptCardDao();

    public abstract LibraryCardDao libraryCardDao();
}
