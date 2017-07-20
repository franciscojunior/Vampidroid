package name.vampidroid;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.l4digital.fastscroll.FastScroller;

/**
 * Created by fxjr on 17/03/16.
 */

public class CryptCardsListViewAdapter extends CursorRecyclerAdapter<CryptCardsListViewAdapter.ViewHolder>
    implements FastScroller.SectionIndexer
{


    View.OnClickListener editDeckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Clicked edit deck", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

//    public CryptCardsListViewAdapter(Context context, Cursor cursor) {
//        super(context, cursor);
//    }

    public CryptCardsListViewAdapter(Context context, Cursor cursor) {
        super(cursor);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CryptCardsListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypt_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder viewHolder, Cursor cursor) {

        String cardName = cursor.getString(1);

        viewHolder.cardId = cursor.getLong(0);
        viewHolder.txtCardClan.setText(cursor.getString(4));
        viewHolder.txtCardName.setText(cardName);
        viewHolder.txtCardCost.setText(cursor.getString(3));
        viewHolder.txtCardExtraInformation.setText(cursor.getString(2));
        viewHolder.txtCardGroup.setText(cursor.getString(5));
        viewHolder.txtCardAdv = cursor.getString(6);

        Glide
                .with(viewHolder.imageViewCardImage.getContext())
                .load(Utils.getFullCardFileName(cardName, viewHolder.txtCardAdv.length() > 0))
                .fitCenter()
                .placeholder(R.drawable.gold_back)
                .into(viewHolder.imageViewCardImage);
    }

    @Override
    public String getSectionText(int position) {
        return getCursor().getString(1).substring(0, 1);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtCardClan;
        public TextView txtCardName;
        public TextView txtCardExtraInformation;
        public TextView txtCardCost;
        public TextView txtCardGroup;
        public long cardId;
        public ImageView imageViewCardImage;
        public String txtCardAdv;


        public ViewHolder(View v) {
            super(v);

            txtCardClan = (TextView) v.findViewById(R.id.txtCardClan);
            txtCardName = (TextView) v.findViewById(R.id.txtCardName);
            txtCardExtraInformation = (TextView) v.findViewById(R.id.txtCardExtraInformation);
            txtCardCost = (TextView) v.findViewById(R.id.txtCardCost);
            txtCardGroup = (TextView) v.findViewById(R.id.txtCardGroup);
            imageViewCardImage = (ImageView) v.findViewById(R.id.imageViewCardImage);

            v.setOnClickListener(this);

            //imageViewCardImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Context context = view.getContext();

            Intent launch = new Intent(context, CryptCardDetailsActivity.class);

            launch.putExtra("cardId", cardId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(Utils.getActivity(context), imageViewCardImage, "cardImageTransition").toBundle();
                context.startActivity(launch, bundle);
            } else {
                context.startActivity(launch);
            }
        }
    }

}

