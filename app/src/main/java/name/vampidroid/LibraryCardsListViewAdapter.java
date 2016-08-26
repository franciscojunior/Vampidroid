package name.vampidroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fxjr on 17/03/16.
 */
public class LibraryCardsListViewAdapter extends CursorRecyclerAdapter<LibraryCardsListViewAdapter.ViewHolder> {


    public LibraryCardsListViewAdapter(Context context, Cursor cursor) {
        super(cursor);
    }



    // Create new views (invoked by the layout manager)
    @Override
    public LibraryCardsListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder viewHolder, Cursor cursor) {

        String cardName = cursor.getString(1);

        viewHolder.cardId = cursor.getLong(0);
        viewHolder.txtCardType.setText(cursor.getString(2));
        viewHolder.txtCardName.setText(cardName);

        // // TODO: 24/08/16 Move this logic to a future LibraryCard model class
        // Check card cost. It can be PoolCost, BloodCost or ConvictionCost.
        String cardCost = "0";

        if (cursor.getString(3).length() > 0) {
            cardCost = cursor.getString(3);
        } else if (cursor.getString(4).length() > 0) {
            cardCost = cursor.getString(4);
        } else if (cursor.getString(5).length() > 0) {
            cardCost = cursor.getString(5);
        }

        viewHolder.txtCardCost.setText(cardCost);

//        viewHolder.txtCardClan.setText(cursor.getString(3));

//        viewHolder.txtCardDiscipline.setText(cursor.getString(4));

        Utils.loadCardImageThumbnail(viewHolder.imageViewCardImage, Utils.getCardFileName(cardName), R.drawable.green_back);

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageViewCardImage;
        public TextView txtCardName;
        public TextView txtCardType;
        public TextView txtCardClan;
        public TextView txtCardDiscipline;
        public TextView txtCardCost;
        public long cardId;


        public ViewHolder(View v) {
            super(v);

            txtCardType = (TextView) v.findViewById(R.id.txtCardType);
            txtCardName = (TextView) v.findViewById(R.id.txtCardName);
//            txtCardClan = (TextView) v.findViewById(R.id.txtCardClan);
//            txtCardDiscipline = (TextView) v.findViewById(R.id.txtCardDiscipline);
            txtCardCost = (TextView) v.findViewById(R.id.txtCardCost);
            imageViewCardImage = (ImageView) v.findViewById(R.id.imageViewCardImage);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Context context = v.getContext();
            Intent launch = new Intent(context, LibraryCardDetailsActivity.class);
            launch.putExtra("cardId", cardId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(Utils.getActivity(context), imageViewCardImage, "cardImageTransition").toBundle();
                v.getContext().startActivity(launch, bundle);
            } else {
                v.getContext().startActivity(launch);
            }

        }
    }

}


