//package name.vampidroid;
//
//import name.vampidroid.DatabaseHelper.CardType;
//import android.os.Bundle;
//import android.util.Log;
//
//
//public class FavoriteCards extends VampiDroidBase {
//
//
//
//
//	protected String getLibraryQuery() {
//		return DatabaseHelper.ALL_FROM_LIBRARY_FAVORITES_QUERY;
//	}
//
//	protected String getCryptQuery() {
//		return DatabaseHelper.ALL_FROM_CRYPT_FAVORITES_QUERY;
//	}
//
//
//
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		Log.d("vampidroid", "vampidroid.oncreate");
//
//
//
//	}
//
//
////	@Override
////	public boolean onCreateOptionsMenu(Menu menu) {
////		getMenuInflater().inflate(R.menu.main_menu, menu);
////
////		return super.onCreateOptionsMenu(menu);
////
////	}
//
//
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		Log.d("vampidroid", "vampidroid.onstart");
//
//		CardListCursorAdapter adapter = new CardListCursorAdapter(CardType.CRYPT,
//				this, R.layout.cryptlistitem, null,
//				DatabaseHelper.STRING_ARRAY_CRYPT_LIST_COLUMNS,
//				new int[] { R.id.txtCardName, R.id.txtCardExtraInformation,
//						R.id.txtCardCost, R.id.txtCardInitialText, R.id.txtCardGroup }) ;
//
//
//		mVampidroidFragment.getCryptListFragment().setListAdapter(adapter);
//		mVampidroidFragment.getCryptListFragment().setQuery(getCryptQuery());
//		mVampidroidFragment.getCryptListFragment().setOrderBy(DatabaseHelper.ORDER_BY_NAME);
//
//
//		adapter = new CardListCursorAdapter(CardType.LIBRARY, this,
//				R.layout.librarylistitem, null, new String[] { "Name", "Type",
//						"Clan", "Discipline" }, new int[] { R.id.txtCardName,
//						R.id.txtCardType, R.id.txtCardClan,
//						R.id.txtCardDiscipline });
//
//		mVampidroidFragment.getLibraryListFragment().setListAdapter(adapter);
//		mVampidroidFragment.getLibraryListFragment().setQuery(getLibraryQuery());
//		mVampidroidFragment.getLibraryListFragment().setOrderBy(DatabaseHelper.ORDER_BY_NAME);
//
//
//	}
//
//}
//
