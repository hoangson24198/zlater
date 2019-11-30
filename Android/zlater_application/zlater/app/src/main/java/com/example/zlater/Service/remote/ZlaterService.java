package com.example.zlater.Service.remote;


import com.example.zlater.Model.History;
import com.example.zlater.Model.Responses.HistoryResponse;
import com.example.zlater.Model.Responses.UserResponse;
import com.example.zlater.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ZlaterService {
    @GET("user/getCurrentUser/{id}")
    Call<UserResponse> getCurrentUser(@Path("id") int userId);

    @POST("user/register")
    Call<UserResponse> registerUser(@Body() User user);

    @POST("user/login")
    Call<UserResponse> loginUser(@Body() User user);

    @POST("user/update")
    Call<UserResponse> updateUser(@Body User user);

    @POST("user/logout")
    @FormUrlEncoded
    Call<UserResponse> userLogout(@Field("id") Integer id);

    @GET("user/getUserByUsername/{username}")
    Call<UserResponse> getUserByUserName(@Path("username") String username);


    @POST("history/create")
    Call<HistoryResponse> addHistory(@Body History history );

    @GET("user/profile")
    Call<String> getUser(@Header("Authorization") String token);


//    @POST("user/register")
//    Observable<Response> register(@Body User user);
//
//    @POST("user/login")
//    Observable<Response> login();
//
//    @GET("users/{email}")
//    Observable<User> getProfile(@Path("email") String email);
//
//    @PUT("users/{email}")
//    Observable<Response> changePassword(@Path("email") String email, @Body User user);
//
//    @POST("users/{email}/password")
//    Observable<Response> resetPasswordInit(@Path("email") String email);
//
//    @POST("users/{email}/password")
//    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);


//
//    // Category
//    @GET("categories/listAllCategory")
//    Call<List<Category>> getAllCategoryDetail();
//
//    @POST("categories/")
//    Call<Category> createCategory(@Body Category category);
//
//    @DELETE("categories/{id}")
//    Call<Category> deleteCategory(@Path("id") String _id);
//
//    @PUT("categories/{id}")
//    Call<Category> updateCategory(@Path("id") String _id, @Body Category category);
//
//    // Product
//    @GET("products/listAllProduct")
//    Call<List<Product>> getAllProductDetail();
//
//    @POST("products/")
//    Call<Product> createProduct(@Body Product product);
//
//    @DELETE("products/{id}")
//    Call<Product> deleteProduct(@Path("id") String _id);
//
//    @PUT("products/{id}")
//    Call<Product> updateProduct(@Path("id") String _id, @Body Product product);

}
