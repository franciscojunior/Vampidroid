package name.vampidroid;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SplashScreen extends FragmentActivity {

    private static final String TAG = "SplashScreen";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		startActivity(new Intent(this, VampiDroid.class));

		finish();
	}

}
