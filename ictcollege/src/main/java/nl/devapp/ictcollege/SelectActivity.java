package nl.devapp.ictcollege;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.Window;

import nl.devapp.ictcollege.adapters.SelectPagerAdapter;


public class SelectActivity extends ActionBarActivity implements ActionBar.TabListener {

    private SelectPagerAdapter mSectionsPagerAdapter;

    private SharedPreferences preferences;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_select);

        setProgressBarIndeterminate(true);

        setTitle("Rooster keuze");

        preferences = this.getSharedPreferences("global", Context.MODE_PRIVATE);

        if (preferences.getInt("targetId", 0) != 0)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SelectPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.select_pager);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

}
