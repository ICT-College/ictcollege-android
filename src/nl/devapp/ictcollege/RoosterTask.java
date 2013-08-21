package nl.devapp.ictcollege;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

class RoosterTask extends AsyncTask<Void, Void, Element> {
	
	private MainActivity activity;
	
	public RoosterTask(MainActivity activity){
		this.activity = activity;
	}
	
	@Override
	protected void onPostExecute(Element result) {
		super.onPostExecute(result);
		
		if(result != null){
			
			activity.loadDialog.dismiss();
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("We're unable to parse the school website, this normally means that you don't have an working internet connection.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           	activity.finish();
                        }
                    });
            
            builder.show();
			activity.loadDialog.dismiss();
		}
	}

	@Override
	protected Element doInBackground(Void... params) {

		try{
			Document doc = Jsoup.connect("http://interaa.nl/rooster/35/" + activity.fastSave.getString("class", "") + ".htm").get();
	    	Element option = (Element) doc.select("pre").first();
	    	
	    	return option;
		}catch(Exception e){
			return null;
		}
	}
}
