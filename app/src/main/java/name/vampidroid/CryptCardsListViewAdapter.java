package name.vampidroid;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by fxjr on 17/03/16.
 */
public class CryptCardsListViewAdapter extends CursorRecyclerViewAdapter<CryptCardsListViewAdapter.ViewHolder> {



    View.OnClickListener editDeckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Clicked edit deck", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    public CryptCardsListViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
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
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {


        viewHolder.txtInitialCardText.setText(cursor.getString(4));
        viewHolder.txtCardName.setText(cursor.getString(1));
        viewHolder.txtCardCost.setText(cursor.getString(3));
        viewHolder.txtCardExtraInformation.setText(cursor.getString(2));
        viewHolder.txtCardGroup.setText(cursor.getString(5));



    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtInitialCardText;
        public TextView txtCardName;
        public TextView txtCardExtraInformation;
        public TextView txtCardCost;
        public TextView txtCardGroup;


        public ViewHolder(View v) {
            super(v);

            txtInitialCardText = (TextView) v.findViewById(R.id.txtCardInitialText);
            txtCardName = (TextView) v.findViewById(R.id.txtCardName);
            txtCardExtraInformation = (TextView) v.findViewById(R.id.txtCardExtraInformation);
            txtCardCost = (TextView) v.findViewById(R.id.txtCardCost);
            txtCardGroup = (TextView) v.findViewById(R.id.txtCardGroup);

            v.setOnClickListener(this);

            v.findViewById(R.id.imgEditDeckCard).setOnClickListener(editDeckListener);
        }

        @Override
        public void onClick(View v) {

            Snackbar.make(v, "Clicked on card " + txtCardName.getText(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
    }

}

