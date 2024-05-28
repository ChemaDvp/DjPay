package com.vedruna.djpay;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vedruna.djpay.utils.TokenManager;

public class FrameLayaout extends AppCompatActivity {
    //FirebaseAuth salirApp;

    /**
     * Método llamado cuando se crea la actividad.
     *
     * @param savedInstanceState Un objeto Bundle que contiene el estado previamente guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.frame_layaout);

        //salirApp = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.inicio);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_inicio) {
                navController.navigate(R.id.inicio);
            } else if (item.getItemId() == R.id.navigation_envio) {
                navController.navigate(R.id.enviar);
            } else if (item.getItemId() == R.id.navigation_peticiones) {
                navController.navigate(R.id.peticiones);
            } else if (item.getItemId() == R.id.navigation_perfil) {
                navController.navigate(R.id.perfil);
            } else if (item.getItemId() == R.id.navigation_salir) {
                showLogoutConfirmationDialog();
                TokenManager.getInstance(FrameLayaout.this).clearToken();
                Log.d(TAG, "Token cleared on logout");
            }
            return true;
        });
    }

    /**
     * Método para cerrar sesión.
     */


    private void logOut() {
        //salirApp.signOut();
        goLogin();
    }

    /**
     * Método para ir a la pantalla de inicio de sesión.
     */
    private void goLogin() {
        Intent intent = new Intent(FrameLayaout.this, Login.class);
        startActivity(intent);
    }

    /**
     * Método para mostrar un diálogo de confirmación de cierre de sesión.
     */
    private void showLogoutConfirmationDialog() {
        // Construir el diálogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Quieres Salir?");

        // Establecer el texto y el color del botón positivo ("Sí")
        builder.setPositiveButton("Si", (dialog, which) -> logOut());

        // Establecer el texto y el color del botón negativo ("Cancelar")
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        // Obtener el color principal
        int colorMain = getResources().getColor(R.color.main); // Cambia R.color.main por el identificador de tu color principal

        // Crear el diálogo y mostrarlo
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // Obtener los botones del diálogo
                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                // Establecer el color del texto de los botones
                positiveButton.setTextColor(colorMain);
                negativeButton.setTextColor(colorMain);
            }
        });
        dialog.show();
    }
}
