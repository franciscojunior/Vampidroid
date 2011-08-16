package name.vampidroid.fragments;

import name.vampidroid.DatabaseHelper;
import name.vampidroid.R;
import name.vampidroid.VampiDroidBase;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LibraryDetailsFragment extends Fragment {
	
	static String QUERY_LIBRARY = "select Name, Type, Clan, Discipline, CardText, PoolCost, BloodCost, Artist, _Set from library where _id = ";

	
	/**
     * Create a new instance of CryptDetailsFragment, initialized to
     * show the cart with id 'id'.
     */
    public static LibraryDetailsFragment newInstance(long id) {
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putLong(VampiDroidBase.CARD_ID, id);
        
        LibraryDetailsFragment f = new LibraryDetailsFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        long card_id = getArguments().getLong(VampiDroidBase.CARD_ID, 0);
		
		String query;
		
		query = QUERY_LIBRARY;
		
		
		View v = inflater.inflate(R.layout.librarycarddetails, null);
		
		SQLiteDatabase db = DatabaseHelper.getDatabase(getActivity().getApplicationContext());
		Cursor c = db.rawQuery(query + String.valueOf(card_id), null );
		c.moveToFirst();
		
		String cardName = c.getString(0);
		String cardType = c.getString(1);
		String cardClan = c.getString(2);
		String cardDisciplines = c.getString(3);
		String cardText = c.getString(4);
		String cardPoolCost = c.getString(5);
		String cardBloodCost = c.getString(6);
		String cardArtist = c.getString(7);
		String cardSetRarity = c.getString(8);

		c.close();
		
		
		TextView txt = (TextView) v.findViewById(R.id.txtCardName);
		txt.setText(cardName);

		txt = (TextView) v.findViewById(R.id.txtCardPoolCost);
		txt.setText(cardPoolCost);

		txt = (TextView) v.findViewById(R.id.txtCardBloodCost);
		txt.setText(cardBloodCost);

		txt = (TextView) v.findViewById(R.id.txtCardType);
		txt.setText(cardType);

		txt = (TextView) v.findViewById(R.id.txtCardClan);
		txt.setText(cardClan);

		txt = (TextView) v.findViewById(R.id.txtCardDisciplines);
		txt.setText(cardDisciplines);

		txt = (TextView) v.findViewById(R.id.txtCardArtist);
		txt.setText(cardArtist);

		txt = (TextView) v.findViewById(R.id.txtCardText);
		txt.setText(cardText);

		txt = (TextView) v.findViewById(R.id.txtSetRarity);
		txt.setText(cardSetRarity);

		
		return v;
    }
}