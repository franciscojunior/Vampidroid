package name.vampidroid;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.text.Normalizer;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * Created by fxjr on 03/07/16.
 */

public class Utils {

    private static final String TAG = "Utils";

    static SimpleArrayMap<String, Integer> disciplinesDrawableResourceIdsMap;
    static Resources resources;



    interface LoadCardImageAsync {

        public void onImageLoaded(BitmapDrawable image);

    }
    static String cardImagesPath;

    public static void setResources(Resources resources) {
        Utils.resources = resources;
    }

    public static void setCardImagesPath(String cardImagesPath) {
        Utils.cardImagesPath = cardImagesPath;
    }

    public static String getCardImagesPath() {
        return cardImagesPath;
    }


    static void loadCardImageThumbnail(ImageView cardImageView, final String cardImageFileName, final int resIdFallbackCardImage) {

//        cardImageView.setImageDrawable(null);
//        AsyncTask loadTask = (AsyncTask)cardImageView.getTag();
//
//        if (loadTask != null) {
//            loadTask.cancel(false);
//        }
//        cardImageView.setTag(new LoadImageOperation(cardImageView, cardImageFileName, resIdFallbackCardImage, 8).execute());

        File imageFile = new File(cardImagesPath + "/" + cardImageFileName);

        Picasso.with(cardImageView.getContext()).load(imageFile).placeholder(resIdFallbackCardImage).fit().centerInside().into(cardImageView);

    }

    static void loadCardImage(final ImageView cardImageView, final String cardImageFileName, final int resIdFallbackCardImage, final LoadCardImageAsync callback) {

        new LoadImageOperation(cardImageView, cardImageFileName, resIdFallbackCardImage, callback).execute();
//        File imageFile = new File(cardImagesPath + "/" + cardImageFileName);
//
//        Picasso
//                .with(cardImageView.getContext().getApplicationContext())
//                .load(imageFile)
//                .error(resIdFallbackCardImage)
//                .into(cardImageView, new Callback.EmptyCallback() {
//                    @Override
//                    public void onSuccess() {
//                        callback.onImageLoaded((BitmapDrawable)cardImageView.getDrawable());
//
//                    }
//                });


    }

