package nl.devapp.ictcollege;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.TextView;

public class RoosterActivity extends Activity {

	protected String classNumber;
	protected ProgressDialog loadDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rooster);
		
		Intent intent = getIntent();
		classNumber = intent.getExtras().getString("class");
		
		TextView text = (TextView) findViewById(R.id.test);
		
		try {
	        loadDialog = ProgressDialog.show(this, "Please wait", "Loading and fetching data", true);
	        loadDialog.show();
	        
			Document doc = Jsoup.connect("http://interaa.nl/rooster/35/" + classNumber + ".htm").get();
	    	Element option = (Element) doc.select("pre").first();
	    	
			text.setText(option.text());
			
			loadDialog.dismiss();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
