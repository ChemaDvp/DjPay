package com.vedruna.djpay;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vedruna.djpay.network.ApiService;
import com.vedruna.djpay.network.AuthResponse;
import com.vedruna.djpay.network.LoginRequest;
import com.vedruna.djpay.network.RetrofitClient;
import com.vedruna.djpay.utils.TokenManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private TextView email;
    private TextView password;
    Button registroUsuario;
    Button inicioSesion;
    private ApiService apiService;
    private TokenManager tokenManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        registroUsuario = findViewById(R.id.btnRegistro);
        apiService = RetrofitClient.getApiService(this);
        tokenManager = TokenManager.getInstance(this);

        // Agregar el mensaje de log
        Log.d("MainActivity", "Aplicacion conectada a la API");

        initView();
    }

    private void initView() {
        email = findViewById(R.id.usertxt);
        password = findViewById(R.id.passwordtxt);
        inicioSesion = findViewById(R.id.btnInicio);

        inicioSesion.setOnClickListener(v -> {
            String emailRellenado = email.getText().toString().trim();
            String passwordRellenada = password.getText().toString().trim();

            if (emailRellenado.isEmpty() || passwordRellenada.isEmpty()) {
                Toast.makeText(Login.this, "El email y la contraseña no pueden estar vacíos",
                        Toast.LENGTH_SHORT).show();
            } else {
                login(emailRellenado, passwordRellenada);
            }
        });

        registroUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, RegistroUsuario.class);
            startActivity(intent);
        });

        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar la aplicación
                finishAffinity();
            }
        });
    }

    private void login(String email, String password) {
        tokenManager.clearToken(); // Limpiar el token antes de iniciar sesión

        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<AuthResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    Log.d("Login", "Login successful. Token received: " + token);
                    tokenManager.saveToken(token); // Guardar el token utilizando TokenManager

                    // Verificar el token guardado
                    String savedToken = tokenManager.getToken();
                    Log.d("Login", "Saved token: " + savedToken);

                    Intent intent = new Intent(Login.this, FrameLayaout.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d("Login", "Response code: " + response.code());
                    Log.d("Login", "Response message: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.d("Login", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(Login.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Login", "An error occurred: ", t);
            }
        });
    }
}