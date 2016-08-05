package name.vampidroid;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;
import android.widget.ImageView;

import java.io.File;

import name.vampidroid.fragments.SettingsFragment;

import static name.vampidroid.fragments.SettingsFragment.DEFAULT_IMAGES_FOLDER;


/**
 * Created by fxjr on 03/07/16.
 */

class Utils {

    private static final String TAG = "Utils";

    static void loadCardImage(Activity activity, ImageView cardImageView, String cardName) {


//        Resources res = activity.getResources();
//
//        String cardImagesPath = PreferenceManager.getDefaultSharedPreferences(activity).getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, DEFAULT_IMAGES_FOLDER);
//        String cardFileName = cardName == null ? "" : getCardFileName(cardName) + ".jpg";
//
//        File imageFile = new File(cardImagesPath + "/" + cardFileName);
//
//        Bitmap image;
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 1;
//
//        if (imageFile.exists()) {
//            image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
//        } else {
//            options.inSampleSize = 2; // When loading the default image, downsampling it because it is very big.
//            image = BitmapFactory.decodeResource(res, R.drawable.gold_back, options);
//        }
//
//        cardImageView.setImageDrawable(new BitmapDrawable(res, image));

        new LoadImageOperation(activity, cardImageView, cardName).execute();

    }

    private static String getCardFileName(String cardName) {

//        Reference: http://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
        return cardName.replaceAll("\\W", "").toLowerCase();
    }


    private static class LoadImageOperation extends AsyncTask<Void, Void, Void> {


        private final ImageView cardImageView;
        private final String cardName;
        private final Resources resources;
        private BitmapDrawable bitmapDrawable;
        private final String cardImagesPath;

        public LoadImageOperation(Activity activity, ImageView cardImageView, String cardName) {

            resources = activity.getResources();
            this.cardImageView = cardImageView;
            this.cardName = cardName;
            cardImagesPath = PreferenceManager.getDefaultSharedPreferences(activity).getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, DEFAULT_IMAGES_FOLDER);
        }


        @Override
        protected Void doInBackground(Void... voids) {


            String cardFileName = cardName == null ? "" : getCardFileName(cardName) + ".jpg";

            File imageFile = new File(cardImagesPath + "/" + cardFileName);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;

            Bitmap image;

            if (imageFile.exists()) {
                image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            } else {
                options.inSampleSize = 2; // When loading the default image, downsampling it because it is very big.
                // TODO: 05/08/16 Convert the default image to a lower resolution.
                image = BitmapFactory.decodeResource(resources, R.drawable.gold_back, options);
            }

            bitmapDrawable = new BitmapDrawable(resources, image);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            cardImageView.setImageDrawable(bitmapDrawable);
        }
    }
}

