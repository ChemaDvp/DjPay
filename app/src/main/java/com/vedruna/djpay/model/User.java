package com.vedruna.djpay.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class User implements Serializable {
    private int id;
    private String username;
    private String email;
    private String password;
    private String imgPerfil;
    private List<Peticion> peticiones;
    private Rol role;

    public User(String username, String email, String password, Rol rol) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = rol;
    }

    public String getUsername() {
        return username;
    }
    public int getId() {
        return id;
    }
}
