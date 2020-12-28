package com.appcocha.llajtacomida.models.plate;
import com.appcocha.llajtacomida.models.ObjectParent;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
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
        return name + " " + ingredients + " " + origin + " " + id + " " + punctuation;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name",  name);
        result.put("url",  url);
        result.put("ingredients",  ingredients);
        result.put("origin",  origin);
        result.put("punctuation",  punctuation);
        return result;
    }

}
