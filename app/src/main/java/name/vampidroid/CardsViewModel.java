package name.vampidroid;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.LibraryCard;
import name.vampidroid.data.source.CardsRepository;
import name.vampidroid.data.source.PreferenceRepository;
import name.vampidroid.utils.CardsEvent;
import name.vampidroid.utils.FilterStateQueryConverter;


/**
 * Created by fxjr on 14/11/2016.
 */

public class CardsViewModel extends AndroidViewModel {

    private CardsRepository cardsRepository;

    private PreferenceRepository preferenceRepository;

    private int cryptCardsCount;

    private int libraryCardsCount;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private boolean shouldSearchTextCard;
    private boolean shouldShowCardsCount;

    MutableLiveData<String> cryptCardsFilter = new MutableLiveData<>();
    private LiveData<PagedList<CryptCard>> cryptCardsLiveData;

    MutableLiveData<String> libraryCardsFilter = new MutableLiveData<>();
    private LiveData<PagedList<LibraryCard>> libraryCardsLiveData;


    private final MutableLiveData<String> cryptTabTitleLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> libraryTabTitleLiveData = new MutableLiveData<>();

    // This live data will signal when the card images path has changed and thus need to refresh the
    // images.
    private final MutableLiveData<CardsEvent> refreshCardImagesLiveData = new MutableLiveData<>();

    // SingleEvent to indicate that the cards need to be refreshed.
    private MutableLiveData<CardsEvent> refreshCardsListingLiveData = new MutableLiveData<>();



    public CardsViewModel(@NonNull Application application) {
        super(application);

        VampiDroidApplication vampiDroidApplication = getApplication();
        this.cardsRepository = vampiDroidApplication.getCardsRepository();
        this.preferenceRepository = vampiDroidApplication.getPreferenceRepository();


        // Subscribe to know when the search text card preference is updated.
        // Then send the event that this preference has been changed.
        //
        // This observer triggers initial cards loading because the getSearchTextCardObservable will
        // emit an initial value when subscribed which will make the refreshCardsListingLiveData to carry a
        // new CardsEvent which will signal that the cards listing needs to be refreshed.
        subscriptions.add(preferenceRepository
                .getSearchTextCardObservable()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean searchTextCard) throws Exception {
                        shouldSearchTextCard = searchTextCard;
                        refreshCardsListingLiveData.postValue(new CardsEvent());
                    }
                })

        );


        // Subscribe to know when the show cards count preference is updated.
        // First update the flag to show cards count
        // Then update the tabs title
        // In other words, map the show cards count preference observable to the livedata of crypt and library tab titles.
        subscriptions.add(preferenceRepository
                .getShowCardsCountObservable()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean showCardsCount) throws Exception {
                        shouldShowCardsCount = showCardsCount;

                        cryptTabTitleLiveData.postValue(getTabTitle("Crypt", showCardsCount, cryptCardsCount));
                        libraryTabTitleLiveData.postValue(getTabTitle("Library", showCardsCount, libraryCardsCount));
                    }
                })
        );

        subscriptions.add(preferenceRepository
                .getCardImagesFolderObservable()
                .skip(1) // Skip first emission the preference library does on subscribe.
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        refreshCardImagesLiveData.postValue(new CardsEvent());
                    }
                })
        );


        cryptCardsLiveData = Transformations.map(

                // For each filter received, return a new list of cards.
                Transformations.switchMap(cryptCardsFilter,
                        new android.arch.core.util.Function<String, LiveData<PagedList<CryptCard>>>() {
                            @Override
                            public LiveData<PagedList<CryptCard>> apply(String filterQuery) {
                                return cardsRepository.getCryptCardsLiveData(filterQuery);
                            }
                        }),
                // Then, based on this returned list, get the number of items and update the tab title with the count.
                // Simulate onNext of RxJava
                new android.arch.core.util.Function<PagedList<CryptCard>, PagedList<CryptCard>>() {
                    @Override
                    public PagedList<CryptCard> apply(PagedList<CryptCard> cryptCards) {
                        cryptCardsCount = cryptCards.size();
                        cryptTabTitleLiveData.postValue(getTabTitle("Crypt", shouldShowCardsCount, cryptCardsCount));
                        return cryptCards;
                    }
                });


        libraryCardsLiveData = Transformations.map(

                // For each filter received, return a new list of cards.
                Transformations.switchMap(libraryCardsFilter,
                        new android.arch.core.util.Function<String, LiveData<PagedList<LibraryCard>>>() {
                            @Override
                            public LiveData<PagedList<LibraryCard>> apply(String filterQuery) {
                                return cardsRepository.getLibraryCardsLiveData(filterQuery);
                            }
                        }),
                // Then, based on this returned list, get the number of items and update the tab title with the count.
                // Simulate onNext of RxJava
                new android.arch.core.util.Function<PagedList<LibraryCard>, PagedList<LibraryCard>>() {
                    @Override
                    public PagedList<LibraryCard> apply(PagedList<LibraryCard> libraryCards) {
                        libraryCardsCount = libraryCards.size();
                        libraryTabTitleLiveData.postValue(getTabTitle("Library", shouldShowCardsCount, libraryCardsCount));
                        return libraryCards;
                    }
                });





    }

    @Override
    protected void onCleared() {
        super.onCleared();

        subscriptions.dispose();
    }

    public LiveData<PagedList<CryptCard>> getCryptCardsLiveData() {

        return cryptCardsLiveData;

    }

    public LiveData<PagedList<LibraryCard>> getLibraryCardsLiveData() {

        return libraryCardsLiveData;

    }


