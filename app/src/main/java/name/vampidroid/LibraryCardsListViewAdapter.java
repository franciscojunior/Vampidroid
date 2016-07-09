package name.vampidroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import name.vampidroid.CursorRecyclerViewAdapter;

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

        viewHolder.cardId = cursor.getLong(0);
        viewHolder.txtCardType.setText(cursor.getString(2));
        viewHolder.txtCardName.setText(cursor.getString(1));
        viewHolder.txtCardClan.setText(cursor.getString(3));

        viewHolder.txtCardDiscipline.setText(cursor.getString(4));




    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtCardName;
        public TextView txtCardType;
        public TextView txtCardClan;
        public TextView txtCardDiscipline;
        public long cardId;


        public ViewHolder(View v) {
            super(v);

            txtCardType = (TextView) v.findViewById(R.id.txtCardType);
            txtCardName = (TextView) v.findViewById(R.id.txtCardName);
            txtCardClan = (TextView) v.findViewById(R.id.txtCardClan);
            txtCardDiscipline = (TextView) v.findViewById(R.id.txtCardDiscipline);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent launch = new Intent(v.getContext(), LibraryCardDetailsActivity.class);
            launch.putExtra("cardId", cardId);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//
//                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)v.getContext(), txtInitialCardText, "cardTextTransition").toBundle();
//                v.getContext().startActivity(launch, bundle);
//            } else
            v.getContext().startActivity(launch);

        }
    }

}


