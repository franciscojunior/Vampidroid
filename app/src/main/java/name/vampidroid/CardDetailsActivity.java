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

public class CardDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CardDetailsActivity";
    private static String QUERY_CRYPT = "select Name, Type, Clan, Disciplines, CardText, Capacity, Artist, _Set, _Group from crypt where _id = ?";
    private ImageView cardImage;
    private String cardName;


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
                showCardImage.putExtra("cardName", cardName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(CardDetailsActivity.this, cardImage, "cardImageTransition").toBundle();
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

        Log.d(TAG, "setupCardData: Thread Id: " + Thread.currentThread().getId());

        new UpdateDisciplineImagesOperation().execute(cardDisciplines);

        getSupportActionBar().setTitle(cardName);
        txtCardText.setText(cardText);

        Utils.loadCardImage(cardImage, cardName, getResources());
    }


    //    Reference: http://stackoverflow.com/questions/3243215/how-to-use-weakreference-in-java-and-android-development
    private static WeakReference<HashMap<String, Drawable>> imageViewsDrawablesMapReference;


    private static void fillImageViewsDrawablesMap(Context context) {

        HashMap<String, Drawable> imageViewsDrawablesMap;

        if (imageViewsDrawablesMapReference == null || imageViewsDrawablesMapReference.get() == null) {
            imageViewsDrawablesMap = new HashMap<>();
            imageViewsDrawablesMapReference = new WeakReference<>(imageViewsDrawablesMap);
        } else {
            imageViewsDrawablesMap = imageViewsDrawablesMapReference.get();
        }

        if (imageViewsDrawablesMap.isEmpty()) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Resources res = context.getResources();


            imageViewsDrawablesMap.put("abo", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_abombwe, options)));
            imageViewsDrawablesMap.put("ABO", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_abombwe_sup, options)));
            imageViewsDrawablesMap.put("ani", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_animalism, options)));
            imageViewsDrawablesMap.put("ANI", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_animalism_sup, options)));
            imageViewsDrawablesMap.put("aus", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_auspex, options)));
            imageViewsDrawablesMap.put("AUS", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_auspex_sup, options)));
            imageViewsDrawablesMap.put("cel", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_celerity, options)));
            imageViewsDrawablesMap.put("CEL", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_celerity_sup, options)));
            imageViewsDrawablesMap.put("chi", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_chimerstry, options)));
            imageViewsDrawablesMap.put("CHI", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_chimerstry_sup, options)));
            imageViewsDrawablesMap.put("dai", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_daimoinon, options)));
            imageViewsDrawablesMap.put("DAI", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_daimoinon_sup, options)));
            imageViewsDrawablesMap.put("dem", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_dementation, options)));
            imageViewsDrawablesMap.put("DEM", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_dementation_sup, options)));
            imageViewsDrawablesMap.put("dom", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_dominate, options)));
            imageViewsDrawablesMap.put("DOM", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_dominate_sup, options)));
            imageViewsDrawablesMap.put("for", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_fortitude, options)));
            imageViewsDrawablesMap.put("FOR", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_fortitude_sup, options)));
            imageViewsDrawablesMap.put("mel", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_melpominee, options)));
            imageViewsDrawablesMap.put("MEL", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_melpominee_sup, options)));
            imageViewsDrawablesMap.put("myt", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_mytherceria, options)));
            imageViewsDrawablesMap.put("MYT", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_mytherceria_sup, options)));
            imageViewsDrawablesMap.put("nec", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_necromancy, options)));
            imageViewsDrawablesMap.put("NEC", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_necromancy_sup, options)));
            imageViewsDrawablesMap.put("obe", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_obeah, options)));
            imageViewsDrawablesMap.put("OBE", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_obeah_sup, options)));
            imageViewsDrawablesMap.put("obf", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_obfuscate, options)));
            imageViewsDrawablesMap.put("OBF", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_obfuscate_sup, options)));
            imageViewsDrawablesMap.put("obt", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_obtenebration, options)));
            imageViewsDrawablesMap.put("OBT", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_obtenebration_sup, options)));
            imageViewsDrawablesMap.put("pot", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_potence, options)));
            imageViewsDrawablesMap.put("POT", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_potence_sup, options)));
            imageViewsDrawablesMap.put("pre", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_presence, options)));
            imageViewsDrawablesMap.put("PRE", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_presence_sup, options)));
            imageViewsDrawablesMap.put("pro", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_protean, options)));
            imageViewsDrawablesMap.put("PRO", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_protean_sup, options)));
            imageViewsDrawablesMap.put("qui", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_quietus, options)));
            imageViewsDrawablesMap.put("QUI", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_quietus_sup, options)));
            imageViewsDrawablesMap.put("san", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_sanguinus, options)));
            imageViewsDrawablesMap.put("SAN", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_sanguinus_sup, options)));
            imageViewsDrawablesMap.put("ser", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_serpentis, options)));
            imageViewsDrawablesMap.put("SER", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_serpentis_sup, options)));
            imageViewsDrawablesMap.put("spi", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_spiritus, options)));
            imageViewsDrawablesMap.put("SPI", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_spiritus_sup, options)));
            imageViewsDrawablesMap.put("tem", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_temporis, options)));
            imageViewsDrawablesMap.put("TEM", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_temporis_sup, options)));
            imageViewsDrawablesMap.put("thn", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_thanatosis, options)));
            imageViewsDrawablesMap.put("THN", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_thanatosis_sup, options)));
            imageViewsDrawablesMap.put("tha", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_thaumaturgy, options)));
            imageViewsDrawablesMap.put("THA", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_thaumaturgy_sup, options)));
            imageViewsDrawablesMap.put("val", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_valeren, options)));
            imageViewsDrawablesMap.put("VAL", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_valeren_sup, options)));
            imageViewsDrawablesMap.put("vic", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_vicissitude, options)));
            imageViewsDrawablesMap.put("VIC", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_vicissitude_sup, options)));
            imageViewsDrawablesMap.put("vis", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_visceratika, options)));
            imageViewsDrawablesMap.put("VIS", new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.ic_dis_visceratika_sup, options)));
        }
    }

    // Discipline images

    private ImageView[] disciplineImageViews = new ImageView[6];

    private void clearDisciplineImageViews() {
        clearDisciplineImageViews(0);

    }

    private void clearDisciplineImageViews(int fromIndex) {
        // Optimization to clear imageviews only from fromIndex and above.
        for (int i = fromIndex; i < disciplineImageViews.length; i++) {
            disciplineImageViews[i].setVisibility(View.INVISIBLE);

        }

    }




    private class UpdateDisciplineImagesOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            Log.d(TAG, "doInBackground: Thread Id: " + Thread.currentThread().getId());
            fillImageViewsDrawablesMap(CardDetailsActivity.this);

            return strings[0];
        }

        @Override
        protected void onPostExecute(String cardDisciplines) {

            Log.d(TAG, "onPostExecute... ");

            Log.d(TAG, "onPostExecute: Thread Id: " + Thread.currentThread().getId());

            String[] disciplines = cardDisciplines.split(" ");

            HashMap<String, Drawable> imageViewsDrawablesMap = imageViewsDrawablesMapReference.get();
            if (imageViewsDrawablesMap != null) {

                int disIndex = 0;
                for (String discipline : disciplines) {
                    disciplineImageViews[disIndex].setImageDrawable(imageViewsDrawablesMap.get(discipline));
                    //viewHolder.disciplineImageViews[disIndex].setImageBitmap(imageViewsDrawablesMap.get(discipline));
                    disciplineImageViews[disIndex].setVisibility(View.VISIBLE);
                    disIndex++;
                }

                clearDisciplineImageViews(disIndex);
            } else {
                clearDisciplineImageViews();
            }





        }
    }
}
