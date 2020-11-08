package com.example.llajtacomida.models;

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
        return result;
    }
}
