package com.vedruna.djpay.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Peticion implements Serializable {
    private int idPeticion;
    private User author;
    private String contenido;
    private Boolean estado;

    public Peticion(int idPeticion, User author, String contenido, Boolean estado) {
        this.idPeticion = idPeticion;
        this.author = author;
        this.contenido = contenido;
        this.estado = estado;
    }

    public String getContenido() {
        return contenido;
    }
    public int getId() {
        return idPeticion;
    }
}
