package nl.devapp.ictcollege.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import nl.devapp.ictcollege.fragments.SelectFragment;

public class SelectPagerAdapter extends FragmentPagerAdapter {

    public SelectPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("sectionNumber", position + 1);
        SelectFragment selectFragment = new SelectFragment();
        selectFragment.setArguments(bundle);

        return selectFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Klas";
            case 1:
                return "Docent";
        }
        return null;
    }
}