package com.example.llajtacomida.models;

import java.io.Serializable;
import java.util.UUID;

public abstract class ObjectParent implements Serializable {

    protected String id;
    protected String name;
    protected String url;

//    public Hashtable<String, Image> images;

    /**
     * Utilizamos poliformismo
     * Para evitarse de mal entendidos el objeto le dará un id automaticamente si no se lo proporciona
     */
    public ObjectParent(){
        id = UUID.randomUUID().toString();
        this.url = "";
//        images = new Hashtable<String, Image>();
    }
    public ObjectParent(String id){
        this.id = id;
        this.url = "";
//        images = new Hashtable<String, Image>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public abstract String getResume(); // Para mostrar un pequeño resumen de lo que se quiere obtener (nombre, descripcion, etc)

    public  abstract String toString(); // Se usará alimentar al buscador (Algunos paquetes buscan a este método por defecto para obtener info)


    //    Prueba
//    public void setImages(Hashtable<String, Image> images){
//        this.images = images;
//    }
//
//    public Hashtable<String, Image> getImages(){
//        return images;
//    }

    public void getNode(){

    }



}
