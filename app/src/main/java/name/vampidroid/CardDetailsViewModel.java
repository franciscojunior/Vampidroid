package name.vampidroid;

import android.database.Cursor;

import name.vampidroid.data.source.CardsRepository;
import rx.Observable;

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


    public Observable<Cursor> getCryptCard() {

        return cardsRepository.getCryptCard(cardId);
    }

    public Observable<Cursor> getLibraryCard() {

        return cardsRepository.getLibraryCard(cardId);
    }
}
