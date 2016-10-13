package name.vampidroid;

import android.app.Application;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import name.vampidroid.fragments.SettingsFragment;

import static name.vampidroid.fragments.SettingsFragment.DEFAULT_IMAGES_FOLDER;

public class VampiDroidApplication extends Application {

    private static final String TAG = "VampiDroidApplication";

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.d(TAG, "finishing application");
        DatabaseHelper.closeDatabase();
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        Log.d(TAG, "starting application");
        DatabaseHelper.setApplicationContext(getApplicationContext());

        Utils.setResources(getResources());
        Utils.setCardImagesPath(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, DEFAULT_IMAGES_FOLDER));
        //new UpdateDatabaseOperation().execute();


//		FilterModel.initFilterModel(
//				Arrays.asList(getResources().getStringArray(R.array.clans)),
//				Arrays.asList(getResources().getStringArray(R.array.types)),
//				Arrays.asList(getResources().getStringArray(R.array.disciplineslibrary)),
//				Arrays.asList(getResources().getStringArray(R.array.disciplinescrypt)));
//


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Utils.disciplineDrawablesCache.evictAll();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Utils.disciplineDrawablesCache.evictAll();
    }
}
