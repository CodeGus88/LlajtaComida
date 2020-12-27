package com.appcocha.llajtacomida.models.user;
import androidx.annotation.NonNull;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String id;
    private String fulName;
    private String avatarUrl; // firebase no permite agregar objetos Uri, por eso en String
    private String email;
    private String role;

    /**
     * Para ser leido desde firebase, siempre es necesario tener un constructor vacio
     */
    public User(){
    }

    public User(@NonNull String id) {

    }

    public User(@NonNull String id, @NonNull String fulName, @NonNull String email, @NonNull String avatarUrl, @NonNull String role) {
        this.id = id;
        this.fulName = fulName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFulName() {
        return fulName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFulName(String fulName) {
        this.fulName = fulName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return fulName + " " + email + " " + id + " " + role;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("fulName", fulName);
        result.put("email",  email);
        result.put("avatarUrl", avatarUrl);
        result.put("role", role);
        return result;
    }



}
