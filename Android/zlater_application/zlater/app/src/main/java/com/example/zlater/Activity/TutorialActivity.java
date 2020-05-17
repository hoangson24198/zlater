package com.example.zlater.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.zlater.Fragments.TutorialCardFragment;
import com.example.zlater.Model.Tutorial;
import com.example.zlater.R;
import com.example.zlater.Utils.CheckInternetConnection;

import java.util.ArrayList;
import java.util.Objects;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager vpager_tutorial;
    private Button prevTutorial, nextTutorial;
    private ArrayList<Tutorial> sampleTutorials;

    private void initView() {
        vpager_tutorial = findViewById(R.id.vpager_tutorial);
        prevTutorial = findViewById(R.id.btn_prev_tutorial);
        nextTutorial = findViewById(R.id.btn_next_tutorial);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initView();
        new CheckInternetConnection(this).checkConnection();
        sampleTutorials = new ArrayList<>();
        sampleTutorials.add(new Tutorial(getText(R.string.exercies).toString(), getText(R.string.exercise_des_tut).toString(),R.drawable.undraw_healthy_habit_bh5w));
        sampleTutorials.add(new Tutorial(getText(R.string.tracking_on_health).toString(), getText(R.string.tracking_des).toString(),R.drawable.undraw_fitness_tracker_3033));
        sampleTutorials.add(new Tutorial(getText(R.string.check_your_project).toString(), getText(R.string.check_des).toString(),R.drawable.undraw_calendar_dutt));
        TutorialPagerAdapter tutorialPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager(), sampleTutorials, this);
        vpager_tutorial.setAdapter(tutorialPagerAdapter);

        prevTutorial.setOnClickListener(this);
        nextTutorial.setOnClickListener(this);

        vpager_tutorial.setPadding(60, 0 , 60 , 0);

        vpager_tutorial.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setButtonVisibility();
            }

            @Override
            public void onPageSelected(int position) {
                setButtonVisibility();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                setButtonVisibility();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int currentIndex = vpager_tutorial.getCurrentItem();
        switch(view.getId()) {
            case R.id.btn_prev_tutorial:
                if(currentIndex > 0) {
                    vpager_tutorial.setCurrentItem(currentIndex - 1);
                }
                break;
            case R.id.btn_next_tutorial:
                if(currentIndex < sampleTutorials.size()) {
                    vpager_tutorial.setCurrentItem(currentIndex + 1);
                }

                if(currentIndex == sampleTutorials.size() - 1) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
        }
    }

    private void setButtonVisibility() {
        int currentIndex =  vpager_tutorial.getCurrentItem();
        if(currentIndex == 0) {
            prevTutorial.setVisibility(View.INVISIBLE);
        } else {
            prevTutorial.setVisibility(View.VISIBLE);
        }

        if(currentIndex == sampleTutorials.size() - 1) {
            nextTutorial.setText(R.string.get_started);
        } else {
            nextTutorial.setVisibility(View.VISIBLE);
        }
    }
}

class TutorialPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Tutorial> tutorials;
    private Context context;

    public TutorialPagerAdapter(FragmentManager fm, ArrayList<Tutorial> tutorials, Context context) {
        super(fm);
        this.tutorials = tutorials;
        this.context = context;
    }

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Tutorial tutorial = tutorials.get(position);
        switch (position) {
            case 0:
                fragment = new TutorialCardFragment(tutorial.getTitle(), tutorial.getDescription(),tutorial.getImage());
                break;
            case 1:
                fragment = new TutorialCardFragment(tutorial.getTitle(), tutorial.getDescription(),tutorial.getImage());
                break;
            case 2:
                fragment = new TutorialCardFragment(tutorial.getTitle(), tutorial.getDescription(),tutorial.getImage());
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tutorials.size();
    }
}
