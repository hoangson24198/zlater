package com.example.zlater.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.zlater.Fragments.DietsFragment;
import com.example.zlater.Fragments.ExerciseFragment;
import com.example.zlater.Fragments.HomeFragment;
import com.example.zlater.Fragments.ProfileFragment;

import org.jetbrains.annotations.NotNull;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs = numberOfTabs;
    }

    @NotNull
    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new HomeFragment();
            case 1:
                return new DietsFragment();
            case 2:
                return new ExerciseFragment();
            case 3:
                return new ProfileFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
