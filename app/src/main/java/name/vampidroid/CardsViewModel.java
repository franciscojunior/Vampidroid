package name.vampidroid;

import android.database.Cursor;

import name.vampidroid.data.source.CardsRepository;
import name.vampidroid.data.source.PreferenceRepository;
import name.vampidroid.utils.FilterStateQueryConverter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;


/**
 * Created by fxjr on 14/11/2016.
 */

public class CardsViewModel {

    private final CardsRepository cardsRepository;

    private final PublishSubject<FilterState> filterCryptCards = PublishSubject.create();

    private final PublishSubject<FilterState> filterLibraryCards = PublishSubject.create();

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

    public Observable<Cursor> getCryptCards() {


        return filterCryptCards
                .observeOn(Schedulers.computation())
                .flatMap(new Func1<FilterState, Observable<Cursor>>() {
                    @Override
                    public Observable<Cursor> call(FilterState filterState) {
                        filterState.setSearchInsideCardText(preferenceRepository.shouldSearchTextCard());
                        return cardsRepository.getCryptCards(FilterStateQueryConverter.getCryptFilter(filterState));
                    }
                })
                .doOnNext(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        cryptCardsCount = cursor.getCount();
                        cryptTabTitle.onNext(getTabTitle("Crypt", preferenceRepository.shouldShowCardsCount(), cryptCardsCount));
                    }
                });

    }

    public Observable<Cursor> getLibraryCards() {

        return filterLibraryCards
                .observeOn(Schedulers.computation())
                .flatMap(new Func1<FilterState, Observable<Cursor>>() {
                    @Override
                    public Observable<Cursor> call(FilterState filterState) {
                        filterState.setSearchInsideCardText(preferenceRepository.shouldSearchTextCard());
                        return cardsRepository.getLibraryCards(FilterStateQueryConverter.getLibraryFilter(filterState));
                    }
                })
                .doOnNext(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        libraryCardsCount = cursor.getCount();
                        libraryTabTitle.onNext(getTabTitle("Library", preferenceRepository.shouldShowCardsCount(), libraryCardsCount));
                    }
                });
    }


    public Observable<Cursor> getCards(int cardType) {
        return cardType == 0 ? getCryptCards() : getLibraryCards();

    }

    public void filterCryptCards(FilterState filterState) {
        filterCryptCards.onNext(filterState);
    }

    public void filterLibraryCards(FilterState filterState) {
        filterLibraryCards.onNext(filterState);
    }

    public Observable<Boolean> getSearchTextCardObservable() {
        return preferenceRepository.getSearchTextCardObservable();
    }

    public Observable<String> getCardsImagesFolderObservable() {
        return preferenceRepository.getCardsImagesFolderObservable();
    }

    public Observable<String> getCryptTabTitle() {

        // The crypt tab title will need to be changed when either a new data set is loaded
        // (and the preference is set to show the card count) or when the preference itself
        // is changed. Here we merge the two source observables to create a single observable.
        return cryptTabTitle
                .mergeWith(showCardsCountPreferenceObservable
                        .skip(1) // Skip first emission on subscribe
                        .map(new Func1<Boolean, String>() {
                            @Override
                            public String call(Boolean showCards) {
                                return getTabTitle("Crypt", showCards, cryptCardsCount);
                            }
                        }));
    }


    public Observable<String> getLibraryTabTitle() {
        return libraryTabTitle
                .mergeWith(showCardsCountPreferenceObservable
                        .skip(1) // Skip first emission on subscribe
                        .map(new Func1<Boolean, String>() {
                            @Override
                            public String call(Boolean showCards) {
                                return getTabTitle("Library", showCards, libraryCardsCount);
                            }
                        }));
    }

    private String getTabTitle(String tabTitlePrefix, boolean includeCount, int count) {

        return (includeCount ? String.format("%s (%d)", tabTitlePrefix, count) : tabTitlePrefix);

    }
}
