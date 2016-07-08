package name.vampidroid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;


/**
 * Created by fxjr on 03/07/16.
 */

class Utils {

    private static final String TAG = "Utils";

    static void loadCardImage(ImageView cardImage, String cardName, Resources res) {


        String cardFileName = cardName == null ? "" : getCardFileName(cardName) + ".jpg";

        File imageFile = new File(Environment.getExternalStorageDirectory() + "/" + res.getString(R.string.vtes_images_folder) + "/" + cardFileName);

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

