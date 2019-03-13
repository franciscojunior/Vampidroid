package name.vampidroid;

/**
 * Created by francisco on 04/03/18.
 */

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    private static CardsRepository cardsRepository;
    // With this rule, we can reset the schedulers used throughout the system
    // to use the trampoline scheduler, regardless the scheduler originally used.
    // This facilitates asynchronous code testing.
//    @Rule
//    public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();

    @Rule
    public final InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        cardsRepository = new CardsRepository();

        DatabaseHelper.setApplicationContext(InstrumentationRegistry.getTargetContext());
        DatabaseHelper.setTesting(true);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

        DatabaseHelper.closeDatabase();
    }


    @Test
    public void getAllCryptCards() throws InterruptedException {


        FilterState filterState = new FilterState();

        LiveData<PagedList<CryptCard>> cryptCardList = cardsRepository.getCryptCardsLiveData(FilterStateQueryConverter.getCryptFilter(filterState));

        Assert.assertEquals("CryptCard List size", 1519, getValue(cryptCardList).size());

    }

    @Test
    public void getCryptCardsWithCardNameRich() throws InterruptedException {

        final String textToSearch = "Rich".toLowerCase();

        FilterState filterState = new FilterState();
        filterState.searchInsideCardText = false;
        filterState.setName(textToSearch);

        LiveData<PagedList<CryptCard>> cryptCardListLiveData = cardsRepository.getCryptCardsLiveData(FilterStateQueryConverter.getCryptFilter(filterState));

        PagedList<CryptCard> cryptCardList = getValue(cryptCardListLiveData);
        Assert.assertEquals("CryptCard List size", 5, cryptCardList.size());

        for (CryptCard cryptCard : cryptCardList) {

            Assert.assertThat(cryptCard.getName().toLowerCase(), containsString(textToSearch));

        }
    }

    @Test
    public void getCryptCardsWithCardNameBlood() throws InterruptedException {

        final String textToSearch = "Blood".toLowerCase();
        final int listSize = 7;

        FilterState filterState = new FilterState();
        filterState.searchInsideCardText = false;
        filterState.setName(textToSearch);

        LiveData<PagedList<CryptCard>> cryptCardListLiveData = cardsRepository.getCryptCardsLiveData(FilterStateQueryConverter.getCryptFilter(filterState));

        PagedList<CryptCard> cryptCardList = getValue(cryptCardListLiveData);
        Assert.assertEquals("CryptCard List size", listSize, cryptCardList.size());

        for (CryptCard cryptCard : cryptCardList) {

            Assert.assertThat(cryptCard.getName().toLowerCase(), containsString(textToSearch));

        }
    }

    @Test
    public void getAllLibraryCards() throws InterruptedException {


        FilterState filterState = new FilterState();

        LiveData<PagedList<LibraryCard>> libraryCardList = cardsRepository.getLibraryCardsLiveData(FilterStateQueryConverter.getLibraryFilter(filterState));

        Assert.assertEquals("LibraryCard List size", 2212, getValue(libraryCardList).size());

    }


//    Reference: https://stackoverflow.com/questions/44270688/unit-testing-room-and-livedata
    // By using the InstantTaskExecutorRule, there is no need to wait for the value to be observed anymore
    // (onChanged be called). It is done synchronously.
    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
//        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
//                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
//        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }

}
