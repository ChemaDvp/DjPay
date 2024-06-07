package com.vedruna.djpay;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.vedruna.djpay.DTO.PeticionDTO;
import com.vedruna.djpay.adapter.PeticionAdapter;
import com.vedruna.djpay.interfaces.PeticionInterface;
import com.vedruna.djpay.model.Peticion;
import com.vedruna.djpay.utils.Constants;
import com.vedruna.djpay.utils.JWTUtils;
import com.vedruna.djpay.utils.TokenManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class peticiones extends Fragment {

    private ListView listView;
    private List<Peticion> peticionList;
    private PeticionAdapter peticionAdapter;
    private PeticionInterface peticionInterface;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_peticiones, container, false);
        listView = rootView.findViewById(R.id.listaPeticiones);

        tokenManager = TokenManager.getInstance(getActivity());

        String token2 = tokenManager.getToken();
        Log.d("LOGIN", "Login successful. MIRA AQUI: " + token2);

        try {
            String body = JWTUtils.getBodyFromJWT(token2);
            Log.d("JWT_DECODED_BODY", "Body: " + body);

            JSONObject jsonObject = new JSONObject(body);
            int id = jsonObject.getInt("id");

            mostrarPeticiones(id);

            Log.d("JWT_USER_ID", "User ID: " + id);
        } catch (Exception e) {
            Log.e("JWT_DECODE_ERROR", "Error decoding JWT", e);
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Peticion selectedPeticion = (Peticion) parent.getItemAtPosition(position);
                peticionAdapter.setSelectedPeticion(selectedPeticion);

                // Mostrar AlertDialog para confirmar eliminación
                new AlertDialog.Builder(getActivity())
                        .setTitle("Aceptar Petición")
                        .setMessage("¿Estás seguro de que quieres aceptar esta petición?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            peticionAdapter.removeSelectedPeticion();
                            Toast.makeText(getActivity(), "Petición rechazada", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        return rootView;
    }

    private void mostrarPeticiones(long id) {
        String token2 = tokenManager.getToken();

        // Agregar el token JWT al encabezado de autorización de las solicitudes HTTP
        Interceptor interceptor = new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token2)
                        .build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        // Configurar Retrofit con el cliente OkHttpClient personalizado
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        peticionInterface = retrofit.create(PeticionInterface.class);
        Call<List<Peticion>> call = peticionInterface.getPeticiones(id);

        call.enqueue(new Callback<List<Peticion>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Peticion>> call, Response<List<Peticion>> response) {
                Log.d("Login", "Login successful. Token received: " + token2);

                if (response.isSuccessful()) {
                    peticionList = response.body();
                    PeticionAdapter peticionAdapter1 = new PeticionAdapter(peticionList, getActivity().getApplicationContext());
                    listView.setAdapter(peticionAdapter1);
                    peticionList.forEach(peticion -> Log.i("Djs: ", peticion.toString()));
                } else {
                    Log.e("peticiones", "Error en la respuesta: " + response.errorBody());
                    Log.d("Login", "Login successful. Token received: " + token2);
                    Log.e("peticiones", "Código de estado: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Peticion>> call, Throwable t) {
                Log.e("Peticiones", "Error en la solicitud: " + t.getMessage());
                if (t instanceof IOException) {
                    Log.e("Peticiones", "Error de red: " + t.getMessage());
                } else {
                    Log.e("Peticiones", "Error inesperado: " + t.getMessage());
                }
            }
        });
    }
}