    static void loadCardImage(final ImageView cardImageView, final String cardImageFileName, final int resIdFallbackCardImage, final LoadCardImageWithPalette callback) {

//        new LoadImageOperation(cardImageView, cardImageFileName, resIdFallbackCardImage, callback).execute();
        File imageFile = new File(cardImagesPath + "/" + cardImageFileName);

        Picasso
                .with(cardImageView.getContext())
                .load(imageFile)
                .transform(PaletteTransformation.instance())
                .error(resIdFallbackCardImage)
                .into(cardImageView, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                        callback.onImageLoaded(PaletteTransformation.getPalette(((BitmapDrawable) cardImageView.getDrawable()).getBitmap()));

                    }
                });


    }


    static void loadCardImage(ImageView cardImageView, final String cardImageFileName, final int resIdFallbackCardImage) {


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

        new LoadImageOperation(cardImageView, cardImageFileName, resIdFallbackCardImage).execute();

    }

    public static File getCardFileNameFullPath(String cardFileName) {

        File imageFile = new File(cardImagesPath + "/" + cardFileName);
        return imageFile;
    }

    static String getCardFileName(String cardName) {
        return getCardFileName(cardName, false);
    }

    static String getCardFileName(String cardName, boolean advanced) {

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

    static void setupExpandLayout(final View header, final View layoutToExpand, final ImageView imgArrow) {
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

    static void playDrawerToggleAnim(final DrawerArrowDrawable d) {
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


    static LruCache<String, Drawable> disciplineDrawablesCache = new LruCache<String, Drawable>(15) {

        @Override
        protected void entryRemoved(boolean evicted, String key, Drawable oldValue, Drawable newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            ((BitmapDrawable) oldValue).getBitmap().recycle();
        }
    };


    public static SimpleArrayMap<String, Integer> getDisciplinesDrawableResourceIdsMap() {
        return disciplinesDrawableResourceIdsMap;
    }

    static {

        disciplinesDrawableResourceIdsMap = new SimpleArrayMap<>();

        disciplinesDrawableResourceIdsMap.put("abo", R.drawable.ic_dis_abombwe);
        disciplinesDrawableResourceIdsMap.put("Abombwe", R.drawable.ic_dis_abombwe);
        disciplinesDrawableResourceIdsMap.put("ABO", R.drawable.ic_dis_abombwe_sup);
        disciplinesDrawableResourceIdsMap.put("ani", R.drawable.ic_dis_animalism);
        disciplinesDrawableResourceIdsMap.put("Animalism", R.drawable.ic_dis_animalism);
        disciplinesDrawableResourceIdsMap.put("ANI", R.drawable.ic_dis_animalism_sup);
        disciplinesDrawableResourceIdsMap.put("aus", R.drawable.ic_dis_auspex);
        disciplinesDrawableResourceIdsMap.put("Auspex", R.drawable.ic_dis_auspex);
        disciplinesDrawableResourceIdsMap.put("AUS", R.drawable.ic_dis_auspex_sup);
        disciplinesDrawableResourceIdsMap.put("cel", R.drawable.ic_dis_celerity);
        disciplinesDrawableResourceIdsMap.put("Celerity", R.drawable.ic_dis_celerity);
        disciplinesDrawableResourceIdsMap.put("CEL", R.drawable.ic_dis_celerity_sup);
        disciplinesDrawableResourceIdsMap.put("chi", R.drawable.ic_dis_chimerstry);
        disciplinesDrawableResourceIdsMap.put("Chimerstry", R.drawable.ic_dis_chimerstry);
        disciplinesDrawableResourceIdsMap.put("CHI", R.drawable.ic_dis_chimerstry_sup);
        disciplinesDrawableResourceIdsMap.put("dai", R.drawable.ic_dis_daimoinon);
        disciplinesDrawableResourceIdsMap.put("Daimoinon", R.drawable.ic_dis_daimoinon);
        disciplinesDrawableResourceIdsMap.put("DAI", R.drawable.ic_dis_daimoinon_sup);
        disciplinesDrawableResourceIdsMap.put("dem", R.drawable.ic_dis_dementation);
        disciplinesDrawableResourceIdsMap.put("Dementation", R.drawable.ic_dis_dementation);
        disciplinesDrawableResourceIdsMap.put("DEM", R.drawable.ic_dis_dementation_sup);
        disciplinesDrawableResourceIdsMap.put("dom", R.drawable.ic_dis_dominate);
        disciplinesDrawableResourceIdsMap.put("Dominate", R.drawable.ic_dis_dominate);
        disciplinesDrawableResourceIdsMap.put("DOM", R.drawable.ic_dis_dominate_sup);
        disciplinesDrawableResourceIdsMap.put("for", R.drawable.ic_dis_fortitude);
        disciplinesDrawableResourceIdsMap.put("Fortitude", R.drawable.ic_dis_fortitude);
        disciplinesDrawableResourceIdsMap.put("FOR", R.drawable.ic_dis_fortitude_sup);
        disciplinesDrawableResourceIdsMap.put("mel", R.drawable.ic_dis_melpominee);
        disciplinesDrawableResourceIdsMap.put("Melpominee", R.drawable.ic_dis_melpominee);
        disciplinesDrawableResourceIdsMap.put("MEL", R.drawable.ic_dis_melpominee_sup);
        disciplinesDrawableResourceIdsMap.put("myt", R.drawable.ic_dis_mytherceria);
        disciplinesDrawableResourceIdsMap.put("Mytherceria", R.drawable.ic_dis_mytherceria);
        disciplinesDrawableResourceIdsMap.put("MYT", R.drawable.ic_dis_mytherceria_sup);
        disciplinesDrawableResourceIdsMap.put("nec", R.drawable.ic_dis_necromancy);
        disciplinesDrawableResourceIdsMap.put("Necromancy", R.drawable.ic_dis_necromancy);
        disciplinesDrawableResourceIdsMap.put("NEC", R.drawable.ic_dis_necromancy_sup);
        disciplinesDrawableResourceIdsMap.put("obe", R.drawable.ic_dis_obeah);
        disciplinesDrawableResourceIdsMap.put("Obeah", R.drawable.ic_dis_obeah);
        disciplinesDrawableResourceIdsMap.put("OBE", R.drawable.ic_dis_obeah_sup);
        disciplinesDrawableResourceIdsMap.put("obf", R.drawable.ic_dis_obfuscate);
        disciplinesDrawableResourceIdsMap.put("Obfuscate", R.drawable.ic_dis_obfuscate);
        disciplinesDrawableResourceIdsMap.put("OBF", R.drawable.ic_dis_obfuscate_sup);
        disciplinesDrawableResourceIdsMap.put("obt", R.drawable.ic_dis_obtenebration);
        disciplinesDrawableResourceIdsMap.put("Obtenebration", R.drawable.ic_dis_obtenebration);
        disciplinesDrawableResourceIdsMap.put("OBT", R.drawable.ic_dis_obtenebration_sup);
        disciplinesDrawableResourceIdsMap.put("pot", R.drawable.ic_dis_potence);
        disciplinesDrawableResourceIdsMap.put("Potence", R.drawable.ic_dis_potence);
        disciplinesDrawableResourceIdsMap.put("POT", R.drawable.ic_dis_potence_sup);
        disciplinesDrawableResourceIdsMap.put("pre", R.drawable.ic_dis_presence);
        disciplinesDrawableResourceIdsMap.put("Presence", R.drawable.ic_dis_presence);
        disciplinesDrawableResourceIdsMap.put("PRE", R.drawable.ic_dis_presence_sup);
        disciplinesDrawableResourceIdsMap.put("pro", R.drawable.ic_dis_protean);
        disciplinesDrawableResourceIdsMap.put("Protean", R.drawable.ic_dis_protean);
        disciplinesDrawableResourceIdsMap.put("PRO", R.drawable.ic_dis_protean_sup);
        disciplinesDrawableResourceIdsMap.put("qui", R.drawable.ic_dis_quietus);
        disciplinesDrawableResourceIdsMap.put("Quietus", R.drawable.ic_dis_quietus);
        disciplinesDrawableResourceIdsMap.put("QUI", R.drawable.ic_dis_quietus_sup);
        disciplinesDrawableResourceIdsMap.put("san", R.drawable.ic_dis_sanguinus);
        disciplinesDrawableResourceIdsMap.put("Sanguinus", R.drawable.ic_dis_sanguinus);
        disciplinesDrawableResourceIdsMap.put("SAN", R.drawable.ic_dis_sanguinus_sup);
        disciplinesDrawableResourceIdsMap.put("ser", R.drawable.ic_dis_serpentis);
        disciplinesDrawableResourceIdsMap.put("Serpentis", R.drawable.ic_dis_serpentis);
        disciplinesDrawableResourceIdsMap.put("SER", R.drawable.ic_dis_serpentis_sup);
        disciplinesDrawableResourceIdsMap.put("spi", R.drawable.ic_dis_spiritus);
        disciplinesDrawableResourceIdsMap.put("Spiritus", R.drawable.ic_dis_spiritus);
        disciplinesDrawableResourceIdsMap.put("SPI", R.drawable.ic_dis_spiritus_sup);
        disciplinesDrawableResourceIdsMap.put("tem", R.drawable.ic_dis_temporis);
        disciplinesDrawableResourceIdsMap.put("Temporis", R.drawable.ic_dis_temporis);
        disciplinesDrawableResourceIdsMap.put("TEM", R.drawable.ic_dis_temporis_sup);
        disciplinesDrawableResourceIdsMap.put("thn", R.drawable.ic_dis_thanatosis);
        disciplinesDrawableResourceIdsMap.put("Thanatosis", R.drawable.ic_dis_thanatosis);
        disciplinesDrawableResourceIdsMap.put("THN", R.drawable.ic_dis_thanatosis_sup);
        disciplinesDrawableResourceIdsMap.put("tha", R.drawable.ic_dis_thaumaturgy);
        disciplinesDrawableResourceIdsMap.put("Thaumaturgy", R.drawable.ic_dis_thaumaturgy);
        disciplinesDrawableResourceIdsMap.put("THA", R.drawable.ic_dis_thaumaturgy_sup);
        disciplinesDrawableResourceIdsMap.put("val", R.drawable.ic_dis_valeren);
        disciplinesDrawableResourceIdsMap.put("Valeren", R.drawable.ic_dis_valeren);
        disciplinesDrawableResourceIdsMap.put("VAL", R.drawable.ic_dis_valeren_sup);
        disciplinesDrawableResourceIdsMap.put("vic", R.drawable.ic_dis_vicissitude);
        disciplinesDrawableResourceIdsMap.put("Vicissitude", R.drawable.ic_dis_vicissitude);
        disciplinesDrawableResourceIdsMap.put("VIC", R.drawable.ic_dis_vicissitude_sup);
        disciplinesDrawableResourceIdsMap.put("vis", R.drawable.ic_dis_visceratika);
        disciplinesDrawableResourceIdsMap.put("Visceratika", R.drawable.ic_dis_visceratika);
        disciplinesDrawableResourceIdsMap.put("VIS", R.drawable.ic_dis_visceratika_sup);
    }



//    static void updateDisciplineImages(Context context, ImageView[] disciplineImageViews, String cardDisciplines) {
//        new Utils.UpdateDisciplineImagesOperation(context, disciplineImageViews).execute(cardDisciplines);
//    }

    static void updateDisciplineImages(Context context, ImageView[] disciplineImageViews, String cardDisciplines) {

        int disIndex = 0;
        for (String discipline: cardDisciplines.split(" |/")) {
            Integer disciplineDrawableResourceId = disciplinesDrawableResourceIdsMap.get(discipline);
            if (disciplineDrawableResourceId == null)
                continue;
            Picasso
                    .with(context)
                    .load(disciplineDrawableResourceId)
                    .noFade()
                    .fit()
                    .into(disciplineImageViews[disIndex]);

            disciplineImageViews[disIndex++].setVisibility(View.VISIBLE);
        }

    }

    private static class LoadImageOperation extends AsyncTask<Void, Void, Void> {



        private final ImageView cardImageView;
        private final String cardImageFileName;
        private final LoadCardImageAsync callback;
        private final int bitmapSampleSize;
        private BitmapDrawable bitmapDrawable;
        private final int resIdFallbackCardImage;

        LoadImageOperation(ImageView cardImageView, String cardImageFileName, int resIdFallbackCardImage, int inSampleSize) {
            this(cardImageView, cardImageFileName, resIdFallbackCardImage, inSampleSize, null);
        }

        LoadImageOperation(ImageView cardImageView, String cardImageFileName, int resIdFallbackCardImage) {
            this(cardImageView, cardImageFileName, resIdFallbackCardImage, null);
        }

        LoadImageOperation(ImageView cardImageView, String cardImageFileName, int resIdFallbackCardImage, LoadCardImageAsync callback) {
            this(cardImageView, cardImageFileName, resIdFallbackCardImage, 1, callback);
        }

        LoadImageOperation(ImageView cardImageView, String cardImageFileName, int resIdFallbackCardImage, int inSampleSize, LoadCardImageAsync callback) {

            this.cardImageView = cardImageView;
            this.cardImageFileName = cardImageFileName;
            this.resIdFallbackCardImage = resIdFallbackCardImage;
            this.callback = callback;
            this.bitmapSampleSize = inSampleSize;

        }

        @Override
        protected Void doInBackground(Void... voids) {


            File imageFile = new File(cardImagesPath + "/" + cardImageFileName);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = bitmapSampleSize;

            if (isCancelled()) {
                Log.d(TAG, "doInBackground: doing nothing because the task was cancelled.");
                return null;
            }

            Bitmap image;

            if (imageFile.exists()) {
                image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            } else {
                image = BitmapFactory.decodeResource(resources, resIdFallbackCardImage, options);
            }

            bitmapDrawable = new BitmapDrawable(resources, image);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            cardImageView.setImageDrawable(bitmapDrawable);
            if (callback != null) {
                callback.onImageLoaded(bitmapDrawable);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

    }


    static class UpdateDisciplineImagesOperation extends AsyncTask<String, Void, String[]> {



        private static BitmapFactory.Options options = new BitmapFactory.Options();

        private Resources res;

        private ImageView[] imageViewsToUpdate;

        static  {

            options.inSampleSize = 4;
        }

        UpdateDisciplineImagesOperation(Context context, ImageView[] imageViews) {

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
            if (disciplineDrawablesCache.get(disciplineKey) == null) {
                Integer resourceId = disciplinesDrawableResourceIdsMap.get(disciplineKey);
                if (resourceId != null) {
                    disciplineDrawablesCache.put(disciplineKey, new BitmapDrawable(res, BitmapFactory.decodeResource(res, resourceId, options)));
                }
            }

        }

        @Override
        protected void onPostExecute(String[] disciplines) {

            int disIndex = 0;
            for (String discipline : disciplines) {
                imageViewsToUpdate[disIndex].setImageDrawable(disciplineDrawablesCache.get(discipline));
                //viewHolder.disciplineImageViews[disIndex].setImageBitmap(disciplineDrawablesCache.get(discipline));
                imageViewsToUpdate[disIndex].setVisibility(View.VISIBLE);
                disIndex++;

            }

        }
    }

    //    Reference: http://stackoverflow.com/questions/8276634/android-get-hosting-activity-from-a-view
    static Activity getActivity(Context fromContext) {
        while (fromContext instanceof ContextWrapper) {
            if (fromContext instanceof Activity) {
                return (Activity)fromContext;
            }
            fromContext = ((ContextWrapper)fromContext).getBaseContext();
        }
        return null;
    }

    public final static class PaletteTransformation implements Transformation {
        private static final PaletteTransformation INSTANCE = new PaletteTransformation();
        private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();

        public static PaletteTransformation instance() {
            return INSTANCE;
        }

        public static Palette getPalette(Bitmap bitmap) {
            return CACHE.get(bitmap);
        }

        private PaletteTransformation() {}

        @Override public Bitmap transform(Bitmap source) {
            Palette palette = Palette.generate(source);
            CACHE.put(source, palette);
            return source;
        }

        // ...

        @Override
        public String key() {
            return "PaletteTransformation";
        }
    }

    public interface LoadCardImageWithPalette {

        public void onImageLoaded(Palette palette);

    }

    public static class EmptyLoadCardImageWithPalette implements LoadCardImageWithPalette {
        @Override
        public void onImageLoaded(Palette palette) {

        }
    }


}

