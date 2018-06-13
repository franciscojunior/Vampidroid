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

    public void setLocalCardImagesFolder(String directoryPath) {
        preferenceRepository.setLocalCardImagesFolder(directoryPath);
    }

    public Observable<String> getLocalCardImagesFolderObservable() {
        return preferenceRepository.getLocalCardImagesFolderObservable();
    }

    public void setRemoteCardImagesFolder(String remoteServerPath) {
        preferenceRepository.setRemoteCardImagesFolder(remoteServerPath);
    }

    public Observable<String> getRemoteCardImagesFolderObservable() {
        return preferenceRepository.getRemoteCardImagesFolderObservable();

    }

    public Observable<Boolean> getUseLocalCardsSwitchObservable() {
        return preferenceRepository.getUseLocalFolderFlagObservable();
    }
}
