package com.vedruna.djpay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vedruna.djpay.adapter.UserAdapter;
import com.vedruna.djpay.interfaces.PeticionInterface;
import com.vedruna.djpay.interfaces.UserInterface;
import com.vedruna.djpay.model.User;
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


public class Enviar extends Fragment {

    private ListView listView;
    private List<User> djList;
    private UserAdapter userAdapter;
    private UserInterface userInterface;
    private TokenManager tokenManager;
    private PeticionInterface peticionInterface;
    private Button enviarButton;
    private EditText enviarPeticionEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enviar, container, false);
        listView = rootView.findViewById(R.id.listaDJS);
        enviarButton = rootView.findViewById(R.id.button3); // Inicializar el botón de enviar
        enviarPeticionEditText = rootView.findViewById(R.id.enviarPeticion); // Inicializar el EditText

        djList = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), djList);
        listView.setAdapter(userAdapter);

        tokenManager = TokenManager.getInstance(getActivity());

        mostrarUsuariosPorRol();

        // Agregar un listener para los clics en los elementos de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = djList.get(position);
                userAdapter.setSelectedUser(selectedUser); // Marcar el usuario seleccionado en el adaptador
                Log.d("Enviar", "Usuario seleccionado: " + selectedUser.getUsername() + " con ID: " + selectedUser.getId());

            }
        });

        // Agregar un listener para el botón de enviar
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = enviarPeticionEditText.getText().toString();
                if (!mensaje.isEmpty()) {
                    enviarMensajeAlUsuarioSeleccionado(mensaje);
                } else {
                    // Mostrar un mensaje indicando que el campo de la petición está vacío
                    Toast.makeText(getActivity(), "Por favor, ingresa una petición",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void mostrarUsuariosPorRol() {
        String token = tokenManager.getToken(); // Obtener el token utilizando TokenManager

        if (token == null || token.isEmpty()) {
            Log.e("Enviar", "Token no encontrado");
            Toast.makeText(getActivity(), "Token no encontrado. Por favor, inicia sesión.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("Enviar", "Token encontrado: " + token);

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        userInterface = retrofit.create(UserInterface.class);
        Call<List<User>> call = userInterface.getUsersWithDjRole();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    if (users != null) {
                        for (User user : users) {
                            Log.d("Enviar", "Usuario: " + user.getUsername() + ", ID: " + user.getId());
                        }
                        djList.clear();
                        djList.addAll(users);
                        userAdapter.notifyDataSetChanged();
                        Log.d("Enviar", "Lista de usuarios obtenida: " + djList.toString());
                    } else {
                        Log.e("Enviar", "La respuesta del servidor es nula");
                    }
                } else {
                    Log.e("Enviar", "Error en la respuesta: " + response.errorBody());
                    Log.e("Enviar", "Código de estado: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Enviar", "Error en la solicitud: " + t.getMessage());
                if (t instanceof IOException) {
                    Log.e("Enviar", "Error de red: " + t.getMessage());
                } else {
                    Log.e("Enviar", "Error inesperado: " + t.getMessage());
                }
            }
        });
    }

    // Método para enviar un mensaje al usuario seleccionado
    private void enviarMensajeAlUsuarioSeleccionado(String mensaje) {
        User selectedUser = userAdapter.getSelectedUser();
        if (selectedUser != null) {
            long usuarioId = selectedUser.getId(); // Obtener el ID del usuario seleccionado
            Log.d("Enviar", "Enviando mensaje al usuario con ID: " + usuarioId);

            String token = tokenManager.getToken();

            if (token == null || token.isEmpty()) {
                Log.e("Enviar", "Token no encontrado");
                Toast.makeText(getActivity(), "Token no encontrado. Por favor, inicia sesión.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("Enviar", "Token encontrado: " + token);
            Toast.makeText(getActivity(), "Petición enviada",
                    Toast.LENGTH_SHORT).show();

            Interceptor interceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(request);
                }
            };

            OkHttpClient client2 = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client2)
                    .build();

            peticionInterface = retrofit.create(PeticionInterface.class);

            // Llamar al método de la API para enviar la petición con el mensaje
            Call<String> call = peticionInterface.enviarPeticion(usuarioId, mensaje);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String mensajeRespuesta = response.body();
                        Log.d("Enviar", "Petición enviada correctamente. Respuesta del servidor: " + mensajeRespuesta);
                        // Aquí puedes manejar la respuesta de la API, por ejemplo, mostrar un Toast
                        Toast.makeText(getActivity(), mensajeRespuesta, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Enviar", "Error en la respuesta: " + response.errorBody());
                        Log.e("Enviar", "Código de estado: " + response.code());
                        // Mostrar un mensaje de error si la solicitud no fue exitosa
                        Toast.makeText(getActivity(), "Error al enviar la petición",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log para verificar el error en la solicitud
                    Log.e("Enviar", "Error en la solicitud: " + t.getMessage());

                }
            });
        } else {
            // Mostrar un mensaje indicando que no se ha seleccionado ningún usuario
            Toast.makeText(getActivity(), "No se ha seleccionado ningún usuario",
                    Toast.LENGTH_SHORT).show();
        }
    }
}