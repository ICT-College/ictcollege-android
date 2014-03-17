package nl.devapp.ictcollege.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nl.devapp.ictcollege.MainActivity;
import nl.devapp.ictcollege.R;
import nl.devapp.ictcollege.adapters.ScheduleAdapter;
import nl.devapp.ictcollege.models.Schedule;

public class ScheduleFragment extends Fragment {

    int weekDay;

    private ListView scheduleList;
    public ArrayList<Schedule> scheduleArray = new ArrayList<Schedule>();
    public ScheduleAdapter scheduleAdapter;

    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        try {
            scheduleArray = null;
            scheduleArray = new ArrayList<Schedule>();

            scheduleAdapter = new ScheduleAdapter(activity, scheduleArray);

            Log.d("ScheduleFragment", "Fragment of day: " + weekDay);

            for (int i = 0; i < activity.scheduleArray.size(); i++) {
                Log.d("ScheduleFragment", "Tester day: " + activity.scheduleArray.get(i).getWeekDay());

                if (activity.scheduleArray.get(i).getWeekDay() == weekDay) {
                    scheduleArray.add(activity.scheduleArray.get(i));
                }
            }

            scheduleList = (ListView) rootView.findViewById(R.id.rooster_list);
            scheduleList.setAdapter(scheduleAdapter);
            scheduleList.addFooterView(new View(rootView.getContext()), null, true);

            scheduleAdapter.notifyDataSetChanged();

            Long unixTimestamp = Long.parseLong(new BufferedReader(new FileReader(activity.cacheRoosterTimeFile.getAbsoluteFile())).readLine());

            TextView lastSyncView = (TextView) rootView.findViewById(R.id.last_sync);

            lastSyncView.setText("Last Sync: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(unixTimestamp)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = (MainActivity) activity;
        this.weekDay = getArguments().getInt("weekDay");

        this.scheduleAdapter = new ScheduleAdapter(activity, scheduleArray);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.activity = null;
    }

}
