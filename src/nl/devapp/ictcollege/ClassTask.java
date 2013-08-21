package nl.devapp.ictcollege;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

class ClassTask extends AsyncTask<ClassActivity, Void, Elements> {

	private ClassActivity activity;
	
	public ClassTask(ClassActivity activity){
		this.activity = activity;
	}
	
	@Override
	protected void onPostExecute(Elements results) {
		super.onPostExecute(results);
		
		if(results != null){
			for(Element result : results){
				activity.classList.add(result.text());
			}
			activity.adapter.notifyDataSetChanged();
			
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
		
		return;
	}

	@Override
	protected Elements doInBackground(ClassActivity... arg0) {

		try {
			Document doc = Jsoup.connect("http://interaa.nl/rooster/35/menu.htm").get();
	    	return doc.select("select[name=menu2] option:not([selected])");
	    	
		} catch (Exception e) {
			return null;
		}
	}

}
