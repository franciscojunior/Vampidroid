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
    private final Preference<Boolean> showCardsCount;

    public PreferenceRepository(RxSharedPreferences sharedPreferences) {

        searchTextCard = sharedPreferences.getBoolean(SettingsFragment.KEY_PREF_SEARCH_CARD_TEXT, false);
        cardImagesFolder = sharedPreferences.getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, SettingsFragment.DEFAULT_IMAGES_FOLDER);
        showCardsCount = sharedPreferences.getBoolean(SettingsFragment.KEY_PREF_SHOW_CARDS_COUNT, false);
    }

    public boolean shouldSearchTextCard() {
        return searchTextCard.get().booleanValue();
    }

    public Observable<Boolean> getSearchTextCardObservable() {

        return searchTextCard.asObservable();
    }

    public Observable<String> getCardsImagesFolderObservable() {
        return cardImagesFolder.asObservable();
    }

    public boolean shouldShowCardsCount() {
        return showCardsCount.get().booleanValue();
    }

    public Observable<Boolean> getShowCardsCountObservable() {
        return showCardsCount.asObservable();
    }

    public void setCardsImagesFolder(String directoryPath) {
        cardImagesFolder.set(directoryPath);
    }
}
