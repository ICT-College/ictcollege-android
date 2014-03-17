package nl.devapp.ictcollege.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nl.devapp.ictcollege.R;
import nl.devapp.ictcollege.models.Schedule;

public class ScheduleAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Schedule> items;

    private boolean hasProgressbar = false;

    public ScheduleAdapter(Context context, List<Schedule> list) {
        this.items = list;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isProgressbar()) {
                hasProgressbar = true;
            }
        }
    }

    @Override
    public int getCount() {
        if (this.items == null || this.items.isEmpty()) {
            return 0;
        } else {
            return this.items.size() + ((hasProgressbar) ? 1 : 0);
        }
    }

    @Override
    public Schedule getItem(int position) {
        Log.d("ScheduleAdapter", "Get position: " + position + ", but this.items contains: " + this.items.size());
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        final Schedule item = this.getItem(position);

        Log.d("ScheduleAdapter", "GetView position: " + position + ", but this.items contains: " + this.items.size() + ", progressbar: " + item.isProgressbar());

        if (item.isProgressbar()) {
            view = this.inflater.inflate(R.layout.row_schedule_progress, null);

            final ProgressBar progessBar = (ProgressBar) view.findViewById(R.id.schedule_progress);

            progessBar.setMax((int) item.getMax());

            final Handler handler = new Handler();
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            progessBar.setProgress((int) (System.currentTimeMillis() - item.getStart()));
                            Log.d("ScheduleAdapter", "Progess: " + (System.currentTimeMillis() - item.getStart()) + ", currentProgess: " + progessBar.getProgress() + ", max: " + item.getMax() + ", currentMax: " + progessBar.getMax());
                        }
                    });
                }
            }, 0, 5000);
        } else {
            view = this.inflater.inflate(R.layout.row_schedule, null);

            TextView number = (TextView) view.findViewById(R.id.item_number);
            TextView lesson = (TextView) view.findViewById(R.id.item_lesson);
            TextView to = (TextView) view.findViewById(R.id.item_to);
            TextView classRoom = (TextView) view.findViewById(R.id.item_class_room);

            number.setText(Integer.toString(item.getHour()));
            lesson.setText(item.getLesson());
            to.setText(item.getTo());
            classRoom.setText(item.getClassRoom());
        }

        return view;
    }

}