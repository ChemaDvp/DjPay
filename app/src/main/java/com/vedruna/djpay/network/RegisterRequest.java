package com.vedruna.djpay.network;


import java.io.Serializable;

public class RegisterRequest implements Serializable{
    private String username;
    private String password;
    private String email;
    private String rol;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String email, String rol) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
    }

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public  String getEmail(String email){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public  String getRol(String rol){
        return rol;
    }
    public void setRol(String rol){
        this.rol = rol;
    }
}
