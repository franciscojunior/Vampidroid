package name.vampidroid.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import name.vampidroid.R;
import name.vampidroid.SettingsViewModel;

/**
 * Created by fxjr on 09/07/16.
 */

//Reference: https://developer.android.com/guide/topics/ui/dialogs.html
//https://developer.android.com/reference/android/preference/DialogPreference.html
//Reference: https://github.com/codepath/android_guides/wiki/Using-DialogFragment
//Reference: https://github.com/codepath/android_guides/wiki/Settings-with-PreferenceFragment

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "SettingsFragment";

    public static final String DIRECTORY_CHOOSER_FRAGMENT_TAG = "directorychooser";
    private SettingsViewModel settingsViewModel;

    private CompositeDisposable subscriptions = new CompositeDisposable();
    private Preference localImagesFolderButton;
    private Preference remoteImagesFolderButton;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        setupCardImagesButtons();

        bind();

    }

    private void setupCardImagesButtons() {
        localImagesFolderButton = findPreference(getString(R.string.pref_local_card_images_folder));

        localImagesFolderButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Log.d(TAG, "onPreferenceClick: ");


                DirectoryChooserFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(localImagesFolderButton.getSummary().toString());
                directoryChooserFragment.setTargetFragment(SettingsFragment.this, 0);
                directoryChooserFragment.show(getFragmentManager(), DIRECTORY_CHOOSER_FRAGMENT_TAG);

                return true;

            }
        });

        remoteImagesFolderButton = findPreference(getString(R.string.pref_remote_card_images_folder));

        remoteImagesFolderButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Log.d(TAG, "onPreferenceClick: ");

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Remote card images folder");

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cards_image_input, null);

                // Set up the input
                final EditText input = viewInflated.findViewById(R.id.editTextServerURL);

                builder.setView(viewInflated);

                input.setText(remoteImagesFolderButton.getSummary());


                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingsViewModel.setRemoteCardImagesFolder(input.getText().toString());

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;

            }
        });
    }

    private void bind() {

        subscriptions.add(settingsViewModel.getLocalCardImagesFolderObservable()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String cardImagesFolderPath) throws Exception {
                        localImagesFolderButton.setSummary(cardImagesFolderPath);

                    }
                }));

        subscriptions.add(settingsViewModel.getRemoteCardImagesFolderObservable()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String cardImagesFolderPath) throws Exception {
                        remoteImagesFolderButton.setSummary(cardImagesFolderPath);

                    }
                }));

        // Enable/Disable local/remote card image buttons based on current useLocalFolder switch value.
        subscriptions.add(settingsViewModel.getUseLocalCardsSwitchObservable()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean useLocalFolder) throws Exception {

                        localImagesFolderButton.setEnabled(useLocalFolder);
                        remoteImagesFolderButton.setEnabled(!useLocalFolder);

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
}
