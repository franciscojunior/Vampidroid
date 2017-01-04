package name.vampidroid.data.source;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import name.vampidroid.fragments.SettingsFragment;
import rx.Observable;

/**
 * Created by fxjr on 03/01/17.
 */

public class PreferenceRepository {

    private final Preference<Boolean> searchTextCard;
    private final Preference<String> cardImagesFolder;

    public PreferenceRepository(RxSharedPreferences sharedPreferences) {

        searchTextCard = sharedPreferences.getBoolean(SettingsFragment.KEY_PREF_SEARCH_CARD_TEXT, false);
        cardImagesFolder = sharedPreferences.getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, SettingsFragment.DEFAULT_IMAGES_FOLDER);
    }

    public Preference<Boolean> getSearchTextCard() {
        return searchTextCard;
    }

    public Observable<Boolean> getSearchTextCardObservable() {

        return searchTextCard.asObservable();
    }


    public Preference<String> getCardsImagesFolder() {
        return cardImagesFolder;
    }

    public Observable<String> getCardsImagesFolderObservable() {
        return cardImagesFolder.asObservable();
    }
}
