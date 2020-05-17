package com.example.zlater.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.zlater.Adapter.PagerAdapter;
import com.example.zlater.Fragments.DietsFragment;
import com.example.zlater.Fragments.HistoriesFragment;
import com.example.zlater.Fragments.HomeFragment;
import com.example.zlater.Fragments.ProfileFragment;
import com.example.zlater.Helper.DateTimeHelper;
import com.example.zlater.Model.Responses.RoutineResponse;
import com.example.zlater.Model.Routine;
import com.example.zlater.Model.RoutineRequest;
import com.example.zlater.Model.step.StepModel;
import com.example.zlater.R;

import com.example.zlater.Service.ForegroundServices;
import com.example.zlater.Service.local.ZlaterDatabase;
//import com.example.zlater.Service.local.step.StepCountServices;
import com.example.zlater.Service.local.step.StepService;
import com.example.zlater.Service.remote.RetrofitClient;
import com.example.zlater.Service.remote.RoutineAPI;
import com.example.zlater.Utils.CheckInternetConnection;
import com.example.zlater.Utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import devlight.io.library.ntb.NavigationTabBar;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, DietsFragment.OnFragmentInteractionListener,
        HistoriesFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {
    private NavigationTabBar tabBar;
    List<Routine> routines;
    private RoutineAPI routineAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Retrofit retrofit = RetrofitClient.getInstance();
        routineAPI = retrofit.create(RoutineAPI.class);
//        runServices();
        tabBar = findViewById(R.id.tabs_main);
        addModelTab();
        final ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(4);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(adapter);
        tabBar.setViewPager(viewPager);
        new CheckInternetConnection(this).checkConnection();
        getAndSaveRoutine();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void addModelTab() {
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.house),
                        getResources().getColor(R.color.coral)
                ).title("Trang chủ")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.diet),
                        getResources().getColor(R.color.coral)
                ).title("Món ăn")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.gym),
                        getResources().getColor(R.color.coral)
                ).title("Bài tập")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.skills),
                        getResources().getColor(R.color.coral)
                ).title("Thông tin")
                        .badgeTitle("icon")
                        .build()
        );
        tabBar.setModels(models);
    }

//    private void runServices() {
//        Intent serviceIntent = new Intent(this, StepService.class);
//        ContextCompat.startForegroundService(this, serviceIntent);
//    }


    //get routine
    private void getAndSaveRoutine() {
        routines = ZlaterDatabase.getInstance(getApplicationContext()).routineDAO().getRoutine();
        if (!routines.isEmpty()) {
            postRoutine(routines);
            Log.d("HS:::", "saved routing list: ");
        }
        if(routines.isEmpty()){
           Log.e("HS:::","List empty!!!");
        }
    }

    private void postRoutine(List<Routine> routineList) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
        for (int i = 0; i < routineList.size(); i++) {
            Log.e("HS:::", routineList.get(i).getStepCount() + "get routing");
            RoutineRequest routine = new RoutineRequest(routineList.get(i).getStepCount(), routineList.get(i).getCreatedAt(),routineList.get(i).getTimePractice(), (routineList.get(i).getStepCount() * 4) + "", sharedPreferences.getInt("id", 0));

            Call<RoutineResponse> callRoutine = routineAPI.createRoutine(routine);
            callRoutine.enqueue(new Callback<RoutineResponse>() {
                @Override
                public void onResponse(Call<RoutineResponse> call, Response<RoutineResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("HS::", "save routing success");
                        ZlaterDatabase.getInstance(MainActivity.this).routineDAO().deleteAll();
                    }
                    if (!response.isSuccessful()) {
                        Log.e("HS::", response.code() + " : " + response.body()+"post routing failed");
                    }

                }

                @Override
                public void onFailure(Call<RoutineResponse> call, Throwable t) {
                    Log.e("HS:::", "save routing failed" + "\n" + call.request() + " :: " + call.toString());
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new CheckInternetConnection(this).checkConnection();
    }
}
