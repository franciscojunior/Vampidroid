package name.vampidroid;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fxjr on 17/03/16.
 */

public class CryptCardsListViewAdapter extends CursorRecyclerAdapter<CryptCardsListViewAdapter.ViewHolder> {



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
        viewHolder.txtInitialCardText.setText(cursor.getString(4));
        viewHolder.txtCardName.setText(cardName);
        viewHolder.txtCardCost.setText(cursor.getString(3));
        viewHolder.txtCardExtraInformation.setText(cursor.getString(2));
        viewHolder.txtCardGroup.setText(cursor.getString(5));
        viewHolder.txtCardAdv = cursor.getString(6);

        String cardImageFileName = Utils.getCardFileName(cardName, viewHolder.txtCardAdv.length() > 0);

        // Loading optimization.
        // To avoid setting an image in a imageview which may be out of screen, set a tag in the imageview.
        // The image loader will compare this tag when seting the image in the imageview and only
        // set the image if the tag matches.
        viewHolder.imageViewCardImage.setTag(cardImageFileName);
        Utils.loadCardImageThumbnail(viewHolder.imageViewCardImage, cardImageFileName, R.drawable.gold_back);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtInitialCardText;
        public TextView txtCardName;
        public TextView txtCardExtraInformation;
        public TextView txtCardCost;
        public TextView txtCardGroup;
        public long cardId;
        public ImageView imageViewCardImage;
        public String txtCardAdv;


        public ViewHolder(View v) {
            super(v);

            txtInitialCardText = (TextView) v.findViewById(R.id.txtCardInitialText);
            txtCardName = (TextView) v.findViewById(R.id.txtCardName);
            txtCardExtraInformation = (TextView) v.findViewById(R.id.txtCardExtraInformation);
            txtCardCost = (TextView) v.findViewById(R.id.txtCardCost);
            txtCardGroup = (TextView) v.findViewById(R.id.txtCardGroup);
            imageViewCardImage = (ImageView) v.findViewById(R.id.imageViewCardImage);

            v.setOnClickListener(this);

            //imageViewCardImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Context context = v.getContext();

            Intent launch;

            if (ImageView.class.isInstance(v)) {
                launch = new Intent(context, CardImageActivity.class);
            } else {
                launch = new Intent(context, CryptCardDetailsActivity.class);
            }

            launch.putExtra("cardName", txtCardName.getText());
            launch.putExtra("cardText", txtInitialCardText.getText());
            launch.putExtra("cardId", cardId);
            launch.putExtra("cardImageFileName", Utils.getCardFileName(txtCardName.getText().toString(), txtCardAdv.length() > 0));
            launch.putExtra("resIdFallbackCardImage", R.drawable.gold_back);

//            showCardImage.putExtra("cardId", getIntent().getExtras().getLong("cardId"));
//            showCardImage.putExtra("cardImageFileName", Utils.getCardFileName(cardName, cardAdvanced.length() > 0));
//            showCardImage.putExtra("resIdFallbackCardImage", R.drawable.gold_back);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//
//                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(CryptCardDetailsActivity.this, cardImage, "cardImageTransition").toBundle();
//                view.getContext().startActivity(showCardImage, bundle);
//            } else
//                view.getContext().startActivity(showCardImage);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                Bundle bundle;

//                if (ImageView.class.isInstance(v)) {
//                    bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(Utils.getActivity(context), imageViewCardImage, "cardImageTransition").toBundle();
//                } else {
//                    bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(Utils.getActivity(context), txtInitialCardText, "cardTextTransition").toBundle();
//                }


                //    Reference: https://github.com/codepath/android_guides/wiki/Shared-Element-Activity-Transition
                Pair<View, String> element1 = Pair.create((View)txtInitialCardText, "cardTextTransition");
                Pair<View, String> element2 = Pair.create((View)imageViewCardImage, "cardImageTransition");


                bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(Utils.getActivity(context), element1, element2).toBundle();

                context.startActivity(launch, bundle);
            } else
                context.startActivity(launch);



        }
    }

}

