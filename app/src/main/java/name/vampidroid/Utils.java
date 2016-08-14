package name.vampidroid;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import java.io.File;
import java.text.Normalizer;
import java.util.HashMap;

import name.vampidroid.fragments.SettingsFragment;

import static name.vampidroid.fragments.SettingsFragment.DEFAULT_IMAGES_FOLDER;


/**
 * Created by fxjr on 03/07/16.
 */

class Utils {

    private static final String TAG = "Utils";

    private static HashMap<String, Integer> disciplinesDrawableResourcesMap;

    static void loadCardImage(Activity activity, ImageView cardImageView, final String cardImageFileName, final int resIdFallbackCardImage) {


//        Resources res = activity.getResources();
//
//        String cardImagesPath = PreferenceManager.getDefaultSharedPreferences(activity).getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, DEFAULT_IMAGES_FOLDER);
//        String cardFileName = cardImageFileName == null ? "" : getCardFileName(cardImageFileName) + ".jpg";
//
//        File imageFile = new File(cardImagesPath + "/" + cardFileName);
//
//        Bitmap image;
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 1;
//
//        if (imageFile.exists()) {
//            image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
//        } else {
//            options.inSampleSize = 2; // When loading the default image, downsampling it because it is very big.
//            image = BitmapFactory.decodeResource(res, R.drawable.gold_back, options);
//        }
//
//        cardImageView.setImageDrawable(new BitmapDrawable(res, image));

        new LoadImageOperation(activity, cardImageView, cardImageFileName, resIdFallbackCardImage).execute();

    }

