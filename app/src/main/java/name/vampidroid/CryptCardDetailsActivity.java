package name.vampidroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypt_card_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            collapsingToolbarLayout.setExpandedTitleMarginBottom(220);
            collapsingToolbarLayout.setExpandedTitleMarginStart(200);


            // If we are in landscape, let the tile fixed on top.
            // Reference: http://stackoverflow.com/questions/2795833/check-orientation-on-android-phone#2799001
            //collapsingToolbarLayout.setTitleEnabled(false);
//            toolbar.setTitleTextAppearance(this, R.style.CardDetailsCollapsedAppBarTitle);

//            Reference: http://stackoverflow.com/questions/36560292/how-to-increase-font-size-of-title-in-collapsingtoolbarlayout
            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CardDetailsCollapsedAppBarTitle);

        }


        //fillImageViewsDrawablesMap(this);


        setupDisciplineImagesArray();

        setupCardData();

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


        Utils.updateDisciplineImages(this, disciplineImageViews, cardDisciplines);


        getSupportActionBar().setTitle(cardName);
        txtCardText.setText(cardText);

        Utils.loadCardImage(this, cardImage, Utils.getCardFileName(cardName, cardAdvanced.length() > 0), R.drawable.gold_back);
    }
}
