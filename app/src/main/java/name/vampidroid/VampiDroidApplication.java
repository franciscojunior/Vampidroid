package name.vampidroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.f2prateek.rx.preferences.RxSharedPreferences;

import java.lang.reflect.Method;

import name.vampidroid.data.source.CardsRepository;
import name.vampidroid.data.source.PreferenceRepository;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class VampiDroidApplication extends Application {

    private static final String TAG = "VampiDroidApplication";
    private CardsRepository cardsRepository;

    private PreferenceRepository preferenceRepository;


    private CompositeSubscription subscriptions;

    public VampiDroidApplication() {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.d(TAG, "finishing application");
        DatabaseHelper.closeDatabase();

        subscriptions.unsubscribe();
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.d(TAG, "starting application");

        super.onCreate();

        cardsRepository = new CardsRepository();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);

        preferenceRepository = new PreferenceRepository(rxPreferences);


        subscriptions = new CompositeSubscription();


        DatabaseHelper.setApplicationContext(getApplicationContext());

        Utils.setResources(getResources());


        subscriptions.add(getPreferenceRepository().getCardsImagesFolderObservable()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String path) {
                        Utils.setCardImagesPath(path);
                    }
                }));

//		FilterModel.initFilterModel(
//				Arrays.asList(getResources().getStringArray(R.array.clans)),
//				Arrays.asList(getResources().getStringArray(R.array.types)),
//				Arrays.asList(getResources().getStringArray(R.array.disciplineslibrary)),
//				Arrays.asList(getResources().getStringArray(R.array.disciplinescrypt)));
//



        // Workaround a leak bug in ClipboardUIManager.
        // Reference: https://gist.github.com/cypressious/91c4fb1455470d803a602838dfcd5774
        // Reference: https://github.com/square/leakcanary/issues/133
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Class<?> cls = Class.forName("android.sec.clipboard.ClipboardUIManager");
                Method m = cls.getDeclaredMethod("getInstance", Context.class);
                m.setAccessible(true);
                Object o = m.invoke(null, this);
            } catch (Exception ignored) { }
        }

    }



    public CardsRepository getCardsRepository() {
        return cardsRepository;
    }

    public PreferenceRepository getPreferenceRepository() {
        return preferenceRepository;
    }

    public CardsViewModel getCardsViewModel() {

        return new CardsViewModel(getCardsRepository(), getPreferenceRepository());

    }


    public SettingsViewModel getSettingsViewModel() {
        return new SettingsViewModel(getPreferenceRepository());
    }

    public CardDetailsViewModel getCardDetailsViewModel(long cardId) {
        return new CardDetailsViewModel(cardId, cardsRepository);
    }
}
