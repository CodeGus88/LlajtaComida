package com.example.llajtacomida.models;

import java.util.UUID;

public class Image {

    private String id;
    private String url;

    public Image(String id){
        this.id = id;
    }

    public Image(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
