package name.vampidroid;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import name.vampidroid.data.CryptCard;


/**
 * Created by fxjr on 06/07/16.
 */

public class CryptCardDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CryptCardDetailsActivit";
    private ImageView cardImage;

    // Discipline images

    private ImageView[] disciplineImageViews = new ImageView[7];
    private FloatingActionButton fab;

    private CardDetailsViewModel cardDetailsViewModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CompositeDisposable subscriptions;
    private String shareSubject;
    private String shareBody;
    private CryptCard cryptCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypt_card_details);
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
                showCardImage.putExtra("cardImageFileName", Utils.getCardFileName(cryptCard.getName(), cryptCard.isAdvanced()));
                showCardImage.putExtra("resIdFallbackCardImage", R.drawable.gold_back);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(CryptCardDetailsActivity.this, cardImage, "cardImageTransition").toBundle();
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
        inflater.inflate(R.menu.crypt_details_menu, menu);

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

                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_crypt_card_text)));
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
        disciplineImageViews[3] = (ImageView) findViewById(R.id.img_card_details_discipline4);
        disciplineImageViews[4] = (ImageView) findViewById(R.id.img_card_details_discipline5);
        disciplineImageViews[5] = (ImageView) findViewById(R.id.img_card_details_discipline6);
        disciplineImageViews[6] = (ImageView) findViewById(R.id.img_card_details_discipline7);


    }


    private void setupCardData() {

        final TextView txtCardText = (TextView) findViewById(R.id.cardText);
        final TextView txtCardCapacity = (TextView) findViewById(R.id.txtCardCapacity);
        final TextView txtCardSetRarity = (TextView) findViewById(R.id.txtCardSetRarity);

        cardDetailsViewModel = ((VampiDroidApplication)getApplication()).getCardDetailsViewModel(getIntent().getExtras().getLong("cardId"));

        subscriptions.add(
                cardDetailsViewModel.getCryptCard()
                        .flatMap(new Function<CryptCard, Observable<Pair<Pair<BitmapDrawable, Palette>, Drawable[]>>>() {
                            @Override
                            public Observable<Pair<Pair<BitmapDrawable, Palette>, Drawable[]>> apply(CryptCard cryptCard) throws Exception {
                                CryptCardDetailsActivity.this.cryptCard = cryptCard;

                                setupShareInfo();

                                return Observable.zip(
                                        Utils.loadCryptCardImageWithPalette(cryptCard.getName(), cryptCard.isAdvanced()).subscribeOn(Schedulers.io()),

                                        Utils.getDisciplinesArrayObservable(CryptCardDetailsActivity.this, cryptCard.getDisciplines()).subscribeOn(Schedulers.io()),

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

                                collapsingToolbarLayout.setTitle(cryptCard.getName());
                                txtCardText.setText(cryptCard.getText());
                                txtCardCapacity.setText(cryptCard.getCapacity());
                                txtCardSetRarity.setText(cryptCard.getSetRarity());


                                cardImage.setImageDrawable(imageDrawable);

                                final TextView txtDisciplinesLabel = (TextView) findViewById(R.id.textCardDisciplines);
                                final TextView txtTextLabel = (TextView) findViewById(R.id.textCardText);
                                final TextView txtCapacityLabel = (TextView) findViewById(R.id.txtCapacityLabel);
                                final TextView txtSetRarityLabel = (TextView) findViewById(R.id.txtSetRarityLabel);


                                final int paletteColor = palette.getVibrantColor(ContextCompat.getColor(CryptCardDetailsActivity.this, R.color.colorAccent));


                                txtDisciplinesLabel.setTextColor(paletteColor);
                                txtTextLabel.setTextColor(paletteColor);
                                txtCapacityLabel.setTextColor(paletteColor);
                                txtSetRarityLabel.setTextColor(paletteColor);

                                // Reference: http://stackoverflow.com/questions/30966222/change-color-of-floating-action-button-from-appcompat-22-2-0-programmatically
//                        fab.setBackgroundTintList(ColorStateList.valueOf(palette.getVibrantColor(defaultColor)));

                                Drawable[] disciplineDrawables = data.second;

                                int disciplineIndex;
                                for (disciplineIndex = 0; disciplineIndex < disciplineDrawables.length; disciplineIndex++) {
                                    disciplineImageViews[disciplineIndex].setImageDrawable(disciplineDrawables[disciplineIndex]);
                                    disciplineImageViews[disciplineIndex].setVisibility(View.VISIBLE);
                                }

                                supportStartPostponedEnterTransition();


                            }
                        }));


    }

    private void setupShareInfo() {
        shareSubject = cryptCard.getName();

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Name: %s %n", cryptCard.getName()));
        sb.append(String.format("Capacity: %s %n", cryptCard.getCapacity()));
        sb.append(String.format("Type: %s %n", cryptCard.getType()));
        sb.append(String.format("Group: %s %n", cryptCard.getGroup()));
        sb.append(String.format("Clan: %s %n", cryptCard.getClan()));
        sb.append(String.format("Disciplines: %s %n", cryptCard.getDisciplines()));
        sb.append(String.format("Set/Rarity: %s %n", cryptCard.getSetRarity()));
        sb.append(String.format("Artist: %s %n", cryptCard.getArtist()));
        sb.append(String.format("CardText: %s %n", cryptCard.getText()));


        shareBody = sb.toString();

    }


    @Override
    protected void onResume() {
        super.onResume();


    }
}
