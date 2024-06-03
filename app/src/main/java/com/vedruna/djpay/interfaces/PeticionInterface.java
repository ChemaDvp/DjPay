package com.vedruna.djpay.interfaces;

import com.vedruna.djpay.DTO.PeticionDTO;
import com.vedruna.djpay.model.Peticion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PeticionInterface {
    @POST("/api/v1/peticion/enviar/{usuarioId}")
    Call<String> enviarPeticion(@Path("usuarioId") int usuarioId, @Body String mensaje);

    @GET("/api/v1/usuario/peticiones")
    Call<List<Peticion>> getPeticiones(@Header("Authorization") String authHeader);
}
