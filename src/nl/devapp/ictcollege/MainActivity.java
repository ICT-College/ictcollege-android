package nl.devapp.ictcollege;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

    private final Activity self = this;
    public SharedPreferences fastSave;
    public ProgressDialog loadDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fastSave = this.getSharedPreferences("global", Context.MODE_PRIVATE);
        
        if(!isOnline()){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You're not online, please get online first.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           	self.finish();
                        }
                    });
            
            builder.show();
            
            return;
        }
        
        if(fastSave.getString("class", null) == null)
        {
        	Intent i = new Intent(MainActivity.this, ClassActivity.class);
        	startActivity(i);
        	
        	this.finish();
        	
        	return;
        }
        
        loadDialog = ProgressDialog.show(this, "Please wait", "Loading and fetching data", true);
        
        RoosterTask roosterTask = new RoosterTask(this);
        roosterTask.execute();
        
        final ListView l = (ListView) findViewById(R.id.listView1);
        String[] values = new String[] { "Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      	  @Override
      	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    		  String clickedDay = ((String) l.getItemAtPosition(position)).substring(0, 2);
      		  
      		  
      	  }
      	  
      	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main, menu);
    	
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
	        case R.id.action_settings:
	        	Intent launchNewIntent = new Intent(MainActivity.this, ClassActivity.class);
	        	startActivityForResult(launchNewIntent, 0);
	            return true;            
        }
        return false;
    }
    
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        
        return false;
    }
    
}
