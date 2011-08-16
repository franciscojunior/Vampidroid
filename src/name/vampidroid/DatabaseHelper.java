package name.vampidroid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

public class DatabaseHelper {
	
	public static final String VAMPIDROID_DB = "VampiDroid.db";
	public static final String KEY_DATABASE_VERSION = "database_version";
	public static final int DATABASE_VERSION = 2;
	public static final String[] STRING_ARRAY_NAME_DISCIPLINES_CAPACITY_INITIALCARDTEXT = new String[] {
			"Name", "Disciplines", "Capacity", "InitialCardText" };
	static final String[] STRING_ARRAY_NAME_DISCIPLINES_CAPACITY_INITIALCARDTEXT_ADV = new String[] {
			"Name", "Disciplines", "Capacity", "InitialCardText", "Adv" };

	public static final String ALL_FROM_CRYPT_QUERY = "select _id, case when length(Adv) > 0 then 'Adv.' || ' ' || Name else Name end as Name, Disciplines, Capacity, substr(CardText, 1, 40) as InitialCardText from crypt where 1=1";

	public static final String[] ALL_FROM_CRYPT_QUERY_AS_COLUMNS = new String[] {
			"_id", "Name", "Disciplines", "Capacity",
			"substr(CardText, 1, 40) as InitialCardText" };
	public static final String ALL_FROM_LIBRARY_QUERY = "select _id, Name, Type, Clan, Discipline from library where 1=1";

	public static final String[] STRING_ARRAY_NAME_DISCIPLINES_CAPACITY = new String[] {
			"Name", "Disciplines", "Capacity" };

	public static SQLiteDatabase DATABASE = null;


	public static SQLiteDatabase getDatabase(Context context) {
		/*
		 * File databaseFile = context.getFileStreamPath(VAMPIDROID_DB); return
		 * SQLiteDatabase.openDatabase(databaseFile.getAbsolutePath(), null,
		 * SQLiteDatabase.OPEN_READWRITE);
		 */
	
		if (DATABASE == null) {
			checkAndCreateDatabaseFile(context);
			DATABASE = SQLiteDatabase.openDatabase(
					context.getFileStreamPath(VAMPIDROID_DB).getAbsolutePath(),
					null, SQLiteDatabase.OPEN_READWRITE);
	
		}
	
		return DATABASE;
	}
	
	
	public static void closeDatabase() {
		if (DATABASE != null) {
			DATABASE.close();
			DATABASE = null;
		}

	}


	private static void checkAndCreateDatabaseFile(Context context) {
	
		// The file will be stored in the private data area of the application.
		// Reference:
		// http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
		File databaseFile = context.getFileStreamPath(VAMPIDROID_DB);
	
		// version where changelog has been viewed
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		int databaseVersion = settings.getInt(KEY_DATABASE_VERSION, 1);
	
		if (databaseVersion < DATABASE_VERSION || !databaseFile.exists()) {
			createDatabaseFile(context);
	
			Editor editor = settings.edit();
			editor.putInt(KEY_DATABASE_VERSION, DATABASE_VERSION);
			editor.commit();
	
		}
	
	}


	private static void createDatabaseFile(Context context) {
	
		AssetManager am = context.getAssets();
	
		try {
	
			// Asset Manager doesn't work with files bigger than 1Mb at a time.
			// Check here for explanation:
			// http://stackoverflow.com/questions/2860157/load-files-bigger-than-1m-from-assets-folder
			// Had to change the file suffix to .mp3 so it isn't compressed and
			// can be opened directly.
	
			InputStream in = am.open("VampiDroid.mp3");
	
			OutputStream out = context.openFileOutput(VAMPIDROID_DB,
					Context.MODE_PRIVATE);
	
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
