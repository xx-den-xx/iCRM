package ru.bda.icrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("token")
    @Expose
    String token;

    @SerializedName("manager_id")
    @Expose
    String manager;

    public Token(String token, String manager) {
        this.token = token;
        this.manager = manager;
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", manager='" + manager + '\'' +
                '}';
    }
}
