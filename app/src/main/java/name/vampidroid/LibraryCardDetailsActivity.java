package name.vampidroid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import name.vampidroid.data.LibraryCard;


/**
 * Created by fxjr on 06/07/16.
 */

public class LibraryCardDetailsActivity extends AppCompatActivity {

    private static final String TAG = "LibraryCardDetailsActiv";
    private ImageView cardImage;

    // Discipline images

    private static int[] imageViewIds = {
            R.id.img_card_details_discipline1,
            R.id.img_card_details_discipline2,
            R.id.img_card_details_discipline3
    };

    private CardDetailsViewModel cardDetailsViewModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CompositeDisposable subscriptions;
    private LibraryCard libraryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_card_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        //        Reference: https://plus.google.com/+AlexLockwood/posts/FJsp1N9XNLS
        supportPostponeEnterTransition();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cardImage = findViewById(R.id.cardImage);


        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent showCardImage = new Intent(view.getContext(), CardImageActivity.class);
                showCardImage.putExtra("cardId", getIntent().getExtras().getLong("cardId"));
                showCardImage.putExtra("cardImageFileName", Utils.getFullCardFileName(libraryCard.getName()));
                showCardImage.putExtra("resIdFallbackCardImage", R.drawable.green_back);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(LibraryCardDetailsActivity.this, cardImage, "cardImageTransition").toBundle();
                    view.getContext().startActivity(showCardImage, bundle);
                } else
                    view.getContext().startActivity(showCardImage);


            }
        });

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

                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, libraryCard.getName());

                StringBuilder sb = new StringBuilder();

                sb.append(String.format("Name: %s %n", libraryCard.getName()));
                sb.append(String.format("Type: %s %n", libraryCard.getType()));
                sb.append(String.format("Clan: %s %n", libraryCard.getClan()));
                sb.append(String.format("PoolCost: %s %n", libraryCard.getPoolCost()));
                sb.append(String.format("BloodCost: %s %n", libraryCard.getBloodCost()));
                sb.append(String.format("Disciplines: %s %n", libraryCard.getDisciplines()));
                sb.append(String.format("Set/Rarity: %s %n", libraryCard.getSetRarity()));
                sb.append(String.format("Artist: %s %n", libraryCard.getArtist()));
                sb.append(String.format("CardText: %s %n", libraryCard.getText()));

                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());

                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_library_card_text)));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void setupCardData() {

        final TextView txtCardText = findViewById(R.id.cardText);
        final TextView txtCardType = findViewById(R.id.txtCardType);
        final TextView txtCardCost = findViewById(R.id.txtCardCost);
        final TextView txtCardSetRarity = findViewById(R.id.txtCardSetRarity);


        cardDetailsViewModel = ViewModelProviders.of(this).get(CardDetailsViewModel.class);

        subscriptions.add(
                cardDetailsViewModel.getLibraryCard(getIntent().getExtras().getLong("cardId"))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LibraryCard>() {
                            @Override
                            public void accept(LibraryCard libraryCard) throws Exception {

                                LibraryCardDetailsActivity.this.libraryCard = libraryCard;

                                collapsingToolbarLayout.setTitle(libraryCard.getName());
                                txtCardText.setText(libraryCard.getText());
                                txtCardType.setText(libraryCard.getType());
                                txtCardSetRarity.setText(libraryCard.getSetRarity());


                                final TextView txtCardTypeLabel = findViewById(R.id.txtCardTypeLabel);
                                final TextView txtDisciplinesLabel = findViewById(R.id.txtCardDisciplinesLabel);
                                final TextView txtCardTextLabel = findViewById(R.id.txtCardTextLabel);
                                final TextView txtCardCostLabel = findViewById(R.id.txtCardCostLabel);
                                final TextView txtCardSetRarityLabel = findViewById(R.id.txtCardSetRarityLabel);

                                if (!libraryCard.getBloodCost().isEmpty()) {
                                    txtCardCostLabel.setText("Blood Cost:");
                                    txtCardCost.setText(libraryCard.getBloodCost());
                                } else if (!libraryCard.getPoolCost().isEmpty()) {
                                    txtCardCostLabel.setText("Pool Cost:");
                                    txtCardCost.setText(libraryCard.getPoolCost());
                                } else {
                                    txtCardCostLabel.setVisibility(View.GONE);
                                    txtCardCost.setVisibility(View.GONE);
                                }

                                Glide.with(LibraryCardDetailsActivity.this)
                                        .load(Utils.getFullCardFileName(libraryCard.getName()))
                                        .asBitmap()
                                        .error(R.drawable.green_back)
//                                        .fitCenter()
                                        .dontAnimate()
                                        .into(new BitmapImageViewTarget(cardImage) {
                                            @Override
                                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                super.onLoadFailed(e, errorDrawable);
                                                supportStartPostponedEnterTransition();
                                            }

                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                super.onResourceReady(bitmap, glideAnimation);
                                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                                    @Override
                                                    public void onGenerated(Palette palette) {
                                                        // Here's your generated palette
                                                        final int paletteColor = palette.getVibrantColor(ContextCompat.getColor(LibraryCardDetailsActivity.this, R.color.colorAccent));
//

                                                        txtCardTypeLabel.setTextColor(palette.getVibrantColor(paletteColor));
                                                        txtDisciplinesLabel.setTextColor(palette.getVibrantColor(paletteColor));
                                                        txtCardTextLabel.setTextColor(palette.getVibrantColor(paletteColor));
                                                        txtCardCostLabel.setTextColor(palette.getVibrantColor(paletteColor));
                                                        txtCardSetRarityLabel.setTextColor(palette.getVibrantColor(paletteColor));
//
//                                                      // Reference: http://stackoverflow.com/questions/30966222/change-color-of-floating-action-button-from-appcompat-22-2-0-programmatically
//                                                      // fab.setBackgroundTintList(ColorStateList.valueOf(palette.getVibrantColor(defaultColor)));
//
                                                        supportStartPostponedEnterTransition();
                                                    }
                                                });
                                            }
                                        });


                                String disciplinesText = libraryCard.getDisciplines().trim();

                                if (disciplinesText.length() > 0) {
                                    txtDisciplinesLabel.setVisibility(View.VISIBLE);

                                    final Integer[] disciplinesDrawables = Utils.getDisciplinesDrawablesIDs(libraryCard.getDisciplines());

                                    for (int disciplineIndex = 0; disciplineIndex < disciplinesDrawables.length; disciplineIndex++) {

                                        Integer disciplineDrawableID = disciplinesDrawables[disciplineIndex];

                                        if (disciplineDrawableID != null) {
                                            ImageView disciplineImageView = findViewById(imageViewIds[disciplineIndex]);
                                            Glide.with(LibraryCardDetailsActivity.this)
                                                    .load(disciplineDrawableID)
                                                    .fitCenter()
                                                    .into(disciplineImageView);
                                            disciplineImageView.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }

                                supportStartPostponedEnterTransition();


                            }
                        }));

    }

}

