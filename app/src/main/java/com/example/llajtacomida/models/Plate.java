package com.example.llajtacomida.models;

public class Plate extends ObjectParent {

    private String ingredients;
    private String origin;

    public Plate(String id){
        super(id);
    }
    public Plate(){
        super();
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

    @Override
    public String toString() {
        return name;
    }
    @Override
    public String getResume() {
        return origin;
    }
}
