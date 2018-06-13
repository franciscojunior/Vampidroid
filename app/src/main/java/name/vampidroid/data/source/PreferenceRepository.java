package name.vampidroid.data.source;


import android.os.Environment;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by fxjr on 03/01/17.
 */

public class PreferenceRepository {

    public static final String KEY_PREF_CARD_IMAGES_FOLDER = "pref_cardImagesFolder";
    public static final String KEY_PREF_REMOTE_CARD_IMAGES_FOLDER = "pref_remoteCardImagesFolder";
    public static final String KEY_PREF_USE_LOCAL_CARD_IMAGES_FOLDER = "pref_useLocalCardImagesFolder";

    public static final String KEY_PREF_SEARCH_CARD_TEXT = "pref_searchCardText";
    public static final String KEY_PREF_SHOW_CARDS_COUNT = "pref_showCardsCountTabHeader";


    private final Preference<Boolean> searchTextCard;
    private final Preference<String> cardImagesFolder;
    private final Preference<Boolean> showCardsCount;
    private final Preference<Boolean> useLocalCardsPathSwitch;
    private final Preference<String> remoteCardImagesFolder;


    public PreferenceRepository(RxSharedPreferences sharedPreferences) {

        searchTextCard = sharedPreferences.getBoolean(KEY_PREF_SEARCH_CARD_TEXT, false);
        cardImagesFolder = sharedPreferences.getString(KEY_PREF_CARD_IMAGES_FOLDER, Environment.getExternalStorageDirectory().getAbsolutePath());
        remoteCardImagesFolder = sharedPreferences.getString(KEY_PREF_REMOTE_CARD_IMAGES_FOLDER, "");
        showCardsCount = sharedPreferences.getBoolean(KEY_PREF_SHOW_CARDS_COUNT, false);
        useLocalCardsPathSwitch = sharedPreferences.getBoolean(KEY_PREF_USE_LOCAL_CARD_IMAGES_FOLDER, true);

    }

    public Observable<Boolean> getSearchTextCardObservable() {

        return searchTextCard.asObservable();
    }


    public Observable<String> getCardImagesFolderObservable() {


        // Use the local cards image folder or the network depending on the preference setting.
        // Also, emit when the preference switch to use local folder or network folder is changed too.

        return cardImagesFolder.asObservable()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return useLocalCardsPathSwitch.get();
                    }
                })
                .mergeWith(remoteCardImagesFolder.asObservable()
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return !useLocalCardsPathSwitch.get();
                            }
                        }))
                // Skip(1) below is to avoid the initial emission sent when subscribing.
                .mergeWith(getUseLocalFolderFlagObservable().skip(1)
                        .map(new Function<Boolean, String>() {
                            @Override
                            public String apply(Boolean useLocalCards) throws Exception {
                                if (useLocalCards) {
                                    return cardImagesFolder.get();
                                } else {
                                    return remoteCardImagesFolder.get();
                                }
                            }
                        }));
    }

    public Observable<Boolean> getShowCardsCountObservable() {
        return showCardsCount.asObservable();
    }

    public void setLocalCardImagesFolder(String directoryPath) {
        cardImagesFolder.set(directoryPath);
    }

    public void setRemoteCardImagesFolder(String remoteCardsImagesFolder) {
        remoteCardImagesFolder.set(remoteCardsImagesFolder);
    }

    public Observable<String> getLocalCardImagesFolderObservable() {
        return cardImagesFolder.asObservable();
    }

    public Observable<String> getRemoteCardImagesFolderObservable() {
        return remoteCardImagesFolder.asObservable();
    }

    public Observable<Boolean> getUseLocalFolderFlagObservable() {
        return useLocalCardsPathSwitch.asObservable();
    }
}
