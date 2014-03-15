package nl.devapp.ictcollege;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import nl.devapp.ictcollege.adapters.MainPagerAdapter;
import nl.devapp.ictcollege.models.Schedule;
import nl.devapp.ictcollege.tasks.ScheduleTask;

/*
--- TargetType ---
0 = Null/nothing
1 = Class
2 = Teacher

--- Variables ---
weekDay = Day of the week (1 = monday, 5 = friday)
dayId = Index ID of day (0 = monday, 4 = friday)
dayName = Name of the day
currentDay = Current selected WeekDay.
 */

public class MainActivity
        extends ActionBarActivity implements ActionBar.TabListener {

    private SharedPreferences preferences;

    public String[] days = {"Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag"};
    private int[] dayIds = {2, 3, 4, 5, 6};

    private int currentDay;

    private MainPagerAdapter mSectionsPagerAdapter;
    public ViewPager mViewPager;

    public ArrayList<Schedule> scheduleArray = new ArrayList<Schedule>();

    private Calendar calendar = Calendar.getInstance();

    public File cacheRoosterJsonFile;
    public File cacheRoosterTimeFile;

    private Menu menu;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

        setProgressBarIndeterminate(true);

        preferences = getSharedPreferences("global", Context.MODE_PRIVATE);
        cacheRoosterJsonFile = new File(getApplicationContext().getCacheDir(), "rooster_json.cache");
        cacheRoosterTimeFile = new File(getApplicationContext().getCacheDir(), "rooster_time.cache");

        if (preferences.getInt("targetType", 0) == 0 || preferences.getInt("targetId", 0) == 0) {
            startActivity(new Intent(this, SelectActivity.class));

            this.finish();

            return;
        }

        currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        Log.d("MainActivity", "currentDay: " + currentDay + ", dayId: " + getDayIdByWeekDay(currentDay));

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);

        mViewPager = (ViewPager) findViewById(R.id.schedule_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            getSupportActionBar().addTab(
                    getSupportActionBar().newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

        mViewPager.setCurrentItem(getDayIdByWeekDay(currentDay), true);

        getSupportActionBar().setTitle(getDayNameByWeekDay(currentDay));

        ScheduleTask scheduleTask = new ScheduleTask(this);
        scheduleTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);

        menu.getItem(0).setVisible(!isLoading);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select:
                startActivity(new Intent(this, SelectActivity.class));
                return true;
            case R.id.action_refresh:
                cacheRoosterJsonFile.delete();
                cacheRoosterTimeFile.delete();

                ScheduleTask scheduleTask = new ScheduleTask(this);
                scheduleTask.execute();
                return true;
//            case R.id.action_open_settings:
//                Intent settingsIntent = new Intent(this, SettingsActivity.class);
//                startActivity(settingsIntent);
//                return true;
        }
        return false;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());

        currentDay = getWeekDayByDayId(tab.getPosition());
        getSupportActionBar().setTitle(getDayNameByWeekDay(currentDay));
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    private int getDayIdByWeekDay(int weekDay) {
        for (int i = 0; i < dayIds.length; i++) {
            if (dayIds[i] == weekDay) {
                return i;
            }
        }

        return 0;
    }

    public int getWeekDayByDayId(int dayId) {
        return dayIds[dayId];
    }

    private String getDayNameByWeekDay(int weekDay) {
        return getDayNameByDayId(getDayIdByWeekDay(weekDay));
    }

    public String getDayNameByDayId(int dayId) {
        return days[dayId];
    }

    public void setLoading(final Boolean type) {
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProgressBarIndeterminateVisibility(type);

                    isLoading = type;

                    if (menu != null)
                        menu.getItem(0).setVisible(!type);
                }
            });
        }
    }

}
