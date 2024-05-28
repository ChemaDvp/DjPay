package com.vedruna.djpay.network;

import android.content.Context;

public class RetrofitClient {
    private static ApiService apiService;
    public static ApiService getApiService(Context context) {
        if (apiService == null) {
            apiService = ApiClient.getClient(context).create(ApiService.class);
        }
        return apiService;
    }
}
