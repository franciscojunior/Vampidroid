package name.vampidroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import name.vampidroid.fragments.SettingsFragment;

/**
 * Created by fxjr on 09/07/16.
 */

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
