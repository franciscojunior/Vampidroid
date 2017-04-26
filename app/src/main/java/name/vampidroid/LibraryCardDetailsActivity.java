package name.vampidroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by fxjr on 06/07/16.
 */

public class LibraryCardDetailsActivity extends AppCompatActivity {

    private static final String TAG = "LibraryCardDetailsActiv";
    private ImageView cardImage;
    private String cardName;

    // Discipline images

    private ImageView[] disciplineImageViews = new ImageView[3];
    private FloatingActionButton fab;
    private String cardDisciplines;

    private CardDetailsViewModel cardDetailsViewModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CompositeDisposable subscriptions;
    private String cardText;
    private String cardType;
    private String shareSubject;
    private String shareBody;
    private String cardPoolCost;
    private String cardBloodCost;
    private String cardSetRarity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_card_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

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

        subscriptions = new CompositeDisposable();

        setupCardData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.library_details_menu, menu);

        return true;

    }

    //    Reference: https://github.com/codepath/android_guides/wiki/Shared-Element-Activity-Transition
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            case R.id.action_share:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_library_card_text)));
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

//        fab.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fab.show();
//            }
//        }, 300);

    }


    private void setupDisciplineImagesArray() {

        disciplineImageViews[0] = (ImageView) findViewById(R.id.img_card_details_discipline1);
        disciplineImageViews[1] = (ImageView) findViewById(R.id.img_card_details_discipline2);
        disciplineImageViews[2] = (ImageView) findViewById(R.id.img_card_details_discipline3);


    }


    private void setupCardData() {

        final TextView txtCardText = (TextView) findViewById(R.id.cardText);
        final TextView txtCardType = (TextView) findViewById(R.id.cardType);
        final TextView txtCardCost = (TextView) findViewById(R.id.txtCardCost);
        final TextView txtCardSetRarity = (TextView) findViewById(R.id.txtCardSetRarity);


        cardDetailsViewModel = ((VampiDroidApplication)getApplication()).getCardDetailsViewModel(getIntent().getExtras().getLong("cardId"));


        subscriptions.add(
                cardDetailsViewModel.getLibraryCard()
                        .flatMap(new Function<Cursor, Observable<Pair<Pair<BitmapDrawable, Palette>, Drawable[]>>>() {
                            @Override
                            public Observable<Pair<Pair<BitmapDrawable, Palette>, Drawable[]>> apply(Cursor c) throws Exception {
                                cardName = c.getString(0);
                                cardType = c.getString(1);
                                String cardClan = c.getString(2);
                                cardDisciplines = c.getString(3);
                                cardText = c.getString(4);
                                cardPoolCost = c.getString(5);
                                cardBloodCost = c.getString(6);
                                String cardArtist = c.getString(7);
                                cardSetRarity = c.getString(8);

                                c.close();


                                setupShareInfo(cardType, cardClan, cardPoolCost, cardBloodCost, cardArtist, cardSetRarity);


                                return Observable.zip(
                                        Utils.loadLibraryCardImageWithPalette(cardName).subscribeOn(Schedulers.io()),

                                        Utils.getDisciplinesArrayObservable(LibraryCardDetailsActivity.this, cardDisciplines).subscribeOn(Schedulers.io()),

                                        new BiFunction<Pair<BitmapDrawable, Palette>, Drawable[], Pair<Pair<BitmapDrawable, Palette>, Drawable[]>>() {
                                            @Override
                                            public Pair<Pair<BitmapDrawable, Palette>, Drawable[]> apply(Pair<BitmapDrawable, Palette> bitmapDrawablePalettePair, Drawable[] drawables) throws Exception {
                                                return Pair.create(bitmapDrawablePalettePair, drawables);
                                            }
                                        }

                                );
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Pair<Pair<BitmapDrawable, Palette>, Drawable[]>>() {
                            @Override
                            public void accept(Pair<Pair<BitmapDrawable, Palette>, Drawable[]> data) throws Exception {

                                Pair<BitmapDrawable, Palette> bitmapDrawablePalettePair = data.first;

                                BitmapDrawable imageDrawable = bitmapDrawablePalettePair.first;
                                Palette palette = bitmapDrawablePalettePair.second;

                                collapsingToolbarLayout.setTitle(cardName);
                                txtCardText.setText(cardText);
                                txtCardType.setText(cardType);
                                txtCardSetRarity.setText(cardSetRarity);


                                cardImage.setImageDrawable(imageDrawable);

                                final TextView txtCardTypeLabel = (TextView) findViewById(R.id.txtCardTypeLabel);
                                final TextView txtDisciplinesLabel = (TextView) findViewById(R.id.txtCardDisciplinesLabel);
                                final TextView txtCardTextLabel = (TextView) findViewById(R.id.txtCardTextLabel);
                                final TextView txtCardCostLabel = (TextView) findViewById(R.id.txtCardCostLabel);
                                final TextView txtCardSetRarityLabel = (TextView) findViewById(R.id.txtSetRarityLabel);

                                final int defaultColor = ContextCompat.getColor(LibraryCardDetailsActivity.this, R.color.colorAccent);

                                txtCardTypeLabel.setTextColor(palette.getVibrantColor(defaultColor));
                                txtDisciplinesLabel.setTextColor(palette.getVibrantColor(defaultColor));
                                txtCardTextLabel.setTextColor(palette.getVibrantColor(defaultColor));
                                txtCardCostLabel.setTextColor(palette.getVibrantColor(defaultColor));
                                txtCardSetRarityLabel.setTextColor(palette.getVibrantColor(defaultColor));


                                if (!cardBloodCost.isEmpty()) {
                                    txtCardCostLabel.setText("Blood Cost");
                                    txtCardCost.setText(cardBloodCost);
                                } else if (!cardPoolCost.isEmpty()) {
                                    txtCardCostLabel.setText("Pool Cost");
                                    txtCardCost.setText(cardPoolCost);
                                }





                                // Reference: http://stackoverflow.com/questions/30966222/change-color-of-floating-action-button-from-appcompat-22-2-0-programmatically
//                                fab.setBackgroundTintList(ColorStateList.valueOf(palette.getVibrantColor(defaultColor)));

                                Drawable[] drawables = data.second;

                                if (cardDisciplines.length() > 0) {
                                    findViewById(R.id.cardViewDisciplines).setVisibility(View.VISIBLE);
                                    for (int disIndex = 0; disIndex < drawables.length; disIndex++) {
                                        disciplineImageViews[disIndex].setImageDrawable(drawables[disIndex]);
                                        disciplineImageViews[disIndex].setVisibility(View.VISIBLE);
                                    }
                                }

                                supportStartPostponedEnterTransition();


                            }
                        }));

    }

    private void setupShareInfo(String cardType, String cardClan, String cardPoolCost, String cardBloodCost, String cardArtist, String cardSetRarity) {

        shareSubject = cardName;

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Name: %s %n", cardName));
        sb.append(String.format("Type: %s %n", cardType));
        sb.append(String.format("Clan: %s %n", cardClan));
        sb.append(String.format("PoolCost: %s %n", cardPoolCost));
        sb.append(String.format("BloodCost: %s %n", cardBloodCost));
        sb.append(String.format("Disciplines: %s %n", cardDisciplines));
        sb.append(String.format("Set/Rarity: %s %n", cardSetRarity));
        sb.append(String.format("Artist: %s %n", cardArtist));
        sb.append(String.format("CardText: %s %n", cardText));

        shareBody = sb.toString();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

}

