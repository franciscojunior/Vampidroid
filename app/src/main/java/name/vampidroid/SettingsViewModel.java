package name.vampidroid;

import io.reactivex.Observable;
import name.vampidroid.data.source.PreferenceRepository;

/**
 * Created by FranciscoJunior on 16/12/2016.
 */

public class SettingsViewModel {

    private final PreferenceRepository preferenceRepository;

    public SettingsViewModel(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;

    }

    public void setCardsImagesFolder(String directoryPath) {
        preferenceRepository.setCardsImagesFolder(directoryPath);
    }

    public Observable<String> getCardsImagesFolderObservable() {
        return preferenceRepository.getCardsImagesFolderObservable();
    }
}
