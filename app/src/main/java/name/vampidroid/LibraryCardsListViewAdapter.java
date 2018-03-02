package name.vampidroid;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.l4digital.fastscroll.FastScroller;

import name.vampidroid.data.LibraryCard;

/**
 * Created by fxjr on 17/03/16.
 */
public class LibraryCardsListViewAdapter extends ListAdapter<LibraryCard, LibraryCardsListViewAdapter.ViewHolder>
        implements FastScroller.SectionIndexer
{


    public LibraryCardsListViewAdapter()  {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<LibraryCard> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<LibraryCard>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull LibraryCard oldCard, @NonNull LibraryCard newCard) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldCard.getName().equals(newCard.getName());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull LibraryCard oldCard, @NonNull LibraryCard newCard) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldCard.getName().equals(newCard.getName());
                }
            };

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
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        LibraryCard libraryCard = getItem(position);

        String cardName = libraryCard.getName();

        viewHolder.cardId = libraryCard.getUid();
        viewHolder.txtCardType.setText(libraryCard.getType());
        viewHolder.txtCardName.setText(cardName);

//        // // TODO: 24/08/16 Move this logic to a future LibraryCard model class
//        // Check card cost. It can be PoolCost, BloodCost or ConvictionCost.
//        String cardCost = "0";
//
//        if (cursor.getString(3).length() > 0) {
//            cardCost = cursor.getString(3);
//        } else if (cursor.getString(4).length() > 0) {
//            cardCost = cursor.getString(4);
//        } else if (cursor.getString(5).length() > 0) {
//            cardCost = cursor.getString(5);
//        }

        viewHolder.txtCardCost.setText(libraryCard.getCost());

//        viewHolder.txtCardClan.setText(cursor.getTabTitle(3));

//        viewHolder.txtCardDiscipline.setText(cursor.getTabTitle(4));

        Glide
                .with(viewHolder.imageViewCardImage.getContext())
                .load(Utils.getFullCardFileName(cardName))
                .fitCenter()
                .placeholder(R.drawable.green_back)
                .into(viewHolder.imageViewCardImage);
    }

    @Override
    public String getSectionText(int position) {
        return getItem(position).getName().substring(0, 1);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageViewCardImage;
        public TextView txtCardName;
        public TextView txtCardType;
        public TextView txtCardClan;
        public TextView txtCardDiscipline;
        public TextView txtCardCost;
        public long cardId;


        public ViewHolder(View v) {
            super(v);

            txtCardType = v.findViewById(R.id.txtCardType);
            txtCardName = v.findViewById(R.id.txtCardName);
//            txtCardClan = (TextView) v.findViewById(R.id.txtCardClan);
//            txtCardDiscipline = (TextView) v.findViewById(R.id.txtCardDiscipline);
            txtCardCost = v.findViewById(R.id.txtCardCost);
            imageViewCardImage = v.findViewById(R.id.imageViewCardImage);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Context context = v.getContext();
            Intent launch = new Intent(context, LibraryCardDetailsActivity.class);
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


