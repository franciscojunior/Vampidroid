package name.vampidroid;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by fxjr on 06/07/16.
 */

public class LibraryCardDetailsActivity extends AppCompatActivity {

    private static final String TAG = "LibraryCardDetailsActiv";
    private static String QUERY_LIBRARY = "select Name, Type, Clan, Discipline, CardText, PoolCost, BloodCost, Artist, _Set from library where _id = ?";
    private ImageView cardImage;
    private String cardName;

    // Discipline images

    private ImageView[] disciplineImageViews = new ImageView[3];
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_card_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //        Reference: https://plus.google.com/+AlexLockwood/posts/FJsp1N9XNLS
        supportPostponeEnterTransition();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cardImage = (ImageView) findViewById(R.id.cardImage);


        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent showCardImage = new Intent(view.getContext(), CardImageActivity.class);
                showCardImage.putExtra("cardId", getIntent().getExtras().getLong("cardId"));
                showCardImage.putExtra("cardImageFileName", Utils.getCardFileName(cardName, false));
                showCardImage.putExtra("resIdFallbackCardImage", R.drawable.green_back);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(LibraryCardDetailsActivity.this, cardImage, "cardImageTransition").toBundle();
                    view.getContext().startActivity(showCardImage, bundle);
                } else
                    view.getContext().startActivity(showCardImage);


            }
        });

        setupDisciplineImagesArray();

        setupCardData();

    }

    //    Reference: https://github.com/codepath/android_guides/wiki/Shared-Element-Activity-Transition
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

    @Override
    public void onBackPressed() {
        fab.hide();
        super.onBackPressed();

    }


    //    Reference: http://stackoverflow.com/questions/19312109/execute-a-method-after-an-activity-is-visible-to-user

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        fab.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
            }
        }, 300);

    }


    private void setupDisciplineImagesArray() {

        disciplineImageViews[0] = (ImageView) findViewById(R.id.img_card_details_discipline1);
        disciplineImageViews[1] = (ImageView) findViewById(R.id.img_card_details_discipline2);
        disciplineImageViews[2] = (ImageView) findViewById(R.id.img_card_details_discipline3);


    }


    private void setupCardData() {

        TextView txtCardText = (TextView) findViewById(R.id.cardText);
        TextView txtCardType = (TextView) findViewById(R.id.cardType);


        long cardId = getIntent().getExtras().getLong("cardId");

        String query;

        query = QUERY_LIBRARY;

        SQLiteDatabase db = DatabaseHelper.getDatabase();
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(cardId)});
        c.moveToFirst();

        cardName = c.getString(0);
        String cardType = c.getString(1);
        String cardClan = c.getString(2);
        String cardDisciplines = c.getString(3);
        String cardText = c.getString(4);
        String cardPoolCost = c.getString(5);
        String cardBloodCost = c.getString(5);
        String cardArtist = c.getString(6);
        String cardSetRarity = c.getString(7);

        c.close();


        if (cardDisciplines.length() > 0) {
            findViewById(R.id.cardViewDisciplines).setVisibility(View.VISIBLE);
            Utils.updateDisciplineImages(this, disciplineImageViews, cardDisciplines);
        }

        getSupportActionBar().setTitle(cardName);


        txtCardText.setText(cardText);
        txtCardType.setText(cardType);

        Utils.loadCardImage(cardImage, Utils.getCardFileName(cardName), R.drawable.green_back, new Utils.EmptyLoadCardImageAsync() {
            @Override
            public void onImageLoaded(BitmapDrawable image, Palette palette) {

                final TextView txtCardTypeLabel = (TextView) findViewById(R.id.txtCardTypeLabel);
                final TextView txtDisciplinesLabel = (TextView) findViewById(R.id.txtCardDisciplinesLabel);
                final TextView txtCardTextLabel = (TextView) findViewById(R.id.txtCardTextLabel);

                supportStartPostponedEnterTransition();

                final int defaultColor = ContextCompat.getColor(LibraryCardDetailsActivity.this, R.color.colorAccent);

                txtCardTypeLabel.setTextColor(palette.getVibrantColor(defaultColor));
                txtDisciplinesLabel.setTextColor(palette.getVibrantColor(defaultColor));
                txtCardTextLabel.setTextColor(palette.getVibrantColor(defaultColor));


                // Reference: http://stackoverflow.com/questions/30966222/change-color-of-floating-action-button-from-appcompat-22-2-0-programmatically
                fab.setBackgroundTintList(ColorStateList.valueOf(palette.getVibrantColor(defaultColor)));

            }
        });
    }


}

