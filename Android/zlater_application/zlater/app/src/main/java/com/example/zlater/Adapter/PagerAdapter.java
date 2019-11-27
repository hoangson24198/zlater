package com.example.zlater.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.zlater.Fragments.DietsFragment;
import com.example.zlater.Fragments.ExerciseFragment;
import com.example.zlater.Fragments.HomeFragment;
import com.example.zlater.Fragments.ProfileFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                DietsFragment dietsFragment = new DietsFragment();
                return dietsFragment;
            case 2:
                ExerciseFragment exerciseFragment = new ExerciseFragment();
                return exerciseFragment;
            case 3:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
