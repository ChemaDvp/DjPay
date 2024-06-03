package com.vedruna.djpay;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vedruna.djpay.DTO.PeticionDTO;
import com.vedruna.djpay.adapter.PeticionAdapter;
import com.vedruna.djpay.interfaces.PeticionInterface;
import com.vedruna.djpay.model.Peticion;
import com.vedruna.djpay.utils.Constants;
import com.vedruna.djpay.utils.TokenManager;

import java.io.IOException;
import java.util.ArrayList;
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

        peticionList = new ArrayList<>();
        peticionAdapter = new PeticionAdapter(getActivity(), peticionList);
        listView.setAdapter(peticionAdapter);

        tokenManager = TokenManager.getInstance(getActivity());

        mostrarPeticiones();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Acción al hacer clic en un elemento de la lista
            }
        });

        return rootView;
    }

    private void mostrarPeticiones() {
        String token = tokenManager.getToken();

        if (token == null || token.isEmpty()) {
            Log.e("Peticiones", "Token no encontrado");
            Toast.makeText(getActivity(), "Token no encontrado. Por favor, inicia sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("Peticiones", "Token encontrado: " + token);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        peticionInterface = retrofit.create(PeticionInterface.class);
        Call<List<Peticion>> call = peticionInterface.getPeticiones("Bearer " + token);

        call.enqueue(new Callback<List<Peticion>>() {
            @Override
            public void onResponse(Call<List<Peticion>> call, Response<List<Peticion>> response) {
                if (response.isSuccessful()) {
                    List<Peticion> peticiones = response.body();
                    if (peticiones != null) {
                        peticionList.clear();
                        peticionList.addAll(peticiones);
                        peticionAdapter.notifyDataSetChanged();
                        Log.d("Peticiones", "Lista de peticiones obtenida: " + peticionList.toString());
                    } else {
                        Log.e("Peticiones", "La respuesta del servidor es nula");
                    }
                } else {
                    Log.e("Peticiones", "Error en la respuesta: " + response.errorBody());
                    Log.e("Peticiones", "Código de estado: " + response.code());
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