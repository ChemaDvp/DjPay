package com.vedruna.djpay.interfaces;

import com.vedruna.djpay.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserInterface {

    /**
     * Recupera todos los DJS.
     * @return Una llamada asíncrona que devuelve una lista de objetos Player.
     */
    @GET("/users/dj")
    Call<List<User>> getUsersWithDjRole();
}
