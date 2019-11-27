package com.example.zlater.Service.remote;

import com.example.zlater.Model.Responses.DishesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DishesAPI {
    @GET("dishes/getAllDishesByMeal/{id}")
    Call<DishesResponse> getDishesByMeal(@Path("id") int id);
}
