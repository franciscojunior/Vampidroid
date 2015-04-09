package name.vampidroid;

import java.util.HashSet;

import name.vampidroid.DatabaseHelper.CardType;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        viewHolder.clearDisciplineImageViews();

        String[] disciplines = cursor.getString(2).split(" ");

        int disIndex = 0;
        for (String discipline : disciplines) {
            if (discipline.compareTo("for") == 0 )
                viewHolder.disciplineImageViews[disIndex].setImageResource(R.drawable.ic_dis_fortitude);
            else if (discipline.compareTo("FOR") == 0 )
                viewHolder.disciplineImageViews[disIndex].setImageResource(R.drawable.ic_dis_fortitude_sup);
            else if (discipline.compareTo("cel") == 0 )
                viewHolder.disciplineImageViews[disIndex].setImageResource(R.drawable.ic_dis_celerity);
            else if (discipline.compareTo("CEL") == 0 )
                viewHolder.disciplineImageViews[disIndex].setImageResource(R.drawable.ic_dis_celerity_sup);

            disIndex++;
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
                if (disciplineImageViews[i] != null)
                    disciplineImageViews[i].setImageResource(0);
            }
        }

	}




}
