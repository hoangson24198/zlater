package com.example.zlater.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.zlater.Fragments.Meals.MorningFragment;
import com.example.zlater.Fragments.Meals.NightFragment;
import com.example.zlater.Fragments.Meals.NoonFragment;
import com.example.zlater.R;
import com.example.zlater.Utils.CheckInternetConnection;

import java.util.ArrayList;
import java.util.Objects;

import devlight.io.library.ntb.NavigationTabBar;

public class MealsActivity extends AppCompatActivity {
    private NavigationTabBar tab_meals;
    private ViewPager pager_meals;
    private Toolbar toolbar;

    private void initView() {
        tab_meals = findViewById(R.id.tab_meals);
        pager_meals = findViewById(R.id.pager_meals);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initView();
        new CheckInternetConnection(this).checkConnection();
        addModelTab();

        Intent i = getIntent();
        String dietTitle = i.getStringExtra("title");
        toolbar.setTitle("Chế độ ăn " + dietTitle.toLowerCase());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pager_meals.setOffscreenPageLimit(3);
        MealsPagerAdapter mealsPagerAdapter = new MealsPagerAdapter(getSupportFragmentManager());
        pager_meals.setAdapter(mealsPagerAdapter);
        tab_meals.setViewPager(pager_meals);
    }

    private void addModelTab() {
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.tab_morning),
                        getResources().getColor(R.color.morning)
                ).title(getText(R.string.morning).toString())
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.tab_noon),
                        getResources().getColor(R.color.noon)
                ).title(getText(R.string.afternoon).toString())
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.tab_night),
                        getResources().getColor(R.color.night)
                ).title(getText(R.string.night).toString())
                        .badgeTitle("state")
                        .build()
        );
        tab_meals.setModels(models);
    }
}

class MealsPagerAdapter extends FragmentStatePagerAdapter {

    public MealsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MorningFragment();
                break;
            case 1:
                fragment = new NoonFragment();
                break;
            case 2:
                fragment = new NightFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
