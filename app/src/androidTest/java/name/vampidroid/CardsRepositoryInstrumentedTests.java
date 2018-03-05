package name.vampidroid;

/**
 * Created by francisco on 04/03/18.
 */

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.functions.Predicate;
import io.reactivex.subscribers.TestSubscriber;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.LibraryCard;
import name.vampidroid.data.source.CardsRepository;
import name.vampidroid.utils.FilterStateQueryConverter;

import static org.hamcrest.CoreMatchers.containsString;

/**
 * Instrumented test, which will execute on an Android device.
 * <p>
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4.class)
public class CardsRepositoryInstrumentedTests {

    private static Context appContext;
    private static CardsRepository cardsRepository;
    // With this rule, we can reset the schedulers used throughout the system
    // to use the trampoline scheduler, regardless the scheduler originally used.
    // This facilitates asynchronous code testing.
    @Rule
    public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        cardsRepository = new CardsRepository();

        DatabaseHelper.setApplicationContext(appContext);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

        DatabaseHelper.closeDatabase();
    }


    @Test
    public void getAllCryptCards() throws InterruptedException {


        FilterState filterState = new FilterState();

        TestSubscriber<List<CryptCard>> testSubscriber2 = cardsRepository.getCryptCards(FilterStateQueryConverter.getCryptFilter(filterState)).test();

        testSubscriber2.assertValueCount(1);

        testSubscriber2.assertValue(new Predicate<List<CryptCard>>() {
            @Override
            public boolean test(List<CryptCard> cryptCardList) throws Exception {

                Assert.assertEquals("CryptCard List size", 1485, cryptCardList.size());

                return true;
            }
        });


    }

    @Test
    public void getCryptCardsWithCardNameRich() throws InterruptedException {


        final String textToSearch = "Rich".toLowerCase();

        FilterState filterState = new FilterState();
        filterState.searchInsideCardText = false;
        filterState.setName(textToSearch);

        TestSubscriber<List<CryptCard>> testSubscriber2 = cardsRepository.getCryptCards(FilterStateQueryConverter.getCryptFilter(filterState)).test();

        testSubscriber2.assertValueCount(1);

        testSubscriber2.assertValue(new Predicate<List<CryptCard>>() {
            @Override
            public boolean test(List<CryptCard> cryptCardList) throws Exception {

                Assert.assertEquals("CryptCard List size", 5, cryptCardList.size());


                for (CryptCard cryptCard : cryptCardList) {

                    Assert.assertThat(cryptCard.getName().toLowerCase(), containsString(textToSearch));

                }

                return true;
            }
        });


    }

    @Test
    public void getCryptCardsWithCardNameBlood() throws InterruptedException {


        final String textToSearch = "Blood".toLowerCase();
        final int listSize = 7;

        FilterState filterState = new FilterState();
        filterState.searchInsideCardText = false;
        filterState.setName(textToSearch);

        TestSubscriber<List<CryptCard>> testSubscriber2 = cardsRepository.getCryptCards(FilterStateQueryConverter.getCryptFilter(filterState)).test();

        testSubscriber2.assertValueCount(1);

        testSubscriber2.assertValue(new Predicate<List<CryptCard>>() {
            @Override
            public boolean test(List<CryptCard> cryptCardList) throws Exception {

                Assert.assertEquals("CryptCard List size", listSize, cryptCardList.size());


                for (CryptCard cryptCard : cryptCardList) {

                    Assert.assertThat(cryptCard.getName().toLowerCase(), containsString(textToSearch));

                }

                return true;
            }
        });


    }


    @Test
    public void getAllLibraryCards() throws InterruptedException {


        FilterState filterState = new FilterState();

        TestSubscriber<List<LibraryCard>> testSubscriber2 = cardsRepository.getLibraryCards(FilterStateQueryConverter.getLibraryFilter(filterState)).test();

        testSubscriber2.assertValueCount(1);

        testSubscriber2.assertValue(new Predicate<List<LibraryCard>>() {
            @Override
            public boolean test(List<LibraryCard> libraryCardList) throws Exception {

                Assert.assertEquals("LibraryCard List size", 2194, libraryCardList.size());

                return true;
            }
        });


    }


}
