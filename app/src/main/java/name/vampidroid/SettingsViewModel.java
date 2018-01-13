package name.vampidroid;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import io.reactivex.Observable;
import name.vampidroid.data.source.PreferenceRepository;

/**
 * Created by FranciscoJunior on 16/12/2016.
 */

public class SettingsViewModel extends AndroidViewModel {

    private final PreferenceRepository preferenceRepository;

    public SettingsViewModel(Application application) {
        super(application);
        this.preferenceRepository = ((VampiDroidApplication) application).getPreferenceRepository();

    }

    public void setCardsImagesFolder(String directoryPath) {
        preferenceRepository.setCardsImagesFolder(directoryPath);
    }

    public Observable<String> getCardsImagesFolderObservable() {
        return preferenceRepository.getCardsImagesFolderObservable();
    }
}
