package nl.devapp.ictcollege.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import nl.devapp.ictcollege.MainActivity;
import nl.devapp.ictcollege.fragments.ScheduleFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private MainActivity mainActivity;

    public MainPagerAdapter(android.support.v4.app.FragmentManager fm, MainActivity mainActivity) {
        super(fm);

        this.mainActivity = mainActivity;
    }

    @Override
    public Fragment getItem(int position) {
        //LOG//Log.d("MainActivity", "Day id: " + position + ", week day: " + mainActivity.getWeekDayByDayId(position));
        Bundle bundle = new Bundle();
        bundle.putInt("weekDay", mainActivity.getWeekDayByDayId(position));
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        scheduleFragment.setArguments(bundle);

        return scheduleFragment;
    }

    @Override
    public int getCount() {
        return mainActivity.days.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mainActivity.getDayNameByDayId(position);
    }
}