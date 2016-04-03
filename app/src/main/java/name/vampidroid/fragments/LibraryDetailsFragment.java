//package name.vampidroid.fragments;
//
//import name.vampidroid.DatabaseHelper;
//import name.vampidroid.R;
//import name.vampidroid.VampiDroidBase;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.actionbarsherlock.app.SherlockFragment;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuInflater;
//import com.actionbarsherlock.view.MenuItem;
//
//public class LibraryDetailsFragment extends SherlockFragment {
//
//	static String QUERY_LIBRARY = "select Name, Type, Clan, Discipline, CardText, PoolCost, BloodCost, Artist, _Set from library where _id = ";
//
//	private String mShareSubject;
//	private String mShareBody;
//
//
//	/**
//	 * Create a new instance of CryptDetailsFragment, initialized to show the
//	 * cart with id 'id'.
//	 */
//	public static LibraryDetailsFragment newInstance(long id) {
//		// Supply index input as an argument.
//		Bundle args = new Bundle();
//		args.putLong(VampiDroidBase.CARD_ID, id);
//
//		LibraryDetailsFragment f = new LibraryDetailsFragment();
//		f.setArguments(args);
//
//		return f;
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		setHasOptionsMenu(true);
//	}
//
//
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// TODO Auto-generated method stub
//
//		super.onCreateOptionsMenu(menu, inflater);
//
//		inflater.inflate(R.menu.library_details_menu, menu);
//	}
//
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//
//		case R.id.menu_share:
//			shareCardsText();
//			break;
//
//		}
//
//		return super.onOptionsItemSelected(item);
//	}
//
//	private void shareCardsText() {
//
//		// TODO Auto-generated method stub
//
//		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//		shareIntent.setType("text/plain");
//		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mShareSubject);
//		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, mShareBody);
//
//		startActivity(Intent.createChooser(shareIntent, getResources()
//				.getString(R.string.share_library_card_text)));
//
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		if (container == null) {
//			// We have different layouts, and in one of them this
//			// fragment's containing frame doesn't exist. The fragment
//			// may still be created from its saved state, but there is
//			// no reason to try to create its view hierarchy because it
//			// won't be displayed. Note this is not needed -- we could
//			// just run the code below, where we would create and return
//			// the view hierarchy; it would just never be used.
//			return null;
//		}
//
//		long card_id = getArguments().getLong(VampiDroidBase.CARD_ID, 0);
//
//		String query;
//
//		query = QUERY_LIBRARY;
//
//		View v = inflater.inflate(R.layout.librarycarddetails, null);
//
//		SQLiteDatabase db = DatabaseHelper.getDatabase();
//		Cursor c = db.rawQuery(query + String.valueOf(card_id), null);
//		c.moveToFirst();
//
//		String cardName = c.getString(0);
//		String cardType = c.getString(1);
//		String cardClan = c.getString(2);
//		String cardDisciplines = c.getString(3);
//		String cardText = c.getString(4);
//		String cardPoolCost = c.getString(5);
//		String cardBloodCost = c.getString(6);
//		String cardArtist = c.getString(7);
//		String cardSetRarity = c.getString(8);
//
//		c.close();
//
//		TextView txt = (TextView) v.findViewById(R.id.txtCardName);
//		txt.setText(cardName);
//
//		txt = (TextView) v.findViewById(R.id.txtCardPoolCost);
//		txt.setText(cardPoolCost);
//
//		txt = (TextView) v.findViewById(R.id.txtCardBloodCost);
//		txt.setText(cardBloodCost);
//
//		txt = (TextView) v.findViewById(R.id.txtCardType);
//		txt.setText(cardType);
//
//		txt = (TextView) v.findViewById(R.id.txtCardClan);
//		txt.setText(cardClan);
//
//		txt = (TextView) v.findViewById(R.id.txtCardDisciplines);
//		txt.setText(cardDisciplines);
//
//		txt = (TextView) v.findViewById(R.id.txtCardArtist);
//		txt.setText(cardArtist);
//
//		txt = (TextView) v.findViewById(R.id.txtCardText);
//		txt.setText(cardText);
//
//		txt = (TextView) v.findViewById(R.id.txtSetRarity);
//		txt.setText(cardSetRarity);
//
//
//
//		mShareSubject = cardName;
//
//		mShareBody =  "Name: " + cardName + "\n" +
//			"Type: " + cardType + "\n" +
//			"PoolCost: " + cardPoolCost + "\n" +
//			"BloodCost: " + cardBloodCost + "\n" +
//			"CardText: " + cardText + "\n";
//
//
//
//		return v;
//	}
//}