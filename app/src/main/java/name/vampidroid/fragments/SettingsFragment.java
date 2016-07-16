package name.vampidroid.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import name.vampidroid.R;

/**
 * Created by fxjr on 09/07/16.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
