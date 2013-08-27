package nl.devapp.ictcollege;

import java.util.ArrayList;

import nl.devapp.ictcollege.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ClassActivity extends Activity {

	public ProgressDialog loadDialog;
	public ArrayList<String> classList = new ArrayList<String>();
	public ArrayAdapter<String> adapter;
	public ListView classViewList;
	private SharedPreferences fastSave;
	public final Activity self = this;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class);

		fastSave = this.getSharedPreferences("global", Context.MODE_PRIVATE);

		if (fastSave.getString("class", null) != null)
			getActionBar().setDisplayHomeAsUpEnabled(true);

		loadDialog = ProgressDialog.show(this,
				this.getResources()
						.getString(R.string.dialog_title_please_wait),
				this.getResources()
						.getString(R.string.dialog_text_loading_data), true);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, classList);

		classViewList = (ListView) findViewById(R.id.listView1);
		classViewList.setAdapter(adapter);
		classViewList.setClickable(true);
		classViewList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						String classNumber = (String) classViewList
								.getItemAtPosition(position);

						SharedPreferences.Editor editor = fastSave.edit();
						editor.putString("class", classNumber);
						editor.commit();

						Intent i = new Intent(ClassActivity.this,
								MainActivity.class);
						startActivity(i);

						ClassActivity.this.finish();
					}

				});

		ClassTask classTask = new ClassTask(this);
		classTask.execute();

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
}
