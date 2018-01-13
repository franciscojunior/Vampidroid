package name.vampidroid;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import io.reactivex.Flowable;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.LibraryCard;
import name.vampidroid.data.source.CardsRepository;

/**
 * Created by FranciscoJunior on 24/01/2017.
 */

public class CardDetailsViewModel extends AndroidViewModel {


    private final CardsRepository cardsRepository;

    public CardDetailsViewModel(@NonNull Application application) {
        super(application);
        cardsRepository = ((VampiDroidApplication) application).getCardsRepository();
    }

    public Flowable<CryptCard> getCryptCard(Long cardId) {

        return cardsRepository.getCryptCard(cardId);
    }

    public Flowable<LibraryCard> getLibraryCard(Long cardId) {

        return cardsRepository.getLibraryCard(cardId);
    }

}
