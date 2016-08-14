package name.vampidroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import java.lang.ref.WeakReference;
import java.util.HashMap;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_card_details);
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
                showCardImage.putExtra("cardName", cardName);
                showCardImage.putExtra("cardType", 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(LibraryCardDetailsActivity.this, cardImage, "cardImageTransition").toBundle();
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


    }


    private void setupCardData() {

        TextView txtCardText = (TextView) findViewById(R.id.cardText);
        TextView txtCardType = (TextView) findViewById(R.id.cardType);




        long cardId = getIntent().getExtras().getLong("cardId");

        String query;

        query = QUERY_LIBRARY;

        SQLiteDatabase db = DatabaseHelper.getDatabase();
        Cursor c = db.rawQuery(query, new String[] {String.valueOf(cardId)});
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



//        String[] disciplines = cardDisciplines.split(" ");
//
//        int disIndex = 0;
//        for (String discipline : disciplines) {
//            disciplineImageViews[disIndex].setImageDrawable(imageViewsDrawablesMap.get(discipline));
//            //viewHolder.disciplineImageViews[disIndex].setImageBitmap(imageViewsDrawablesMap.get(discipline));
//            disciplineImageViews[disIndex].setVisibility(View.VISIBLE);
//            disIndex++;
//        }
//
//        clearDisciplineImageViews(disIndex);

        Utils.updateDisciplineImages(this, disciplineImageViews, cardDisciplines);

        getSupportActionBar().setTitle(cardName);


        txtCardText.setText(cardText);
        txtCardType.setText(cardType);

        Utils.loadCardImage(this, cardImage, cardName, 1);
    }


}

