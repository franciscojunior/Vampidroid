package name.vampidroid.data.source;


import android.os.Environment;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import io.reactivex.Observable;

/**
 * Created by fxjr on 03/01/17.
 */

public class PreferenceRepository {

    public static final String KEY_PREF_CARD_IMAGES_FOLDER = "pref_cardImagesFolder";

    public static final String KEY_PREF_SEARCH_CARD_TEXT = "pref_searchCardText";
    public static final String KEY_PREF_SHOW_CARDS_COUNT = "pref_showCardsCountTabHeader";


    private final Preference<Boolean> searchTextCard;
    private final Preference<String> cardImagesFolder;
    private final Preference<Boolean> showCardsCount;

    public PreferenceRepository(RxSharedPreferences sharedPreferences) {

        searchTextCard = sharedPreferences.getBoolean(KEY_PREF_SEARCH_CARD_TEXT, false);
        cardImagesFolder = sharedPreferences.getString(KEY_PREF_CARD_IMAGES_FOLDER, Environment.getExternalStorageDirectory().getAbsolutePath());
        showCardsCount = sharedPreferences.getBoolean(KEY_PREF_SHOW_CARDS_COUNT, false);
    }

    public Observable<Boolean> getSearchTextCardObservable() {

        return searchTextCard.asObservable();
    }


    public Observable<String> getCardsImagesFolderObservable() {
        return cardImagesFolder.asObservable();
    }

    public Observable<Boolean> getShowCardsCountObservable() {
        return showCardsCount.asObservable();
    }

    public void setCardsImagesFolder(String directoryPath) {
        cardImagesFolder.set(directoryPath);
    }
}
