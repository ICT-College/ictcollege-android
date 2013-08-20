package nl.devapp.ictcollege;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;

public class MainActivity extends Activity {

    protected ProgressDialog loadDialog;
    protected AlertDialog.Builder closeDialog;
    protected final Activity self = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadDialog = ProgressDialog.show(this, "Please wait", "Loading and fetching data", true);
                
        if(!isOnline()){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You're not online, please get online first.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           	self.finish();
                        }
                    });
            
        	loadDialog.dismiss();
            builder.show();
            
            return;
        }
        
        new Thread(new Runnable(){
            public void run() {
                loadDropdown();
                loadDialog.dismiss();
            }
        }).start();
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
    //	
    //    return true;
    //}
    
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        
        return false;
    }
    
    public void loadDropdown(){
        new GregorianCalendar().get(Calendar.WEEK_OF_YEAR);
    }
    
}
