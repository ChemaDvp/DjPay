package com.vedruna.djpay;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.vedruna.djpay.network.ApiService;
import com.vedruna.djpay.network.AuthResponse;
import com.vedruna.djpay.network.RegisterRequest;
import com.vedruna.djpay.network.RetrofitClient;
import com.vedruna.djpay.utils.TokenManager;

import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class RegistroUsuario extends AppCompatActivity {

    private TextView username;
    private TextView email;
    private TextView password;
    private CheckBox usuarioCheck;
    private CheckBox djCheck;
    private Button registrarse;
    Button volver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        volver = findViewById(R.id.btnvolverRegistro);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroUsuario.this, Login.class);
                startActivity(intent);
            }
        });

        initView();
    }

    private void initView() {
        username = findViewById(R.id.usertxt);
        email = findViewById(R.id.emailtxt);
        password = findViewById(R.id.passRegistertxt);
        usuarioCheck = findViewById(R.id.usuarioCheck);
        djCheck = findViewById(R.id.djCheck);
        registrarse = findViewById(R.id.registrarse);

        registrarse.setOnClickListener(v -> {
            String usernameRellenado = username.getText().toString();
            String emailRellenado = email.getText().toString();
            String passwordRellenada = password.getText().toString();
            String usuarioCheckActivo = usuarioCheck.getText().toString();
            String djCheckActivo = djCheck.getText().toString();

            if (usernameRellenado.isEmpty() || emailRellenado.isEmpty() ||
            passwordRellenada.isEmpty() || usuarioCheckActivo.isEmpty() ||
            djCheckActivo.isEmpty()) {
                Toast.makeText(RegistroUsuario.this, "No puede haber campos vacios",
                        Toast.LENGTH_SHORT).show();
            } else {
                registerUser(usernameRellenado, emailRellenado, passwordRellenada,
                        usuarioCheckActivo);
            }
        });
        registrarse.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroUsuario.this, Login.class);
            startActivity(intent);
        });
    }

    private void registerUser(String username, String email, String password, String rol){
        ApiService apiService = RetrofitClient.getApiService(this);
        RegisterRequest registerRequest = new RegisterRequest(username, email , password, rol);

        apiService.registerUser(registerRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    TokenManager.getInstance(RegistroUsuario.this).saveToken(token);
                    Toast.makeText(RegistroUsuario.this, "Registro completado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistroUsuario.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegistroUsuario.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(RegistroUsuario.this, "Ha ocurrido un problema: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}