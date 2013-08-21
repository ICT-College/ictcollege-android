package nl.devapp.ictcollege;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
        fastSave = this.getSharedPreferences("global", Context.MODE_PRIVATE);
		
        loadDialog = ProgressDialog.show(this, "Please wait", "Loading and fetching data", true);
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classList);
        
        classViewList = (ListView)findViewById(R.id.listView1);
        classViewList.setAdapter(adapter);
        classViewList.setClickable(true);
        classViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    	  @Override
    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    		  
    		  String classNumber = (String) classViewList.getItemAtPosition(position);
    		  
    		  SharedPreferences.Editor editor = fastSave.edit();
    		  editor.putString("class", classNumber);
    		  editor.commit();
    		  
    		  Intent i = new Intent(ClassActivity.this, MainActivity.class);    		  
    		  startActivity(i);
    		  
    		  self.finish();
    	  }
    	  
    	});
        
        ClassTask classTask = new ClassTask(this);
        classTask.execute();
        
	}

}
