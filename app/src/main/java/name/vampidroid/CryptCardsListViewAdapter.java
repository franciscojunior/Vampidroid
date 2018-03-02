package name.vampidroid;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import java.util.List;
import java.util.Objects;

import name.vampidroid.data.Card;
import name.vampidroid.data.CryptCard;

/**
 * Created by francisco on 06/09/17.
 */

public class CryptCardsListViewAdapter extends ListAdapter<CryptCard, CryptCardsListViewAdapter.ViewHolder>
        implements FastScroller.SectionIndexer {




    public CryptCardsListViewAdapter() {
        super(DIFF_CALLBACK);
    }


    View.OnClickListener editDeckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Clicked edit deck", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    public static final DiffUtil.ItemCallback<CryptCard> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CryptCard>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull CryptCard oldCard, @NonNull CryptCard newCard) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldCard.getName().equals(newCard.getName());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull CryptCard oldCard, @NonNull CryptCard newCard) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldCard.getName().equals(newCard.getName());
                }
            };



    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypt_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        CryptCardsListViewAdapter.ViewHolder vh = new CryptCardsListViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {


        CryptCard cryptCard = getItem(position);

        String cardName = cryptCard.getName();

        viewHolder.cardId = cryptCard.getUid();
        viewHolder.txtCardClan.setText(cryptCard.getClan());
        viewHolder.txtCardName.setText(cardName);
        viewHolder.txtCardCost.setText(cryptCard.getCapacity());
        viewHolder.txtCardExtraInformation.setText(cryptCard.getDisciplines());
        viewHolder.txtCardGroup.setText(cryptCard.getGroup());
        viewHolder.txtCardAdv = cryptCard.getAdvanced();

        Glide
                .with(viewHolder.imageViewCardImage.getContext())
                .load(Utils.getFullCardFileName(cardName, viewHolder.txtCardAdv.length() > 0))
                .fitCenter()
                .placeholder(R.drawable.gold_back)
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

            txtCardClan = v.findViewById(R.id.txtCardClan);
            txtCardName = v.findViewById(R.id.txtCardName);
            txtCardExtraInformation = v.findViewById(R.id.txtCardExtraInformation);
            txtCardCost = v.findViewById(R.id.txtCardCost);
            txtCardGroup = v.findViewById(R.id.txtCardGroup);
            imageViewCardImage = v.findViewById(R.id.imageViewCardImage);

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

