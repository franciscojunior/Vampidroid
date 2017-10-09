package name.vampidroid;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import name.vampidroid.data.AppDatabase;

public class DatabaseHelper {

    private final static String TAG = "DatabaseHelper";
    public static final String VAMPIDROID_UPDATE_DB = "VampiDroid.update.db";
    public static final int DATABASE_VERSION = 6;

    public static final String MAIN_LIST_CRYPT_QUERY = "select uid, Name, Clan, Advanced, Capacity, `group`, Disciplines from CryptCard where 1=1";

    public static final String MAIN_LIST_LIBRARY_QUERY = "select uid, Name, Type, Clan, Disciplines, PoolCost, BloodCost, ConvictionCost from LibraryCard where 1=1";

    private static Context APPLICATION_CONTEXT;

    public static AppDatabase getRoomDatabase() {
//        Reference: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
//        Reference: http://stackoverflow.com/questions/7855700/why-is-volatile-used-in-this-example-of-double-checked-locking/36099644#36099644

        return LazyHolder.appDatabase;
    }

    private static class LazyHolder {

        private static final AppDatabase appDatabase = initializeRoomDatabase();

    }


    private static AppDatabase initializeRoomDatabase() {

        AppDatabase appDatabase = Room.databaseBuilder(APPLICATION_CONTEXT, AppDatabase.class, "vampidroid")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Log.d(TAG, "onCreate: databaseroom");
                        db.setTransactionSuccessful();
                        db.endTransaction();

                        DatabaseHelper.updateDatabaseCardsRoom(db);

                        db.beginTransaction();
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                        db.execSQL("PRAGMA case_sensitive_like = true;");

                    }
                })
                .build();

        return appDatabase;

    }

    static class ReplaceCryptLibraryCardsMigration extends Migration {
        /**
         * Creates a new migration between {@code startVersion} and {@code endVersion}.
         *
         * @param startVersion The start version of the database.
         * @param endVersion   The end version of the database after this migration is applied.
         */
        public ReplaceCryptLibraryCardsMigration(int startVersion, int endVersion) {
            super(startVersion, endVersion);
        }

        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.setTransactionSuccessful();
            database.endTransaction();

            DatabaseHelper.updateDatabaseCardsRoom(database);

            database.beginTransaction();

        }
    }

    public static void updateDatabaseCardsRoom(SupportSQLiteDatabase database) {

        createUpdateDatabaseFile();


        database.execSQL(" attach '" + APPLICATION_CONTEXT.getFileStreamPath(VAMPIDROID_UPDATE_DB)
                .getAbsolutePath() + "' as updatedb");


        database.beginTransaction();

        try {

            database.execSQL("update sqlite_sequence set seq = 0 where name = 'CryptCard'");
            database.execSQL("update sqlite_sequence set seq = 0 where name = 'LibraryCard'");

            database.delete("CryptCard", null, null);
            database.delete("LibraryCard", null, null);

            database.execSQL("insert into CryptCard(Name , Type , Clan , Advanced, `Group`, Capacity , Disciplines , Text , SetRarity , Artist)  select Name , Type , Clan , Adv , _Group , Capacity , Disciplines , CardText , _Set , Artist from updatedb.crypt");
            database.execSQL("insert into LibraryCard(Name , Type , Clan , Disciplines , PoolCost , BloodCost, ConvictionCost, Text , SetRarity , Artist)  select Name , Type , Clan , Discipline , PoolCost , BloodCost, ConvictionCost, CardText , _Set, Artist from updatedb.library");


            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }


    }

    static public void setApplicationContext(Context context) {
        APPLICATION_CONTEXT = context.getApplicationContext();
    }

    public static void closeDatabase() {
        LazyHolder.appDatabase.close();
    }

    private static void createUpdateDatabaseFile() {

        createDatabaseFile("VampiDroid.mp3", VAMPIDROID_UPDATE_DB);

    }


    private static void createDatabaseFile(String databaseFileNameSource, String databaseFileNameDestination) {

        AssetManager am = APPLICATION_CONTEXT.getAssets();

        try {

            // Asset Manager doesn't work with files bigger than 1Mb at a time.
            // Check here for explanation:
            // http://stackoverflow.com/questions/2860157/load-files-bigger-than-1m-from-assets-folder
            // Had to change the file suffix to .mp3 so it isn't compressed and
            // can be opened directly.

            //InputStream in = am.open("VampiDroid.mp3");
            InputStream in = am.open(databaseFileNameSource);

            OutputStream out = APPLICATION_CONTEXT.openFileOutput(databaseFileNameDestination, Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();

            out.flush();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

}

