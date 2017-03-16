package name.vampidroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import name.vampidroid.R;
import name.vampidroid.SettingsViewModel;
import name.vampidroid.VampiDroidApplication;

/**
 * Created by fxjr on 09/07/16.
 */

//Reference: https://developer.android.com/guide/topics/ui/dialogs.html
//https://developer.android.com/reference/android/preference/DialogPreference.html
//Reference: https://github.com/codepath/android_guides/wiki/Using-DialogFragment
//Reference: https://github.com/codepath/android_guides/wiki/Settings-with-PreferenceFragment

public class SettingsFragment extends PreferenceFragmentCompat implements DirectoryChooserFragment.DirectoryChooserFragmentListener {

    private static final String TAG = "SettingsFragment";
    public static final String KEY_PREF_CARD_IMAGES_FOLDER = "pref_cardImagesFolder";

    public static final String KEY_PREF_SEARCH_CARD_TEXT = "pref_searchCardText";
    public static final String DEFAULT_IMAGES_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String KEY_PREF_SHOW_CARDS_COUNT = "pref_showCardsCountTabHeader";

    public static final String DIRECTORY_CHOOSER_FRAGMENT_TAG = "directorychooser";
    private SettingsViewModel settingsViewModel;

    private CompositeDisposable subscriptions = new CompositeDisposable();
    private Preference imagesFolderButton;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        settingsViewModel = ((VampiDroidApplication) getActivity().getApplication()).getSettingsViewModel();


        imagesFolderButton = findPreference(getString(R.string.pref_card_images_folder));

        imagesFolderButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Log.d(TAG, "onPreferenceClick: ");


                DirectoryChooserFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(imagesFolderButton.getSummary().toString());
                directoryChooserFragment.setTargetFragment(SettingsFragment.this, 0);
                directoryChooserFragment.show(getFragmentManager(), DIRECTORY_CHOOSER_FRAGMENT_TAG);

                return true;

            }
        });

        bind();

    }

    private void bind() {

        subscriptions.add(settingsViewModel.getCardsImagesFolderObservable()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String cardImagesFolderPath) throws Exception {
                        imagesFolderButton.setSummary(cardImagesFolderPath);

                    }
                }));

    }

    private void unbind() {
        subscriptions.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbind();
    }

    @Override
    public void onAttach(Context context) {

        // When rotating the device, if the DirectoryChooserFragment is being shown, it will have a reference to a previous, non valid, parent fragment.
        // This gives problem of leaks and exceptions in saveInstanceState.
        // So, refresh this reference if the DirectoryChooserFragment is being shown.

        super.onAttach(context);

        DirectoryChooserFragment fragment = (DirectoryChooserFragment) getFragmentManager().findFragmentByTag(DIRECTORY_CHOOSER_FRAGMENT_TAG);

        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
        }


    }


    @Override
    public void onDirectorySelected(String directoryPath) {


        Log.d(TAG, "onDirectorySelected() called with: directoryPath = [" + directoryPath + "]");

        settingsViewModel.setCardsImagesFolder(directoryPath);

    }

}
