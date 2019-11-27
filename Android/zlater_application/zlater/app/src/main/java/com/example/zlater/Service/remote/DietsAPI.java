package com.example.zlater.Service.remote;

import com.example.zlater.Model.Responses.DietsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DietsAPI {
    @GET("diets/getAll")
    Call<DietsResponse> getAllDiets();
}
