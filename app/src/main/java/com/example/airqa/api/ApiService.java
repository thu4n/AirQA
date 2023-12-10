package com.example.airqa.api;

import com.example.airqa.models.AuthResponse;
import com.example.airqa.models.DataPoint;
import com.example.airqa.models.User;
import com.example.airqa.models.assetGroup.Asset;
import com.example.airqa.models.weatherAssetGroup.WeatherAsset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.RequestBody;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    @GET("api/master/asset/{dynamicPath}")
    Call<WeatherAsset> getAssetInfo(@Header("Authorization") String token, @Path("dynamicPath") String dynamicPath);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<AuthResponse> refreshToken(@Field("client_id") String client_id,
                                 @Field("refresh_token") String refresh_token,
                                 @Field("grant_type") String grant_type);

    @FormUrlEncoded
    @PUT("api/master/user/master/reset-password/")
    Call<Void> resetPassword(@Header("Authorization") String token,@Body RequestBody body);

    @POST("api/master/asset/query")
    Call<List<Asset>> getAllAsset(@Header("Authorization") String token, @Body RequestBody rawJsonBody);

    @POST("api/master/asset/datapoint/{assetId}/attribute/{attributeName}")
    Call<List<DataPoint>> getDataPoint(@Header("Authorization") String token, @Body RequestBody rawJsonBody,@Path("assetId") String assetId,@Path("attributeName") String attributeName );
}
