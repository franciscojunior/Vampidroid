package name.vampidroid.utils;

/**
 * An utility class to crypt and library events signaling.
 * It's used with livedata. For more info, check
 * https://medium.com/google-developers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
public class CardsEvent {

    private boolean cryptEventHasBeenHandled = false;
    private boolean libraryEventHasBeenHandled = false;


    public boolean hasCryptEventToHandle() {
        if (cryptEventHasBeenHandled) {
            return false;
        } else {
            cryptEventHasBeenHandled = true;
            return true;
        }

    }

    public boolean hasLibraryEventToHandle() {
        if (libraryEventHasBeenHandled) {
            return false;
        } else {
            libraryEventHasBeenHandled = true;
            return true;
        }

    }

}
