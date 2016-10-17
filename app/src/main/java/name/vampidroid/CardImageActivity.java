package name.vampidroid;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by fxjr on 06/07/16.
 */

public class CardImageActivity extends AppCompatActivity {

    private static final String TAG = "CardImageActivity";
    private int defaultUIOptions;

    // Reference: https://developer.android.com/training/system-ui/navigation.html
    private final int uiOptionsFullScreen = View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

    private boolean inFullScreenMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView imageView = (ImageView) findViewById(R.id.cardImage);


        Bundle parameters = getIntent().getExtras();

        // Reference: https://plus.google.com/+AlexLockwood/posts/FJsp1N9XNLS
        supportPostponeEnterTransition();

        Utils.loadCardImage(imageView, parameters.getString("cardImageFileName"), parameters.getInt("resIdFallbackCardImage"), new Utils.EmptyLoadCardImageAsync() {
            @Override
            public void onImageLoaded(BitmapDrawable image, Palette palette) {

                supportStartPostponedEnterTransition();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            defaultUIOptions = getWindow().getDecorView().getSystemUiVisibility();
        }

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    View decorView = getWindow().getDecorView();
//                        decorView.setSystemUiVisibility(uiOptionsFullScreen);
//
//                    }
//
//                }
//
//        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fullScreen() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            // BEGIN_INCLUDE (get_current_ui_flags)
            // The UI options currently enabled are represented by a bitfield.
            // getSystemUiVisibility() gives us that bitfield.
            int uiOptions = 0;
            int newUiOptions = uiOptions;
            uiOptions = getWindow().getDecorView().getSystemUiVisibility();

            // END_INCLUDE (get_current_ui_flags)
            // BEGIN_INCLUDE (toggle_ui_flags)
            boolean isImmersiveModeEnabled =
                    ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
            if (isImmersiveModeEnabled) {
                Log.i(TAG, "Turning immersive mode mode off. ");
            } else {
                Log.i(TAG, "Turning immersive mode mode on.");
            }

//            // Navigation bar hiding:  Backwards compatible to ICS.
//            if (Build.VERSION.SDK_INT >= 14) {
//                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//            }

            // Status bar hiding: Backwards compatible to Jellybean
            if (Build.VERSION.SDK_INT >= 16) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }

            // Immersive mode: Backward compatible to KitKat.
            // Note that this flag doesn't do anything by itself, it only augments the behavior
            // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
            // all three flags are being toggled together.
            // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
            // Sticky immersive mode differs in that it makes the navigation and status bars
            // semi-transparent, and the UI flag does not get cleared when the user interacts with
            // the screen.
//            if (Build.VERSION.SDK_INT >= 18) {
//                newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            }

            getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
            //END_INCLUDE (set_ui_flags)
        }
    }
}

