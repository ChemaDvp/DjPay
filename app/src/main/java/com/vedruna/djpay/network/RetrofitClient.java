package com.vedruna.djpay.network;

import android.content.Context;
import android.util.Log;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static ApiService apiService;

    public static ApiService getApiService(Context context) {
        if (apiService == null) {
            Log.d(TAG, "apiService is null, creating new instance");
            apiService = ApiClient.getClient(context).create(ApiService.class);
        } else {
            Log.d(TAG, "apiService already exists, returning existing instance");
        }
        return apiService;
    }
}
