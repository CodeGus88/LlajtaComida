package com.appcocha.llajtacomida.models.restaurant;

import com.appcocha.llajtacomida.models.ObjectParent;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Modelo
 */
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
     * El contructor o gredado del padre
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

    /**
     * Obtiene el nombre del propietario del restaurante
     * @return ownerName
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Establece el nombre del propietario
     * @param ownerName
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Obtiene la dirección del restaurante
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Establece la direcci1ón del restaurante
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Obtiene el teléfono(s)
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Establece el teléfono(s)
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Obtiene el origen y desrcipción del restaurante
     * @return originAndDescription
     */
    public String getOriginAndDescription() {
        return originAndDescription;
    }

    /**
     * Establece el origen y descripción del restaurante
     * @param originAndDescription
     */
    public void setOriginAndDescription(String originAndDescription) {
        this.originAndDescription = originAndDescription;
    }

    /**
     * Obtiene la latitud de la ubicación del restaurante
     * @return latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Establece la latitud de la ubicación del restaurante
     * @param latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Obtiene la longitud de la ubicación del restaurante
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Establece la longitud de la ubicación del restaurante
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Obtiene el estado público o no público (true, false) del restaurante
     * @return isPublic
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Modifica el estado (público,, no público) del restaurante
     * @param aPublic
     */
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    /**
     * Establece el id del autor del registro
     * @param author
     */
    public void setAuthor(String author){
        this.author = author;
    }

    /**
     * Obtiene el id del autor del registro
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Retorna referencia (Se usa para el buscador)
     * @return String
     */
    @Override
    public String toString() {
        return name + " " + id + " " + ownerName + " " +
                address + " " + phone + " " + originAndDescription + " " +
                author + " " + punctuation;
    }

    /**
     * Crea un mapa de referencia de un objeto (Sirve para actualizar en la BD)
     * @return Map
     */
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