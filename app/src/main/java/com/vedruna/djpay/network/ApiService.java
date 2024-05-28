package com.vedruna.djpay.network;

import android.content.Context;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    // Authentication endpoints
    @POST("/auth/register")
    default Call<AuthResponse> registerUser(@Body RegisterRequest registerRequest) {
        return null;
    }

    @POST("/auth/login")
    default Call<AuthResponse> loginUser(@Body LoginRequest loginRequest) {
        return null;
    }

}
