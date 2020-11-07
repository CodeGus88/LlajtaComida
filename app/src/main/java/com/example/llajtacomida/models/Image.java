package com.example.llajtacomida.models;

import java.util.UUID;

public class Image {

    private String id;
    private String url;
    private String fileName;

    public Image(String id){
        this.id = id;
        fileName = id + ".jpg";
    }

    public Image(){
        this.id = UUID.randomUUID().toString();
        fileName = id + ".jpg";
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
