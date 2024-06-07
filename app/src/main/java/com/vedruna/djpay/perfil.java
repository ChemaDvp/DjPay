package com.vedruna.djpay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedruna.djpay.model.User;
import com.vedruna.djpay.network.ApiService;
import com.vedruna.djpay.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class perfil extends Fragment {

    private TextView nombreUsuarioTextView;
    private TextView emailUsuarioTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtiene las referencias de los TextView
        nombreUsuarioTextView = view.findViewById(R.id.nombreUsuario);
        emailUsuarioTextView = view.findViewById(R.id.emailUsuario);

        // Obtiene los detalles del usuario desde la API utilizando Retrofit
        ApiService apiService = RetrofitClient.getApiService(requireContext()); // Cambia ApiService por el nombre de tu interfaz Retrofit
        Call<User> call = apiService.getUserDetails(); // Cambia getUserDetails por el nombre de tu método para obtener los detalles del usuario

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    // Muestra los datos del usuario en los TextView
                    nombreUsuarioTextView.setText(user.getName());
                    emailUsuarioTextView.setText(user.getEmail());
                } else {
                    // Maneja el caso de respuesta no exitosa, por ejemplo, mostrando un mensaje de error
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Maneja el caso de fallo en la solicitud, por ejemplo, mostrando un mensaje de error
            }
        });
    }
}