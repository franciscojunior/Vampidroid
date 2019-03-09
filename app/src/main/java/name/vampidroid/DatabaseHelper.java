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
    public static final int DATABASE_VERSION = 8;

    public static final String MAIN_LIST_CRYPT_QUERY = "select uid, name, clan, advanced, capacity, `group`, disciplines from CryptCard where 1=1";

    public static final String MAIN_LIST_LIBRARY_QUERY = "select uid, name, type, clan, disciplines, poolCost, bloodCost, convictionCost from LibraryCard where 1=1";

    private static Context APPLICATION_CONTEXT;

    private static boolean TESTING = false;

    public static void setTesting(boolean value) {
        TESTING = value;
    }

    public static AppDatabase getRoomDatabase() {
//        Reference: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
//        Reference: http://stackoverflow.com/questions/7855700/why-is-volatile-used-in-this-example-of-double-checked-locking/36099644#36099644

        return LazyHolder.appDatabase;
    }

    private static class LazyHolder {

        private static final AppDatabase appDatabase = initializeRoomDatabase();

    }


    private static AppDatabase initializeRoomDatabase() {


        RoomDatabase.Builder<AppDatabase> builder = Room.databaseBuilder(APPLICATION_CONTEXT, AppDatabase.class, "vampidroid");

        if (TESTING) {
            builder = builder
                    .allowMainThreadQueries();
        }

        AppDatabase appDatabase = builder.addCallback(new RoomDatabase.Callback() {
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
                .addMigrations(MIGRATION_6_7)
                .addMigrations(MIGRATION_7_8)
                .build();

        return appDatabase;

    }

    /**
     * This migration class only replaces crypt and library card listings.
     * It is used as a base template for migrations which only do that.
     */
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

    // This is the migration to update card listings to Lost Kindred expansion
    static Migration MIGRATION_6_7 = new ReplaceCryptLibraryCardsMigration(6, 7);

    // This is the migration to update card listings to Sabbat Sets expansion
    static Migration MIGRATION_7_8 = new ReplaceCryptLibraryCardsMigration(7, 8);

    public static void updateDatabaseCardsRoom(SupportSQLiteDatabase database) {

        createUpdateDatabaseFile();


        database.execSQL(" attach '" + APPLICATION_CONTEXT.getFileStreamPath(VAMPIDROID_UPDATE_DB)
                .getAbsolutePath() + "' as updatedb");


        database.beginTransaction();

        try {
            database.delete("CryptCard", null, null);
            database.delete("LibraryCard", null, null);

            database.execSQL("insert into CryptCard(`uid`, `Name`, `Type`, `Clan`, `Advanced`, `Group`, `Capacity`, `Disciplines`, `Text`, `SetRarity`, `Artist`)  select `Id`, `Name`, `Type`, coalesce(`Clan`,''), coalesce(`Adv`,''), `Group`, `Capacity`, `Disciplines`, `CardText`, `Set`, `Artist` from updatedb.crypt");
            database.execSQL("insert into LibraryCard(`uid`, `Name`, `Type`, `Clan`, `Disciplines`, `PoolCost`, `BloodCost`, `ConvictionCost`, `Text`, `SetRarity`, `Artist`)  select `Id`, `Name`, `Type`, coalesce(`Clan`,''), coalesce(`Discipline`,''), coalesce(`PoolCost`,''), coalesce(`BloodCost`,''), coalesce(`ConvictionCost`,''), `CardText`, `Set`, `Artist` from updatedb.library");

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        // Delete update database file. It is not needed anymore.
        APPLICATION_CONTEXT.deleteFile(VAMPIDROID_UPDATE_DB);

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

