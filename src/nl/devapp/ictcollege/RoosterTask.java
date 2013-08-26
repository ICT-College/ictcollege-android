package nl.devapp.ictcollege;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

class RoosterTask extends AsyncTask<Void, Void, Element> {

	private MainActivity activity;

	public RoosterTask(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPostExecute(Element result) {
		super.onPostExecute(result);

		if (result != null) {
			Editor fastEdit = activity.fastSave.edit();
			fastEdit.putString("cacheRooster",
					result.html().replace("&quot;", "'"));
			fastEdit.putInt("cacheTime",
					(int) (System.currentTimeMillis() / 1000L));
			fastEdit.commit();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(activity.getResources().getString(
					R.string.net_error_cant_reach_server));
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							activity.finish();
						}
					});

			builder.show();
		}
		activity.loadDialog.dismiss();
	}

	@Override
	protected Element doInBackground(Void... params) {

		try {
			Document doc = Jsoup.connect(
					this.activity.getResources().getString(R.string.server_baseurl) + "/?week=35&class="
							+ activity.fastSave.getString("class", null)).get();

			return doc.select("body").first();
		} catch (Exception e) {
			return null;
		}
	}
}
