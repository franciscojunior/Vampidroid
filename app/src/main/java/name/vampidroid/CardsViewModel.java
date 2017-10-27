package name.vampidroid;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.LibraryCard;
import name.vampidroid.data.source.CardsRepository;
import name.vampidroid.data.source.PreferenceRepository;
import name.vampidroid.utils.FilterStateQueryConverter;


/**
 * Created by fxjr on 14/11/2016.
 */

public class CardsViewModel {

    private final CardsRepository cardsRepository;

    private final PublishProcessor<FilterState> filterCryptCards = PublishProcessor.create();

    private final PublishProcessor<FilterState> filterLibraryCards = PublishProcessor.create();

    private final PublishSubject<String> cryptTabTitle = PublishSubject.create();

    private final PublishSubject<String> libraryTabTitle = PublishSubject.create();

    private final Observable<Boolean> showCardsCountPreferenceObservable;

    private final PreferenceRepository preferenceRepository;

    private int cryptCardsCount;

    private int libraryCardsCount;

    public CardsViewModel(CardsRepository cardsRepository, PreferenceRepository preferenceRepository) {

        this.cardsRepository = cardsRepository;
        this.preferenceRepository = preferenceRepository;
        showCardsCountPreferenceObservable = preferenceRepository.getShowCardsCountObservable();

    }

    public Flowable<List<CryptCard>> getCryptCards() {


        return filterCryptCards
                .observeOn(Schedulers.computation())
                .flatMap(new Function<FilterState, Flowable<List<CryptCard>>>() {
                    @Override
                    public Flowable<List<CryptCard>> apply(FilterState filterState) throws Exception {
                        filterState.setSearchInsideCardText(preferenceRepository.shouldSearchTextCard());
                        return cardsRepository.getCryptCards(FilterStateQueryConverter.getCryptFilter(filterState));
                    }
                })
                .doOnNext(new Consumer<List<CryptCard>>() {
                    @Override
                    public void accept(List<CryptCard> cryptCards) throws Exception {
                        cryptCardsCount = cryptCards.size();
                        cryptTabTitle.onNext(getTabTitle("Crypt", preferenceRepository.shouldShowCardsCount(), cryptCardsCount));
                    }
                });

    }

    public Flowable<List<LibraryCard>> getLibraryCards() {

        return filterLibraryCards
                .observeOn(Schedulers.computation())
                .flatMap(new Function<FilterState, Flowable<List<LibraryCard>>>() {
                    @Override
                    public Flowable<List<LibraryCard>> apply(FilterState filterState) throws Exception {
                        filterState.setSearchInsideCardText(preferenceRepository.shouldSearchTextCard());
                        return cardsRepository.getLibraryCards(FilterStateQueryConverter.getLibraryFilter(filterState));
                    }
                })
                .doOnNext(new Consumer<List<LibraryCard>>() {
                    @Override
                    public void accept(List<LibraryCard> libraryCards) throws Exception {
                        libraryCardsCount = libraryCards.size();
                        libraryTabTitle.onNext(getTabTitle("Library", preferenceRepository.shouldShowCardsCount(), libraryCardsCount));
                    }
                });

    }


//    public Observable<Cursor> getCards(int cardType) {
//        return cardType == 0 ? getCryptCards() : getLibraryCards();
//
//    }

    public void filterCryptCards(FilterState filterState) {
        filterCryptCards.onNext(filterState);
    }

    public void filterLibraryCards(FilterState filterState) {
        filterLibraryCards.onNext(filterState);
    }

    public Observable<String> getCryptTabTitle() {

        // The crypt tab title will need to be changed when either a new data set is loaded
        // (and the preference is set to show the card count) or when the preference itself
        // is changed. Here we merge the two source observables to create a single observable.
        return cryptTabTitle
                .mergeWith(showCardsCountPreferenceObservable
                        .skip(1) // Skip first emission on subscribe
                        .map(new Function<Boolean, String>() {
                            @Override
                            public String apply(Boolean showCards) throws Exception {
                                return getTabTitle("Crypt", showCards, cryptCardsCount);
                            }
                        }));
    }


    public Observable<String> getLibraryTabTitle() {
        return libraryTabTitle
                .mergeWith(showCardsCountPreferenceObservable
                        .skip(1) // Skip first emission on subscribe
                        .map(new Function<Boolean, String>() {
                            @Override
                            public String apply(Boolean showCards) throws Exception {
                                return getTabTitle("Library", showCards, libraryCardsCount);
                            }
                        }));
    }


    /**
     * Get an observable which emits the text hint that should be used in the search text field.
     * This text hint changes according to the settings if the search should be done only in the card name
     * or in the card text too.
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
     * This observable will emit an item (not used) to indicate that the cards listing needs to be refreshed.
     * This means a preference has changed which reflects how cards are searched, for example when changing
     * the preference of searching to include the card text besides the card name.
     * @return An observable which emits an String used only to indicate that a refresh is needed.
     */
    public Observable<String> getNeedRefreshCardsListing() {


        return preferenceRepository
                .getSearchTextCardObservable()
                .map(new Function<Boolean, String>() {
                    @Override
                    public String apply(Boolean aBoolean) throws Exception {
                        return "";
                    }
                });
    }


    /**
     * Whenever the card images folder is changed, the cards images need to be refreshed as well.
     *
     * @return an Observable which emits an empty string whenever the image folder is changed which indicated
     * that the images need to be refreshed.
     */
    public Observable<String> getNeedRefreshCardImages() {

        return preferenceRepository
                .getCardsImagesFolderObservable()
                .skip(1) // Skip first emission on subscribe
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return "";
                    }
                });
    }


    private String getTabTitle(String tabTitlePrefix, boolean includeCount, int count) {

        return (includeCount ? String.format("%s (%d)", tabTitlePrefix, count) : tabTitlePrefix);

    }
}
