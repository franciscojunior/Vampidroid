package name.vampidroid;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.text.Normalizer;


/**
 * Created by fxjr on 03/07/16.
 */

public class Utils {

    private static final String TAG = "Utils";

    static SimpleArrayMap<String, Integer> disciplinesDrawableResourceIdsMap;
    static String cardImagesPath;

    public static SimpleArrayMap<String, Integer> getDisciplinesDrawableResourceIdsMap() {
        return disciplinesDrawableResourceIdsMap;
    }

    public static void setCardImagesPath(String cardImagesPath) {
        Log.d(TAG, "setCardImagesPath: " + cardImagesPath);
        Utils.cardImagesPath = cardImagesPath;
    }

    static String getFullCardFileName(String cardName) {
        return cardImagesPath + "/" + getCardFileName(cardName);
    }

    static String getFullCardFileName(String cardName, boolean advanced) {
        return cardImagesPath + "/" + getCardFileName(cardName, advanced);
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


    static void playDrawerToggleAnim(final DrawerArrowDrawable d) {
        float start = d.getProgress();
        float end = Math.abs(start - 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
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
        } else
            d.setProgress(end);
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

    //    Reference: http://stackoverflow.com/questions/8276634/android-get-hosting-activity-from-a-view
    static Activity getActivity(Context fromContext) {
        while (fromContext instanceof ContextWrapper) {
            if (fromContext instanceof Activity) {
                return (Activity) fromContext;
            }
            fromContext = ((ContextWrapper) fromContext).getBaseContext();
        }
        return null;
    }

}

