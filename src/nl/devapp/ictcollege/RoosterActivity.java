package nl.devapp.ictcollege;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

public class RoosterActivity extends Activity {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TableLayout table = new TableLayout(this);
	    table.setStretchAllColumns(true);  
	    table.setShrinkAllColumns(true);  
		
		//LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		//table.setLayoutParams(layoutParams);
		
		Intent intent = getIntent();
		
		try {
			JSONObject lessons = new JSONObject(intent.getExtras().getString("rooster")).getJSONObject("data").getJSONObject(intent.getExtras().getInt("day")+"");
			
			for(int i = 1; i <= lessons.length(); i++) {
				JSONObject lesson = lessons.getJSONObject(i+"");
				
			    TextView viewHour = new TextView(this);  
			    viewHour.setText(i+"");  
			    viewHour.setTypeface(Typeface.DEFAULT_BOLD); 

			    TextView viewLesson = new TextView(this);  
			    viewLesson.setText(lesson.getString("lesson"));  
			    viewLesson.setTypeface(Typeface.DEFAULT_BOLD); 

			    TextView viewTeacher = new TextView(this);  
			    viewTeacher.setText(lesson.getString("teacher")); 
			    viewTeacher.setTypeface(Typeface.DEFAULT_BOLD); 

			    TextView viewClassRoom = new TextView(this);  
			    viewClassRoom.setText(lesson.getString("classroom")); 
			    viewClassRoom.setTypeface(Typeface.DEFAULT_BOLD); 
			    
			    TableRow lessonRow = new TableRow(this);
			    
			    lessonRow.addView(viewHour);
			    lessonRow.addView(viewLesson);
			    lessonRow.addView(viewTeacher);
			    lessonRow.addView(viewClassRoom);
			    
				table.addView(lessonRow);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setContentView(table);
		
    	getActionBar().setDisplayHomeAsUpEnabled(true);
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
