package com.nguyenthanh.placearound.LoginApi.services;

import com.nguyenthanh.placearound.LoginApi.object.ObjectAccessToken;
import com.nguyenthanh.placearound.LoginApi.object.ObjectFullResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by GameNet on 3/13/2017.
 */

public interface IRestService {

    @FormUrlEncoded
    @POST("/api/token")
    Call<ObjectAccessToken> login(
            @Field("grant_type") String grant_type,
            @Field("username") String username,
            @Field("password") String password);


    @Headers("Accept: application/vnd.app.atoms.mobile-v1+json")
    @GET("/api/stories")
        Call<ObjectFullResponseData> loadData(@Header("Authorization") String bearerToken);
}
