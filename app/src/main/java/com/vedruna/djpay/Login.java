package com.vedruna.djpay;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private TextView username;
    private TextView password;
    Button registroUsuario;
    Button inicioSesion;
    private ApiService apiService;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        registroUsuario = findViewById(R.id.btnRegistro);
        inicioSesion = findViewById(R.id.btnInicio);

        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FrameLayaout.class);
                startActivity(intent);
                finish();
            }
        });

        apiService = RetrofitClient.getApiService(this);
        initView();
    }

    private void initView() {
        username = findViewById(R.id.usertxt);
        password = findViewById(R.id.passwordtxt);
        inicioSesion = findViewById(R.id.btnInicio);

        inicioSesion.setOnClickListener(v -> {
            String usernameRellenado = username.getText().toString().trim();
            String passwordRellenada = password.getText().toString().trim();

            if (usernameRellenado.isEmpty() || passwordRellenada.isEmpty()) {
                Toast.makeText(Login.this, "El email y la contraseña no pueden estar vacios",
                        Toast.LENGTH_SHORT).show();
            } else {
                login(usernameRellenado, passwordRellenada);
            }
        });

        registroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegistroUsuario.class);
                startActivity(intent);
            }
        });
    }

    private void login(String username, String password) {
        TokenManager.getInstance(Login.this).clearToken();

        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<AuthResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    Log.d(TAG, "Login successful. Token received: " + token);
                    TokenManager.getInstance(Login.this).saveToken(token);

                    // Log the saved token
                    Log.d(TAG, "Saved token: " + TokenManager.getInstance(Login.this)
                            .getToken());

                    Intent intent = new Intent(Login.this, FrameLayaout.class);
                    startActivity(intent);
                    finish();
            } else {
                    Toast.makeText(Login.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response message: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.d(TAG, "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(Login.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}