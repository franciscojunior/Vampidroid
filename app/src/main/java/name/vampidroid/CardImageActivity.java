package name.vampidroid;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //fullScreen();


        ImageView imageView = (ImageView) findViewById(R.id.cardImage);


        Bundle parameters = getIntent().getExtras();

        Utils.loadCardImage(this, imageView, parameters.getString("cardImageFileName"), parameters.getInt("resIdFallbackCardImage"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            defaultUIOptions = getWindow().getDecorView().getSystemUiVisibility();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    View decorView = getWindow().getDecorView();

                    if (inFullScreenMode) {
                        decorView.setSystemUiVisibility(defaultUIOptions);

                    } else {
                        decorView.setSystemUiVisibility(uiOptionsFullScreen);
                    }
                }

            }
        });

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

