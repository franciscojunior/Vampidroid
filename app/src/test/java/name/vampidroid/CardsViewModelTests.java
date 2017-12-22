package name.vampidroid;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.LibraryCard;
import name.vampidroid.data.source.CardsRepository;
import name.vampidroid.data.source.PreferenceRepository;
import name.vampidroid.utils.FilterStateQueryConverter;

import static org.mockito.Mockito.when;

/**
 * Created by fxjr on 12/10/17.
 */

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class CardsViewModelTests {

    // References:
    // https://medium.com/@fabioCollini/testing-asynchronous-rxjava-code-using-mockito-8ad831a16877
    // https://www.infoq.com/articles/Testing-RxJava
    @Mock
    PreferenceRepository mockPreferenceRepository;

    @Mock
    CardsRepository mockCardsRepository;

    // With this rule, we can reset the schedulers used throughout the system
    // to use the trampoline scheduler, regardless the scheduler originally used.
    // This facilitates asynchronous code testing.
    @Rule
    public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();

    @Test
    public void get_crypt_cards() throws InterruptedException {

        List<CryptCard> cryptCardsTestValues = getCryptCards();
        FilterState testFilterState = new FilterState();
        when(mockCardsRepository.getCryptCards(FilterStateQueryConverter.getCryptFilter(testFilterState))).thenReturn(Flowable.just(cryptCardsTestValues));



        CardsViewModel viewModel = new CardsViewModel(mockCardsRepository, mockPreferenceRepository);

        Flowable<List<CryptCard>> cryptCards = viewModel.getCryptCards();

        TestSubscriber<List<CryptCard>> testObserver = cryptCards.test();

        testObserver.assertNoValues();

        viewModel.filterCryptCards(testFilterState);

        testObserver.assertValueCount(1);

        testObserver.assertValue(cryptCardsTestValues);

        testObserver.assertValue(new Predicate<List<CryptCard>>() {
            @Override
            public boolean test(List<CryptCard> cryptCards) throws Exception {

                return cryptCards.size() == 3;
            }
        });


        testObserver.assertValue(new Predicate<List<CryptCard>>() {
            @Override
            public boolean test(List<CryptCard> cryptCards) throws Exception {

                return Observable.fromIterable(cryptCards)
                        .map(new Function<CryptCard, String>() {
                            @Override
                            public String apply(CryptCard cryptCard) throws Exception {
                                return cryptCard.getName();
                            }
                        })
                        .toList()
                        .blockingGet()
                        .equals(Arrays.asList("Crypt Test Card", "Crypt Test Card 2", "Crypt Test Card 3"));


            }
        });
    }

    @Test
    public void get_library_cards() throws InterruptedException {

        List<LibraryCard> libraryCardsTestValues = getLibraryCards();
        FilterState testFilterState = new FilterState();
        when(mockCardsRepository.getLibraryCards(FilterStateQueryConverter.getLibraryFilter(testFilterState))).thenReturn(Flowable.just(libraryCardsTestValues));



        CardsViewModel viewModel = new CardsViewModel(mockCardsRepository, mockPreferenceRepository);

        Flowable<List<LibraryCard>> libraryCards = viewModel.getLibraryCards();

        TestSubscriber<List<LibraryCard>> testObserver = libraryCards.test();

        testObserver.assertNoValues();

        viewModel.filterLibraryCards(testFilterState);

        testObserver.assertValueCount(1);

        testObserver.assertValue(libraryCardsTestValues);

        testObserver.assertValue(new Predicate<List<LibraryCard>>() {
            @Override
            public boolean test(List<LibraryCard> libraryCards) throws Exception {

                return libraryCards.size() == 3;
            }
        });


        testObserver.assertValue(new Predicate<List<LibraryCard>>() {
            @Override
            public boolean test(List<LibraryCard> libraryCards) throws Exception {

                return
                        Observable.fromIterable(libraryCards)
                        .map(new Function<LibraryCard, String>() {
                            @Override
                            public String apply(LibraryCard libraryCard) throws Exception {
                                return libraryCard.getName();
                            }
                        })
                        .toList()
                        .blockingGet()
                        .equals(Arrays.asList("Library Test Card", "Library Test Card 2", "Library Test Card 3"));

            }
        });

        testObserver.assertValue(new Predicate<List<LibraryCard>>() {
            @Override
            public boolean test(List<LibraryCard> libraryCards) throws Exception {

                return
                        Observable.fromIterable(libraryCards)
                                .map(new Function<LibraryCard, String>() {
                                    @Override
                                    public String apply(LibraryCard libraryCard) throws Exception {
                                        return libraryCard.getType();
                                    }
                                })
                                .toList()
                                .blockingGet()
                                .equals(Arrays.asList("Master", "Equipment", "Retainer"));

            }
        });
    }




    @Test
    public void change_crypt_tab_title_when_changing_show_card_count_preference() {

        PublishSubject<Boolean> preferenceShowCardCount = PublishSubject.create();
        // First, test get the crypt tab with show cards count preference set to false
        when(mockPreferenceRepository.getShowCardsCountObservable()).thenReturn(preferenceShowCardCount);
        FilterState testFilterState = new FilterState();
        when(mockCardsRepository.getCryptCards(FilterStateQueryConverter.getCryptFilter(testFilterState))).thenReturn(Flowable.just(getCryptCards()));



        CardsViewModel viewModel = new CardsViewModel(mockCardsRepository, mockPreferenceRepository);

        // Test card list request, although in this test we are only concerned about the title.
        viewModel.getCryptCards().test();

        TestObserver<String> testObserver = viewModel.getCryptTabTitle().test();

        // Request a list of cards first even though we are not going to use them.
        viewModel.filterCryptCards(testFilterState);

        // Needs to be called twice because internally, the viewModel ignores the first emission.
        // See CardsViewModel.getCryptTabTitle() for more information
        preferenceShowCardCount.onNext(true);
        preferenceShowCardCount.onNext(true);

        // And set it to false again.
        preferenceShowCardCount.onNext(false);


        // Now we should have 3 title emissions:
        // 1. for the request of cards with the preference to show card count set to false.
        // 2. for the preference change to true.
        // 3. for the preference change back to false.
        testObserver.assertValueCount(3);

        testObserver.assertValues("Crypt", "Crypt (3)", "Crypt");

    }

    @Test
    public void change_library_tab_title_when_changing_show_card_count_preference() {

        PublishSubject<Boolean> preferenceShowCardCount = PublishSubject.create();

        // First, test get the library tab with show cards count preference set to false
        when(mockPreferenceRepository.getShowCardsCountObservable()).thenReturn(preferenceShowCardCount);

        FilterState testFilterState = new FilterState();

        when(mockCardsRepository.getLibraryCards(FilterStateQueryConverter.getLibraryFilter(testFilterState))).thenReturn(Flowable.just(getLibraryCards()));


        CardsViewModel viewModel = new CardsViewModel(mockCardsRepository, mockPreferenceRepository);

        // Test card list request, although in this test we are only concerned about the title.
        viewModel.getLibraryCards().test();

        TestObserver<String> testObserver = viewModel.getLibraryTabTitle().test();

        // Request a list of cards first even though we are not going to use them.
        viewModel.filterLibraryCards(testFilterState);

        // Needs to be called twice because internally, the viewModel ignores the first emission.
        // See CardsViewModel.getLibraryTabTitle() for more information
        preferenceShowCardCount.onNext(true);
        preferenceShowCardCount.onNext(true);

        // And set it to false again.
        preferenceShowCardCount.onNext(false);


        // Now we should have 3 title emissions:
        // 1. for the request of cards with the preference to show card count set to false.
        // 2. for the preference change to true.
        // 3. for the preference change back to false.
        testObserver.assertValueCount(3);

        testObserver.assertValues("Library", "Library (3)", "Library");

    }

    @Test
    public void getSearchTextHint() {

        PublishSubject<Boolean> preferenceSearchTextHint = PublishSubject.create();

        // First, test get the library tab with show cards count preference set to false
        when(mockPreferenceRepository.getSearchTextCardObservable()).thenReturn(preferenceSearchTextHint);

        CardsViewModel viewModel = new CardsViewModel(mockCardsRepository, mockPreferenceRepository);

        TestObserver<Integer> testObserver = viewModel.getSearchTextHintObservable().test();

        preferenceSearchTextHint.onNext(false);
        preferenceSearchTextHint.onNext(true);

        testObserver.assertValues(R.string.search_bar_filter_card_name, R.string.search_bar_filter_card_name_and_card_text);

    }

    @Test
    public void getNeedRefreshCardsListing() {

        PublishSubject<Boolean> preferenceSearchTextCard = PublishSubject.create();

        // First, test get the library tab with show cards count preference set to false
        when(mockPreferenceRepository.getSearchTextCardObservable()).thenReturn(preferenceSearchTextCard);

        CardsViewModel viewModel = new CardsViewModel(mockCardsRepository, mockPreferenceRepository);

        TestObserver<String> testObserver = viewModel.getNeedRefreshCardsListing().test();

        // Needs to be called twice because internally, the viewModel ignores the first emission.
        preferenceSearchTextCard.onNext(false);


        // Change the searchTextHint preference.
        preferenceSearchTextCard.onNext(true);

        testObserver.assertValueCount(1);
        testObserver.assertValues("");

    }

    @Test
    public void getNeedRefreshCardImages() {

        final PublishSubject<String> preferenceCardsImageFolder = PublishSubject.create();


        // First, test get the library tab with show cards count preference set to false
        when(mockPreferenceRepository.getCardsImagesFolderObservable()).thenReturn(preferenceCardsImageFolder);

        CardsViewModel viewModel = new CardsViewModel(mockCardsRepository, mockPreferenceRepository);


        TestObserver<String> testObserver = viewModel.getNeedRefreshCardImages().test();

        // Needs to be called twice because internally, the viewModel ignores the first emission.
        preferenceCardsImageFolder.onNext("");

        // Change the cards image folder preference.
        preferenceCardsImageFolder.onNext("some/path");

        testObserver.assertValueCount(1);
        testObserver.assertValues("");

    }


    static List<CryptCard> getCryptCards() {

        CryptCard cryptCard = new CryptCard("Crypt Test Card", "Vampire", "", "", "", "", "", "", "", "");
        CryptCard cryptCard2 = new CryptCard("Crypt Test Card 2", "Vampire", "", "", "", "", "", "", "", "");
        CryptCard cryptCard3 = new CryptCard("Crypt Test Card 3", "Vampire", "", "", "", "", "", "", "", "");

        return Arrays.asList(cryptCard, cryptCard2, cryptCard3);
    }

    static List<LibraryCard> getLibraryCards() {

        LibraryCard libraryCard = new LibraryCard("Library Test Card", "Master", "", "", "", "", "", "", "", "");
        LibraryCard libraryCard2 = new LibraryCard("Library Test Card 2", "Equipment", "", "", "", "", "", "", "", "");
        LibraryCard libraryCard3 = new LibraryCard("Library Test Card 3", "Retainer", "", "", "", "", "", "", "", "");

        return Arrays.asList(libraryCard, libraryCard2, libraryCard3);
    }

}
