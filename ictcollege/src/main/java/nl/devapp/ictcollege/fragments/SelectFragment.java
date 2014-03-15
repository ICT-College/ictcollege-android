package nl.devapp.ictcollege.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import nl.devapp.ictcollege.MainActivity;
import nl.devapp.ictcollege.R;
import nl.devapp.ictcollege.tasks.SelectTask;

public class SelectFragment extends Fragment {
    private int sectionNumber; //Same as TargetId

    private SharedPreferences preferences;

    public ArrayAdapter<String> targetListAdapter;

    public ArrayList<String> targetListValues = new ArrayList<String>();
    public ArrayList<Integer> targetListIds = new ArrayList<Integer>();

    private ListView targetListView;

    public File cacheRoosterJsonFile;
    public File cacheRoosterTimeFile;

    @Override
    public void onCreate(Bundle savedNogwat) {
        super.onCreate(savedNogwat);

        cacheRoosterJsonFile = new File(getActivity().getApplicationContext().getCacheDir(), "rooster_json.cache");
        cacheRoosterTimeFile = new File(getActivity().getApplicationContext().getCacheDir(), "rooster_time.cache");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select, container, false);

        sectionNumber = getArguments().getInt("sectionNumber");

        preferences = getActivity().getSharedPreferences("global", Context.MODE_PRIVATE);

        targetListAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1, targetListValues);

        targetListView = (ListView) rootView.findViewById(R.id.targetListView);
        targetListView.setClickable(true);
        targetListView.setAdapter(targetListAdapter);
        targetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                String targetName = targetListValues.get(position);
                int targetId = targetListIds.get(position);

                Log.d("SelectFragment", "Got click, name: " + targetName + ", id: " + targetId);

                SharedPreferences.Editor preferencesEditor = preferences.edit();

                preferencesEditor.putInt("targetType", sectionNumber);
                preferencesEditor.putInt("targetId", targetId);

                preferencesEditor.commit();

                cacheRoosterJsonFile.delete();
                cacheRoosterTimeFile.delete();

                startActivity(new Intent(getActivity(), MainActivity.class));

                getActivity().finish();

                return;
            }

        });

        SelectTask selectTask = new SelectTask(this, sectionNumber);
        selectTask.execute();

        return rootView;
    }

    public void setLoading(final Boolean type) {
        if (!isDetached() && getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().setProgressBarIndeterminateVisibility(type);
                }
            });
        }
    }
}