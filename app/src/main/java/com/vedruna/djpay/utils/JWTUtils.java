package com.vedruna.djpay.utils;

import android.util.Base64;
import org.json.JSONObject;
import android.util.Base64;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

public class JWTUtils {
    public static String getBodyFromJWT(String JWTEncoded) throws Exception {
        String[] split = JWTEncoded.split("\\.");
        return getJson(split[1]);
    }

    private static String getJson(String strEncoded) throws Exception {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
