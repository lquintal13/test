package com.example.testmacropay.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class Login {

    private boolean success;
    private String token;

    public Login(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}