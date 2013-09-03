package nl.devapp.ictcollege;

import java.util.ArrayList;
import java.util.List;

import nl.devapp.ictcollege.adapters.ScheduleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RoosterActivity extends ListActivity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

		setContentView(R.layout.activity_rooster);

		new ListLoader(this, intent.getExtras().getString("rooster"), intent
				.getExtras().getInt("day")).execute();

		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public class ListLoader extends AsyncTask<Void, Void, List<Schedule>> {
		private Activity activity;
		private String scheduleJson;
		private int day;

		public ListLoader(Activity activity, String scheduleJson, int day) {
			this.activity = activity;
			this.scheduleJson = scheduleJson;
			this.day = day;
		}

		@Override
		protected List<Schedule> doInBackground(Void... params) {
			List<Schedule> list = new ArrayList<Schedule>();

			JSONObject lessons = null;
			try {
				lessons = new JSONObject(this.scheduleJson).getJSONObject(
						"data").getJSONObject(Integer.toString(this.day));

				for (int i = 1; i <= lessons.length(); i++) {
					JSONObject lesson = lessons.getJSONObject(i + "");

					Schedule item = new Schedule();
					item.setLesson(lesson.getString("lesson"));
					item.setTeacher(lesson.getString("teacher"));
					item.setClassRoom(lesson.getString("classroom"));

					list.add(item);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return list;
		}

		@Override
		protected void onPostExecute(List<Schedule> schedule) {
			ListView list = (ListView) this.activity
					.findViewById(android.R.id.list);

			ListAdapter adapter = new ScheduleAdapter(this.activity, schedule);
			list.setAdapter(adapter);
		}
	}

}
