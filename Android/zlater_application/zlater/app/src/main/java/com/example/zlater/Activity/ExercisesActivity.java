package com.example.zlater.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zlater.Adapter.ExercisesAdapter;
import com.example.zlater.Interface.ItemClickListener;
import com.example.zlater.Model.Exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zlater.Model.Responses.BodypartResponse;
import com.example.zlater.R;
import com.example.zlater.Service.remote.BodypartsAPI;
import com.example.zlater.Service.remote.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExercisesActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView rv_exercises;
    private BodypartsAPI bodypartsAPI;
    private TextView tv_totalWorkouts, tv_workoutDescription;
    private int bodyPartId;

    private void initView() {
        rv_exercises = findViewById(R.id.rv_exercises);
        tv_totalWorkouts = findViewById(R.id.tv_total_workout_header_exercise);
        tv_workoutDescription = findViewById(R.id.tv_description_header_exercise);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        Retrofit retrofit = RetrofitClient.getInstance();
        bodypartsAPI = retrofit.create(BodypartsAPI.class);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent i = getIntent();
        bodyPartId = i.getIntExtra("id", 0);
        String title = i.getStringExtra("title");

        getSupportActionBar().setTitle(title);
        tv_workoutDescription.setText(getHeaderDescription(title));

        this.getExerciseData(bodyPartId);
    }

    private String getHeaderDescription(String title) {
        switch (title) {
            case "Tay":
                return "Tổng hợp các bài tập tay bao gồm cơ tay sau, tay trước, cổ tay, cẳng tay, ... .";
            case "Chân":
                return "Phát triển toàn hiện phần dưới cơ thể bao gồm mông, đùi, chân.";
            case "Bụng":
                return "Đôt cháy lượng mỡ bụng để có cơ bụng săn chắc";
            case "Ngực":
                return "Các bài tập ngực phù hợp cho cả nam và nữ.";
            default:
                return "Tổng hợp các bài lưng, xô, và vai";
        }
    }

    private void getExerciseData(int id) {
        Call<BodypartResponse> bodypartResponseCall = bodypartsAPI.getDataOfBodypart(id);
        bodypartResponseCall.enqueue(new Callback<BodypartResponse>() {
            @Override
            public void onResponse(Call<BodypartResponse> call, Response<BodypartResponse> response) {
                if(response.isSuccessful()) {
                    BodypartResponse bodypartResponse = response.body();
                    if(bodypartResponse.getStatus() == 0) {
                        tv_totalWorkouts.setText(bodypartResponse.getObject().getExercises().size() + " workouts");
                        setDataForExcerciseList(bodypartResponse.getObject().getExercises());
                    }
                }
            }

            @Override
            public void onFailure(Call<BodypartResponse> call, Throwable t) {

            }
        });
    }

    private void setDataForExcerciseList(ArrayList<Exercise> exercises) {
        rv_exercises.setHasFixedSize(true);
        rv_exercises.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ExercisesAdapter exercisesAdapter = new ExercisesAdapter(exercises, this, this);
        rv_exercises.setAdapter(exercisesAdapter);
    }

    @Override
    public void onClickItem(int id) {
        Intent i = new Intent(this, ExerciseDetailsActivity.class);
        i.putExtra("id", id);
        i.putExtra("part", getSupportActionBar().getTitle());
        i.putExtra("bodyPartId", bodyPartId);
        startActivity(i);
    }

    @Override
    public void onClick(View view, int posittion) {

    }
}
