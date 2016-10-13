package name.vampidroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;

import name.vampidroid.DatabaseHelper.CardType;

public class CardListCursorAdapter extends SimpleCursorAdapter {

    private HashSet<Long> favoriteCards;
    private CardType cardType;

    public CardListCursorAdapter(CardType cardType, Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        // TODO Auto-generated constructor stub

        switch (cardType) {

            case CRYPT:
                this.favoriteCards = DatabaseHelper.getFavoriteCryptCards();
                break;

            case LIBRARY:
                this.favoriteCards = DatabaseHelper.getFavoriteLibraryCards();
                break;
        }

        this.cardType = cardType;

        fillImageViewsDrawablesMap(context);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = super.newView(context, cursor, parent);

        ViewHolder vh = new ViewHolder();
        vh.favoriteImageView = (ImageView) v.findViewById(R.id.imgFavorite);

        vh.favoriteImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Long cardid = (Long) v.getTag();

                if (favoriteCards.contains(cardid)) {

                    ((ImageView) v).setImageResource(R.drawable.btn_star_big_off);

                    DatabaseHelper.removeFavoriteCard(cardid, cardType);

                    favoriteCards.remove(cardid);
                } else {
                    ((ImageView) v).setImageResource(R.drawable.btn_star_big_on);
                    DatabaseHelper.addFavoriteCard(cardid, cardType);

                    favoriteCards.add(cardid);
                }

            }
        });

        vh.editDeckCardImageView = (ImageView) v.findViewById(R.id.imgEditDeckCard);

        vh.editDeckCardImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Long cardid = (Long) v.getTag();

                Intent data = new Intent(mContext, AddRemoveDeckCard.class);
                data.putExtra("CardType", cardType);
                data.putExtra("CardId", cardid);

                mContext.startActivity(data);

            }
        });

        vh.txtCardName = (TextView) v.findViewById(R.id.txtCardName);


        vh.disciplineImageViews[0] = (ImageView) v.findViewById(R.id.imgDis1);
        vh.disciplineImageViews[1] = (ImageView) v.findViewById(R.id.imgDis2);
        vh.disciplineImageViews[2] = (ImageView) v.findViewById(R.id.imgDis3);
        vh.disciplineImageViews[3] = (ImageView) v.findViewById(R.id.imgDis4);
        vh.disciplineImageViews[4] = (ImageView) v.findViewById(R.id.imgDis5);
        vh.disciplineImageViews[5] = (ImageView) v.findViewById(R.id.imgDis6);
        vh.disciplineImageViews[6] = (ImageView) v.findViewById(R.id.imgDis7);
        vh.disciplineImageViews[7] = (ImageView) v.findViewById(R.id.imgDis8);

        v.setTag(vh);

        return v;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View v = super.getView(position, convertView, parent);

        Long cardid = Long.valueOf(getItemId(position));

        ViewHolder viewHolder = (ViewHolder) v.getTag();

        if (favoriteCards.contains(cardid))
            viewHolder.favoriteImageView.setImageResource(R.drawable.btn_star_big_on);
        else
            viewHolder.favoriteImageView.setImageResource(R.drawable.btn_star_big_off);

        // Set images tags to cardid so we can find it later when we click them.
        viewHolder.favoriteImageView.setTag(cardid);
        viewHolder.editDeckCardImageView.setTag(cardid);

        return v;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        super.bindView(view, context, cursor);

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int advColumnIndex = cursor.getColumnIndex("Adv");
        if (advColumnIndex != -1 && cursor.getString(advColumnIndex).length() > 0) {
            viewHolder.txtCardName.setText("Adv. " + viewHolder.txtCardName.getText());
        }

        if (cardType.equals(CardType.CRYPT)) {

            String[] disciplines = cursor.getString(2).split(" ");

            int disIndex = 0;
            for (String discipline : disciplines) {
                viewHolder.disciplineImageViews[disIndex].setImageDrawable(imageViewsDrawablesMap.get(discipline));
                //viewHolder.disciplineImageViews[disIndex].setImageBitmap(imageViewsDrawablesMap.get(discipline));
                viewHolder.disciplineImageViews[disIndex].setVisibility(View.VISIBLE);
                disIndex++;
            }

            viewHolder.clearDisciplineImageViews(disIndex);
        }


    }

    private final static class ViewHolder {
        public ImageView favoriteImageView;
        public ImageView editDeckCardImageView;
        public TextView txtCardName;

        // Discipline images

        public ImageView[] disciplineImageViews = new ImageView[8];

        public void clearDisciplineImageViews() {
            for (int i = 0; i < 8; i++) {
                disciplineImageViews[i].setImageDrawable(null);
            }
        }

        public void clearDisciplineImageViews(int fromIndex) {
            // Optimization to clear imageviews only from fromIndex and above.
            for (int i = fromIndex; i < 8; i++) {
                //disciplineImageViews[i].setImageDrawable(null);
                disciplineImageViews[i].setVisibility(View.INVISIBLE);

            }
        }

    }


    public static final HashMap<String, Drawable> imageViewsDrawablesMap = new HashMap<>();


    public static void fillImageViewsDrawablesMap(Context context) {

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

}
