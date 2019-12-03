package com.example.zlater.Service.remote;

import com.example.zlater.Model.Responses.RoutineResponse;
import com.example.zlater.Model.RoutineRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Hoang Son on 02,November,2019
 **/
public interface RoutineAPI {
    @GET("routine/getRoutinesByUserId/{id}")
    Call<RoutineResponse> getRoutinesByUserId(@Path("id") int userId);

    @POST("routine/create")
    Call<RoutineResponse> createRoutine (@Body() RoutineRequest routine);
}
