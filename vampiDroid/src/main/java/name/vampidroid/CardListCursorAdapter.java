package name.vampidroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
				}

				else {
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
                disciplineImageViews[i].setImageDrawable(null);
            }
        }

	}


    public static final HashMap<String, Drawable> imageViewsDrawablesMap = new HashMap<>();


    public static void fillImageViewsDrawablesMap(Context context) {

        if (imageViewsDrawablesMap.isEmpty()) {

            imageViewsDrawablesMap.put("abo", context.getResources().getDrawable(R.drawable.ic_dis_abombwe));
            imageViewsDrawablesMap.put("ABO", context.getResources().getDrawable(R.drawable.ic_dis_abombwe_sup));
            imageViewsDrawablesMap.put("ani", context.getResources().getDrawable(R.drawable.ic_dis_animalism));
            imageViewsDrawablesMap.put("ANI", context.getResources().getDrawable(R.drawable.ic_dis_animalism_sup));
            imageViewsDrawablesMap.put("aus", context.getResources().getDrawable(R.drawable.ic_dis_auspex));
            imageViewsDrawablesMap.put("AUS", context.getResources().getDrawable(R.drawable.ic_dis_auspex_sup));
            imageViewsDrawablesMap.put("cel", context.getResources().getDrawable(R.drawable.ic_dis_celerity));
            imageViewsDrawablesMap.put("CEL", context.getResources().getDrawable(R.drawable.ic_dis_celerity_sup));
            imageViewsDrawablesMap.put("chi", context.getResources().getDrawable(R.drawable.ic_dis_chimerstry));
            imageViewsDrawablesMap.put("CHI", context.getResources().getDrawable(R.drawable.ic_dis_chimerstry_sup));
            imageViewsDrawablesMap.put("dai", context.getResources().getDrawable(R.drawable.ic_dis_daimoinon));
            imageViewsDrawablesMap.put("DAI", context.getResources().getDrawable(R.drawable.ic_dis_daimoinon_sup));
            imageViewsDrawablesMap.put("dem", context.getResources().getDrawable(R.drawable.ic_dis_dementation));
            imageViewsDrawablesMap.put("DEM", context.getResources().getDrawable(R.drawable.ic_dis_dementation_sup));
            imageViewsDrawablesMap.put("dom", context.getResources().getDrawable(R.drawable.ic_dis_dominate));
            imageViewsDrawablesMap.put("DOM", context.getResources().getDrawable(R.drawable.ic_dis_dominate_sup));
            imageViewsDrawablesMap.put("for", context.getResources().getDrawable(R.drawable.ic_dis_fortitude));
            imageViewsDrawablesMap.put("FOR", context.getResources().getDrawable(R.drawable.ic_dis_fortitude_sup));
            imageViewsDrawablesMap.put("mel", context.getResources().getDrawable(R.drawable.ic_dis_melpominee));
            imageViewsDrawablesMap.put("MEL", context.getResources().getDrawable(R.drawable.ic_dis_melpominee_sup));
            imageViewsDrawablesMap.put("myt", context.getResources().getDrawable(R.drawable.ic_dis_mytherceria));
            imageViewsDrawablesMap.put("MYT", context.getResources().getDrawable(R.drawable.ic_dis_mytherceria_sup));
            imageViewsDrawablesMap.put("nec", context.getResources().getDrawable(R.drawable.ic_dis_necromancy));
            imageViewsDrawablesMap.put("NEC", context.getResources().getDrawable(R.drawable.ic_dis_necromancy_sup));
            imageViewsDrawablesMap.put("obe", context.getResources().getDrawable(R.drawable.ic_dis_obeah));
            imageViewsDrawablesMap.put("OBE", context.getResources().getDrawable(R.drawable.ic_dis_obeah_sup));
            imageViewsDrawablesMap.put("obf", context.getResources().getDrawable(R.drawable.ic_dis_obfuscate));
            imageViewsDrawablesMap.put("OBF", context.getResources().getDrawable(R.drawable.ic_dis_obfuscate_sup));
            imageViewsDrawablesMap.put("obt", context.getResources().getDrawable(R.drawable.ic_dis_obtenebration));
            imageViewsDrawablesMap.put("OBT", context.getResources().getDrawable(R.drawable.ic_dis_obtenebration_sup));
            imageViewsDrawablesMap.put("pot", context.getResources().getDrawable(R.drawable.ic_dis_potence));
            imageViewsDrawablesMap.put("POT", context.getResources().getDrawable(R.drawable.ic_dis_potence_sup));
            imageViewsDrawablesMap.put("pre", context.getResources().getDrawable(R.drawable.ic_dis_presence));
            imageViewsDrawablesMap.put("PRE", context.getResources().getDrawable(R.drawable.ic_dis_presence_sup));
            imageViewsDrawablesMap.put("pro", context.getResources().getDrawable(R.drawable.ic_dis_protean));
            imageViewsDrawablesMap.put("PRO", context.getResources().getDrawable(R.drawable.ic_dis_protean_sup));
            imageViewsDrawablesMap.put("qui", context.getResources().getDrawable(R.drawable.ic_dis_quietus));
            imageViewsDrawablesMap.put("QUI", context.getResources().getDrawable(R.drawable.ic_dis_quietus_sup));
            imageViewsDrawablesMap.put("san", context.getResources().getDrawable(R.drawable.ic_dis_sanguinus));
            imageViewsDrawablesMap.put("SAN", context.getResources().getDrawable(R.drawable.ic_dis_sanguinus_sup));
            imageViewsDrawablesMap.put("ser", context.getResources().getDrawable(R.drawable.ic_dis_serpentis));
            imageViewsDrawablesMap.put("SER", context.getResources().getDrawable(R.drawable.ic_dis_serpentis_sup));
            imageViewsDrawablesMap.put("spi", context.getResources().getDrawable(R.drawable.ic_dis_spiritus));
            imageViewsDrawablesMap.put("SPI", context.getResources().getDrawable(R.drawable.ic_dis_spiritus_sup));
            imageViewsDrawablesMap.put("tem", context.getResources().getDrawable(R.drawable.ic_dis_temporis));
            imageViewsDrawablesMap.put("TEM", context.getResources().getDrawable(R.drawable.ic_dis_temporis_sup));
            imageViewsDrawablesMap.put("thn", context.getResources().getDrawable(R.drawable.ic_dis_thanatosis));
            imageViewsDrawablesMap.put("THN", context.getResources().getDrawable(R.drawable.ic_dis_thanatosis_sup));
            imageViewsDrawablesMap.put("tha", context.getResources().getDrawable(R.drawable.ic_dis_thaumaturgy));
            imageViewsDrawablesMap.put("THA", context.getResources().getDrawable(R.drawable.ic_dis_thaumaturgy_sup));
            imageViewsDrawablesMap.put("val", context.getResources().getDrawable(R.drawable.ic_dis_valeren));
            imageViewsDrawablesMap.put("VAL", context.getResources().getDrawable(R.drawable.ic_dis_valeren_sup));
            imageViewsDrawablesMap.put("vic", context.getResources().getDrawable(R.drawable.ic_dis_vicissitude));
            imageViewsDrawablesMap.put("VIC", context.getResources().getDrawable(R.drawable.ic_dis_vicissitude_sup));
            imageViewsDrawablesMap.put("vis", context.getResources().getDrawable(R.drawable.ic_dis_visceratika));
            imageViewsDrawablesMap.put("VIS", context.getResources().getDrawable(R.drawable.ic_dis_visceratika_sup));
        }
    }

}
