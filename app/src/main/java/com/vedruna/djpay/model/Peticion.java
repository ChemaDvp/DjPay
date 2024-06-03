package com.vedruna.djpay.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Peticion implements Serializable {
    private int idPeticion;
    private User author;
    private String contenido;
    private Boolean estado;

    public String getString() {
        return contenido;
    }
}
