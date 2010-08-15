package name.vampidroid;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

public class VampiDroid extends TabActivity {
	
	public static SQLiteDatabase db;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        
        db = SQLiteDatabase.openDatabase("/sdcard/VampiDroid.db", null, SQLiteDatabase.OPEN_READONLY);
        boolean b = db.isOpen();
        
        
        
        // Fill crypt list.
        
        ListView listCrypt = (ListView) findViewById(R.id.ListViewCrypt);
        
        Cursor c = db.rawQuery("select _id, Name from crypt", null);
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listitem, 
        		c, new String[] { "Name" }, new int[] {android.R.id.text1});
        
        
        listCrypt.setAdapter(adapter);
        
        
        
        listCrypt.setOnItemClickListener(new OnItemClickListener() {

        	public void onItemClick(AdapterView<?> parent, 
		            View v, int position, long id) {
				
				// TODO Auto-generated method stub
				Cursor c = db.rawQuery("select CardText from crypt where _id = " + String.valueOf(id), null );
				c.moveToFirst();
				String cardText = c.getString(0);
				c.close();
				
								
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				
				builder.setMessage(cardText);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(  DialogInterface dialog, int id) {
			                dialog.dismiss();
			           }
			       });
        
        
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
				
				Intent cryptCardIntent = new Intent(v.getContext(), CardDetails.class );
							
				
				cryptCardIntent.setData(Uri.withAppendedPath(Uri.parse("vampidroid://library/id"), String.valueOf(id)));
				startActivity(cryptCardIntent);
				
				
				
				
				
				
/*				Toast.makeText(getBaseContext(), 
                        cardText, 
                        Toast.LENGTH_LONG).show();
*/				
			}
        	
        	
		});
			
			
        
        
        //listCrypt.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, COUNTRIES));
        
        
        // Fill crypt list.
        
        ListView listLibrary = (ListView) findViewById(R.id.ListViewLibrary);
        
        c = db.rawQuery("select _id, Name from library", null);
        
        adapter = new SimpleCursorAdapter(this, R.layout.listitem, 
        		c, new String[] { "Name" }, new int[] {android.R.id.text1});
        
        
        listLibrary.setAdapter(adapter);
        
        
        listLibrary.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, 
		            View v, int position, long id) {
				
				// TODO Auto-generated method stub
				Cursor c = db.rawQuery("select CardText from library where _id = " + String.valueOf(id), null );
				c.moveToFirst();
				String cardText = c.getString(0);
				c.close();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				
				builder.setMessage(cardText);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(  DialogInterface dialog, int id) {
			                dialog.dismiss();
			           }
			       });
        
        
				AlertDialog alertDialog = builder.create();
				alertDialog.show(); 
				

				
			}
        	
        	
		});
        
        TabHost mTabHost = getTabHost();
        
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_crypt").setIndicator("Crypt").setContent(R.id.ListViewCrypt));
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_library").setIndicator("Library").setContent(R.id.ListViewLibrary));
        
        
        mTabHost.setCurrentTab(0);
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		db.close();
	}
}