package name.vampidroid;

import android.database.Cursor;
import android.util.Log;

import com.f2prateek.rx.preferences.Preference;

import name.vampidroid.data.source.CardsRepository;
import name.vampidroid.data.source.PreferenceRepository;
import name.vampidroid.utils.FilterStateQueryConverter;
import rx.Observable;
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



    private final PreferenceRepository preferenceRepository;


    public CardsViewModel(CardsRepository cardsRepository, PreferenceRepository preferenceRepository) {

        this.cardsRepository = cardsRepository;
        this.preferenceRepository = preferenceRepository;

    }

    public Observable<Cursor> getCryptCards() {

//        return cardsRepository.getCryptCards();

        return filterCryptCards
//                .observeOn(Schedulers.computation())
//                .map(new Func1<FilterState, String>() {
//                    @Override
//                    public String call(FilterState filterState) {
//                        Log.d("test", "map1: Thread Id: " + Thread.currentThread().getId());
//                        Log.d("test", "map1: Thread Name: " + Thread.currentThread().getName());
//                        return FilterStateTranslator.getCryptFilter(filterState);
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .flatMap(new Func1<String, Observable<Cursor>>() {
//                    @Override
//                    public Observable<Cursor> call(String filter) {
//                        Log.d("test", "map2: Thread Id: " + Thread.currentThread().getId());
//                        Log.d("test", "map2: Thread Name: " + Thread.currentThread().getName());
//                        return cardsRepository.getCryptCards(filter);
//                    }
//                });

                .observeOn(Schedulers.computation())
                .flatMap(new Func1<FilterState, Observable<Cursor>>() {
                    @Override
                    public Observable<Cursor> call(FilterState filterState) {
//                        Log.d("test", "map2: Thread Id: " + Thread.currentThread().getId());
//                        Log.d("test", "map2: Thread Name: " + Thread.currentThread().getName());
                        return cardsRepository.getCryptCards(FilterStateQueryConverter.getCryptFilter(filterState));
                    }
                });


    }

    public Observable<Cursor> getLibraryCards() {

        return filterLibraryCards
//                .observeOn(Schedulers.computation())
//                .map(new Func1<FilterState, String>() {
//                    @Override
//                    public String call(FilterState filterState) {
//                        Log.d("test", "map1: Thread Id: " + Thread.currentThread().getId());
//                        Log.d("test", "map1: Thread Name: " + Thread.currentThread().getName());
//                        return FilterStateTranslator.getLibraryFilter(filterState);
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .flatMap(new Func1<String, Observable<Cursor>>() {
//                    @Override
//                    public Observable<Cursor> call(String filter) {
//                        Log.d("test", "map2: Thread Id: " + Thread.currentThread().getId());
//                        Log.d("test", "map2: Thread Name: " + Thread.currentThread().getName());
//                        return cardsRepository.getLibraryCards(filter);
//                    }
//                });

                .observeOn(Schedulers.computation())
                .flatMap(new Func1<FilterState, Observable<Cursor>>() {
                    @Override
                    public Observable<Cursor> call(FilterState filterState) {
//                        Log.d("test", "map2: Thread Id: " + Thread.currentThread().getId());
//                        Log.d("test", "map2: Thread Name: " + Thread.currentThread().getName());
                        return cardsRepository.getLibraryCards(FilterStateQueryConverter.getLibraryFilter(filterState));
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

    public Preference<Boolean> getSearchTextCardPreference() {
        return preferenceRepository.getSearchTextCard();
    }

    public Observable<String> getCardsImagesFolderObservable() {
        return preferenceRepository.getCardsImagesFolderObservable();
    }



}
