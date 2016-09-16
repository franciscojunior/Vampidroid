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
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by fxjr on 06/07/16.
 */

public class CryptCardDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CryptCardDetailsActivit";
    private static String QUERY_CRYPT = "select Name, Type, Clan, Disciplines, CardText, Capacity, Artist, _Set, _Group, Adv from crypt where _id = ?";
    private ImageView cardImage;
    private String cardName;

    // Discipline images

    private ImageView[] disciplineImageViews = new ImageView[7];
    private String cardAdvanced;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypt_card_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                showCardImage.putExtra("cardImageFileName", Utils.getCardFileName(cardName, cardAdvanced.length() > 0));
                showCardImage.putExtra("resIdFallbackCardImage", R.drawable.gold_back);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(CryptCardDetailsActivity.this, cardImage, "cardImageTransition").toBundle();
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
        Log.d(TAG, "onBackPressed() called");
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
        disciplineImageViews[3] = (ImageView) findViewById(R.id.img_card_details_discipline4);
        disciplineImageViews[4] = (ImageView) findViewById(R.id.img_card_details_discipline5);
        disciplineImageViews[5] = (ImageView) findViewById(R.id.img_card_details_discipline6);
        disciplineImageViews[6] = (ImageView) findViewById(R.id.img_card_details_discipline7);


    }



    private void setupCardData() {

        TextView txtCardText = (TextView) findViewById(R.id.cardText);



        long cardId = getIntent().getExtras().getLong("cardId");

        String query;

        query = QUERY_CRYPT;

        SQLiteDatabase db = DatabaseHelper.getDatabase();
        Cursor c = db.rawQuery(query, new String[] {String.valueOf(cardId)});
        c.moveToFirst();

        cardName = c.getString(0);
        String cardType = c.getString(1);
        String cardClan = c.getString(2);
        String cardDisciplines = c.getString(3);
        String cardText = c.getString(4);
        String cardCapacity = c.getString(5);
        String cardArtist = c.getString(6);
        String cardSetRarity = c.getString(7);
        String cardGroup = c.getString(8);
        cardAdvanced = c.getString(9);

        c.close();

        //        Reference: https://plus.google.com/+AlexLockwood/posts/FJsp1N9XNLS
        supportPostponeEnterTransition();

//        Utils.updateDisciplineImages(this, disciplineImageViews, cardDisciplines);

        updateDisciplineImages(cardDisciplines);


        getSupportActionBar().setTitle(cardName);
        txtCardText.setText(cardText);


        Picasso
                .with(this)
                .load(Utils.getCardFileNameFullPath(Utils.getCardFileName(cardName, cardAdvanced.length() > 0)))
                .transform(Utils.PaletteTransformation.instance())
                .noFade()
                .placeholder(R.drawable.gold_back)
                .into(cardImage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                        supportStartPostponedEnterTransition();

                        Palette p = Utils.PaletteTransformation.getPalette(((BitmapDrawable) cardImage.getDrawable()).getBitmap());

                        final TextView txtDisciplinesLabel = (TextView) findViewById(R.id.textCardDisciplines);
                        final TextView txtTextLabel = (TextView) findViewById(R.id.textCardText);

                        final int defaultColor = ContextCompat.getColor(CryptCardDetailsActivity.this, R.color.colorAccent);

                        txtDisciplinesLabel.setTextColor(p.getVibrantColor(defaultColor));
                        txtTextLabel.setTextColor(p.getVibrantColor(defaultColor));


                        // Reference: http://stackoverflow.com/questions/30966222/change-color-of-floating-action-button-from-appcompat-22-2-0-programmatically
                        fab.setBackgroundTintList(ColorStateList.valueOf(p.getVibrantColor(defaultColor)));

                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();

                    }
                });



//        Utils.loadCardImage(cardImage, Utils.getCardFileName(cardName, cardAdvanced.length() > 0), R.drawable.gold_back, new Utils.LoadCardImageAsync() {
//            @Override
//            public void onImageLoaded(BitmapDrawable image) {
//
//                supportStartPostponedEnterTransition();
//
//                final TextView txtDisciplinesLabel = (TextView) findViewById(R.id.textCardDisciplines);
//                final TextView txtTextLabel = (TextView) findViewById(R.id.textCardText);
//
//
//                Palette.from(image.getBitmap()).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette p) {
//
//                        final int defaultColor = ContextCompat.getColor(CryptCardDetailsActivity.this, R.color.colorAccent);
//
//                        txtDisciplinesLabel.setTextColor(p.getVibrantColor(defaultColor));
//                        txtTextLabel.setTextColor(p.getVibrantColor(defaultColor));
//
//
//                        // Reference: http://stackoverflow.com/questions/30966222/change-color-of-floating-action-button-from-appcompat-22-2-0-programmatically
//                        fab.setBackgroundTintList(ColorStateList.valueOf(p.getVibrantColor(defaultColor)));
//                    }
//                });
//
//
//
//            }
//        });

//        Utils.loadCardImage(cardImage, Utils.getCardFileName(cardName, cardAdvanced.length() > 0), R.drawable.gold_back, new Utils.EmptyLoadCardImageWithPalette() {
//            @Override
//            public void onImageLoaded(Palette p) {
//
//                supportStartPostponedEnterTransition();
//
//                final TextView txtDisciplinesLabel = (TextView) findViewById(R.id.textCardDisciplines);
//                final TextView txtTextLabel = (TextView) findViewById(R.id.textCardText);
//
//
//                final int defaultColor = ContextCompat.getColor(CryptCardDetailsActivity.this, R.color.colorAccent);
//
//                txtDisciplinesLabel.setTextColor(p.getVibrantColor(defaultColor));
//                txtTextLabel.setTextColor(p.getVibrantColor(defaultColor));
//
//
//                // Reference: http://stackoverflow.com/questions/30966222/change-color-of-floating-action-button-from-appcompat-22-2-0-programmatically
//                fab.setBackgroundTintList(ColorStateList.valueOf(p.getVibrantColor(defaultColor)));
//
//            }
//        });
    }

    private void updateDisciplineImages(String cardDisciplines) {

        int disIndex = 0;
        SimpleArrayMap<String, Integer> disciplinesDrawableResourceIdsMap = Utils.getDisciplinesDrawableResourceIdsMap();

        for (String discipline: cardDisciplines.split(" ")) {
            Integer disciplineDrawableResourceId = disciplinesDrawableResourceIdsMap.get(discipline);
            if (disciplineDrawableResourceId == null)
                continue;
            Picasso
                    .with(this)
                    .load(disciplineDrawableResourceId)
                    .noFade()
                    .fit()
                    .into(disciplineImageViews[disIndex]);

            disciplineImageViews[disIndex++].setVisibility(View.VISIBLE);
        }

    }


}
