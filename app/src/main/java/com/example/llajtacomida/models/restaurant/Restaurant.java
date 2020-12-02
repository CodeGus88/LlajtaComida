package com.example.llajtacomida.models.restaurant;

import com.example.llajtacomida.models.ObjectParent;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Restaurant extends ObjectParent implements Serializable {

    private String ownerName;
    private String address;
    private String phone;
    private String originAndDescription;
    private String latitude;
    private String longitude;
    private String author;
    private boolean isPublic;

    /**
     * Los siguientes dos contructores son heredatos del padre, se argumenta con la inicialización del isPublic
     */
    public Restaurant() {
        isPublic = false; // Cambiar por false
    }

    public Restaurant(String id) {
        super(id);
        isPublic = false;
    }

    /***
     * El contructor no ses heredados del padre
     * @param id
     * @param isPublic
     */
    public Restaurant(String id, boolean isPublic) {
        super(id);
        this.isPublic = isPublic;
    }

    /***
     * El contructor no ses heredados del padre
     * Puede servir para el administrador,
     * para que cuando el agregue restaurantes penga la posibilidad de publicarlos al mismo tiempo
     * @param isPublic
     */
    public Restaurant(boolean isPublic) {
        this.isPublic = isPublic;
    }

    // Inicio de métodos  de la clase
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOriginAndDescription() {
        return originAndDescription;
    }

    public void setOriginAndDescription(String originAndDescription) {
        this.originAndDescription = originAndDescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return name;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name",  name);
        result.put("url",  url);
        result.put("ownerName",  ownerName);
        result.put("address",  address);
        result.put("phone",  phone);
        result.put("originAndDescription",  originAndDescription);
        result.put("latitude",  latitude);
        result.put("longitude",  longitude);
        result.put("public",  isPublic);
        result.put("author", author);
        result.put("punctuation",  punctuation);
        return result;
    }
}