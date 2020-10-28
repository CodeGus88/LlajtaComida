package com.example.llajtacomida.models;

public class Plate {

    private String id;
    private String name;
    private String ingredients;
    private String origin;
    private String url;

    public Plate(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    @Override
    public String toString() {
        return "Plate{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
}
