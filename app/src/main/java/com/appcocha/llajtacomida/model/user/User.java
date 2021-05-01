package com.appcocha.llajtacomida.model.user;
import androidx.annotation.NonNull;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Modelo
 */
public class User {

    private String id;
    private String fulName;
    private String avatarUrl; // firebase no permite agregar objetos Uri, por eso en String
    private String email;
    private String role;

    /**
     * Constructor
     * Para ser leido desde firebase, siempre es necesario tener un constructor vacio
     */
    public User(){
    }

    /**
     * Constructor, establece el id
     * @param id
     */
    public User(@NonNull String id) {
        this.id = id;
    }

    /**
     * Inicializa todos los atributos
     * @param id
     * @param fulName
     * @param email
     * @param avatarUrl
     * @param role
     */
    public User(@NonNull String id, @NonNull String fulName, @NonNull String email, @NonNull String avatarUrl, @NonNull String role) {
        this.id = id;
        this.fulName = fulName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }

    /**
     * Obtiene el id del usuario
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el email del usuario
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene el nombre completo del usuario
     * @return fulName
     */
    public String getFulName() {
        return fulName;
    }

    /**
     * Obtiene el url del avatar
     * @return avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Obtiene el rol del usuario
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece el id del usuario
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Establece el nombre completo del usuario
     * @param fulName
     */
    public void setFulName(String fulName) {
        this.fulName = fulName;
    }

    /**
     * Establece el emaildel usuario
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Establece el url del avatar del usuario
     * @param avatarUrl
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * Establece el rol del usuario
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Retorna referencia (Se usa para el buscador)
     * @return String
     */
    @Override
    public String toString() {
        return fulName + " " + email + " " + id + " " + role;
    }

    /**
     * Crea un mapa de referencia del objeto (Sirve para actualizar en la BD)
     * @return Map
     */
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
