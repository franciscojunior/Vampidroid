package name.vampidroid;

import java.util.Arrays;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

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

		//new UpdateDatabaseOperation().execute();


		FilterModel.initFilterModel(
				Arrays.asList(getResources().getStringArray(R.array.clans)),
				Arrays.asList(getResources().getStringArray(R.array.types)),
				Arrays.asList(getResources().getStringArray(R.array.disciplineslibrary)),
				Arrays.asList(getResources().getStringArray(R.array.disciplinescrypt)));



	}



	
}