    public static String getCardFileName(String cardName, boolean advanced) {

        StringBuilder cardFileName = new StringBuilder();

        // Reference: http://stackoverflow.com/questions/16282083/how-to-ignore-accent-in-sqlite-query-android/16283863#16283863
        // Reference: http://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java

        // // TODO: 14/08/16 Check the size impact of adding the Normalizer class. Maybe use the replace function instead?
        cardFileName.append(Normalizer.normalize(cardName, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]|\\W", "").toLowerCase());

        cardFileName.append(advanced ? "adv" : "");

        cardFileName.append(".jpg");

        return cardFileName.toString();
    }

    public static void setupExpandLayout(final View header, final View layoutToExpand, final ImageView imgArrow) {
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layoutToExpand.isShown()) {
                    layoutToExpand.setVisibility(View.GONE);
                    imgArrow.setImageResource(R.drawable.ic_expand_more_black_24dp);
                } else {

                    imgArrow.setImageResource(R.drawable.ic_expand_less_black_24dp);


//                    Reference: http://stackoverflow.com/questions/19765938/show-and-hide-a-view-with-a-slide-up-down-animation
                    // Prepare the View for the animation
                    layoutToExpand.setVisibility(View.VISIBLE);
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
//						layoutToExpand.setAlpha(0.0f);
//
//						// Start the animation
//						layoutToExpand.animate()
//								.alpha(1.0f);
//					}
                }

            }
        });


    }

    public static void playDrawerToggleAnim(final DrawerArrowDrawable d) {
        float start = d.getProgress();
        float end = Math.abs(start - 1);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator offsetAnimator = ValueAnimator.ofFloat(start, end);
            offsetAnimator.setDuration(300);
            offsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float offset = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        offset = (Float) animation.getAnimatedValue();
                    }
                    d.setProgress(offset);
                }
            });
            offsetAnimator.start();
        }
        else
            d.setProgress(end);
    }


    public static HashMap<String, Integer> getDisciplineStringsDrawableResourcesMap() {
        return disciplinesDrawableResourcesMap;
    }



    static {

        disciplinesDrawableResourcesMap = new HashMap<>();

        disciplinesDrawableResourcesMap.put("abo", R.drawable.ic_dis_abombwe);
        disciplinesDrawableResourcesMap.put("ABO", R.drawable.ic_dis_abombwe_sup);
        disciplinesDrawableResourcesMap.put("ani", R.drawable.ic_dis_animalism);
        disciplinesDrawableResourcesMap.put("ANI", R.drawable.ic_dis_animalism_sup);
        disciplinesDrawableResourcesMap.put("aus", R.drawable.ic_dis_auspex);
        disciplinesDrawableResourcesMap.put("AUS", R.drawable.ic_dis_auspex_sup);
        disciplinesDrawableResourcesMap.put("cel", R.drawable.ic_dis_celerity);
        disciplinesDrawableResourcesMap.put("CEL", R.drawable.ic_dis_celerity_sup);
        disciplinesDrawableResourcesMap.put("chi", R.drawable.ic_dis_chimerstry);
        disciplinesDrawableResourcesMap.put("CHI", R.drawable.ic_dis_chimerstry_sup);
        disciplinesDrawableResourcesMap.put("dai", R.drawable.ic_dis_daimoinon);
        disciplinesDrawableResourcesMap.put("DAI", R.drawable.ic_dis_daimoinon_sup);
        disciplinesDrawableResourcesMap.put("dem", R.drawable.ic_dis_dementation);
        disciplinesDrawableResourcesMap.put("DEM", R.drawable.ic_dis_dementation_sup);
        disciplinesDrawableResourcesMap.put("dom", R.drawable.ic_dis_dominate);
        disciplinesDrawableResourcesMap.put("DOM", R.drawable.ic_dis_dominate_sup);
        disciplinesDrawableResourcesMap.put("for", R.drawable.ic_dis_fortitude);
        disciplinesDrawableResourcesMap.put("FOR", R.drawable.ic_dis_fortitude_sup);
        disciplinesDrawableResourcesMap.put("mel", R.drawable.ic_dis_melpominee);
        disciplinesDrawableResourcesMap.put("MEL", R.drawable.ic_dis_melpominee_sup);
        disciplinesDrawableResourcesMap.put("myt", R.drawable.ic_dis_mytherceria);
        disciplinesDrawableResourcesMap.put("MYT", R.drawable.ic_dis_mytherceria_sup);
        disciplinesDrawableResourcesMap.put("nec", R.drawable.ic_dis_necromancy);
        disciplinesDrawableResourcesMap.put("NEC", R.drawable.ic_dis_necromancy_sup);
        disciplinesDrawableResourcesMap.put("obe", R.drawable.ic_dis_obeah);
        disciplinesDrawableResourcesMap.put("OBE", R.drawable.ic_dis_obeah_sup);
        disciplinesDrawableResourcesMap.put("obf", R.drawable.ic_dis_obfuscate);
        disciplinesDrawableResourcesMap.put("OBF", R.drawable.ic_dis_obfuscate_sup);
        disciplinesDrawableResourcesMap.put("obt", R.drawable.ic_dis_obtenebration);
        disciplinesDrawableResourcesMap.put("OBT", R.drawable.ic_dis_obtenebration_sup);
        disciplinesDrawableResourcesMap.put("pot", R.drawable.ic_dis_potence);
        disciplinesDrawableResourcesMap.put("POT", R.drawable.ic_dis_potence_sup);
        disciplinesDrawableResourcesMap.put("pre", R.drawable.ic_dis_presence);
        disciplinesDrawableResourcesMap.put("PRE", R.drawable.ic_dis_presence_sup);
        disciplinesDrawableResourcesMap.put("pro", R.drawable.ic_dis_protean);
        disciplinesDrawableResourcesMap.put("PRO", R.drawable.ic_dis_protean_sup);
        disciplinesDrawableResourcesMap.put("qui", R.drawable.ic_dis_quietus);
        disciplinesDrawableResourcesMap.put("QUI", R.drawable.ic_dis_quietus_sup);
        disciplinesDrawableResourcesMap.put("san", R.drawable.ic_dis_sanguinus);
        disciplinesDrawableResourcesMap.put("SAN", R.drawable.ic_dis_sanguinus_sup);
        disciplinesDrawableResourcesMap.put("ser", R.drawable.ic_dis_serpentis);
        disciplinesDrawableResourcesMap.put("SER", R.drawable.ic_dis_serpentis_sup);
        disciplinesDrawableResourcesMap.put("spi", R.drawable.ic_dis_spiritus);
        disciplinesDrawableResourcesMap.put("SPI", R.drawable.ic_dis_spiritus_sup);
        disciplinesDrawableResourcesMap.put("tem", R.drawable.ic_dis_temporis);
        disciplinesDrawableResourcesMap.put("TEM", R.drawable.ic_dis_temporis_sup);
        disciplinesDrawableResourcesMap.put("thn", R.drawable.ic_dis_thanatosis);
        disciplinesDrawableResourcesMap.put("THN", R.drawable.ic_dis_thanatosis_sup);
        disciplinesDrawableResourcesMap.put("tha", R.drawable.ic_dis_thaumaturgy);
        disciplinesDrawableResourcesMap.put("THA", R.drawable.ic_dis_thaumaturgy_sup);
        disciplinesDrawableResourcesMap.put("val", R.drawable.ic_dis_valeren);
        disciplinesDrawableResourcesMap.put("VAL", R.drawable.ic_dis_valeren_sup);
        disciplinesDrawableResourcesMap.put("vic", R.drawable.ic_dis_vicissitude);
        disciplinesDrawableResourcesMap.put("VIC", R.drawable.ic_dis_vicissitude_sup);
        disciplinesDrawableResourcesMap.put("vis", R.drawable.ic_dis_visceratika);
        disciplinesDrawableResourcesMap.put("VIS", R.drawable.ic_dis_visceratika_sup);
    }

    public static void updateDisciplineImages(Context context, ImageView[] disciplineImageViews, String cardDisciplines) {
        new Utils.UpdateDisciplineImagesOperation(context, disciplineImageViews).execute(cardDisciplines);
    }


    private static class LoadImageOperation extends AsyncTask<Void, Void, Void> {


        private final ImageView cardImageView;
        private final String cardImageFileName;
        private final Resources resources;
        private BitmapDrawable bitmapDrawable;
        private final String cardImagesPath;
        private final int resIdFallbackCardImage;


        public LoadImageOperation(Activity activity, ImageView cardImageView, String cardImageFileName, int resIdFallbackCardImage) {

            this.resources = activity.getResources();
            this.cardImageView = cardImageView;
            this.cardImageFileName = cardImageFileName;
            this.cardImagesPath = PreferenceManager.getDefaultSharedPreferences(activity).getString(SettingsFragment.KEY_PREF_CARD_IMAGES_FOLDER, DEFAULT_IMAGES_FOLDER);
            this.resIdFallbackCardImage = resIdFallbackCardImage;
        }


        @Override
        protected Void doInBackground(Void... voids) {


//            String cardFileName = cardImageFileName == null ? "" : getCardFileName(cardImageFileName) + ".jpg";

            File imageFile = new File(cardImagesPath + "/" + cardImageFileName);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;

            Bitmap image;


            if (imageFile.exists()) {
                image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            } else {
                options.inSampleSize = 2; // When loading the default image, downsampling it because it is very big.
                // TODO: 05/08/16 Convert the default image to a lower resolution.
                image = BitmapFactory.decodeResource(resources, resIdFallbackCardImage, options);
            }

            bitmapDrawable = new BitmapDrawable(resources, image);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            cardImageView.setImageDrawable(bitmapDrawable);
        }
    }

    public static class UpdateDisciplineImagesOperation extends AsyncTask<String, Void, String[]> {

        private static LruCache<String, Drawable> imageViewsDrawablesMap = new LruCache<>(30);

        private static BitmapFactory.Options options = new BitmapFactory.Options();

        private Resources res;

        private ImageView[] imageViewsToUpdate;

        static  {

            options.inSampleSize = 4;
        }

        public UpdateDisciplineImagesOperation(Context context, ImageView[] imageViews) {

            res = context.getResources();
            imageViewsToUpdate = imageViews;
        }

        @Override
        protected String[] doInBackground(String... strings) {

            String[] disciplines = strings[0].split(" ");
            for (String disciplineKey : disciplines) {
                addImageToCache(disciplineKey);
            }

            return disciplines;
        }

        private void addImageToCache(String disciplineKey) {
            if (imageViewsDrawablesMap.get(disciplineKey) == null) {
                Integer resourceId = Utils.getDisciplineStringsDrawableResourcesMap().get(disciplineKey);
                if (resourceId != null) {
                    imageViewsDrawablesMap.put(disciplineKey, new BitmapDrawable(res, BitmapFactory.decodeResource(res, resourceId, options)));
                }
            }
        }

        @Override
        protected void onPostExecute(String[] disciplines) {

            int disIndex = 0;
            for (String discipline : disciplines) {
                imageViewsToUpdate[disIndex].setImageDrawable(imageViewsDrawablesMap.get(discipline));
                //viewHolder.disciplineImageViews[disIndex].setImageBitmap(imageViewsDrawablesMap.get(discipline));
                imageViewsToUpdate[disIndex].setVisibility(View.VISIBLE);
                disIndex++;

            }

        }
    }
}

