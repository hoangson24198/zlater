package com.krahs.adminzlater.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.krahs.adminzlater.Adapter.PagerAdapter;
import com.krahs.adminzlater.Fragments.DishFragment;
import com.krahs.adminzlater.Fragments.ExerciseFragment;
import com.krahs.adminzlater.Fragments.IngredientFragment;
import com.krahs.adminzlater.Fragments.MixFragment;
import com.krahs.adminzlater.Model.User;
import com.krahs.adminzlater.R;
import com.krahs.adminzlater.Services.AdminZlaterServices;
import com.krahs.adminzlater.Services.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements ExerciseFragment.OnFragmentInteractionListener, DishFragment.OnFragmentInteractionListener,
        IngredientFragment.OnFragmentInteractionListener, MixFragment.OnFragmentInteractionListener {
   NavigationView navigationView;
   ImageView layoutOption;
    List<User> users =new ArrayList<>();

    private AdminZlaterServices adminZlaterServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
//        getReminder();
        Retrofit retrofit = RetrofitClient.getInstance();
        adminZlaterServices = retrofit.create(AdminZlaterServices.class);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_exercise));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_diet));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ingredient));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.mix));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.pager);
        final com.krahs.adminzlater.Adapter.PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
