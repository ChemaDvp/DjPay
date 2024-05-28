package com.vedruna.djpay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vedruna.djpay.adapter.UserAdapter;
import com.vedruna.djpay.interfaces.UserInterface;
import com.vedruna.djpay.model.User;
import com.vedruna.djpay.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Enviar extends Fragment {

    private ListView listView;
    private List<User> djList;
    private UserAdapter userAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_enviar, container, false);
        listView = rootView.findViewById(R.id.listaDJS);

        // Aquí debes tener una lista de datos que quieras mostrar en el ListView
        //String[] datos = obtenerTodosDjs();
        // Crear un adaptador para el ListView
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, datos);
        // Establecer el adaptador en el ListView
        //listView.setAdapter(adapter);

        djList = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), djList);
        listView.setAdapter(userAdapter);
        mostrarTodosDjs();

        return rootView;
    }

    /*
    // Método para obtener datos desde la API
    private String[] obtenerTodosDjs() {
        // Aquí debes implementar la lógica para obtener los datos desde la API
        // Por ejemplo, puedes usar Retrofit o Volley para realizar la solicitud HTTP
        // y obtener los datos de la respuesta.
        // Por ahora, simplemente retornaremos datos de ejemplo
        return new String[]{"Dato 1", "Dato 2", "Dato 3", "Dato 4", "Dato 5", "Dato 6", "Dato 7"
                , "Dato 8", "Dato 9", "Dato 10"};
    }

     */

    private void mostrarTodosDjs() {
        // Aquí realizas la llamada a tu API para obtener los DJs
        // Puedes usar Retrofit, Volley u otra biblioteca para hacer la solicitud HTTP
        // y recibir la lista de DJs desde tu API

        // Por ejemplo, usando Retrofit:
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<List<User>> call = userInterface.getUsersWithDjRole();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    djList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                } else {
                    // Manejar el error de la respuesta no exitosa
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Manejar el error de la solicitud fallida
            }
        });
    }
}