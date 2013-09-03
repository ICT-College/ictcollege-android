package nl.devapp.ictcollege;

import java.util.Calendar;

import nl.devapp.ictcollege.activities.SettingsActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	public SharedPreferences fastSave;
	public ProgressDialog loadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fastSave = this.getSharedPreferences("global", Context.MODE_PRIVATE);

		if (!isOnline()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(this.getResources().getString(
					R.string.net_error_not_online));
			builder.setPositiveButton(
					this.getResources().getString(R.string.dialog_button_okay),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							MainActivity.this.finish();
						}
					});

			builder.show();

			return;
		}

		if (fastSave.getString("class", null) == null) {
			Intent i = new Intent(this, ClassActivity.class);
			startActivity(i);

			this.finish();

			return;
		}

		loadDialog = ProgressDialog.show(this,
				this.getResources()
						.getString(R.string.dialog_title_please_wait),
				this.getResources()
						.getString(R.string.dialog_text_loading_data), true);
		loadDialog.dismiss();

		final ListView l = (ListView) findViewById(R.id.listView1);

		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

		String[] values = null;

		if (day != Calendar.SUNDAY && day != Calendar.SATURDAY) {
			values = new String[] {
					this.getResources().getString(R.string.day_today),
					this.getResources().getString(R.string.day_monday),
					this.getResources().getString(R.string.day_tuesday),
					this.getResources().getString(R.string.day_wednesday),
					this.getResources().getString(R.string.day_thursday),
					this.getResources().getString(R.string.day_friday) };
		} else {
			values = new String[] {
					this.getResources().getString(R.string.day_monday),
					this.getResources().getString(R.string.day_tuesday),
					this.getResources().getString(R.string.day_wednesday),
					this.getResources().getString(R.string.day_thursday),
					this.getResources().getString(R.string.day_friday) };
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		l.setAdapter(adapter);
		l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				String clickedDay = (String) l.getItemAtPosition(position);
				int day;

				// Damn android; we can't use a Switch because of the String.

				if (clickedDay.equals(getResources().getString(
						R.string.day_monday)))
					day = Calendar.MONDAY;
				else if (clickedDay.equals(getResources().getString(
						R.string.day_tuesday)))
					day = Calendar.TUESDAY;
				else if (clickedDay.equals(getResources().getString(
						R.string.day_wednesday)))
					day = Calendar.WEDNESDAY;
				else if (clickedDay.equals(getResources().getString(
						R.string.day_thursday)))
					day = Calendar.THURSDAY;
				else if (clickedDay.equals(getResources().getString(
						R.string.day_friday)))
					day = Calendar.FRIDAY;
				else
					day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

				int currentTime = (int) (System.currentTimeMillis() / 1000L);

				if (fastSave.getString("cacheRooster", null) == null
						|| fastSave.getInt("cacheTime", currentTime) <= ((currentTime) - 100)) {
					loadDialog.show();

					RoosterTask roosterTask = new RoosterTask(
							MainActivity.this, day);
					roosterTask.execute();
				} else {
					Intent i = new Intent(MainActivity.this,
							RoosterActivity.class);
					i.putExtra("day", day);
					i.putExtra("rooster",
							fastSave.getString("cacheRooster", null));

					startActivity(i);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_select_class:
			Intent launchNewIntent = new Intent(this, ClassActivity.class);
			startActivityForResult(launchNewIntent, 0);
			return true;
		case R.id.action_open_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		}
		return false;
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}

}
