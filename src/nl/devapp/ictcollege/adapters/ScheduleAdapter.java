package nl.devapp.ictcollege.adapters;

import java.util.List;

import nl.devapp.ictcollege.R;
import nl.devapp.ictcollege.Schedule;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
		return this.items.size();
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
			view = this.inflater.inflate(R.layout.list_item_schedule, null);

		Schedule item = this.getItem(position);

		TextView number = (TextView) view.findViewById(R.id.item_number);
		TextView lesson = (TextView) view.findViewById(R.id.item_lesson);
		TextView teacher = (TextView) view.findViewById(R.id.item_teacher);
		TextView classRoom = (TextView) view.findViewById(R.id.item_class_room);

		number.setText(Integer.toString(position + 1));
		lesson.setText(item.getLesson());
		teacher.setText(item.getTeacher());
		classRoom.setText(item.getClassRoom());

		return view;
	}

}
