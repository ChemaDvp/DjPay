package com.vedruna.djpay.DTO;

import com.vedruna.djpay.model.User;

public class PeticionDTO {
    //ATRIBUTOS
    public String id;
    public String contenido;
    public User auhthorId;

    public PeticionDTO(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
