package name.vampidroid;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

import name.vampidroid.fragments.SettingsFragment;

import static name.vampidroid.fragments.SettingsFragment.DEFAULT_IMAGES_FOLDER;


/**
 * Created by fxjr on 03/07/16.
 */

class Utils {

    private static final String TAG = "Utils";

    static void loadCardImage(Activity activity, ImageView cardImage, String cardName) {


        Resources res = activity.getResources();

        String cardImagesPath = PreferenceManager.getDefaultSharedPreferences(activity).getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, DEFAULT_IMAGES_FOLDER);
        String cardFileName = cardName == null ? "" : getCardFileName(cardName) + ".jpg";

        File imageFile = new File(cardImagesPath + "/" + cardFileName);

        Log.d(TAG, "setupToolbarImage: imagePath: " + imageFile.getAbsolutePath());
        Log.d(TAG, "setupToolbarImage: imageExists: " + imageFile.exists());

        Bitmap image;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        if (imageFile.exists()) {
            image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        } else {
            options.inSampleSize = 2; // When loading the default image, downsampling it because it is very big.
            image = BitmapFactory.decodeResource(res, R.drawable.gold_back, options);
        }

        cardImage.setImageDrawable(new BitmapDrawable(res, image));
    }

    private static String getCardFileName(String cardName) {

//        Reference: http://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
        return cardName.replaceAll("\\W", "").toLowerCase();
    }
}

