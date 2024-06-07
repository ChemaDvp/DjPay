package com.vedruna.djpay.network;

import android.content.Context;

import com.vedruna.djpay.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // Authentication endpoints
    @POST("/auth/register")
    Call<AuthResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("/auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("api/v1/usuario/users/dj")
    Call<List<User>> getUsersWithDjRole();

    @GET("api/v1/usuario/details")
    Call<User> getUserDetails();
}
