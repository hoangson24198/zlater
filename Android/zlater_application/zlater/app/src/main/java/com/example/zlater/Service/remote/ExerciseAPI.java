package com.example.zlater.Service.remote;

import com.example.zlater.Model.Responses.ExerciseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExerciseAPI {
    @GET("exercises/getAll")
    Call<ExerciseResponse> getAllExercise();

    @GET("exercises/exDetail/{id}")
    Call<ExerciseResponse> getExerciseDetails(@Path("id") int id);
}