//    public Observable<Cursor> getCards(int cardType) {
//        return cardType == 0 ? getCryptCards() : getLibraryCards();
//
//    }

    public void filterCryptCards(FilterState filterState) {
        filterState.setSearchInsideCardText(shouldSearchTextCard);
        filterCards(FilterStateQueryConverter.getCryptFilter(filterState), cryptCardsFilter);
    }

    public void filterLibraryCards(FilterState filterState) {
        filterState.setSearchInsideCardText(shouldSearchTextCard);
        filterCards(FilterStateQueryConverter.getLibraryFilter(filterState), libraryCardsFilter);
    }

    private void filterCards(String cardsFilterQuery, MutableLiveData<String> cardsMutableLiveData) {


        // Only make a new query if the query has changed.
        // This also includes the special case when the viewmodel is brand new
        // either because the app has just started, or the android process has been recreated
        // because it was killed to free memory for other apps.
        // In both cases, the cardsMutableLiveData value will be null, thus different from the
        // the new filter query.

        if (!cardsFilterQuery.equals(cardsMutableLiveData.getValue())) {
            cardsMutableLiveData.postValue(cardsFilterQuery);
        }
    }


    public LiveData<String> getCryptTabTitleLiveData() {
        return cryptTabTitleLiveData;
    }

    public LiveData<String> getLibraryTabTitleLiveData() {
        return libraryTabTitleLiveData;
    }


    /**
     * Get an observable which emits the text hint that should be used in the search text field.
     * This text hint changes according to the settings if the search should be done only in the card name
     * or in the card text too.
     *
     * @return An observable which emits the resourceId of the string to be used as a text hint.
     */
    public Observable<Integer> getSearchTextHintObservable() {

        return preferenceRepository.getSearchTextCardObservable()
                .map(new Function<Boolean, Integer>() {
                    @Override
                    public Integer apply(Boolean searchTextCard) throws Exception {
                        return searchTextCard ? R.string.search_bar_filter_card_name_and_card_text : R.string.search_bar_filter_card_name;
                    }
                });

    }

    /**
     * Whenever the cards listing needs to be refreshed, this livedata will contain a new CardsEvent signaling it.
     * @return A LiveData with the CardsEvent object signaling that the cards listing needs to be refreshed.
     */
    public LiveData<CardsEvent> getNeedRefreshCardsListingLiveData() {
        return refreshCardsListingLiveData;
    }

    /**
     * Whenever the card images folder is changed, the cards images need to be refreshed as well.
     *
     * @return a LiveData which emits a CardsEvent whenever the image folder is changed which indicated
     * that the images need to be refreshed.
     */
    public LiveData<CardsEvent> getNeedRefreshCardImagesLiveData() {
        return refreshCardImagesLiveData;
    }

    private String getTabTitle(String tabTitlePrefix, boolean includeCount, int count) {

        return (includeCount ? String.format("%s (%d)", tabTitlePrefix, count) : tabTitlePrefix);

    }

}
