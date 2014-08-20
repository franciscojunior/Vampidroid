package name.vampidroid;

import java.util.Arrays;

import android.app.Application;

public class VampiDroidApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		DatabaseHelper.setApplicationContext(getApplicationContext());
		
		FilterModel.initFilterModel(
				Arrays.asList(getResources().getStringArray(R.array.clans)),
				Arrays.asList(getResources().getStringArray(R.array.types)),
				Arrays.asList(getResources().getStringArray(R.array.disciplineslibrary)),
				Arrays.asList(getResources().getStringArray(R.array.disciplinescrypt)));
		
		
		
	}

	
}
