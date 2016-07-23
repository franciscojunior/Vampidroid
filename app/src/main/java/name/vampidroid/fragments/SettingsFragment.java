package name.vampidroid.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import name.vampidroid.R;

/**
 * Created by fxjr on 09/07/16.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements DirectoryChooserFragment.DirectoryChooserFragmentListener {

    private static final String TAG = "SettingsFragment";
    public static final String KEY_PREF_CARD_IMAGES_FOLDER = "pref_cardImagesFolder";

    public static final String KEY_PREF_SEARCH_CARD_TEXT = "pref_searchCardText";

    public static final String DEFAULT_IMAGES_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String prefCardImagesFolder;




    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);


        Preference imagesFolderButton = findPreference(getString(R.string.pref_card_images_folder));
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefCardImagesFolder = sharedPref.getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, DEFAULT_IMAGES_FOLDER);

        imagesFolderButton.setSummary(prefCardImagesFolder);
        imagesFolderButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Log.d(TAG, "onPreferenceClick: ");


                DirectoryChooserFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(prefCardImagesFolder);
                directoryChooserFragment.setTargetFragment(SettingsFragment.this, 0);
                directoryChooserFragment.show(getFragmentManager(), "directorychooser");

                return false;

            }
        });

    }

    @Override
    public void onDirectorySelected(String directoryPath) {

        Log.d(TAG, "onDirectorySelected() called with: directoryPath = [" + directoryPath + "]");

        prefCardImagesFolder = directoryPath;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_PREF_CARD_IMAGES_FOLDER, prefCardImagesFolder);
        editor.commit();



        // Update settings UI to reflect the new selected directory.

        Preference imagesFolderButton = findPreference(getString(R.string.pref_card_images_folder));
        imagesFolderButton.setSummary(prefCardImagesFolder);

    }
}
