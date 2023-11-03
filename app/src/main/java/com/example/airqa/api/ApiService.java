package com.example.airqa.api;

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
import retrofit2.http.POST;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    Retrofit apiService = new Retrofit.Builder()
            .baseUrl("https://uiot.ixxc.dev/auth/realms/master/protocol/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @FormUrlEncoded
    @POST("openid-connect/token")
    Call<User> userLogin(@Field("client_id") String client_id,
                         @Field("username") String username,
                         @Field("password") String password,
                         @Field("grant_type") String grant_type);
}
