package com.appcocha.llajtacomida.model.image;

import java.util.UUID;

/**
 * Esta clase es el modelo para los objetos imagen que contienen los objetos platos y restaurantes
 */
public class Image {

    private String id;
    private String url;
    private String fileName;

    /**
     * Contruye un objeto con el id propuesto y define el nombre de la imagen
     * @param id
     */
    public Image(String id){
        this.id = id;
        fileName = id + ".jpg";
    }

    /**
     * Contruye un objeto, generando un id y definiendo el nombre de la imagen
     */
    public Image(){
        this.id = UUID.randomUUID().toString();
        fileName = id + ".jpg";
    }

    /**
     * Obtiene el id de la imagen
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el id de la imagen
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el URL de la imagen
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece el URL de l aimagen
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Obtiene el nombre de la imagen
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Establece el nombre de la imagen
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Retorma infrmación referencial de la imagen
     * @return imageReference
     */
    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
