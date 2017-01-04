package name.vampidroid;

import com.f2prateek.rx.preferences.Preference;

import name.vampidroid.data.source.PreferenceRepository;

/**
 * Created by FranciscoJunior on 16/12/2016.
 */

public class SettingsViewModel {

    private final PreferenceRepository preferenceRepository;

    public SettingsViewModel(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;

    }


    public Preference<String> getCardsImagesFolderPreference() {
        return preferenceRepository.getCardsImagesFolder();
    }


}
