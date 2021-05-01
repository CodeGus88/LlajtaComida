package com.appcocha.llajtacomida.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class ObjectParent implements Serializable {

    protected String id;
    protected String name;
    protected String url;
    protected float punctuation;
    /**
     * Utiliza poliformismo
     * Para evitarse de mal entendidos el objeto le dará un id automaticamente si no se lo proporciona
     */
    public ObjectParent(){
        id = UUID.randomUUID().toString();
        this.url = "";
        punctuation = 0;
    }
    public ObjectParent(String id){
        this.id = id;
        this.url = "";
        punctuation = 0;
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

    public float getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(float punctuation) {
        this.punctuation = punctuation;
    }

    public  abstract String toString(); // Se usará alimentar al buscador (Algunos paquetes buscan a este método por defecto para obtener info)
}
