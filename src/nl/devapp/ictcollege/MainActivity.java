package nl.devapp.ictcollege;

import java.io.IOException;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MainActivity extends Activity {

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
                        	MainActivity.this.finish();
                        }
                    });
            
            builder.show();
            
            return;
        }
        
        if(fastSave.getString("class", null) == null)
        {
        	Intent i = new Intent(this, ClassActivity.class);
        	startActivity(i);
        	
        	this.finish();
        	
        	return;
        }
        
        loadDialog = ProgressDialog.show(this, "Please wait", "Loading and fetching data", true);
        loadDialog.dismiss();
                
        final ListView l = (ListView) findViewById(R.id.listView1);
        
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK); 
        
        String[] values = null;
        
        if(day != Calendar.SUNDAY && day != Calendar.SATURDAY){
            values = new String[] { "Huidige dag", "Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag" };
        }else{
            values = new String[] { "Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag" };
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	
      	  	@Override
      	  	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

      	  		String clickedDay = (String) l.getItemAtPosition(position);
      	  		int day;
      	  		
      	  		//Damn android; we can't use a Switch because of the String.
      		  
      	  		if(clickedDay.equals("Maandag"))
      	  			day = Calendar.MONDAY;
      	  		else if(clickedDay.equals("Dinsdag"))
      	  			day = Calendar.TUESDAY;
      	  		else if(clickedDay.equals("Woensdag"))
      	  			day = Calendar.WEDNESDAY;
      	  		else if(clickedDay.equals("Donderdag"))
      	  			day = Calendar.THURSDAY;
      	  		else if(clickedDay.equals("Vrijdag"))
      	  			day = Calendar.FRIDAY;
      	  		else
      	  			day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
      	  		
      	  		System.out.println(day);
      	  		
      	  		if(fastSave.getString("cacheRooster", null) == null || fastSave.getInt("cacheTime", (int) (System.currentTimeMillis() / 1000L)) != (((int) (System.currentTimeMillis() / 1000L)) - 500))
      	  		{
					loadDialog.show();
					
					RoosterTask roosterTask = new RoosterTask(MainActivity.this);
					roosterTask.execute();
      	  		}
      	  		
      	  		if(fastSave.getString("cacheRooster", null) != null)
      	  		{
					Intent i = new Intent(MainActivity.this, RoosterActivity.class);
					i.putExtra("day", day);
					i.putExtra("rooster", fastSave.getString("cacheRooster", null));
					
		        	startActivity(i);
      	  		}else{
					Toast.makeText(MainActivity.this, "Unknow cache", Toast.LENGTH_LONG).show();
      	  		}
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
	        	Intent launchNewIntent = new Intent(this, ClassActivity.class);
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
