package com.example.testmacropay.data.interfaces;

import com.example.testmacropay.data.model.Login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginAPI {

    @POST(".")
    @FormUrlEncoded
    public Call<Login> postLogin(@Field("email") String email, @Field("password") String password);

}
