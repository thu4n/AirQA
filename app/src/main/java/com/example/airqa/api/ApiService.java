package com.example.airqa.api;

import com.example.airqa.models.AuthResponse;
import com.example.airqa.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://uiot.ixxc.dev/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<AuthResponse> userLogin(@Field("client_id") String client_id,
                                 @Field("username") String username,
                                 @Field("password") String password,
                                 @Field("grant_type") String grant_type);

    @GET("api/master/user/user")
    Call<User> getUserInfo(@Header("Authorization") String token);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<AuthResponse> refreshToken(@Field("client_id") String client_id,
                                 @Field("refresh_token") String refresh_token,
                                 @Field("grant_type") String grant_type);
}
