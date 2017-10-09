package name.vampidroid;

import io.reactivex.Observable;
import name.vampidroid.data.CryptCard;
import name.vampidroid.data.source.CardsRepository;
import name.vampidroid.data.LibraryCard;

/**
 * Created by FranciscoJunior on 24/01/2017.
 */

class CardDetailsViewModel {


    private final long cardId;
    private final CardsRepository cardsRepository;

    public CardDetailsViewModel(long cardId, CardsRepository cardsRepository) {

        this.cardId = cardId;
        this.cardsRepository = cardsRepository;
    }


    public Observable<CryptCard> getCryptCard() {

        return cardsRepository.getCryptCard(cardId).toObservable();
    }

    public Observable<LibraryCard> getLibraryCard() {

        return cardsRepository.getLibraryCard(cardId).toObservable();
    }

}
