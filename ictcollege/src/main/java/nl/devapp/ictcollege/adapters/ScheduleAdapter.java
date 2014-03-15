package nl.devapp.ictcollege.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import nl.devapp.ictcollege.R;
import nl.devapp.ictcollege.models.Schedule;

public class ScheduleAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Schedule> items;

    public ScheduleAdapter(Context context, List<Schedule> list) {
        this.items = list;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (this.items == null || this.items.isEmpty()) {
            return 0;
        } else {
            return this.items.size();
        }
    }

    @Override
    public Schedule getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = this.inflater.inflate(R.layout.row_schedule, null);

        Schedule item = this.getItem(position);

        TextView number = (TextView) view.findViewById(R.id.item_number);
        TextView lesson = (TextView) view.findViewById(R.id.item_lesson);
        TextView to = (TextView) view.findViewById(R.id.item_to);
        TextView classRoom = (TextView) view.findViewById(R.id.item_class_room);

        number.setText(Integer.toString(item.getHour()));
        lesson.setText(item.getLesson());
        to.setText(item.getTo());
        classRoom.setText(item.getClassRoom());

        return view;
    }

}